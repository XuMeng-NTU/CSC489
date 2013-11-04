/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.cleaners;

import java.util.Calendar;
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
        
        java.util.Date BASE_DATE = (java.util.Date) data.get(0).get("DATE");
        java.util.Date NEXT_DATE;
        
        int result = 0;
        int i;
        for (i = 1; i < data.size(); i++) {
            Map<String, Object> current = data.get(i);
            Map<String, Object> previous = data.get(i-1);
            
            NEXT_DATE = nextDate(BASE_DATE);
            data.get(i).put("DATE", NEXT_DATE);
            BASE_DATE = NEXT_DATE;
            
            if (((java.util.Date) current.get("DATE")).getTime() - ((java.util.Date) previous.get("DATE")).getTime() > (MAX_DAY_SPAN * (1000 * 60 * 60 * 24))) {
                result = i;
            }    
        }
        return result;
    }    
    
    public static java.util.Date nextDate(java.util.Date current){
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        cal.add(Calendar.DATE, 1);
        
        return cal.getTime();
    }
}
