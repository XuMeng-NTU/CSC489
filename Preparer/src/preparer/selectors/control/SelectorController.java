/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.selectors.control;

import exception.SettingsException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import preparer.application.Controller;
import preparer.data.DataPair;
import template.components.Component;
import template.components.Components;
import template.configurations.Choice;
import template.configurations.Configuration;
import template.configurations.Configurations;

/**
 *
 * @author Meng
 */
public class SelectorController {
    
    public static final String COMPONENT = "SELECTOR";
    public static Configuration CONFIG;

    private static void initialize(){
        CONFIG = ((Configurations) Controller.getInstance().getSettings().getCurrentConfiguration()).getConfiguration().get(COMPONENT);
    }
    
    private static List<String> tokenize(String string) {
        StringTokenizer tokenizer = new StringTokenizer(string, ",");
        List<String> result = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }

        return result;
    }
    
    public static Properties retrieveProperties(){
        
        initialize();
        
        Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();

        List<String> inputColumns = tokenize(choice.getParameters().getParameter().get("INPUT").getValue());
        List<String> outputColumns = tokenize(choice.getParameters().getParameter().get("OUTPUT").getValue());

        Properties properties = new Properties();
        properties.setProperty("INPUT_FIELD", Integer.toString(inputColumns.size()));
        properties.setProperty("OUTPUT_FIELD", Integer.toString(outputColumns.size()));
        
        return properties;
        
    }

    public static List<String> retrieveInputFields(){
        
        initialize();
        
        Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();
        return tokenize(choice.getParameters().getParameter().get("INPUT").getValue());
    }
    
    public static List<String> retrieveOutputFields(){
        
        initialize();
        
        Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();     
        return tokenize(choice.getParameters().getParameter().get("OUTPUT").getValue());
    }   
    
    public static DataPair select(DataPair data){
        try {
            
            initialize();
            
            Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();
            Component selector = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());
            
            List<String> inputColumns = tokenize(choice.getParameters().getParameter().get("INPUT").getValue());
            List<String> outputColumns = tokenize(choice.getParameters().getParameter().get("OUTPUT").getValue());

            List<Map<String, Object>> inputSelected = (List<Map<String, Object>>) selector.getRepr().getMethod("select", List.class, List.class).invoke(null, data.getInput(), inputColumns);
            List<Map<String, Object>> outputSelected = (List<Map<String, Object>>) selector.getRepr().getMethod("select", List.class, List.class).invoke(null, data.getOutput(), outputColumns);

            return new DataPair(inputSelected, outputSelected);

            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(SelectorController.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }    
    
    
    public static List<DataPair> select(List<? extends DataPair> data){
        try {
            
            initialize();
            
            List<DataPair> result = new ArrayList<>();
            
            Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();
            Component selector = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());
            
            List<String> inputColumns = tokenize(choice.getParameters().getParameter().get("INPUT").getValue());
            List<String> outputColumns = tokenize(choice.getParameters().getParameter().get("OUTPUT").getValue());
            
            for(DataPair pair : data){
            
                List<Map<String, Object>> inputSelected = (List<Map<String, Object>>) selector.getRepr().getMethod("select", List.class, List.class).invoke(null, pair.getInput(), inputColumns);
                List<Map<String, Object>> outputSelected = (List<Map<String, Object>>) selector.getRepr().getMethod("select", List.class, List.class).invoke(null, pair.getOutput(), outputColumns);
            
                result.add(new DataPair(inputSelected, outputSelected));
            }
            
            return result;
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(SelectorController.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
}
