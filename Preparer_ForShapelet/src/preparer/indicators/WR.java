/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.indicators;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import template.configurations.Parameters;

/**
 *
 * @author Meng
 */
public class WR {
    
    private static int WR_DAYS;
    private static String WR;
    
    public static List<Map<String, Object>> calculate(Parameters params, List<Map<String, Object>> data){
        
        WR_DAYS = Integer.parseInt(params.getParameter().get("WR_DAYS").getValue());
        WR = params.getParameter().get("WR").getValue();
        
        
        Double[] highest = new Double[data.size()];
        Double[] lowest = new Double[data.size()];
        
        int position = 0;
        
        while(position<data.size()){
            if(position<WR_DAYS){
                highest[position] = null;
                lowest[position] = null;
            } else{

                Double[] temp = getHighLow(data, "CLOSE", position, WR_DAYS);
                highest[position] = temp[0];
                lowest[position] = temp[1];

            }
            position++;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();

            if(highest[i]==null || lowest[i]==null){
                temp.put(WR, null);
            } else{
                temp.put(WR, 2*WR_DAYS*(highest[i]-(double)data.get(i).get("CLOSE"))/(highest[i]-lowest[i]));
            }
            result.add(temp);
        }
        return result;        
        
    }
    
    private static Double[] getHighLow(List<Map<String, Object>> data, String key, int position, int period){

        Double highest = Double.MIN_VALUE, lowest = Double.MAX_VALUE;
        int i;
        double temp;
        for(i=0;i<period;i++){
            temp = (double)data.get(position-i).get(key);
            if(temp>highest){
                highest = temp;
            }
            
            if(temp<lowest){
                lowest = temp;
            }
        }
        
        Double[] result = new Double[2];
        result[0] = highest;
        result[1] = lowest;
        return result;
    }
    
}
