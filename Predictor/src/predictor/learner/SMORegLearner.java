/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predictor.learner;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RegOptimizer;
import weka.classifiers.functions.supportVector.RegSMOImproved;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author Meng
 */
public class SMORegLearner {
    
    public static Classifier learn(Instances instances){
        try {            
            Classifier classifier = new SMOreg();
            String regOptimizerOption = "-L 0.001 -W 1 -P 1.0E-12 -T 0.001 -V";
            String kernal = "-C 250007 -E 10.0 -L";
            String SMOoption = "-C 50 -N 2";
            
            RegOptimizer regOptimizer = new RegSMOImproved();
            regOptimizer.setOptions(Utils.splitOptions(regOptimizerOption));
            ((SMOreg)classifier).setRegOptimizer(regOptimizer);
            
            Kernel kernel = new PolyKernel();
            kernel.setOptions(Utils.splitOptions(kernal));
            ((SMOreg)classifier).setKernel(kernel);

            ((SMOreg)classifier).setOptions(Utils.splitOptions(SMOoption));
 
            classifier.buildClassifier(instances); 
            
            return classifier;
            
        } catch (Exception ex) {
            Logger.getLogger(SMORegLearner.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
