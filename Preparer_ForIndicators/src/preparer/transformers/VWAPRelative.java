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
public class VWAPRelative {
    
    private static String VWAP;
    private static String VWAP_CHANGE;
    private static String RELATIVE_HIGH;
    private static String RELATIVE_LOW;
    
    
    public static List<Map<String, Object>> transform(Parameters params, List<Map<String, Object>> data){

        VWAP = params.getParameter().get("VWAP").getValue();
        VWAP_CHANGE = params.getParameter().get("VWAP_CHANGE").getValue();
        RELATIVE_HIGH = params.getParameter().get("RELATIVE_HIGH").getValue();
        RELATIVE_LOW = params.getParameter().get("RELATIVE_LOW").getValue();
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        Map<String, Object> entry;
        
        double oldVwap = (double) data.get(0).get("OPEN");
        double vwap;
        
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();
            
            entry = data.get(i);
            
            vwap = (double)entry.get("AMOUNT")/(double)entry.get("VOLUME");
            
            temp.put(VWAP,vwap);
            temp.put(VWAP_CHANGE, (vwap-oldVwap)/oldVwap);
            temp.put(RELATIVE_HIGH, ((double)entry.get("HIGH")-vwap)/vwap);
            temp.put(RELATIVE_LOW, ((double)entry.get("LOW")-vwap)/vwap);
       
            oldVwap = vwap;
            
            result.add(temp);
            
        }
        return result;           
    }    
}
