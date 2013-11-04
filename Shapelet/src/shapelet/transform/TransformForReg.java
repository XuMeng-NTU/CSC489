/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.transform;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import static shapelet.transform.ShapeletRegTransform.getClassDistances;
import static shapelet.transform.ShapeletRegTransform.removeSelfSimilar;
import static shapelet.transform.ShapeletRegTransform.subsequenceDistance;
import static shapelet.transform.ShapeletRegTransform.zNormalise;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.shapelet.OrderLineObj;
import weka.core.shapelet.QualityBound;
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

        //Sort data in round robin order
        data = QualityBound.roundRobinData(data);
        
        if(this.shapeletsTrained == false){ // shapelets discovery has not yet been caried out, so do so
            this.shapelets = findBestKShapeletsCache(this.numShapelets, data, this.minShapeletLength, this.maxShapeletLength); // get k shapelets ATTENTION
            this.shapeletsTrained = true;
        }
        
        Instances output = determineOutputFormat(data);

        // for each data, get distance to each shapelet and create new instance
        for(int i = 0; i < data.numInstances(); i++){ // for each data
            Instance toAdd = new Instance(this.shapelets.size() + 1);
            int shapeletNum = 0;
            for(Shapelet s: this.shapelets){
                double dist = subseqDistance(s.content, data.instance(i));
                toAdd.setValue(shapeletNum++, dist);
            }
      
            toAdd.setValue(this.shapelets.size(), data.instance(i).classValue());
            output.add(toAdd);
        }

        return output;
    }
    public ArrayList<Shapelet> findBestKShapeletsCache(int numShapelets, Instances data, int minShapeletLength, int maxShapeletLength)throws Exception{
                            
        ArrayList<Shapelet> kShapelets = new ArrayList<Shapelet>();         // store (upto) the best k shapelets overall
        ArrayList<Shapelet> seriesShapelets;                                // temp store of all shapelets for each time series

        ArrayList<Double> classDistances = getClassDistances(data); // used to calc info gain

        int numInstances = data.numInstances();
        for (int i = 0; i < numInstances; i++) {

            if (i == 0 || i % (numInstances / 4) == 0) {
                System.out.println("Currently processing instance " + (i + 1) + " of " + numInstances);
            }

            double[] wholeCandidate = data.instance(i).toDoubleArray();
            seriesShapelets = new ArrayList<Shapelet>();

            for (int length = minShapeletLength; length <= maxShapeletLength; length++) {

                double[] candidate = new double[length];
                for (int m = 0; m < length; m++) {
                    candidate[m] = wholeCandidate[wholeCandidate.length - 1 - length + m];
                }

                Shapelet candidateShapelet = checkCandidate(candidate, data, i, wholeCandidate.length - 1 - length, classDistances);
                seriesShapelets.add(candidateShapelet);
            }
            // now that we have all shapelets, self similarity can be fairly assessed without fear of removing potentially
            // good shapelets
            Collections.sort(seriesShapelets);
            seriesShapelets = removeSelfSimilar(seriesShapelets);
            kShapelets = combine(numShapelets,kShapelets,seriesShapelets);
        }
        
        this.numShapelets = kShapelets.size();
        return kShapelets;
    }

    protected Shapelet checkCandidate(double[] candidate, Instances data, int seriesId, int startPos, ArrayList classDistances){

        ArrayList<OrderLineObj> orderline = new ArrayList<OrderLineObj>();

        for(int i = 0; i < data.numInstances(); i++){

            double distance = 0.0;
            if(i != seriesId){
                distance = subseqDistance(candidate, data.instance(i));  
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
            x = x + (orderline.get(i).getDistance()-orderLineMean)*((double)classDistances.get(i)-distanceMean);
            y1 = y1 + (orderline.get(i).getDistance()-orderLineMean)*(orderline.get(i).getDistance()-orderLineMean);
            y2 = y2 + ((double)classDistances.get(i)-distanceMean)*((double)classDistances.get(i)-distanceMean);
        }
        
        return Math.abs(x/(Math.sqrt(y1*y2)));
    }
        
    protected double subseqDistance(double[] candidate, Instance timeSeriesIns){
        return subsequenceDistance(candidate, timeSeriesIns);
    }    
    
    public static double subsequenceDistance(double[] candidate, Instance timeSeriesIns){
        double[] timeSeries = timeSeriesIns.toDoubleArray();
        return subsequenceDistance(candidate, timeSeries);
    }

    public static double subsequenceDistance(double[] candidate, double[] timeSeries) {

        double bestSum = Double.MAX_VALUE;
        double sum;
        double[] subseq;

        sum = 0;
        // get subsequence of two that is the same lenght as one
        subseq = new double[candidate.length];

        for (int j = 0; j < subseq.length; j++) {
            subseq[j] = timeSeries[timeSeries.length - 1 - candidate.length + j];
        }
        //subseq = zNormalise(subseq, false); // Z-NORM HERE

        for (int j = 0; j < candidate.length; j++) {
            sum += (candidate[j] - subseq[j]) * (candidate[j] - subseq[j]);
        }
        if (sum < bestSum) {
            bestSum = sum;
        }
        return (bestSum == 0.0) ? 0.0 : (1.0 / candidate.length * bestSum);
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
    
}
