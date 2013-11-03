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
public class MACD {

    private static int FAST_DAYS;
    private static int SLOW_DAYS;
    private static int SIGNAL_DAYS;
    
    private static String MACD_LINE;
    private static String MACD_SIGNAL;
    private static String MACD_DIFF;
    
    public static List<Map<String, Object>> calculate(Parameters params, List<Map<String, Object>> data){
        
        FAST_DAYS = Integer.parseInt(params.getParameter().get("FAST_DAYS").getValue());
        SLOW_DAYS = Integer.parseInt(params.getParameter().get("SLOW_DAYS").getValue());
        SIGNAL_DAYS = Integer.parseInt(params.getParameter().get("SIGNAL_DAYS").getValue());
        MACD_LINE = params.getParameter().get("MACD_LINE").getValue();        
        MACD_SIGNAL = params.getParameter().get("MACD_SIGNAL").getValue();   
        MACD_DIFF = params.getParameter().get("MACD_DIFF").getValue();   
        
        
        Double[] fastEMA = new Double[data.size()];
        Double[] slowEMA = new Double[data.size()];
        Double[] diff = new Double[data.size()];
        Double[] signal = new Double[data.size()];
        
        int position=0;
        
        while(position<data.size()){
            
            if(position<FAST_DAYS){
                fastEMA[position] = null;
            } else{
                if(fastEMA[position-1]==null){
                    fastEMA[position-1] = calculateAverage(data, "CLOSE", position - FAST_DAYS, position);
                }
                fastEMA[position] = Util.calculateEMA(FAST_DAYS, (Double)data.get(position).get("CLOSE"), fastEMA[position-1]);              
            }
            
            if(position<SLOW_DAYS){
                slowEMA[position] = null;
            } else{
                if(slowEMA[position-1]==null){
                    slowEMA[position-1] = calculateAverage(data, "CLOSE", position - SLOW_DAYS, position);
                }
                slowEMA[position] = Util.calculateEMA(SLOW_DAYS, (Double)data.get(position).get("CLOSE"), slowEMA[position-1]);             
            }
            
            if(fastEMA[position]==null || slowEMA[position]==null){
                diff[position] = null;
                signal[position] = null;
            } else{
                diff[position] = fastEMA[position] - slowEMA[position];
                if(position<SLOW_DAYS+SIGNAL_DAYS){
                    signal[position]=  null;
                } else{
                    if(signal[position-1]==null){
                        signal[position] = Util.calculateEMA(SIGNAL_DAYS, diff[position], calculateAverage(diff, position-SIGNAL_DAYS, position));
                    } else{
                        signal[position] = Util.calculateEMA(SIGNAL_DAYS, diff[position], signal[position-1]);
                    }
                }
            }
            position++;
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();
            temp.put(MACD_LINE, diff[i]);
            temp.put(MACD_SIGNAL, signal[i]);
            if(diff[i]==null || signal[i]==null){
                temp.put(MACD_DIFF, null);
            } else{
                temp.put(MACD_DIFF, diff[i] - signal[i]);
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
    
    private static Double calculateAverage(Double[] data, int start, int end){
        double average = 0;
        for(int i=start;i<end;i++){
            average = data[i]+average;
        }
        return average/(end-start);
    }
        
    
}
