/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.cleaners;

import java.util.List;
import java.util.Map;
import template.configurations.Parameters;

/**
 *
 * @author Meng
 */
public class Price {

    private static double MAX_PRICE_DIFF;
    
    public static int clean(Parameters params, List<Map<String, Object>> data) {
        
        MAX_PRICE_DIFF = Double.parseDouble(params.getParameter().get("MAX_PRICE_DIFF").getValue());
        
        int result = 0;
        int i;
        for (i = 1; i < data.size(); i++) {
            Map<String, Object> current = data.get(i);
            Map<String, Object> previous = data.get(i-1);

            if (Math.abs((double) current.get("CLOSE") - (double) previous.get("CLOSE")) / (double) previous.get("CLOSE") > MAX_PRICE_DIFF) {
                result = i;
            }    
        }
        return result;
    }    
}
