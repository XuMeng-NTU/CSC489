/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.transform;

import java.util.Properties;
import static shapelet.transform.ShapeletTransformer.basicTransform;
import weka.core.Instances;
import weka.core.shapelet.QualityMeasures;
import weka.filters.timeseries.shapelet_transforms.ShapeletTransform;

/**
 *
 * @author Meng
 */
public class ShapeletRegTransformer {
    
    public static Instances[] transform(Instances training, Instances testing, Properties meta){
        
        training.setClass(training.attribute(meta.getProperty("OUTPUT_HEAD")));
        testing.setClass(testing.attribute(meta.getProperty("OUTPUT_HEAD")));
        
        return basicTransform(training, testing);
    }
    
    public static Instances[] basicTransform(Instances train, Instances test){

        ShapeletRegTransform transformer = new ShapeletRegTransform();
        
        transformer =new ShapeletRegTransform();

        int nosShapelets=(train.numAttributes()-1)*train.numInstances()/50;
        if(nosShapelets<ShapeletTransform.DEFAULT_NUMSHAPELETS)
            nosShapelets=ShapeletTransform.DEFAULT_NUMSHAPELETS;
        transformer.setNumberOfShapelets(nosShapelets);

        int minLength=2;
        int maxLength=(train.numAttributes()-1);

        transformer.setShapeletMinAndMax(minLength, maxLength);

        transformer.setQualityMeasure(QualityMeasures.ShapeletQualityChoice.F_STAT);
        transformer.turnOffLog();        
 
        Instances shapeletTraining=null;
        Instances shapeletTesting = null;
        try {
            shapeletTraining=transformer.process(train);
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
