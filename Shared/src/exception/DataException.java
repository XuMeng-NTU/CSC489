/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Xu Meng
 */
public class DataException extends RuntimeException{
    public DataException(String message){
        super(message);
    }
    
    public DataException(Throwable cause){
        super(cause);
    }
}
