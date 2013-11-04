/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.io;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shapelet.application.Controller;
import weka.core.Instances;
import weka.core.converters.CSVSaver;

/**
 *
 * @author Meng
 */
public class Writer {
    
    public static void writeTraining(String code, Instances result){
        try {
            CSVSaver saver = new CSVSaver();
            
            saver.setInstances(result);
            saver.setFile(Controller.getInstance().getSettings().lookupData("RESULT").resolve(code).resolve("training.csv").toFile());
            saver.writeBatch();

        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void writeTesting(String code, Instances result){
        try {
            CSVSaver saver = new CSVSaver();
            saver.setInstances(result);
            saver.setFile(Controller.getInstance().getSettings().lookupData("RESULT").resolve(code).resolve("testing.csv").toFile());
            saver.writeBatch();        
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
