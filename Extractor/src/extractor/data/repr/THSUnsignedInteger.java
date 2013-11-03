/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.data.repr;
import java.math.BigInteger;
/**
 *
 * @author XU MENG
 */
public class THSUnsignedInteger extends BigInteger{
    
    public THSUnsignedInteger(String value){
        super(value);
    }
    
    public THSUnsignedInteger(byte[] array){
        super(1, array);
    }
    
    public static THSUnsignedInteger parseUnsignedInteger(String number){
        return new THSUnsignedInteger(number);
    }    
    
}
