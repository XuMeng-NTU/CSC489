/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.reflect;

import util.jaxb.BeanGenerator;

/**
 *
 * @author Meng
 */
public class Main {

    public static void main(String[] args) {
        BeanGenerator.generate(extractor.settings.definitions.xml.Definitions.class, "extractor.settings.definitions");
        BeanGenerator.generate(extractor.settings.mappings.xml.Mappings.class, "extractor.settings.mappings");
    }
}
