/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.normalizers.control;

import exception.SettingsException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import preparer.application.Controller;
import preparer.data.DataPair;
import preparer.data.NormalizedDataPair;
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
public class NormalizerController {

    public static final String COMPONENT = "NORMALIZER";
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

    public static DataPair denormalize(NormalizedDataPair original) {
        initialize();
        return denormalizeOne(original);
    }    
    
    public static NormalizedDataPair normalize(DataPair original) {
        initialize();
        return normalizeOne(original);
    }

    public static List<DataPair> denormalize(List<NormalizedDataPair> original) {
        
        initialize();
        
        List<DataPair> result = new ArrayList<>();
        for (NormalizedDataPair pair : original) {
            result.add(denormalizeOne(pair));
        }
        return result;
    }    
    
    public static List<NormalizedDataPair> normalize(List<DataPair> original) {
        
        initialize();
        
        List<NormalizedDataPair> result = new ArrayList<>();
        for (DataPair pair : original) {
            result.add(normalizeOne(pair));
        }
        return result;
    }

    private static DataPair denormalizeOne(NormalizedDataPair original) {
        try {
            Map<String, List<Object>> shiftedInput = Util.shift(original.getInput());
            Map<String, List<Object>> shiftedOutput = Util.shift(original.getOutput());
            Map<String, List<Object>> shiftedPrediction = Util.shift(original.getPrediction());
            
            Map<String, List<Object>>[] data = new Map[3];
            data[0] = shiftedInput;
            data[1] = shiftedOutput;
            data[2] = shiftedPrediction;

            for (Choice choice : CONFIG.getChoices().getChoice().values()) {
                List<String>[] groups = new List[3];
                Component normalizer = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());
                groups[0] = tokenize(choice.getParameters().getParameter().get("FIELDS").getValue());
                groups[1] = tokenize(choice.getParameters().getParameter().get("FIELDS").getValue());
                groups[2] = tokenize(choice.getParameters().getParameter().get("FIELDS").getValue());
                
                normalizer.getRepr().getMethod("denormalize", Map.class, Map[].class, List[].class).invoke(null, original.getParams().get(choice.getName()), data, groups);

            }

            List<Map<String, Object>> unshiftedInput = Util.shift(shiftedInput);
            List<Map<String, Object>> unshiftedOutput = Util.shift(shiftedOutput);
            List<Map<String, Object>> unshiftedPrediction = Util.shift(shiftedPrediction);
            
            DataPair result = new DataPair(unshiftedInput, unshiftedOutput);
            result.setPrediction(unshiftedPrediction);
            
            return result;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(NormalizerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }

    private static NormalizedDataPair normalizeOne(DataPair original) {
        try {
            Map<String, List<Object>> shiftedInput = Util.shift(original.getInput());
            Map<String, List<Object>> shiftedOutput = Util.shift(original.getOutput());
            Map<String, List<Object>> shiftedPrediction = Util.shift(original.getPrediction());
            
            Map<String, List<Object>>[] data = new Map[3];

            data[0] = shiftedInput;
            data[1] = shiftedOutput;
            data[2] = shiftedPrediction;


            Map<String, Map<String, Object>> params = new LinkedHashMap<>();

            for (Choice choice : CONFIG.getChoices().getChoice().values()) {
                List<String>[] groups;
                Component normalizer = ((Components)Controller.getInstance().getSettings().lookupComponent(COMPONENT, choice.getCluster())).getComponent().get(choice.getWorker());

                groups = new List[3];
                groups[0] = tokenize(choice.getParameters().getParameter().get("FIELDS").getValue());
                groups[1] = tokenize(choice.getParameters().getParameter().get("FIELDS").getValue());
                groups[2] = tokenize(choice.getParameters().getParameter().get("FIELDS").getValue());

                params.put(choice.getName(), (Map<String, Object>) normalizer.getRepr().getMethod("normalize", Parameters.class, Map[].class, List[].class).invoke(null, choice.getParameters(), data, groups));

            }

            List<Map<String, Object>> unshiftedInput = Util.shift(shiftedInput);
            List<Map<String, Object>> unshiftedOutput = Util.shift(shiftedOutput);
            List<Map<String, Object>> unshiftedPrediction = Util.shift(shiftedPrediction);
            
            NormalizedDataPair result = new NormalizedDataPair(unshiftedInput, unshiftedOutput, params);
            result.setPrediction(unshiftedPrediction);
            
            return result;

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(NormalizerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
}
