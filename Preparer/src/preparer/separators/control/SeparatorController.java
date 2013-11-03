/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.separators.control;

import preparer.data.DataPair;
import exception.DataException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import preparer.application.Controller;
import template.components.Component;
import template.components.Components;
import template.configurations.Choice;
import template.configurations.Configuration;
import template.configurations.Configurations;
import template.configurations.Parameter;
import template.configurations.Parameters;


/**
 *
 * @author Meng
 */
public class SeparatorController {

    public static final String COMPONENT = "SEPARATOR";
    public static Configuration CONFIG;

    private static void initialize(){
        CONFIG = ((Configurations) Controller.getInstance().getSettings().getCurrentConfiguration()).getConfiguration().get(COMPONENT);
    }
    
    public static Properties retrieveProperties(){
        
        initialize();
        
        Map<String, Parameter> params = CONFIG.getChoices().getChoice().values().iterator().next().getParameters().getParameter();
        
        Properties properties = new Properties();
        properties.setProperty("INPUT_WINDOW", params.get("INPUT").getValue());
        properties.setProperty("OUTPUT_WINDOW", params.get("OUTPUT").getValue());        
        
        return properties;
    }
    
    public static List<DataPair> separateForTraining(List<Map<String, Object>> data) {
        try {
            
            initialize();
            
            Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();
            Component separator = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());

            return (List<DataPair>) separator.getRepr().getMethod("separateForTraining", Parameters.class, List.class).invoke(null, choice.getParameters(), data);

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(SeparatorController.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException(ex);
        }
    }
    
    public static DataPair separateForPrediction(List<Map<String, Object>> data) {
        try {
            
            initialize();
            
            Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();
            Component separator = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());

            return (DataPair) separator.getRepr().getMethod("separateForPrediction", Parameters.class, List.class).invoke(null, choice.getParameters(), data);

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(SeparatorController.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException(ex);
        }
    }    
    
}
