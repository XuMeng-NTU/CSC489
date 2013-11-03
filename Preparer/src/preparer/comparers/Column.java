/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.comparers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Meng
 */
public class Column {
    
    public static List<Map<String, Object>> select(List<Map<String, Object>> data, List<String> columns){
        List<Map<String, Object>> result = new ArrayList<>();
        
        int i;
        for(i=0;i<data.size();i++){
            Map<String, Object> temp = new LinkedHashMap<>();
            for(String key : columns){
                temp.put(key, data.get(i).get(key));
            }
            result.add(temp);
        }        
        
        return result;
        
    }    
}
