/*
This class is a helper class to describe the structure of our shapelet code and
* demonstrate how to use it.
 *copyright Anthony Bagnall
 * @author Anthony Bagnall, Jason Lines, Jon Hills and Edgaras Baranauskas
 */
package shapelet.others;

/* Package   weka.core.shapelet.* contains the classes 
 *          Shapelet that stores the actual shapelet, its location
 * in the data set, the quality assessment and a reference to the quality 
 * measure used
 *          BinaryShapelet that extends Shapelet to store the threshold used to 
 *  measure quality
 *          OrderLineObj: A simple class to store <distance,classValue> pairs 
 * for calculating the quality of a shapelet
 *          QualityMeasures: A class to store shapelet quality measure 
 * implementations. This includes an abstract quality measure class,
 * and implementations of each of the four shapelet quality measures
 *          QualityBound: A class to store shapelet quality measure bounding 
 * implementations. This is used to determine whether an early abandonment is 
 * permissible for the four quality measures.
 */
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.shapelet.*;


/* package weka.filters.timeseries.shapelet_transforms.* contains
 *      ShapeletTransform: Enumerative search to find the best k shapelets.
 *        ShapeletTransformDistCaching: subclass of ShapeletTransform that 
 * uses the distance caching algorithm described in Mueen11. This is the fastest
 * exact approach, but is memory intensive. 
 *        ShapeletTransformOnlineNorm: subclass of ShapeletTransform that uses  
 distance online normalisation and early abandon described in ??. Not as fast,
 * but does not require the extra memory.
 *      ClusteredShapeletTransform: contains a ShapeletTransform, and does post 
 * transformation clustering. 
*       
* */
import weka.filters.timeseries.shapelet_transforms.*;

/* package weka.classifiers.trees.shapelet_trees.* contains
 *  ShapeletTreeClassifier: implementation of a shapelet tree to match the 
 * description on the original paper.
 * 4x tree classifiers based on the alternative distance measures in class 
 * QualityMeasures.
 */
import weka.classifiers.trees.shapelet_trees.*;
import weka.core.*;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.SimpleBatchFilter;
public class ShapeletExamples {

    public static ShapeletTransform st;
    public static Instances basicTransformExample(Instances train){
 /*Class to demonstrate the usage of the ShapeletTransform. Returns the 
  * transformed set of instances  
  */
        st =new ShapeletTransform();
/*The number of shapelets defaults to 100. we recommend setting it to a large
value, since there will be many duplicates and there is little overhead in 
* keeping a lot (although the shapelet early abandon becomes less efficient).
* 
*/
//Let m=train.numAttributes()-1 (series length)
//Let n=   train.numInstances() (number of series)      
        int nosShapelets=(train.numAttributes()-1)*train.numInstances()/5;
        if(nosShapelets<ShapeletTransform.DEFAULT_NUMSHAPELETS)
            nosShapelets=ShapeletTransform.DEFAULT_NUMSHAPELETS;
        st.setNumberOfShapelets(nosShapelets);
/* Two other key parameters are minShapeletLength and maxShapeletLength. For 
 * each value between these two, a full search is performed, which is 
 * order (m^2n^2), so clearly there is a time/accuracy trade off. Defaults 
 * to min of 3 max of 30.
 */
        int minLength=5;
        int maxLength=(train.numAttributes()-1)/10;
        if(maxLength<ShapeletTransform.DEFAULT_MINSHAPELETLENGTH)
            maxLength=ShapeletTransform.DEFAULT_MINSHAPELETLENGTH;
        st.setShapeletMinAndMax(minLength, maxLength);

/*Next you need to set the quality measure. This defaults to IG, but         
 * we recommend using the F stat. It is faster and (debatably) more accurate.
 */
        st.setQualityMeasure(QualityMeasures.ShapeletQualityChoice.F_STAT);
// You can set the filter to output details of the shapelets or not  
        st.setLogOutputFile("ShapeletExampleLog.csv");
// Alternatively, you can turn the logging off
//        st.turnOffLog();        
 
/* Thats the basic options. Now you need to perform the transform.
 * ShapeletTransform extends the weka SimpleBatchFilter, but we have made 
 * the method process public to make usage easier.
 */
        Instances shapeletT=null;
        try {
            shapeletT=st.process(train);
        } catch (Exception ex) {
            System.out.println("Error performing the shapelet transform"+ex);
            ex.printStackTrace();
            System.exit(0);
        }
        return shapeletT;
    }
    
    public static Instances clusteredShapeletTransformExample(Instances train){
/* The class ClusteredShapeletTransform contains a ShapeletTransform and
 * post transform clusters it. You can either perform the transform outside of 
 * the ClusteredShapeletTransform or leave it to do it internally.
 * 
 */

        Instances shapeletT=null;
//Cluster down to 10% of the number.        
        int nosShapelets=(train.numAttributes()-1)*train.numInstances()/50;
        ClusteredShapeletTransform cst = new ClusteredShapeletTransform(st,nosShapelets);
        System.out.println(" Clustering down to "+nosShapelets+" Shapelets");
        System.out.println(" From "+st.getNumberOfShapelets()+" Shapelets");
        
        try {
            shapeletT=cst.process(train);
        } catch (Exception ex) {
            System.out.println("Error performing the shapelet clustering"+ex);
            
            ex.printStackTrace();
            System.exit(0);
        }
        return shapeletT;

    }
    
