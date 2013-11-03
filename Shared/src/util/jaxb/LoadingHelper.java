/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.jaxb;

import java.nio.file.Path;

/**
 *
 * @author Meng
 */
public class LoadingHelper {
    public static <E, T> T load(Path path, Class<?> E, Class<?> T){
        E xmlObj = (E) BindingHelper.unmarshal(E, path);
        return (T) PackingHelper.unpack(xmlObj, E.getPackage().getName(), T.getPackage().getName());
    }
    
    public static <E,T> void dump(Path path, Class<?> E, Class<?> T, T obj){
        E xmlObj = (E) PackingHelper.pack(obj, T.getPackage().getName(), E.getPackage().getName());
        BindingHelper.marshal(E, xmlObj, path); 
    }
}
