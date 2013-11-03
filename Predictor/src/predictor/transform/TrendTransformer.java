/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predictor.transform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import predictor.io.Reader;
import weka.classifiers.timeseries.core.TSLagMaker;
import weka.core.Instances;
import weka.core.converters.CSVSaver;

/**
 *
 * @author Meng
 */
public class TrendTransformer {

    public static void main(String[] args) {
        try {

            Instances dataset = Reader.readTraining("600000");

            TSLagMaker maker = new TSLagMaker();
            maker.reset();
            maker.setTimeStampField("DATE_0");
            
            //String fieldsToLag = "VWAP_CHANGE_0,VOLUME_CNG_0,MACD_LINE_0,MACD_SIGNAL_0,KDJ_K_0,KDJ_D_0,RSI_0,WR_0,BOLL_BANDWIDTH_0,BOLL_PERCENT_0";
            String fieldsToLag="VWAP_CHANGE_0";
            
            maker.setFieldsToLag(stringToList(fieldsToLag));
            
//            dataset = weka.classifiers.timeseries.core.Utils.replaceMissing(dataset,
//                    fieldsToForecast, maker.getTimeStampField(), false,
//                    maker.getPeriodicity(), maker.getSkipEntries(),
//                    new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>());
//            
            Instances transformedDataset = maker.getTransformedData(dataset);
            
            CSVSaver saver = new CSVSaver();
            saver.setInstances(transformedDataset);
            saver.setFile(new File("G:\\Github\\CSC489\\Shared\\data\\preparation\\600000\\t.csv"));
            saver.writeBatch();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    public static List<String> stringToList(String list) {
        String[] fieldNames = list.split(",");
        List<String> thelist = new ArrayList<String>();
        for (String f : fieldNames) {
            thelist.add(f);
        }

        return thelist;
    }
      
}
