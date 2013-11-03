/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.io;

import exception.SettingsException;
import extractor.application.Controller;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import template.metadata.Meta;
import template.metadata.Metadata;

/**
 *
 * @author Meng
 */
public class Writer {
    
    public static void write(String code, List<Map<String, Object>> records){
        try {
            CsvMapWriter writer = new CsvMapWriter(new FileWriter(Controller.getInstance().getSettings().lookupData("RESULT").resolve(code + ".csv").toFile()), CsvPreference.STANDARD_PREFERENCE);

            String[] header = null;
  
            if(records==null || records.isEmpty()){
                throw new SettingsException("Insufficient Data");
            }

            for(Map<String, Object> record : records){
                if (header == null) {
                    header = extractKeys(record);
        
                    Metadata metadata = new Metadata();
                    Map<String, Meta> headers = metadata.getMeta();
                    headers = new LinkedHashMap<>();
                    
                    for(String colName : header){
                        Class cls;
                        if(colName.equals("DATE")){
                            cls = ParseDate.class;
                        } else{
                            cls = ParseDouble.class;
                        }
                        
                        Meta meta = new Meta(colName, colName, colName, cls);
                        headers.put(colName, meta);
                    }

                    metadata.setMeta(headers);
                    Metadata.dump(Controller.getInstance().getSettings().lookupData("RESULT").resolve(code + ".meta"), metadata);
                    
                    writer.writeHeader(header);
                }
                writer.write(record, header);
            }
            writer.flush();
            writer.close();            
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException("Insufficient Data");
        }
        
    }
    
    private static String[] extractKeys(Map<String, Object> map) {       
        Set<String> keySet = map.keySet();
        String[] result = new String[keySet.size()];

        return keySet.toArray(result);
    }    
        
    
}
