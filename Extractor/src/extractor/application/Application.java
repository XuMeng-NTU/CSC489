/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.application;

import java.util.Scanner;

/**
 *
 * @author Meng
 */
public class Application {
    
    public static final String REGISTRY = "settings/registry/registry.xml";
    
    public static void main(String[] args){
        
        while (true) {

            System.out.print("Please Enter Stock Code: ");
            Scanner input = new Scanner(System.in);
            String code = input.next();

            if(code.equalsIgnoreCase("Q")){
                System.exit(0);
            }
            
            Controller.getInstance().extract(code);
        }
    }
}
