/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.application;

import extractor.io.Reader;
import extractor.io.Writer;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import template.settings.Settings;
/**
 *
 * @author Meng
 */
public class Controller {
    
    public static final String REGISTRY = "settings/registry/registry.xml";
    
    private Settings settings;

    private Controller(){
        settings = new Settings(Paths.get(REGISTRY),"EXTRACTOR");
    }
    
    public Settings getSettings(){
        return settings;
    }

    public List<Map<String, Object>> extract(String code){
        List<Map<String, Object>> result = Reader.read(code);
        Writer.write(code, result);
        return result;
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
