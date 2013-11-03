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
public class PriceRange {
    private static String PRC_RNG;
    
    public static List<Map<String, Object>> transform(Parameters params, List<Map<String, Object>> data){

        PRC_RNG = params.getParameter().get("PRC_RNG").getValue();
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        Map<String, Object> entry;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();
            
            entry = data.get(i);
            
            temp.put(PRC_RNG,((double)entry.get("HIGH")-(double)entry.get("LOW"))/(double)entry.get("CLOSE"));
            result.add(temp);
            
        }
        return result;           
    }        
}
