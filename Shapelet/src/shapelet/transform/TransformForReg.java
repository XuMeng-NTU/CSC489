/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.transform;

import java.util.ArrayList;
import java.util.Collections;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.shapelet.OrderLineObj;
import weka.core.shapelet.Shapelet;

/**
 *
 * @author Meng
 */
public class TransformForReg {

    protected int minShapeletLength;
    protected int maxShapeletLength;
    protected int numShapelets;
    protected boolean shapeletsTrained;
    protected ArrayList<Shapelet> shapelets;
    protected String ouputFileLocation = "defaultShapeletOutput.txt"; // default store location
    protected boolean recordShapelets = true; // default action is to write an output file
    
    protected int numSeries;
    
    public static int DEFAULT_NUMSHAPELETS=100;
    public static int DEFAULT_MINSHAPELETLENGTH=3;
    public static int DEFAULT_MAXSHAPELETLENGTH=30;
    
    public TransformForReg(){
        this(DEFAULT_NUMSHAPELETS,DEFAULT_MINSHAPELETLENGTH,DEFAULT_MAXSHAPELETLENGTH);
    }    
    
    public TransformForReg(int k, int minShapeletLength, int maxShapeletLength){
        this.minShapeletLength = minShapeletLength;
        this.maxShapeletLength = maxShapeletLength;
        this.numShapelets = k;
        this.shapelets = new ArrayList<Shapelet>();
        this.shapeletsTrained = false;
    }    
    
    public void setNumberOfSeries(int k){
        numSeries = k;
    }
    
    public void setNumberOfShapelets(int k){
        this.numShapelets = k;
    }
    public int getNumberOfShapelets(){ return numShapelets;
    }
    
    public void setShapeletMinAndMax(int minShapeletLength, int maxShapeletLength){
        this.minShapeletLength = minShapeletLength;
        this.maxShapeletLength = maxShapeletLength;
    }
    
    protected Instances determineOutputFormat(Instances inputFormat) throws Exception{

        if(this.numShapelets < 1){
            throw new Exception("ShapeletFilter not initialised correctly - please specify a value of k that is greater than or equal to 1");
        }

        int length = this.shapelets.size();
        FastVector atts = new FastVector();
        String name;
        for(int i = 0; i < length; i++){
            name = "Shapelet_" + i;
            atts.addElement(new Attribute(name));
        }

        atts.addElement(new Attribute(inputFormat.attribute(inputFormat.classIndex()).name()));

        Instances result = new Instances("Shapelets" + inputFormat.relationName(), atts, inputFormat.numInstances());
        if(inputFormat.classIndex() >= 0){
            result.setClassIndex(result.numAttributes() - 1);
        }
        return result;
    }
    
