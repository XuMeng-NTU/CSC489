/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shapelet.application;

import java.util.List;
import java.util.Scanner;
import util.ticker.TickerRetriever;

/**
 *
 * @author Meng
 */
public class Application {
    
    public static void auto(){
        List<String> ticker = TickerRetriever.retrieve(TickerRetriever.FOLDER);
        
        Controller timeseries = Controller.getInstance();
        
        for(int i=1;i<100;i++){
System.out.println(ticker.get(i));            
            timeseries.transform(ticker.get(i));
        }
        
    }
    
    public static void main(String[] args){
        
        Controller timeseries = Controller.getInstance();
        Scanner input = new Scanner(System.in);

        while (true) {
            
            System.out.print("Please Enter Stock Code: ");
            String code = input.next();
            
            if(code.equalsIgnoreCase("Q")){
                System.exit(0);
            }else if(code.equalsIgnoreCase("AUTO")){
                auto();
            } else{

                timeseries.transform(code);
            }

            
            
        }        
        
    }
}
