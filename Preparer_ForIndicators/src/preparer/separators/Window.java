/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.separators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import preparer.data.DataPair;
import template.configurations.Parameters;

/**
 *
 * @author Meng
 */
public class Window {
    
    private static int INPUT_WINDOW;
    private static int OUTPUT_WINDOW;
    private static int GRAY_WINDOW;
    private static int TRAINING_WINDOW;
    
    public static List<DataPair> separateForTraining(Parameters params, List<Map<String, Object>> data) {
        
        INPUT_WINDOW = Integer.parseInt(params.getParameter().get("INPUT").getValue());
        OUTPUT_WINDOW = Integer.parseInt(params.getParameter().get("OUTPUT").getValue());
        GRAY_WINDOW = Integer.parseInt(params.getParameter().get("GRAY").getValue());
        TRAINING_WINDOW = INPUT_WINDOW+GRAY_WINDOW+OUTPUT_WINDOW;
        
        
        int position;
        position = 0;
        
        List<DataPair> samples = new ArrayList<>();
        
        while((position+TRAINING_WINDOW)<=data.size()){
            
            DataPair sample = new DataPair(data, position, position+INPUT_WINDOW, position+INPUT_WINDOW+GRAY_WINDOW, position+INPUT_WINDOW+GRAY_WINDOW+OUTPUT_WINDOW);
            samples.add(sample);
            
            position = position+1;
        }
        return samples;
    }

    public static DataPair separateForPrediction(Parameters params, List<Map<String, Object>> data) {

        INPUT_WINDOW = Integer.parseInt(params.getParameter().get("INPUT").getValue());
        OUTPUT_WINDOW = 0;
        GRAY_WINDOW = Integer.parseInt(params.getParameter().get("GRAY").getValue());
        TRAINING_WINDOW = INPUT_WINDOW+GRAY_WINDOW+OUTPUT_WINDOW;        
        
        int position = data.size() - TRAINING_WINDOW;

        DataPair sample = new DataPair(data, position, position+INPUT_WINDOW, position+INPUT_WINDOW+GRAY_WINDOW, position+INPUT_WINDOW+GRAY_WINDOW+OUTPUT_WINDOW);
        
        return sample;
    }    
    
}
