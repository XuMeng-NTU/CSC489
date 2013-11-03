/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configurer.core;

import java.util.Scanner;


/**
 *
 * @author Meng
 */
public class Test {

    public static void main(String[] args){
        Configurer configurer = Configurer.getInstance();
        
        Scanner keyboard = new Scanner(System.in);
        String instruction = keyboard.next();
        
        while(!instruction.equalsIgnoreCase("Q")){
            if(instruction.equalsIgnoreCase("SHOW")){
                System.out.println(configurer.settings);
            }
            
            instruction = keyboard.next();
        }
        
        
    }
}
