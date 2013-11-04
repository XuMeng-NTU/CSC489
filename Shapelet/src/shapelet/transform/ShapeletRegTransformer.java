/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.transform;

import java.util.Properties;
import static shapelet.transform.ShapeletTransformer.basicTransform;
import weka.core.Instances;
import weka.filters.timeseries.shapelet_transforms.ShapeletTransform;

/**
 *
 * @author Meng
 */
public class ShapeletRegTransformer {
    
    public static Instances[] transform(Instances training, Instances testing, Properties meta){
        
        training.setClass(training.attribute(meta.getProperty("OUTPUT_HEAD")));
        testing.setClass(testing.attribute(meta.getProperty("OUTPUT_HEAD")));
        
        return basicTransform(training, testing, meta);
    }
    
    public static Instances[] basicTransform(Instances train, Instances test, Properties meta){

        //ShapeletRegTransform transformer = new ShapeletRegTransform();
        
        TransformForReg transformer = new TransformForReg();
        
        int numAttrPerSeries = (train.numAttributes()-1)/Integer.parseInt(meta.getProperty("INPUT_FIELD"));
        
        transformer.setNumberOfSeries(Integer.parseInt(meta.getProperty("INPUT_FIELD")));
        
        int nosShapelets=numAttrPerSeries*train.numInstances()/10;
        if(nosShapelets<ShapeletTransform.DEFAULT_NUMSHAPELETS)
            nosShapelets=ShapeletTransform.DEFAULT_NUMSHAPELETS;
        transformer.setNumberOfShapelets(nosShapelets);

        int minLength=3;
        int maxLength=numAttrPerSeries;

        transformer.setShapeletMinAndMax(minLength, maxLength);

        Instances shapeletTraining=null;
        Instances shapeletTesting = null;
        try {

            shapeletTraining = transformer.process(train);
            shapeletTesting = transformer.process(test);


        } catch (Exception ex) {
            System.out.println("Error performing the shapelet transform"+ex);
            ex.printStackTrace();
            System.exit(0);
        }
  
        Instances[] result = new Instances[2];
        result[0] = shapeletTraining;
        result[1] = shapeletTesting;
        
        return result;
    }
}
