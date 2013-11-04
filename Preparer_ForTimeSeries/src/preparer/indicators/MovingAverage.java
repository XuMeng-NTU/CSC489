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
public class MovingAverage {
    private static int MA_DAYS;
    
    private static String MA;
    private static String MA_CHANGE;
    
    public static List<Map<String, Object>> calculate(Parameters params, List<Map<String, Object>> data){
        MA_DAYS = Integer.parseInt(params.getParameter().get("MA_DAYS").getValue());
        MA = params.getParameter().get("MA").getValue();  
        MA_CHANGE = params.getParameter().get("MA_CHANGE").getValue();  
        
        int position=0;
        
        Double[] ema = new Double[data.size()];
        
        while(position<data.size()){
            
            if(position<MA_DAYS){
                ema[position] = null;
            } else{
                if(ema[position-1]==null){
                    ema[position-1] = calculateAverage(data, "CLOSE", position - MA_DAYS, position);
                }
                ema[position] = Util.calculateEMA(MA_DAYS, (Double)data.get(position).get("CLOSE"), ema[position-1]);              
            }
            
            position++;
            
        }
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();
            temp.put(MA, ema[i]);
            if(ema[i]==null){
                temp.put(MA, null);
                temp.put(MA_CHANGE, null);
            } else{
                temp.put(MA, ema[i]);
                temp.put(MA_CHANGE, ((double)data.get(i).get("CLOSE")-ema[i])/ema[i]*10);
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
