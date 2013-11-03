/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.application;

import java.nio.file.Paths;
import java.util.Properties;
import timeseries.io.Reader;
import timeseries.io.Writer;
import template.settings.Settings;
import timeseries.transform.LagTransformer;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 *
 * @author Meng
 */
public class Controller {
    public static final String REGISTRY = "settings/registry/registry.xml";

    private Settings settings;
    
    private Controller(){
        settings = new Settings(Paths.get(REGISTRY), "TIME_SERIES");

    }
    
    
    public Settings getSettings(){
        return settings;
    }
    
    public void transform(String code){
        Instances training = Reader.readTraining(code);
        Instances testing = Reader.readTesting(code);
        Properties meta = Reader.readMeta(code);
        Writer.writeTraining(code, LagTransformer.transform(training, meta));
        Writer.writeTesting(code, LagTransformer.transform(testing, meta));
        
    }
    

    private static Controller application = null;
    
    public static Controller getInstance(){
        if(application==null){
            application = new Controller();
        }
        
        return application;
    }    
    
    public static void destroyInstance(){
        application = null;
    }
        
}
