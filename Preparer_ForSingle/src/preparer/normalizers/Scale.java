/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.normalizers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import template.configurations.Parameters;

/**
 *
 * @author Meng
 */
public class Scale {
    
    private static int SCALE;
    
    public static Map<String, Object> normalize(Parameters params, Map<String, ArrayList<Object>>[] data, List<String>[] groups){
        SCALE = Integer.parseInt(params.getParameter().get("SCALE").getValue());
        
        int k,j;
        
        ArrayList<Object> temp;
        
        for(k=0;k<data.length;k++){
            for(String entry : groups[k]){
                temp = data[k].get(entry);
                if(temp!=null){
                    for (j = 0; j < temp.size(); j++) {
                        Double newValue = ((Double) temp.get(j))*SCALE;
                        temp.set(j, newValue);
                    }
                }
            }    
        }        
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("SCALE", SCALE);

        return result;           
    }
}
