/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.transform;

import java.util.Properties;
import weka.core.Instances;
import weka.core.shapelet.QualityMeasures;
import weka.filters.timeseries.shapelet_transforms.ShapeletTransform;

/**
 *
 * @author Meng
 */
public class ShapeletTransformer {
    
    public static Instances transform(Instances dataset, Properties meta){
        
        dataset.setClass(dataset.attribute(meta.getProperty("OUTPUT_HEAD")));
        
        return basicTransform(dataset);
    }
    
    public static Instances basicTransform(Instances train){

        ShapeletTransform transformer = new ShapeletTransform();
        
        transformer =new ShapeletTransform();

        int nosShapelets=(train.numAttributes()-1)*train.numInstances()/10;
        if(nosShapelets<ShapeletTransform.DEFAULT_NUMSHAPELETS)
            nosShapelets=ShapeletTransform.DEFAULT_NUMSHAPELETS;
        transformer.setNumberOfShapelets(nosShapelets);

        int minLength=2;
        int maxLength=(train.numAttributes()-1);

        transformer.setShapeletMinAndMax(minLength, maxLength);

        transformer.setQualityMeasure(QualityMeasures.ShapeletQualityChoice.F_STAT);
        transformer.turnOffLog();        
 
        Instances shapeletT=null;
        try {
            shapeletT=transformer.process(train);
        } catch (Exception ex) {
            System.out.println("Error performing the shapelet transform"+ex);
            ex.printStackTrace();
            System.exit(0);
        }
  
        return shapeletT;
    }
}
