/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.data.repr;

import java.math.BigDecimal;
import java.math.BigInteger;
/**
 *
 * @author XU MENG
 */
public class THSDecimal extends BigDecimal{
    
    public static final int NUM_INDEX_BITS = 4;
    public static final int BITS_PER_UNIT = 8;
    
    public THSDecimal(byte[] array){
        super(prepare(array).toString());
    }    
    
    public THSDecimal(String value){
        super(value);
    }
    
    private static BigDecimal prepare(byte[] array){
        byte[] indexArray = new byte[(NUM_INDEX_BITS+BITS_PER_UNIT-1)/BITS_PER_UNIT];
        for(int i=0;i<indexArray.length;i++){
            indexArray[i] = array[i];
        }
        
        byte sign = (byte) (indexArray[0] & 0x80);
        int signum;
        if(sign==0){
            signum = 1;
        } else{
            signum= -1;
        }
        
        indexArray[0] = (byte)(indexArray[0] & 0x7F);
        
        BigInteger indexWrap = new BigInteger(signum, indexArray);
        BigInteger index = indexWrap.shiftRight((indexArray.length-1)*BITS_PER_UNIT+(BITS_PER_UNIT-NUM_INDEX_BITS));
                
        
        BigInteger baseWrap = new BigInteger(array);
        boolean baseSign = baseWrap.shiftRight((array.length-1)*BITS_PER_UNIT+(BITS_PER_UNIT-NUM_INDEX_BITS)-1).testBit(0);
        
        BigInteger base;
        if(baseSign){
            array[0] = (byte) (array[0] | 0x80);
            base = new BigInteger(array);
            for(int i=0;i<4;i++){
                base = base.setBit((array.length-1)*BITS_PER_UNIT+NUM_INDEX_BITS+i);
            }            
        } else{
            array[0] = (byte) (array[0] & 0x7F);
            base = new BigInteger(array);
            for(int i=0;i<4;i++){
                base = base.clearBit((array.length-1)*BITS_PER_UNIT+NUM_INDEX_BITS+i);
            }              
        }

        BigDecimal baseDec = new BigDecimal(base);
        return baseDec.movePointRight(index.intValue());
    }
    
    public static THSDecimal parseDecimal(String number){
        return new THSDecimal(number);
    }            
    
}
