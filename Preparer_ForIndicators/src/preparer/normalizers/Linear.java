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
public class Linear {
    
    private static int UPPER_BOUND;
    private static int LOWER_BOUND;

    public static void denormalize(Map<String, Object> params, Map<String, List<Object>>[] data, List<String>[] groups){
        Double upper_bound = (Double)params.get("MAX");
        Double lower_bound = (Double)params.get("MIN");
       
        int j, k;
        List<Object> temp;
        
        Double max = Double.MIN_VALUE;
        Double min = Double.MAX_VALUE;
        
        for(k=0;k<data.length;k++){
            for(String entry : groups[k]){
                temp = data[k].get(entry);      
                if(temp!=null){
                    for(j=0;j<temp.size();j++){
                        if((Double)temp.get(j)>max){
                            max = (Double)temp.get(j);
                        }
                        if((Double)temp.get(j)<min){
                            min = (Double)temp.get(j);
                        }
                    }
                }
            }           
        }
        
        Double diff = max-min;
        for(k=0;k<data.length;k++){
            for(String entry : groups[k]){
                temp = data[k].get(entry);
                       
                if(temp!=null){
                    for (j = 0; j < temp.size(); j++) {
                        Double newValue = (((Double) temp.get(j)) - min) / diff * (upper_bound - lower_bound) + lower_bound;
                        temp.set(j, newValue);
                    }
                }
            }    
        }
    }
    
    public static Map<String, Object> normalize(Parameters params, Map<String, ArrayList<Object>>[] data, List<String>[] groups){

        UPPER_BOUND = Integer.parseInt(params.getParameter().get("UPPER_BOUND").getValue());
        LOWER_BOUND = Integer.parseInt(params.getParameter().get("LOWER_BOUND").getValue());
        
        Double max = Double.MIN_VALUE;
        Double min = Double.MAX_VALUE;
        
        int i, j, k;
        ArrayList<Object> temp;
        
        for(k=0;k<data.length;k++){
            for(String entry : groups[k]){
                temp = data[k].get(entry);
                if(temp!=null){
                    for(j=0;j<temp.size();j++){
                        if((Double)temp.get(j)>max){
                            max = (Double)temp.get(j);
                        }
                        if((Double)temp.get(j)<min){
                            min = (Double)temp.get(j);
                        }
                    }
                }
            }           
        }
        
        Double diff = max-min;
        for(k=0;k<data.length;k++){
            for(String entry : groups[k]){
                temp = data[k].get(entry);
                if(temp!=null){
                    for (j = 0; j < temp.size(); j++) {
                        Double newValue = (((Double) temp.get(j)) - min) / diff * (UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
                        temp.set(j, newValue);
                    }
                }
            }    
        }
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("MIN", min);
        result.put("MAX", max);
        return result;        
        
    }

}
