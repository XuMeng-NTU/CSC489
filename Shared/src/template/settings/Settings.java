/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package template.settings;

import configurer.core.Configurer;
import exception.SettingsException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import template.registry.Configuration;
import template.registry.Registry;
import template.registry.Resource;
import template.registry.Component;

/**
 *
 * @author Meng
 */
public class Settings {
    
    private Map<String, Map<String, Object>> components;
    private Map<String, Map<String, Object>> configurations;
    private Map<String, Path> data;
    
    private String configuration_type;
    private String configuration_profile;
    
    
    public Settings(Path path, String name){
       
        Registry registry = Registry.load(path);
      
        components = new LinkedHashMap<>();
        for(Entry<String, Component> entry : registry.getComponents().getComponent().entrySet()){         
            components.put(entry.getKey(), loadComponent(entry.getValue()));
        }
        
        configurations = new LinkedHashMap<>();
        for(Entry<String, Configuration> entry : registry.getConfigurations().getConfiguration().entrySet()){   
            configurations.put(entry.getKey(), loadConfiguration(entry.getValue()));
        }
        
        data = new LinkedHashMap<>();
        for(Entry<String, Resource> entry : registry.getData().getResources().getResource().entrySet()){
            data.put(entry.getKey(), entry.getValue().getPath());
        }        
        
        Configurer.getInstance().register(name, this);
        
    }

    public Map<String, Map<String, Object>> getComponents(){
        return components;
    }
    
    public Map<String, Map<String, Object>> getConfigurations(){
        return configurations;
    }
    
    public Map<String, Path> getData(){
        return data;
    }
    
    public Object getCurrentConfiguration(){
        return lookupConfiguration(configuration_type, configuration_profile);
    }

    public void setCurrentConfiguration(String config_type, String config_profile){
        configuration_type = config_type;
        configuration_profile = config_profile;
    }    
    
    public Object lookupConfiguration(String category, String name){
        return configurations.get(category).get(name);
    }
    
    public Object lookupComponent(String category, String name){
        return components.get(category).get(name);
    }
    
    public Path lookupData(String key){
        return data.get(key);
    }    
    
    private static Map<String, Object> loadComponent(Component component){    
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, Resource> entry : component.getResources().getResource().entrySet()) {              
                result.put(entry.getKey(), component.getRepr().getMethod("load", Path.class).invoke(null, entry.getValue().getPath()));
            }      
            return result;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
    private static Map<String, Object> loadConfiguration(Configuration config){    
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, Resource> entry : config.getResources().getResource().entrySet()) {              
                result.put(entry.getKey(), config.getRepr().getMethod("load", Path.class).invoke(null, entry.getValue().getPath()));
            }      
            return result;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
}
