/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.indicators;

import preparer.indicators.control.Util;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import template.configurations.Parameters;

/**
 *
 * @author Meng
 */
public class BOLL {
    
    private static int BOLL_DAYS;
    private static int BOLL_K;
    private static String BOLL_PERCENT;
    private static String BOLL_WIDTH;
    
    public static List<Map<String, Object>> calculate(Parameters params, List<Map<String, Object>> data){
        
        BOLL_DAYS = Integer.parseInt(params.getParameter().get("BOLL_DAYS").getValue());
        BOLL_K = Integer.parseInt(params.getParameter().get("BOLL_K").getValue());
        BOLL_PERCENT = params.getParameter().get("BOLL_PERCENT").getValue();
        BOLL_WIDTH = params.getParameter().get("BOLL_WIDTH").getValue();
        
        
        Double[] sma = new Double[data.size()];
        Double[] std = new Double[data.size()];
        
        int position = 0;
        
        while(position<data.size()){
            if(position<BOLL_DAYS){
                sma[position] = null;
                std[position] = null;
            } else{
                if(sma[position-1]==null){
                    sma[position-1] = calculateAverage(data, "CLOSE", position - BOLL_DAYS, position);
                }
                
                sma[position] = Util.calculateSMA(BOLL_DAYS, (Double)data.get(position).get("CLOSE"), sma[position-1]); 
                std[position] = calculateStd(data, "CLOSE", position - BOLL_DAYS, position);
            }
            position++;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();

            if(sma[i]==null || std[i]==null){
                temp.put(BOLL_PERCENT, null);
                temp.put(BOLL_WIDTH, null);
            } else{
                temp.put(BOLL_PERCENT, ((double)data.get(i).get("CLOSE")-(sma[i]-BOLL_K*std[i]))/(2*BOLL_K*std[i]));
                temp.put(BOLL_WIDTH, 2*BOLL_K*std[i]/sma[i]);
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

    private static Double calculateStd(List<Map<String, Object>> data, String key, int start, int end) {
        double average = calculateAverage(data, key, start, end);
        double sum = 0;
        for(int i=start;i<end;i++){
            sum = (((double)data.get(i).get(key)) - average)*(((double)data.get(i).get(key)) - average) + sum;
        }
        return Math.sqrt(sum / (end-start-1));
        
    }
}