    public Instances process(Instances data) throws Exception{
        if(this.numShapelets < 1){
            throw new Exception("Number of shapelets initialised incorrectly - please select value of k greater than or equal to 1 (Usage: setNumberOfShapelets");
        }

        int maxPossibleLength = data.instance(0).numAttributes() - 1;
        if(data.classIndex() < 0) {
            throw new Exception("Require that the class be set for the ShapeletTransform");
        }

        if(this.minShapeletLength < 1 || this.maxShapeletLength < 1 || this.maxShapeletLength < this.minShapeletLength || this.maxShapeletLength > maxPossibleLength){
            throw new Exception("Shapelet length parameters initialised incorrectly");
        }
        
        if(this.shapeletsTrained == false){ // shapelets discovery has not yet been caried out, so do so
            
            for(int i=0;i<numSeries;i++){
                ArrayList<Shapelet> tempResult =  findBestKShapeletsCache(this.numShapelets, data, this.minShapeletLength, this.maxShapeletLength, i);
                this.shapelets = combine(numShapelets,this.shapelets,tempResult);
            }
        
            this.numShapelets = this.shapelets.size();
        
            this.shapeletsTrained = true;
        }
        
        Instances output = determineOutputFormat(data);

        // for each data, get distance to each shapelet and create new instance
        for(int i = 0; i < data.numInstances(); i++){ // for each data
            Instance toAdd = new Instance(this.shapelets.size() + 1);
            int shapeletNum = 0;
            for(Shapelet s: this.shapelets){
                double dist = subseqDistance(s.content, data.instance(i), s.getSeriesAttribute());
                toAdd.setValue(shapeletNum++, dist);
            }
      
            toAdd.setValue(this.shapelets.size(), data.instance(i).classValue());
            output.add(toAdd);
        }

        return output;
    }
    public ArrayList<Shapelet> findBestKShapeletsCache(int numShapelets, Instances data, int minShapeletLength, int maxShapeletLength, int seriesNum)throws Exception{
                            
        ArrayList<Shapelet> kShapelets = new ArrayList<Shapelet>();         // store (upto) the best k shapelets overall
        ArrayList<Shapelet> seriesShapelets;                                // temp store of all shapelets for each time series

        ArrayList<Double> classDistances = getClassDistances(data); // used to calc info gain

        int numInstances = data.numInstances();
        for (int i = 0; i < numInstances; i++) {

            if (i == 0 || i % (numInstances / 4) == 0) {
                System.out.println("Currently processing instance " + (i + 1) + " of " + numInstances);
            }

            double[] wholeCandidate = extractSeries(data.instance(i), seriesNum);

            seriesShapelets = new ArrayList<Shapelet>();

            for (int length = minShapeletLength; length <= maxShapeletLength; length++) {
                double[] candidate = new double[length];
                for (int m = 0; m < length; m++) {                    
                    candidate[m] = wholeCandidate[wholeCandidate.length - length + m];
                }

                Shapelet candidateShapelet = checkCandidate(candidate, data, i, wholeCandidate.length - length, classDistances, seriesNum);
                candidateShapelet.setSeriesAttribute(seriesNum);
                seriesShapelets.add(candidateShapelet);
            }
            // now that we have all shapelets, self similarity can be fairly assessed without fear of removing potentially
            // good shapelets
            //Collections.sort(seriesShapelets);
            ArrayList<Shapelet> bestSeriesShapelets = new ArrayList<>();
            Shapelet bestOne = seriesShapelets.get(0);
            
            for(Shapelet shapelet : seriesShapelets){
                if(shapelet.qualityValue>bestOne.qualityValue){
                    bestOne = shapelet;
                }
            }
            
            bestSeriesShapelets.add(bestOne);
            kShapelets = combine(numShapelets,kShapelets,bestSeriesShapelets);
        }

        return kShapelets;
    }

    public double[] extractSeries(Instance data, int seriesNum){
        double[] wholeRow = data.toDoubleArray();
        
        double[] selected = new double[(wholeRow.length-1)/numSeries];
        
        for(int i=0;i<selected.length;i++){
            selected[i] = wholeRow[i*numSeries+seriesNum];
        }
        
        return selected;
    }
    
    protected Shapelet checkCandidate(double[] candidate, Instances data, int seriesId, int startPos, ArrayList classDistances, int seriesNum){

        ArrayList<OrderLineObj> orderline = new ArrayList<OrderLineObj>();

        for(int i = 0; i < data.numInstances(); i++){

            double distance = 0.0;
            if(i != seriesId){
                distance = subseqDistance(candidate, data.instance(i), seriesNum);  
            }
            
            double classVal = data.instance(i).classValue();

            orderline.add(new OrderLineObj(distance, classVal));
            
        }

        Shapelet shapelet = new Shapelet(candidate, seriesId, startPos, null);

        shapelet.qualityValue = calculateShapeletQuality(orderline, classDistances);

        return shapelet;
    }    
    
    public static double calculateShapeletQuality(ArrayList<OrderLineObj> orderline, ArrayList classDistances){
        double orderLineMean = 0;
        int i;
        for(i=0;i<orderline.size();i++){
            orderLineMean = orderLineMean + orderline.get(i).getDistance();
        }
        
        orderLineMean = orderLineMean / orderline.size();
        
        double distanceMean = 0;
        for(i=0;i<classDistances.size();i++){
            distanceMean = distanceMean + (double)classDistances.get(i);
        }
        
        distanceMean = distanceMean / classDistances.size();
        
        double x = 0;
        double y1=0;
        double y2=0;
        
        for(i=0;i<orderline.size();i++){
            x = x + (orderline.get(i).getDistance()-orderLineMean)*(orderline.get(i).getClassVal()-distanceMean);
            y1 = y1 + (orderline.get(i).getDistance()-orderLineMean)*(orderline.get(i).getDistance()-orderLineMean);
            y2 = y2 + (orderline.get(i).getClassVal()-distanceMean)*(orderline.get(i).getClassVal()-distanceMean);
        }
        
        return Math.abs(x/(Math.sqrt(y1*y2)));
    }
        
