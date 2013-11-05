/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predictor.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 *
 * @author Meng
 */
public class ClassifierEvaluation {
    public static List<Double> evaluate(Classifier classifier, Instances testing){
        try {
            List<Double> result = new ArrayList<>();
            
            Evaluation eval = new Evaluation(testing);

            double[] evalResults = eval.evaluateModel(classifier, testing);
            
            for(double evalResult : evalResults){
                result.add(evalResult);
            }
            
            System.out.println("Mean Absolute Error,"+eval.meanAbsoluteError());
            System.out.println("Root mean squared error,"+eval.rootMeanSquaredError());
            
            return result;
        } catch (Exception ex) {
            Logger.getLogger(ClassifierEvaluation.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
                    
    }
}
