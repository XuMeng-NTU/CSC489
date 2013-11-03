/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.transform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.timeseries.core.TSLagMaker;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Meng
 */
public class LagTransformer {

    public static int MIN_LAG = 1;
    public static int MAX_LAG = 10;
    
    public static Instances transform(Instances dataset, Properties meta){
        
        Instances original = new Instances(dataset);
        
        TSLagMaker maker = new TSLagMaker();
        maker.reset();
        maker.setTimeStampField("DATE_0");  
        
        maker.setMinLag(MIN_LAG);
        maker.setMaxLag(MAX_LAG);
        
        try {           
            maker.setFieldsToLag(stringToList(meta.getProperty("INPUT_HEAD")));
            
            Instances transformedDataset = maker.getTransformedData(dataset);
            
            transformedDataset.insertAttributeAt(original.attribute(meta.getProperty("OUTPUT_HEAD")), transformedDataset.numAttributes());
            
            Iterator<Instance> transformedIterator = transformedDataset.iterator();
            Iterator<Instance> originalIterator = original.iterator();
            
            while(transformedIterator.hasNext()){
                transformedIterator.next().setValue(transformedDataset.attribute(meta.getProperty("OUTPUT_HEAD")), originalIterator.next().value(original.attribute(meta.getProperty("OUTPUT_HEAD"))));
            }
                       
            transformedIterator = transformedDataset.iterator();
            int i;
            for(i=0;i<MAX_LAG;i++){
                transformedIterator.next();
                transformedIterator.remove();
            }
            
            transformedDataset.deleteAttributeAt(transformedDataset.attribute("DATE_0").index());
            
            return transformedDataset;
            
        } catch (Exception ex) {
            Logger.getLogger(LagTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    public static List<String> stringToList(String list) {
        String[] fieldNames = list.split(",");
        List<String> thelist = new ArrayList<>();
        for (String f : fieldNames) {
            if(f.indexOf("DATE")==-1){
                thelist.add(f);
            }
        }

        return thelist;
    }
      
}
