/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.comparers.control;

import exception.SettingsException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class ComparerController {
    
    public static final String COMPONENT = "COMPARER";
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
    
    public static DataPair select(DataPair data){
        try {
            
            initialize();
            
            Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();
            Component selector = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());

            List<String> actualOutputColumns = tokenize(choice.getParameters().getParameter().get("ACTUAL_OUTPUT").getValue());
            List<String> predictedOutputColumns = tokenize(choice.getParameters().getParameter().get("PREDICTED_OUTPUT").getValue());

            List<Map<String, Object>> actualOutputSelected = (List<Map<String, Object>>) selector.getRepr().getMethod("select", List.class, List.class).invoke(null, data.getOutput(), actualOutputColumns);
            List<Map<String, Object>> predictedOutputSelected = (List<Map<String, Object>>) selector.getRepr().getMethod("select", List.class, List.class).invoke(null, data.getPrediction(), predictedOutputColumns);

            return new DataPair(actualOutputSelected, predictedOutputSelected);

            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ComparerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }    
    
    
    public static List<DataPair> select(List<? extends DataPair> data){
        try {
            
            initialize();
            
            List<DataPair> result = new ArrayList<>();
            
            Choice choice = CONFIG.getChoices().getChoice().values().iterator().next();
            Component selector = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());

            List<String> actualOutputColumns = tokenize(choice.getParameters().getParameter().get("ACTUAL_OUTPUT").getValue());
            List<String> predictedOutputColumns = tokenize(choice.getParameters().getParameter().get("PREDICTED_OUTPUT").getValue());

            for(DataPair pair : data){

                List<Map<String, Object>> actualOutputSelected = (List<Map<String, Object>>) selector.getRepr().getMethod("select", List.class, List.class).invoke(null, pair.getOutput(), actualOutputColumns);
                List<Map<String, Object>> predictedOutputSelected = (List<Map<String, Object>>) selector.getRepr().getMethod("select", List.class, List.class).invoke(null, pair.getPrediction(), predictedOutputColumns);

                result.add(new DataPair(actualOutputSelected, predictedOutputSelected));
            }
            
            return result;
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ComparerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
}
