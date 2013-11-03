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
public class RSI {
    
    private static int RSI_DAYS;
    private static String RSI;
    
    public static List<Map<String, Object>> calculate(Parameters params, List<Map<String, Object>> data){
        
        RSI_DAYS = Integer.parseInt(params.getParameter().get("RSI_DAYS").getValue());
        RSI = params.getParameter().get("RSI").getValue();
        
        
        Double[] aveGain = new Double[data.size()];
        Double[] aveLoss = new Double[data.size()];
        
        int position = 0;
        
        while(position<data.size()){
            if(position<RSI_DAYS){
                aveGain[position] = null;
                aveLoss[position] = null;
            } else{
                if(aveGain[position-1]==null){
                    Double[] temp = calculateAveGainLoss(data, "CLOSE", position-1, RSI_DAYS-1);
                    aveGain[position-1] = temp[0];
                    aveLoss[position-1] = temp[1];
                }
                
                Double current = (double)data.get(position).get("CLOSE") - (double)data.get(position-1).get("CLOSE");
                
                if(current>=0){
                    aveGain[position] = Util.calculateSMA(RSI_DAYS, current, aveGain[position-1]);
                    aveLoss[position] = Util.calculateSMA(RSI_DAYS, 0.0, aveLoss[position-1]);
                } else{
                    aveGain[position] = Util.calculateSMA(RSI_DAYS, 0.0, aveGain[position-1]);
                    aveLoss[position] = Util.calculateSMA(RSI_DAYS, -current, aveLoss[position-1]);                    
                }
            }
            position++;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();

            if(aveGain[i]==null || aveGain[i]==null){
                temp.put(RSI, null);
            } else{
                temp.put(RSI, aveGain[i]/(aveGain[i]+aveLoss[i]));
            }
            result.add(temp);
        }
        return result;        
        
    }
    
    private static Double[] calculateAveGainLoss(List<Map<String, Object>> data, String key, int position, int period){

        Double aveGain = 0.0, aveLoss = 0.0;
        int i;
        double temp;
        for(i=0;i<period;i++){
            temp = (double)data.get(position-i).get(key) - (double)data.get(position-i-1).get(key);
            if (temp >= 0) {
                aveGain = aveGain + temp;
            } else {
                aveLoss = aveLoss + (-temp);
            }
        }
        
        Double[] result = new Double[2];
        result[0] = aveGain;
        result[1] = aveLoss;
        return result;
    }
    
}
