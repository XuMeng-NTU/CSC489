/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import preparer.data.NormalizedDataPair;
import util.ticker.TickerRetriever;

/**
 *
 * @author Meng
 */
public class Application {
    
    public static void auto(){
        List<String> ticker = TickerRetriever.retrieve(TickerRetriever.FOLDER);
        
        Controller preparer = Controller.getInstance();
        
        //List<List<NormalizedDataPair>> resultHolders = new ArrayList<>();
        
        for(int i=1;i<100;i++){
System.out.println(ticker.get(i));            
            List<Map<String, Object>> base = Controller.getInstance().clean(ticker.get(i));
            List<NormalizedDataPair> resultHolder = preparer.prepareForTraining(ticker.get(i), base);
            
            preparer.validate(ticker.get(i), resultHolder);

        }
//        System.out.println("Waiting for prediction");
//        Scanner input = new Scanner(System.in);
//        input.next();
//        for(int i=0;i<100;i++){          
//            preparer.validate(ticker.get(i), resultHolders.get(i));
//        }
            
    }
    
    public static void main(String[] args){
        
        Controller preparer = Controller.getInstance();
        Scanner input = new Scanner(System.in);
        
        List<NormalizedDataPair> resultHolder = null;
        
        while (true) {
            
            System.out.print("Please Enter Instruction: ");
            String instruction = input.next();
            
            if(instruction.equalsIgnoreCase("train")){
                System.out.print("Please Enter Stock Code: ");
                String code = input.next();         
                
                List<Map<String, Object>> base = Controller.getInstance().clean(code);
                resultHolder = preparer.prepareForTraining(code, base);
                
            } else if(instruction.equalsIgnoreCase("validate")){
                System.out.print("Please Enter Stock Code: ");
                String code = input.next();         
                preparer.validate(code, resultHolder);               
            }else if(instruction.equalsIgnoreCase("Q")){
                System.exit(0);
            }else if(instruction.equalsIgnoreCase("AUTO")){
                auto();
            }

        }        
    }
    
    public static NormalizedDataPair prepareForPrediction(String code, List<Map<String, Object>> data){
        return Controller.getInstance().prepareForPrediction(code, data);
    }
        
    public static void validate(String code, List<NormalizedDataPair> normalizationResult){
        Controller.getInstance().validate(code, normalizationResult);
    }

    public static List<Map<String, Object>> predict(String code, NormalizedDataPair predictionNormalized){
        return Controller.getInstance().predict(code, predictionNormalized);
    }    
}
