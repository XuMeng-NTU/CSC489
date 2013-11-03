/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configurer.core;

import java.util.LinkedHashMap;
import java.util.Map;
import template.settings.Settings;

/**
 *
 * @author Meng
 */
public class Configurer {

    public Map<String, Settings> settings;
    
    private Configurer(){
        settings = new LinkedHashMap<>();
    }
    
    public void register(String name, Settings settings){
        this.settings.put(name, settings);    
    }
    
    
    private static Configurer application = null;
    
    public static Configurer getInstance(){
        if(application==null){
            application = new Configurer();
        }
        
        return application;
    }    
    
    public static void destroyInstance(){
        application = null;
    }
    
}
