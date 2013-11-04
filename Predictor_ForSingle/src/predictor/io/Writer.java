/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predictor.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import predictor.application.Controller;

/**
 *
 * @author Meng
 */
public class Writer {
    public static void writeEvaluationResult(String code, List<Double> result){
        try {
            Path filepath = Controller.getInstance().getSettings().lookupData("VALIDATION").resolve(code+".csv");
            CsvListWriter writer = new CsvListWriter(new FileWriter(filepath.toFile()), CsvPreference.STANDARD_PREFERENCE);
            
            for(Double num : result){
                writer.write(num);
            }
            
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
