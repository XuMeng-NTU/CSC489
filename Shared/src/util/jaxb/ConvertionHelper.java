/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.jaxb;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xu Meng
 */
public class ConvertionHelper {
    public static Class parseClass(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClassLoader.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }    

    public static String printClass(Class cls){
        return cls.getCanonicalName();
    }
    
    public static Path parsePath(String path){
        return Paths.get(path);
    } 
    
    public static String printPath(Path path){
        return path.toString();
    }
}
