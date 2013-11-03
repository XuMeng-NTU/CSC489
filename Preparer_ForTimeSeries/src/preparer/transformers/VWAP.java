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
public class VWAP {
    
    private static String VWAP;
    
    public static List<Map<String, Object>> transform(Parameters params, List<Map<String, Object>> data){

        VWAP = params.getParameter().get("VWAP").getValue();
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        Map<String, Object> entry;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();
            
            entry = data.get(i);
            
            temp.put(VWAP,((double)entry.get("AMOUNT")/(double)entry.get("VOLUME")));
            result.add(temp);
            
        }
        return result;           
    }    
}
