/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.indicators.control;

/**
 *
 * @author Meng
 */
public class Util {


    public static Double calculateEMA(int period, Double current, Double previous){
        
        double m = 2.0/(period+1);
        return m*current+(1-m)*previous;
        
    }
    
    public static Double calculateSMA(int period, Double current, Double previous){
        double m = 1.0/period;
        return m*current+(1-m)*previous;
    }
}
