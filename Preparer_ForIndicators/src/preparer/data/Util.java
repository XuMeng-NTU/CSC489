/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Meng
 */
public class Util {
    
    public static Map<String, List<Object>> shift (List<Map<String, Object>> data){
        Map<String, List<Object>> result = new LinkedHashMap<>();
        
        if(data.isEmpty()){
            return result;
        }
        
        for(String key : data.get(0).keySet()){
            result.put(key, new ArrayList<>());
        }
        
        int i;
        for(i=0;i<data.size();i++){
            for(Map.Entry<String, Object> entry : data.get(i).entrySet()){
                result.get(entry.getKey()).add(entry.getValue());
            }
        }
        
        return result;
    }
    
    public static List<Map<String, Object>> shift (Map<String, List<Object>> data){
        List<Map<String, Object>> result = new ArrayList();
        
        int i, j;
        
        int size;
        if(!data.values().iterator().hasNext()){
            size = 0;
        } else{
            size = data.values().iterator().next().size();
        }

        Map<String, Object> temp;
        for(i=0;i<size;i++){
            temp = new LinkedHashMap<>();
            result.add(temp);
        }

        for(Map.Entry<String, List<Object>> entry : data.entrySet()){
            String key = entry.getKey();
            List<Object> list = entry.getValue();
            for(j=0;j<list.size();j++){
                result.get(j).put(key, list.get(j));
            }
        }
        
        return result;
    }    
}
