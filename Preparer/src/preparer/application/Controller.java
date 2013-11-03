/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.application;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import preparer.comparers.control.ComparerController;
import preparer.indicators.control.IndicatorController;
import preparer.io.Reader;
import preparer.data.DataPair;
import preparer.data.NormalizedDataPair;
import preparer.io.Writer;
import preparer.normalizers.control.NormalizerController;
import preparer.selectors.control.SelectorController;
import preparer.separators.control.SeparatorController;
import preparer.transformers.control.TransformerController;
import preparer.cleaners.control.CleanerController;
import template.settings.Settings;
/**
 *
 * @author Meng
 */
public class Controller {
    
    public static final String REGISTRY = "settings/registry/registry.xml";
    public static String CONFIGURATION_TYPE = "DATASET";
    public static String CONFIGURATION_PROFILE = "DEFAULT";

    private static double SPLIT_RATE = 0.9;
    
    private Settings settings;
    
    private Controller(){
        settings = new Settings(Paths.get(REGISTRY), "PREPARER");
        settings.setCurrentConfiguration(CONFIGURATION_TYPE, CONFIGURATION_PROFILE);
    }
    
    public Settings getSettings(){
        return settings;
    }
/*
    public void reload(){
        settings = new Settings(Paths.get(REGISTRY));
    }
*/    
    public List<Map<String, Object>> predict(String code, NormalizedDataPair predictionNormalized){
        List<Object> result = Reader.readPrediction(code);
        
        predictionNormalized.parsePrediction(result, SelectorController.retrieveOutputFields());
        DataPair predictionDenormalized = NormalizerController.denormalize(predictionNormalized);

        return predictionDenormalized.getPrediction();

    }    
    
    public void validate(String code, List<NormalizedDataPair> normalizationResult){
        List<List<Object>> result = Reader.readValidation(code);
        int i;
        for(i=0;i<normalizationResult.size();i++){
            normalizationResult.get(i).parsePrediction(result.get(i), SelectorController.retrieveOutputFields());
        }
        List<DataPair> denormalizedResult = NormalizerController.denormalize(normalizationResult);
        List<DataPair> comparisonResult = ComparerController.select(denormalizedResult);
        Writer.writeValidationResult(code, comparisonResult);
    }
    
    public List<NormalizedDataPair> prepareForTraining(String code, List<Map<String, Object>> data){
        
        List<Map<String, Object>> indicationResult = IndicatorController.indicate(data);
        List<Map<String, Object>> transformationResult = TransformerController.transform(indicationResult);

        List<DataPair> separationResult = SeparatorController.separateForTraining(transformationResult);
        List<NormalizedDataPair> normalizationResult = NormalizerController.normalize(separationResult);
        
        List<NormalizedDataPair>[] splitResult = split(normalizationResult);

        List<DataPair> trainingResult = SelectorController.select(splitResult[0]);
        List<DataPair> testingResult = SelectorController.select(splitResult[1]);
        
        Writer.writeTrainingData(code, trainingResult);
        Writer.writeTestingData(code, testingResult);
        Writer.writeMeta(code);

        return splitResult[1];
        
    }
    
    public NormalizedDataPair prepareForPrediction(String code, List<Map<String, Object>> data){
        
        List<Map<String, Object>> indicationResult = IndicatorController.indicate(data);
        List<Map<String, Object>> transformationResult = TransformerController.transform(indicationResult);
        
        DataPair predictionSeparated = SeparatorController.separateForPrediction(transformationResult);
        NormalizedDataPair predictionNormalized = NormalizerController.normalize(predictionSeparated);
        DataPair predictionSelected = SelectorController.select(predictionNormalized); 
        
        Writer.writePredictionData(code, predictionSelected);
        Writer.writeMeta(code);
        
        return predictionNormalized;
        
    }
    
    public List<Map<String, Object>> clean(String code){
        
        List<Map<String, Object>> originalData = Reader.readOriginal(code);
        return CleanerController.clean(originalData); 

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

    private List<NormalizedDataPair>[] split(List<NormalizedDataPair> normalizationResult) {
        List<NormalizedDataPair>[] result = new  List[2];
        
        int split = (int) (normalizationResult.size()*SPLIT_RATE);
        
        result[0] = normalizationResult.subList(0, split);
        result[1] = normalizationResult.subList(split, normalizationResult.size());
        
        return result;
    }
    
}
