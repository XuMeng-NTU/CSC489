/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predictor.io;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import predictor.application.Controller;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Meng
 */
public class Reader {

    public static Instances readTraining(String code) {
        try {
            CSVLoader loader = new CSVLoader();

            loader.setDateAttributes("1");
            loader.setDateFormat("MM/dd/yy");
            
            loader.setSource(Controller.getInstance().getSettings().lookupData("ORIGINAL").resolve(code).resolve("training.csv").toFile());
            Instances training = loader.getDataSet();

            training.setClass(training.attribute("OUTPUTVWAP_CHANGE_0"));

            return training;

        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Instances readTesting(String code) {
        try {
            CSVLoader loader = new CSVLoader();

            loader.setDateAttributes("1");
            loader.setDateFormat("MM/dd/yy");
            
            loader.setSource(Controller.getInstance().getSettings().lookupData("ORIGINAL").resolve(code).resolve("testing.csv").toFile());
            Instances testing = loader.getDataSet();
            testing.setClass(testing.attribute("OUTPUTVWAP_CHANGE_0"));

            return testing;
            
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
}
