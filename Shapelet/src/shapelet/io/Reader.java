/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import shapelet.application.Controller;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Meng
 */
public class Reader {

    public static Properties readMeta(String code){
        try {
            Properties meta = new Properties();
            meta.load(new FileInputStream(Controller.getInstance().getSettings().lookupData("ORIGINAL").resolve(code).resolve("meta.properties").toFile()));
            
            return meta;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static Instances readTraining(String code) {
        try {
            CSVLoader loader = new CSVLoader();

            loader.setSource(Controller.getInstance().getSettings().lookupData("ORIGINAL").resolve(code).resolve("training.csv").toFile());
            Instances training = loader.getDataSet();

            return training;

        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Instances readTesting(String code) {
        try {
            CSVLoader loader = new CSVLoader();
            
            loader.setSource(Controller.getInstance().getSettings().lookupData("ORIGINAL").resolve(code).resolve("testing.csv").toFile());
            Instances testing = loader.getDataSet();

            return testing;
            
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
}
