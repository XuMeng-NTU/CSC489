/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.io;

import exception.SettingsException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;
import preparer.application.Controller;
import template.metadata.Metadata;

/**
 *
 * @author Meng
 */
public class Reader {
    
    public static Metadata readOriginalMetadata(String code){
        return Metadata.load(Controller.getInstance().getSettings().lookupData("ORIGINAL").resolve(code + ".meta"));
    }
    
    public static List<Map<String, Object>> readOriginal(String code){
        try {
            CsvMapReader reader = new CsvMapReader(new FileReader(Controller.getInstance().getSettings().lookupData("ORIGINAL").resolve(code + ".csv").toFile()), CsvPreference.STANDARD_PREFERENCE);
            String[] header = reader.getHeader(true);
            
            CellProcessor[] processors = new CellProcessor[header.length];
            Metadata metadata = readOriginalMetadata(code);
            
            for(int i=0;i<processors.length;i++){
                Class repr = metadata.getMeta().get(header[i]).getRepr();
                if(repr.equals(ParseDate.class)){
                    processors[i] = (CellProcessor) repr.getConstructor(String.class).newInstance("yyyyMMdd");
                } else{
                    processors[i] = (CellProcessor) repr.newInstance();
                }
            }            
            
            List<Map<String, Object>> data = new ArrayList<>();
            
            Map<String, Object> temp = reader.read(header, processors);
            while(temp!=null){
                data.add(temp);
                temp = reader.read(header, processors);
            }
         
            return data;            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
    public static List<List<Object>> readValidation(String code){
        try {
            CsvListReader reader = new CsvListReader(new FileReader(Controller.getInstance().getSettings().lookupData("VALIDATION").resolve(code + ".csv").toFile()), CsvPreference.STANDARD_PREFERENCE);
        
            List<List<Object>> result = new ArrayList<>();
            List<String> row = reader.read();

            CellProcessor[] processors = new CellProcessor[row.size()];

            for (int i = 0; i < processors.length; i++) {
                processors[i] = new ParseDouble();
            }
            
            result.add(reader.executeProcessors(processors));
            
            List<Object> temp = reader.read(processors);
            
            while(temp!=null){
                result.add(temp);
                temp = reader.read(processors);
            }
            
            return result;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }

    public static List<Object> readPrediction(String code){
        try {
            CsvListReader reader = new CsvListReader(new FileReader(Controller.getInstance().getSettings().lookupData("PREDICTION").resolve(code + ".csv").toFile()), CsvPreference.STANDARD_PREFERENCE);
        
            List<String> row = reader.read();

            CellProcessor[] processors = new CellProcessor[row.size()];

            for (int i = 0; i < processors.length; i++) {
                processors[i] = new ParseDouble();
            }
            
            return reader.executeProcessors(processors);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }    
    
}