/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.cleaners.control;

import exception.DataException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
 * @author Xu Meng
 */
public class CleanerController {

    public static final String COMPONENT = "CLEANER";
    public static Configuration CONFIG;

    public static void initialize(){        
        CONFIG = ((Configurations) Controller.getInstance().getSettings().getCurrentConfiguration()).getConfiguration().get(COMPONENT);
    }
    
    public static List<Map<String, Object>> clean(List<Map<String, Object>> data) {
        try {
            
            initialize();
            
            int[] latest = new int[CONFIG.getChoices().getChoice().size()];
            int i = 0;

            for(Choice choice : CONFIG.getChoices().getChoice().values()){
                Component cleaner = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());
                latest[i] = (int) cleaner.getRepr().getMethod("clean", Parameters.class, List.class).invoke(null, choice.getParameters(), data);
                i++;
            }
            
            int max = Integer.MIN_VALUE;
            for(i=0;i<latest.length;i++){
                if(latest[i]>max){
                    max = latest[i];
                }
            }
            
            List<Map<String, Object>> result = new ArrayList<>();
            
            for(i=max;i<data.size();i++){
                result.add(data.get(i));
            }
            return result;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CleanerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException(ex);
        }
    }
}
