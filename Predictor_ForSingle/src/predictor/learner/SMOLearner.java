/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predictor.learner;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author Meng
 */
public class SMOLearner {
    
    public static Classifier learn(Instances instances){
        try {            
            Classifier classifier = new SMO();

            String kernal = "-C 250007 -E 3.0 -L";
            String SMOoption = "-C 5 -L 0.001 -P 1.02E-12 -N 0 -V -1 -W 1";
            
            Kernel kernel = new PolyKernel();
            kernel.setOptions(Utils.splitOptions(kernal));
            ((SMO)classifier).setKernel(kernel);

            ((SMO)classifier).setOptions(Utils.splitOptions(SMOoption));
 
            classifier.buildClassifier(instances); 
            
            return classifier;
            
        } catch (Exception ex) {
            Logger.getLogger(SMOLearner.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
