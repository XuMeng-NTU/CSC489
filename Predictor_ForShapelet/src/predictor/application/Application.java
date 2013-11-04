/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predictor.application;

import java.util.Scanner;

/**
 *
 * @author Meng
 */
public class Application {
    public static void main(String[] args){
        
        Controller predictor = Controller.getInstance();
        Scanner input = new Scanner(System.in);
        
        
        while (true) {
            
            System.out.print("Please Enter Stock Code: ");
            String code = input.next();
            
            if(code.equalsIgnoreCase("Q")){
                System.exit(0);
            }

            predictor.predict(code);

        }        
        
    }
}
