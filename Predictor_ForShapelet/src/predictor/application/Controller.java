/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predictor.application;

import java.nio.file.Paths;
import java.util.Properties;
import predictor.evaluation.ClassifierEvaluation;
import predictor.io.Reader;
import predictor.io.Writer;
import predictor.learner.SMOLearner;
import predictor.learner.SMORegLearner;
import template.settings.Settings;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 *
 * @author Meng
 */
public class Controller {
    public static final String REGISTRY = "settings/registry/registry.xml";

    private Settings settings;
    
    private Controller(){
        settings = new Settings(Paths.get(REGISTRY), "PREDICTOR");

    }
    
    public void predict(String code){
        Instances training = Reader.readTraining(code);
        Instances testing = Reader.readTesting(code);
        
        Properties meta = Reader.readMeta(code);
  
        training.setClass(training.attribute(meta.getProperty("OUTPUT_HEAD")));
        testing.setClass(testing.attribute(meta.getProperty("OUTPUT_HEAD")));

        //Classifier classifier = SMORegLearner.learn(training);
        
        Classifier classifier = SMOLearner.learn(training);
        
        Writer.writeEvaluationResult(code, ClassifierEvaluation.evaluate(classifier, testing));
        
    }
    
    public Settings getSettings(){
        return settings;
    }
    

    private static Controller application = null;
    
    public static Controller getInstance(){
        if(application==null){
            application = new Controller();
        }
        
        return application;
    }    
    
    public static void destroyInstance(){
        application = null;
    }
        
}