    public static void initializeShapelet(ShapeletTransform s,Instances train){
//       int nosShapelets=(train.numAttributes()-1)*train.numInstances()/5;
       s.setNumberOfShapelets(1);        
       int minLength=15;
       int maxLength=36;
//       int maxLength=(train.numAttributes()-1)/10;
       s.setShapeletMinAndMax(minLength, maxLength);
       s.setQualityMeasure(QualityMeasures.ShapeletQualityChoice.F_STAT);
       s.supressOutput();
       s.turnOffLog();
    }
    public static void distanceOptimizations(Instances train){
        Instances shapeletT=null;
        ShapeletTransform s1=new ShapeletTransform();
        initializeShapelet(s1,train);
        ShapeletTransformOnlineNorm s2=new ShapeletTransformOnlineNorm();
        initializeShapelet(s2,train);
        ShapeletTransformDistCaching s3=new ShapeletTransformDistCaching();
        initializeShapelet(s3,train);
        DecimalFormat df =new DecimalFormat("###.####");
        long t1=0;
        long t2=0;
        double time1,time2,time3;
        try {
            t1=System.currentTimeMillis();
            shapeletT=s1.process(train);
            t2=System.currentTimeMillis();
            time1=((t2-t1)/1000.0);
            t1=System.currentTimeMillis();
            shapeletT=s2.process(train);
            t2=System.currentTimeMillis();
            time2=((t2-t1)/1000.0);
            t1=System.currentTimeMillis();
            shapeletT=s3.process(train);
            t2=System.currentTimeMillis();
            time3=((t2-t1)/1000.0);
            System.out.println("TIME (seconds)");
            System.out.println("No Optimization\t Online Norm/Early Abandon\t Distance caching");
            System.out.println(df.format(time1)+"\t\t\t"+df.format(time2)+"\t\t\t"+df.format(time3));
            System.out.println("TIME REDUCTION\t Online Norm/Early Abandon\t Distance caching");
            System.out.println("\t\t\t"+(int)(100.0*time2/time1)+"% \t\t\t"+(int)(100.0*time3/time1)+"%");
            System.out.println("SPEED UP\t Online Norm/Early Abandon\t Distance caching");
            System.out.println("\t\t\t"+df.format(time1/time2)+"\t\t\t"+df.format(time1/time3));
        } catch (Exception ex) {
            System.out.println("Error performing the shapelet transform"+ex);
            ex.printStackTrace();
            System.exit(0);
        }       
    }
    public static void shapeletEarlyAbandons(Instances train){
//Time the speed up from early abandon of the four distance measures.

        //IG:         
        ShapeletTransform[] s=new ShapeletTransform[4];
        ShapeletTransform[] pruned=new ShapeletTransform[4];
        for(int i=0;i<s.length;i++){
            s[i]=new ShapeletTransformDistCaching();
            pruned[i]=new ShapeletTransformDistCaching();
        }
        for(ShapeletTransform s1:s){
            initializeShapelet(s1,train);
            s1.setCandidatePruning(false);
        }
        for(ShapeletTransform s1:pruned){
            initializeShapelet(s1,train);
            s1.setCandidatePruning(true);
        }
        QualityMeasures.ShapeletQualityChoice[] choices=QualityMeasures.ShapeletQualityChoice.values();
        for(int i=0;i<s.length;i++){
            s[i].setQualityMeasure(choices[i]);
            pruned[i].setQualityMeasure(choices[i]);
        }
        long t1,t2;
        double time1,time2;
        DecimalFormat df =new DecimalFormat("###.####");
        try {
            for(int i=0;i<s.length;i++){
                t1=System.currentTimeMillis();
                s[i].process(train);
                t2=System.currentTimeMillis();
                time1=((t2-t1)/1000.0);
                t1=System.currentTimeMillis();
                pruned[i].process(train);
                t2=System.currentTimeMillis();
                time2=((t2-t1)/1000.0);
                System.out.println(" ********* QUALITY MEASURE ="+s[i].getQualityMeasure()+"  **********");
                System.out.println(" NO ABANDON \t\t ABANDON\t\t ABANDON/(NO ABANDON)%\t\t SPEED UP ");
                System.out.println(df.format(time1)+"\t\t\t"+df.format(time2)+"\t\t\t"+(int)(100.0*(time2/time1))+"%"+"\t\t\t"+df.format(time1/time2));
                
            }
       } catch (Exception ex) {
            System.out.println("Error performing the shapelet transform"+ex);
            ex.printStackTrace();
            System.exit(0);
        }       
        
    }
    public static void main(String[] args){
		Instances train=null,test=null;
		FileReader r;
		try{		
			//r= new FileReader("G:\\Github\\CSC489\\Shared\\data\\preparation\\600000\\training.csv"); 
                        
                        CSVLoader loader = new CSVLoader();
                        loader.setSource(new File("G:\\Github\\CSC489\\Shared\\data\\preparation\\600000\\training.csv"));
                        train = loader.getDataSet();

			//train = new Instances(r); 
			train.setClassIndex(train.numAttributes()-1);
                        
                        loader.setSource(new File("G:\\Github\\CSC489\\Shared\\data\\preparation\\600000\\testing.csv"));
			//r= new FileReader("SonyAIBORobotSurface_TEST.arff"); 
			//test = new Instances(r); 
                        test = loader.getDataSet();
			test.setClassIndex(test.numAttributes()-1);
                        
		}
		catch(Exception e)
		{
			System.out.println("Unable to load data. Exception thrown ="+e);
			System.exit(0);
		}
 /*               System.out.println("****************** PERFORMING BASIC TRANSFORM *******");
                Instances shapeletT=basicTransformExample(train);
                System.out.println(" Transformed data set ="+shapeletT);
                System.out.println("\n **************** CLUSTERING *******");
                shapeletT=clusteredShapeletTransformExample(train);
                System.out.println(" Clustered Transformed data set ="+shapeletT);
                System.out.println("\n ******Distance calculation optimizations *******");
                distanceOptimizations(train);                
 */               System.out.println("\n ******Shapelet Early Abandons *******");
                shapeletEarlyAbandons(train);               
    }
}
