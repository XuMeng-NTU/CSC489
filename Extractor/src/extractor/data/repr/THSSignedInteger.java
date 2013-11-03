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
public class THSSignedInteger extends BigInteger{
    
    public THSSignedInteger(String value){
        super(value);
    }
    public THSSignedInteger(byte[] array){
        super(array);
    }
    
    public static THSSignedInteger parseSignedInteger(String number){
        return new THSSignedInteger(number);
    }    
}
