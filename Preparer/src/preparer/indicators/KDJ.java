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
public class KDJ {
    
    private static int RSV_DAYS;
    private static int SMA_DAYS;
    
    private static String KDJ_K;
    private static String KDJ_D;
    private static String KDJ_J;
    
    public static List<Map<String, Object>> calculate(Parameters params, List<Map<String, Object>> data){
        
        RSV_DAYS = Integer.parseInt(params.getParameter().get("RSV_DAYS").getValue());
        SMA_DAYS = Integer.parseInt(params.getParameter().get("SMA_DAYS").getValue());
        KDJ_K = params.getParameter().get("KDJ_K").getValue();
        KDJ_D = params.getParameter().get("KDJ_D").getValue();
        KDJ_J = params.getParameter().get("KDJ_J").getValue();
        
        
        Double[] k = new Double[data.size()];
        Double[] d = new Double[data.size()];
        
        int position = 0;
        while(position<data.size()){
            if(position<RSV_DAYS){
                k[position] = null;
                d[position] = null;
            } else{
                if(k[position-1]==null){
                    k[position-1] = 0.5;
                }
                k[position] = Util.calculateSMA(SMA_DAYS, calculateRSV(data, "CLOSE", position, RSV_DAYS), k[position-1]);
                
                if(d[position-1]==null){
                    d[position-1] = 0.5;
                }
                d[position] = Util.calculateSMA(SMA_DAYS, k[position], d[position-1]);
            }
            position++;
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp;
        for(int i=0;i<data.size();i++){
            temp = new LinkedHashMap<>();
            temp.put(KDJ_K, k[i]);
            temp.put(KDJ_D, d[i]);
            if(k[i]==null || d[i]==null){
                temp.put(KDJ_J, null);
            } else{
                temp.put(KDJ_J, 3*k[i] - 2*d[i]);
            }
            result.add(temp);
        }
        return result;
    }
    
    
    private static Double calculateRSV(List<Map<String, Object>> data, String key, int position, int period){
        
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        
        int i;
        for(i=0;i<period;i++){
            double temp = (double)data.get(position-i).get(key);
            if(max<temp){
                max = temp;
            }
            
            if(min>temp){
                min = temp;
            }
        }
        
        return ((double)data.get(position).get(key) - min)/(max-min);
        
    }         
}
