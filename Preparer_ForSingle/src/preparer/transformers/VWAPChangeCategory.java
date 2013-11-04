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
public class VWAPChangeCategory {
    
    private static String VWAP_CHANGE_CATEGORY;
    
    
    public static List<Map<String, Object>> transform(Parameters params, List<Map<String, Object>> data){

        VWAP_CHANGE_CATEGORY = params.getParameter().get("VWAP_CHANGE_CATEGORY").getValue();
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        Map<String, Object> entry;
        
        double oldVwap = (double) data.get(0).get("OPEN");
        double vwap;
        
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();
            
            entry = data.get(i);
            
            vwap = (double)entry.get("AMOUNT")/(double)entry.get("VOLUME");
            
            double vwapChange = (vwap-oldVwap)/oldVwap;
//            
//            int category = (int) ((Math.abs(vwapChange)-0.005)/0.01);
//            
//            if(vwapChange<0){
//                category = -category;
//            }
            
            if(vwapChange>=-0.005 && vwapChange<=0.005){
                temp.put(VWAP_CHANGE_CATEGORY, 0+"-Class");
            } else if(vwapChange>=0.005 && vwapChange<=0.01){
                temp.put(VWAP_CHANGE_CATEGORY, 1+"-Class");
            } else if(vwapChange>=0.01 && vwapChange<=0.02){
                temp.put(VWAP_CHANGE_CATEGORY, 2+"-Class");
            } else if(vwapChange>=0.02 && vwapChange<=0.03){
                temp.put(VWAP_CHANGE_CATEGORY, 3+"-Class");
            } else if(vwapChange>=0.03 && vwapChange<=0.05){
                temp.put(VWAP_CHANGE_CATEGORY, 4+"-Class");
            } else if(vwapChange>=0.05 && vwapChange<=0.07){
                temp.put(VWAP_CHANGE_CATEGORY, 5+"-Class");
            } else if(vwapChange>=0.07 && vwapChange<=0.09){
                temp.put(VWAP_CHANGE_CATEGORY, 6+"-Class");
            } else if(vwapChange>=0.09){
                temp.put(VWAP_CHANGE_CATEGORY, 7+"-Class");
            }else if(vwapChange>=-0.01 && vwapChange<=-0.005){
                temp.put(VWAP_CHANGE_CATEGORY, -1+"-Class");
            } else if(vwapChange>=-0.02 && vwapChange<=-0.01){
                temp.put(VWAP_CHANGE_CATEGORY, -2+"-Class");
            } else if(vwapChange>=-0.03 && vwapChange<=-0.02){
                temp.put(VWAP_CHANGE_CATEGORY, -3+"-Class");
            } else if(vwapChange>=-0.05 && vwapChange<=-0.03){
                temp.put(VWAP_CHANGE_CATEGORY, -4+"-Class");
            } else if(vwapChange>=-0.07 && vwapChange<=-0.05){
                temp.put(VWAP_CHANGE_CATEGORY, -5+"-Class");
            } else if(vwapChange>=-0.09 && vwapChange<=-0.07){
                temp.put(VWAP_CHANGE_CATEGORY, -6+"-Class");
            } else if(vwapChange<=-0.09){
                temp.put(VWAP_CHANGE_CATEGORY, -7+"-Class");
            }

            oldVwap = vwap;
            
            result.add(temp);
            
        }
        return result;           
    }    
}
