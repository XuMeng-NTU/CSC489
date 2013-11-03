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
public class Date {

    private static long MAX_DAY_SPAN;
    
    public static int clean(Parameters params, List<Map<String, Object>> data) {
        
        MAX_DAY_SPAN = Long.parseLong(params.getParameter().get("MAX_DAY_SPAN").getValue());
        
        int result = 0;
        int i;
        for (i = 1; i < data.size(); i++) {
            Map<String, Object> current = data.get(i);
            Map<String, Object> previous = data.get(i-1);

            if (((java.util.Date) current.get("DATE")).getTime() - ((java.util.Date) previous.get("DATE")).getTime() > (MAX_DAY_SPAN * (1000 * 60 * 60 * 24))) {
                result = i;
            }    
        }
        return result;
    }    
}
