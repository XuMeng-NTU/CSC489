/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Meng
 */
public class DataPair {

    private List<Map<String, Object>> input;
    private List<Map<String, Object>> output;
    private List<Map<String, Object>> prediction;
    
    public DataPair(List<Map<String, Object>> input, List<Map<String, Object>> output){
        this.input = input;
        this.output = output;
        prediction = new ArrayList<>();
    }
    
    public DataPair(List<Map<String, Object>> data, int inputStart, int inputEnd, int outputStart, int outputEnd){
        int i;
        
        input = new ArrayList<>();
        output = new ArrayList<>();
        
        for(i=inputStart; i<inputEnd; i++){
            input.add(data.get(i));
        }
        
        for(i=outputStart; i<outputEnd; i++){
            output.add(data.get(i));
        }
        
        prediction = new ArrayList<>();
        
    }
    public List<Map<String, Object>> getInput(){
        return input;
    }
/*
    public List<String> getInputColumns(){
        List<String> result = new ArrayList<>();
        for(Map<String, Object> item : input){
            for(Entry<String, Object> entry : item.entrySet()){
                result.add(entry.getKey());
            }
        }        
        return result;
    }

    public List<String> getOutputColumns(){
        List<String> result = new ArrayList<>();
        for(Map<String, Object> item : output){
            for(Entry<String, Object> entry : item.entrySet()){
                result.add(entry.getKey());
            }
        }        
        return result;
    }
*/ 
    public List<Map<String, Object>> getOutput(){
        return output;
    }    
    
    public List<Map<String, Object>> getPrediction(){
        return prediction;
    }        
    
    public void setPrediction(List<Map<String, Object>> prediction){
        this.prediction = prediction;
    }
    
    public String toString(){
        return "Input: "+input.toString()+", "+"Output: "+output.toString()+", "+"Prediction: "+prediction.toString();
        //return prediction.toString();
    }
    
    public List<Object> consolidate(){
        List<Object> inputList = new ArrayList<>();

        for(Map<String, Object> item : input){
            for(Entry<String, Object> entry : item.entrySet()){
                inputList.add(entry.getValue());
            }
        }
        
        List<Object> outputList = new ArrayList<>();
        
        for(Map<String, Object> item : output){
            for(Entry<String, Object> entry : item.entrySet()){
                inputList.add(entry.getValue());
            }
        }        
        
        List<Object> result = new ArrayList<>();
        result.addAll(inputList);
        result.addAll(outputList);
        
        return result;
        
    }
    
    public void parsePrediction(List<Object> data, List<String> columns){
        List<Map<String, Object>> result = new ArrayList<>();
        
        int counter = 0;
        for(int i=0;i<data.size()/columns.size();i++){
            Map<String, Object> temp = new LinkedHashMap<>();
            for(String column : columns){
                temp.put(column, data.get(counter));
                counter++;
            }
            result.add(temp);
        }      
        prediction = result;   
    }
    
}
