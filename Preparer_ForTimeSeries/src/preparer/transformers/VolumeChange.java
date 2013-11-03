/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.transformers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import template.configurations.Parameters;

/**
 *
 * @author Meng
 */
public class VolumeChange {
    private static String VOLUME_CNG;
    
    public static List<Map<String, Object>> transform(Parameters params, List<Map<String, Object>> data){

        VOLUME_CNG = params.getParameter().get("VOLUME_CNG").getValue();

        Map<String, Object> temp;
        List<Map<String, Object>> result = new ArrayList<>();
        
        temp = new LinkedHashMap<>();
        temp.put(VOLUME_CNG, 0.0);
        
        result.add(temp);
        
        Map<String, Object> entry, lastEntry;
        for(int i=1;i<data.size();i++){
            temp = new LinkedHashMap<>();
            lastEntry = data.get(i-1);
            entry = data.get(i);
            
            temp.put(VOLUME_CNG, ((double)entry.get("VOLUME")-(double)lastEntry.get("VOLUME"))/(double)lastEntry.get("VOLUME"));
         
            result.add(temp);
            
        }
        return result;           
    }        
}
