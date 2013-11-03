/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.jaxb;

import exception.SettingsException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Xu Meng
 */
public class BindingHelper {

    public static <T> T unmarshal(Class<T> target, Path file){         
        try {
            JAXBContext jaxb = JAXBContext.newInstance(target);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file.toFile());
            Element root = doc.getDocumentElement();
            return unmarshaller.unmarshal(root, target).getValue();

        } catch (JAXBException | ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(BindingHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
    public static <T> void marshal(Class<?> T, T object, Path file){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(T);
            Marshaller marshaller = jaxbContext.createMarshaller();

            Class objFacClass = Class.forName(T.getPackage().getName()+".ObjectFactory");
            
            JAXBElement result = (JAXBElement) objFacClass.getMethod("create"+T.getSimpleName(), T).invoke(objFacClass.newInstance(), object);
            
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(result, file.toFile());
      
        } catch (JAXBException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BindingHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
