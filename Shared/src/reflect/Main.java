/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reflect;

import util.jaxb.BeanGenerator;

/**
 *
 * @author Meng
 */
public class Main {

    public static void main(String[] args) {
        BeanGenerator.generate(template.registry.xml.Registry.class, "template.registry");
        BeanGenerator.generate(template.metadata.xml.Metadata.class, "template.metadata");
        BeanGenerator.generate(template.components.xml.Components.class, "template.components");
        BeanGenerator.generate(template.configurations.xml.Configurations.class, "template.configurations");
/*        
        BeanGenerator.generate(extractor.settings.definitions.xml.Definitions.class, "extractor.settings.definitions");
        BeanGenerator.generate(extractor.settings.mapping.xml.Mappings.class, "extractor.settings.mapping");
        
        BeanGenerator.generate(preparer.settings.mapping.xml.Mappings.class, "preparer.settings.mapping");
        BeanGenerator.generate(preparer.settings.dataset.xml.Dataset.class, "preparer.settings.dataset");
        BeanGenerator.generate(preparer.settings.validators.xml.Validators.class, "preparer.settings.validators");
        BeanGenerator.generate(preparer.settings.indicators.xml.Indicators.class, "preparer.settings.indicators");
        BeanGenerator.generate(preparer.settings.transformers.xml.Transformers.class, "preparer.settings.transformers");
        BeanGenerator.generate(preparer.settings.separators.xml.Separators.class, "preparer.settings.separators");
        BeanGenerator.generate(preparer.settings.normalizers.xml.Normalizers.class, "preparer.settings.normalizers");
        BeanGenerator.generate(preparer.settings.selectors.xml.Selectors.class, "preparer.settings.selectors");
        BeanGenerator.generate(preparer.settings.comparers.xml.Comparers.class, "preparer.settings.comparers");
        
        BeanGenerator.generate(predictor.settings.networks.xml.Networks.class, "predictor.settings.networks");
        BeanGenerator.generate(predictor.settings.trainers.xml.Trainers.class, "predictor.settings.trainers");
        BeanGenerator.generate(predictor.settings.supervisors.xml.Supervisors.class, "predictor.settings.supervisors");
        BeanGenerator.generate(predictor.settings.learning.xml.Learning.class, "predictor.settings.learning");
*/        
    }
}
