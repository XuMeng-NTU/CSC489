/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Xu Meng
 */
public class SettingsException extends RuntimeException{
    public SettingsException(String message){
        super(message);
    }
    
    public SettingsException(Throwable cause){
        super(cause);
    }
}
