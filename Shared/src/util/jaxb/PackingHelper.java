/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.jaxb;

import exception.SettingsException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Meng
 */
public class PackingHelper {

    public static Object unpack(Object repr, String originalPackage, String targetPackage) {
        try {
            
            String className = repr.getClass().getSimpleName();
            Class originalClass = Class.forName(originalPackage+"."+className);
            Class targetClass = Class.forName(targetPackage+"."+className);

            Object result = targetClass.newInstance();
            
            for (Field originalField : originalClass.getDeclaredFields()) {
                originalField.setAccessible(true);
                Field targetField = targetClass.getDeclaredField(originalField.getName());
                targetField.setAccessible(true);  
                if(originalField.getType().getPackage()==null){
                    targetField.set(result, originalField.get(repr));
                } else if (originalField.getType().getPackage().getName().equalsIgnoreCase(originalPackage)) {
                    targetField.set(result, unpack(originalField.get(repr), originalPackage, targetPackage));
                } else if (originalField.get(repr) instanceof List || originalField.getType().equals(List.class)) {
                    Map<String, Object> map = new LinkedHashMap<>();

                    if(originalField.get(repr)!=null){

                        for(Object obj : (List)originalField.get(repr)){
                            Object temp = unpack(obj, originalPackage, targetPackage);
                            map.put((String) obj.getClass().getMethod("getName").invoke(obj), temp);
                        }
                    }
                    
                    targetField.set(result, map);
                } else {
                    targetField.set(result, originalField.get(repr));
                }
            }
            
            return result;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException | ClassNotFoundException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(PackingHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
    public static Object pack(Object repr, String originalPackage, String targetPackage) {
        try {
            
            String className = repr.getClass().getSimpleName();
            Class originalClass = Class.forName(originalPackage+"."+className);
            Class targetClass = Class.forName(targetPackage+"."+className);

            Object result = targetClass.newInstance();
            
            for (Field originalField : originalClass.getDeclaredFields()) {
                originalField.setAccessible(true);
                Field targetField = targetClass.getDeclaredField(originalField.getName());
                targetField.setAccessible(true);  
                
                if(originalField.getType().getPackage()==null){
                    targetField.set(result, originalField.get(repr));
                } else if (originalField.getType().getPackage().getName().equalsIgnoreCase(originalPackage)) {
                    targetField.set(result, pack(originalField.get(repr), originalPackage, targetPackage));
                } else if (originalField.get(repr) instanceof Map) {
                    List<Object> list = new ArrayList<>();
                    
                    for(Object obj : ((Map)originalField.get(repr)).values()){
                        list.add(pack(obj, originalPackage, targetPackage));
                    }

                    targetField.set(result, list);
                } else {
                    targetField.set(result, originalField.get(repr));
                }
            }
            
            return result;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalArgumentException ex) {
            Logger.getLogger(PackingHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }    
    
    
}
