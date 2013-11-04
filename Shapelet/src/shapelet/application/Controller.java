/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.application;

import java.nio.file.Paths;
import java.util.Properties;
import shapelet.io.Reader;
import shapelet.io.Writer;
import shapelet.transform.ShapeletTransformer;
import template.settings.Settings;
import weka.core.Instances;

/**
 *
 * @author Meng
 */
public class Controller {
    public static final String REGISTRY = "settings/registry/registry.xml";

    private Settings settings;
    
    private Controller(){
        settings = new Settings(Paths.get(REGISTRY), "SHAPELET");

    }
    
    
    public Settings getSettings(){
        return settings;
    }
    
    public void transform(String code){
        Instances training = Reader.readTraining(code);
        Instances testing = Reader.readTesting(code);
        Properties meta = Reader.readMeta(code);
        //ShapeletTransformer.transform(training, meta);

        Writer.writeTraining(code, ShapeletTransformer.transform(training, meta));
        Writer.writeTesting(code, ShapeletTransformer.transform(testing, meta));
        
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
