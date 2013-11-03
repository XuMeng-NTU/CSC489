/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.io;

import exception.SettingsException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import preparer.application.Controller;
import preparer.data.DataPair;
import preparer.selectors.control.SelectorController;
import preparer.separators.control.SeparatorController;

/**
 *
 * @author Meng
 */
public class Writer {

    public static String inputHeaderString = "";
    public static String outputHeaderString = "";
    
    public static void writeValidationResult(String code, List<DataPair> data) {
        try {
            Path baseDir = Controller.getInstance().getSettings().lookupData("RESULT").resolve(code);        
            Files.createDirectories(baseDir);
            
            CsvListWriter writer = new CsvListWriter(new FileWriter(baseDir.resolve("validation.csv").toFile()), CsvPreference.STANDARD_PREFERENCE);

            for(DataPair pair : data){
                writer.write(pair.consolidate());
            }
            
            writer.flush();
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    public static void writeTestingData(String code, List<DataPair> data) {
        try {
            Path baseDir = Controller.getInstance().getSettings().lookupData("RESULT").resolve(code);        
            Files.createDirectories(baseDir);
            
            CsvListWriter writer = new CsvListWriter(new FileWriter(baseDir.resolve("testing.csv").toFile()), CsvPreference.STANDARD_PREFERENCE);

            List<Map<String, Object>> inputSample = data.get(0).getInput();
            List<Map<String, Object>> outputSample = data.get(0).getOutput();
            
            String[] inputHeader = new String[inputSample.get(0).size()*inputSample.size()];
            String[] outputHeader = new String[outputSample.get(0).size()*outputSample.size()];
            int i,j;
            
            for(i=0;i<inputSample.size();i++){
                j = 0;
                for(Entry<String, Object> entry : inputSample.get(i).entrySet()){
                    inputHeader[i*inputSample.get(i).size()+j] = entry.getKey()+"_"+i;
                    j++;
                }
            }
 
            for(i=0;i<outputSample.size();i++){
                j = 0;
                for(Entry<String, Object> entry : outputSample.get(i).entrySet()){
                    outputHeader[i*outputSample.get(i).size()+j] = "ACTUAL_"+entry.getKey()+"_"+i;
                    j++;
                }
            }            
            
            String[] header = new String[inputHeader.length+outputHeader.length];
            
            for(i=0;i<inputHeader.length;i++){
                header[i] = inputHeader[i];
            }
            
            for(i=0;i<outputHeader.length;i++){
                header[i+inputHeader.length] = outputHeader[i];
            }
            
            writer.writeHeader(header);
            
            for(DataPair pair : data){
                writer.write(pair.consolidate());
            }
            
            writer.flush();
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    public static void writeTrainingData(String code, List<DataPair> data) {
        try {
            Path baseDir = Controller.getInstance().getSettings().lookupData("RESULT").resolve(code);        
            Files.createDirectories(baseDir);
            
            CsvListWriter writer = new CsvListWriter(new FileWriter(baseDir.resolve("training.csv").toFile()), CsvPreference.STANDARD_PREFERENCE);

            List<Map<String, Object>> inputSample = data.get(0).getInput();
            List<Map<String, Object>> outputSample = data.get(0).getOutput();
            
            String[] inputHeader = new String[inputSample.get(0).size()*inputSample.size()];
            String[] outputHeader = new String[outputSample.get(0).size()*outputSample.size()];
            int i,j;
            
            for(i=0;i<inputSample.size();i++){
                j = 0;
                for(Entry<String, Object> entry : inputSample.get(i).entrySet()){
                    inputHeader[i*inputSample.get(i).size()+j] = entry.getKey()+"_"+i;
                    j++;
                }
            }
            
            inputHeaderString = inputHeader[0];
            for(i=1;i<inputHeader.length;i++){
                inputHeaderString = inputHeaderString+","+inputHeader[i];
            }
            
            for(i=0;i<outputSample.size();i++){
                j = 0;
                for(Entry<String, Object> entry : outputSample.get(i).entrySet()){
                    outputHeader[i*outputSample.get(i).size()+j] = "ACTUAL_"+entry.getKey()+"_"+i;
                    j++;
                }
            }            
            
            outputHeaderString = outputHeader[0];
            for(i=1;i<outputHeader.length;i++){
                outputHeaderString = outputHeaderString+","+outputHeader[i];
            }
            
            String[] header = new String[inputHeader.length+outputHeader.length];
            
            for(i=0;i<inputHeader.length;i++){
                header[i] = inputHeader[i];
            }
            
            for(i=0;i<outputHeader.length;i++){
                header[i+inputHeader.length] = outputHeader[i];
            }
            
            writer.writeHeader(header);
            
            for(DataPair pair : data){
                writer.write(pair.consolidate());
            }
            
            writer.flush();
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void writePredictionData(String code,DataPair data) {
        try {
            Path baseDir = Controller.getInstance().getSettings().lookupData("RESULT").resolve(code);        
            Files.createDirectories(baseDir);
            
            CsvListWriter writer = new CsvListWriter(new FileWriter(baseDir.resolve("prediction.csv").toFile()), CsvPreference.STANDARD_PREFERENCE);

            writer.write(data.consolidate());
            
            writer.flush();
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public static void writeMeta(String code){
        try {
            Path baseDir = Controller.getInstance().getSettings().lookupData("RESULT").resolve(code);
            Files.createDirectories(baseDir);
            
            Properties properties = new Properties();
            
            properties.putAll(SeparatorController.retrieveProperties());
            properties.putAll(SelectorController.retrieveProperties());
            
            properties.put("INPUT_HEAD", inputHeaderString);
            properties.put("OUTPUT_HEAD", outputHeaderString);
            
            properties.store(new FileOutputStream(baseDir.resolve("meta.properties").toFile()), null);
            
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
    private static List<String> tokenize(String string) {
        StringTokenizer tokenizer = new StringTokenizer(string, ",");
        List<String> result = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }

        return result;
    }
        
}
