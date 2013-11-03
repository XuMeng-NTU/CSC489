/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.indicators.control;

import exception.DataException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import preparer.application.Controller;
import template.components.Component;
import template.components.Components;
import template.configurations.Choice;
import template.configurations.Configuration;
import template.configurations.Configurations;
import template.configurations.Parameters;

/**
 *
 * @author Meng
 */
public class IndicatorController {

    public static final String COMPONENT = "INDICATOR";
    public static Configuration CONFIG;

    public static void initialize(){        
        CONFIG = ((Configurations) Controller.getInstance().getSettings().getCurrentConfiguration()).getConfiguration().get(COMPONENT);
    }
    
    public static List<Map<String, Object>> indicate(List<Map<String, Object>> data) {
        try {
            
            initialize();
            
            List<Map<String, Object>>[] indicatorResult = new ArrayList[CONFIG.getChoices().getChoice().size()];
            
            int i=0;
            
            for(Choice choice : CONFIG.getChoices().getChoice().values()){
                Component indicator = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());
                indicatorResult[i] = (ArrayList<Map<String, Object>>) indicator.getRepr().getMethod("calculate", Parameters.class, List.class).invoke(null, choice.getParameters(), data);
                i++;
            }

            List<Map<String, Object>> result = new ArrayList<>();
            
            int j;
            
            Map<String, Object> temp;
            boolean nullElement;
            for (i = 0; i < data.size(); i++) {
                temp = new LinkedHashMap<>();
                temp.putAll(data.get(i));
                for(j=0;j<indicatorResult.length;j++){
                    temp.putAll(indicatorResult[j].get(i));
                }
                nullElement = false;
                for(Entry<String, Object> entry : temp.entrySet()){
                    if(entry.getValue()==null){
                        nullElement = true;
                        break;
                    }
                }
                if (!nullElement) {
                    result.add(temp);
                }
            }
            
            return result;
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(IndicatorController.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException(ex);
        }
    }
}
