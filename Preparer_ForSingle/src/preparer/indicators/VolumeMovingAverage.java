/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.indicators;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import preparer.indicators.control.Util;
import template.configurations.Parameters;

/**
 *
 * @author Meng
 */
public class VolumeMovingAverage {
    private static int VOLUME_MA_DAYS;
    
    private static String VOLUME_MA;
    private static String VOLUME_MA_CHANGE;
    
    public static List<Map<String, Object>> calculate(Parameters params, List<Map<String, Object>> data){
        VOLUME_MA_DAYS = Integer.parseInt(params.getParameter().get("VOLUME_MA_DAYS").getValue());
        VOLUME_MA = params.getParameter().get("VOLUME_MA").getValue();  
        VOLUME_MA_CHANGE = params.getParameter().get("VOLUME_MA_CHANGE").getValue();  
        
        int position=0;
        
        Double[] ema = new Double[data.size()];
        
        while(position<data.size()){
            
            if(position<VOLUME_MA_DAYS){
                ema[position] = null;
            } else{
                if(ema[position-1]==null){
                    ema[position-1] = calculateAverage(data, "VOLUME", position - VOLUME_MA_DAYS, position);
                }
                ema[position] = Util.calculateEMA(VOLUME_MA_DAYS, (Double)data.get(position).get("VOLUME"), ema[position-1]);              
            }
            
            position++;
            
        }
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();
            temp.put(VOLUME_MA, ema[i]);
            if(ema[i]==null){
                temp.put(VOLUME_MA, null);
                temp.put(VOLUME_MA_CHANGE, null);
            } else{
                temp.put(VOLUME_MA, ema[i]);
                temp.put(VOLUME_MA_CHANGE, ((double)data.get(i).get("VOLUME")-ema[i])/ema[i]);
            }
            result.add(temp);
        }
        return result;        
    }
    private static Double calculateAverage(List<Map<String, Object>> data, String key, int start, int end){
        double average = 0;
        for(int i=start;i<end;i++){
            average = ((double)data.get(i).get(key)) + average;
        }
        return average / (end-start);
    }     
}