    protected double subseqDistance(double[] candidate, Instance timeSeriesIns, int attribute){
        return subsequenceDistance(candidate, timeSeriesIns, attribute);
    }    
    
    public double subsequenceDistance(double[] candidate, Instance timeSeriesIns, int attribute){
        return subsequenceDistance(candidate, extractSeries(timeSeriesIns, attribute));
    }

    public static double subsequenceDistance(double[] candidate, double[] timeSeries) {

        double sum;
        double[] subseq;

        sum = 0;
        // get subsequence of two that is the same lenght as one
        subseq = new double[candidate.length];

        for (int j = 0; j < subseq.length; j++) {
            subseq[j] = timeSeries[timeSeries.length - candidate.length + j];
        }
        //subseq = zNormalise(subseq, false); // Z-NORM HERE

        for(int i=1;i<subseq.length;i++){
            sum = sum + ((subseq[i]-subseq[i-1])-(candidate[i]-candidate[i-1]))*((subseq[i]-subseq[i-1])-(candidate[i]-candidate[i-1]));
        }
        
        return (sum == 0.0) ? 0.0 : (1.0 / candidate.length * sum);
    }
    
    protected ArrayList<Shapelet> combine(int k, ArrayList<Shapelet> kBestSoFar, ArrayList<Shapelet> timeSeriesShapelets){

        ArrayList<Shapelet> newBestSoFar = new ArrayList<Shapelet>();
        for(int i = 0; i < timeSeriesShapelets.size();i++){
            kBestSoFar.add(timeSeriesShapelets.get(i));
        }
        Collections.sort(kBestSoFar);
        if(kBestSoFar.size()<k) { // no need to return up to k, as there are not k shapelets yet
            return kBestSoFar;
        } 

        for(int i = 0; i < k; i++){
            newBestSoFar.add(kBestSoFar.get(i));
        }

        return newBestSoFar;
    }
    
    public static ArrayList<Double> getClassDistances(Instances data){
        ArrayList<Double> result = new ArrayList<>();
        
        double[] classValues = new double[data.numInstances()];
        int i;
        
        for(i=0;i<data.numInstances();i++){
            classValues[i] = data.instance(i).classValue();
        }
        
        double mean = getMean(classValues);
        
        for(i=0;i<data.numInstances();i++){
            result.add(i, (classValues[i] - mean)*(classValues[i] - mean));
        }
        

        return result;
    }    
    protected static ArrayList<Shapelet> removeSelfSimilar(ArrayList<Shapelet> shapelets){
        // return a new pruned array list - more efficient than removing
        // self-similar entries on the fly and constantly reindexing
        ArrayList<Shapelet> outputShapelets = new ArrayList<Shapelet>();
        boolean[] selfSimilar = new boolean[shapelets.size()];

        // to keep track of self similarity - assume nothing is similar to begin with
        for(int i = 0; i < shapelets.size(); i++){
            selfSimilar[i] = false;
        }

        for(int i = 0; i < shapelets.size();i++){
            if(selfSimilar[i]==false){
                outputShapelets.add(shapelets.get(i));
                for(int j = i+1; j < shapelets.size(); j++){
                    if(selfSimilar[j]==false && selfSimilarity(shapelets.get(i),shapelets.get(j))){ // no point recalc'ing if already self similar to something
                        selfSimilar[j] = true;
                    }
                }
            }
        }
        return outputShapelets;
    }
    private static boolean selfSimilarity(Shapelet shapelet, Shapelet candidate){
        if(candidate.seriesId == shapelet.seriesId){
            if(candidate.startPos >= shapelet.startPos && candidate.startPos < shapelet.startPos + shapelet.content.length){ //candidate starts within exisiting shapelet
                return true;
            }
            if(shapelet.startPos >= candidate.startPos && shapelet.startPos < candidate.startPos + candidate.content.length){
                return true;
            }
        }
        return false;
    }    

    public static double getMean(double[] data){
        int i;
        double sum = 0;
        
        for(i=0;i<data.length;i++){
            sum = sum+data[i];
        }
        
        return sum/data.length;
    }    
}
