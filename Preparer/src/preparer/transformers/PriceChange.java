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
public class PriceChange {
    private static String OPEN_CNG;
    private static String CLOSE_CNG;
    
    public static List<Map<String, Object>> transform(Parameters params, List<Map<String, Object>> data){

        OPEN_CNG = params.getParameter().get("OPEN_CNG").getValue();
        CLOSE_CNG = params.getParameter().get("CLOSE_CNG").getValue();
        
        Map<String, Object> temp;
        List<Map<String, Object>> result = new ArrayList<>();
        
        temp = new LinkedHashMap<>();
        temp.put(OPEN_CNG, 0.0);
        temp.put(CLOSE_CNG, 0.0);
        
        result.add(temp);
        
        Map<String, Object> entry, lastEntry;
        for(int i=1;i<data.size();i++){
            temp = new LinkedHashMap<>();
            lastEntry = data.get(i-1);
            entry = data.get(i);
            
            temp.put(OPEN_CNG, ((double)entry.get("OPEN")-(double)lastEntry.get("CLOSE"))/(double)lastEntry.get("CLOSE"));
            temp.put(CLOSE_CNG,((double)entry.get("CLOSE")-(double)lastEntry.get("CLOSE"))/(double)lastEntry.get("CLOSE"));
            
            result.add(temp);
            
        }
        return result;           
    }        
}
