/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.jaxb;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;
import exception.SettingsException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.jaxb.LoadingHelper;

/**
 *
 * @author Meng
 */
public class BeanGenerator {
    
    public static final Path OUTPUT = Paths.get("src/");
    
    public static void generate(Class sample, String packageName){
        try {
            JCodeModel model = new JCodeModel();
            JPackage jPackage = model._package(packageName);
            JDefinedClass root = generate(sample, jPackage);
            
            
            JClass loadingHelper = (new JCodeModel()).ref(LoadingHelper.class);
            
            JMethod loadMethod = root.method(JMod.PUBLIC | JMod.STATIC, root, "load");
            JVar pathParamForLoad = loadMethod.param(Path.class, "path");
            JBlock loadMethodBlock = loadMethod.body();

            JInvocation loadStatement = loadingHelper.staticInvoke("load");
            
            loadStatement = loadStatement.arg(pathParamForLoad);
            loadStatement = loadStatement.arg((new JCodeModel()).ref(sample).dotclass());
            loadStatement = loadStatement.arg(root.dotclass());
            
            loadMethodBlock._return(loadStatement);

            
            
            JMethod dumpMethod = root.method(JMod.PUBLIC | JMod.STATIC, void.class, "dump");
            JVar pathParamForDump = dumpMethod.param(Path.class, "path");
            JVar objParamForDump = dumpMethod.param(root, "object");
            JBlock dumpMethodBlock = dumpMethod.body();            
            
            JInvocation dumpStatement = loadingHelper.staticInvoke("dump");
            
            dumpStatement = dumpStatement.arg(pathParamForDump);
            dumpStatement = dumpStatement.arg((new JCodeModel()).ref(sample).dotclass());
            dumpStatement = dumpStatement.arg(root.dotclass());
            dumpStatement = dumpStatement.arg(objParamForDump);
            
            dumpMethodBlock.add(dumpStatement);
            
            Files.createDirectories(OUTPUT);
            model.build(OUTPUT.toFile());
        } catch (IOException ex) {
            Logger.getLogger(BeanGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static JDefinedClass generate(Class sample, JPackage jPackage){
     
        try {
            JDefinedClass jClass = jPackage._class(sample.getSimpleName());
            
            JMethod defaultConstructor = jClass.constructor(JMod.PUBLIC);
            
            JMethod constructor = jClass.constructor(JMod.PUBLIC);
            JBlock constructorBlock = constructor.body();
            
            for (Field originalField : sample.getDeclaredFields()) {
                Class originalClass = originalField.getType();
                JFieldVar jField;
                if(originalClass.getPackage()==null){
                    jField = jClass.field(JMod.PROTECTED, originalClass, originalField.getName());
                } else if (originalClass.getPackage().getName().equalsIgnoreCase(sample.getPackage().getName())) {
                    JDefinedClass fieldClass = generate(originalClass, jPackage);
                    jField = jClass.field(JMod.PROTECTED, fieldClass, originalField.getName());
               
                } else if (List.class.isAssignableFrom(originalClass)) {
                    Class genericType = Class.forName(extractGenericType(originalField.getGenericType()));
                    
                    JClass newGenericType;
                    
                    if(genericType.getPackage()!=null && genericType.getPackage().getName().equalsIgnoreCase(sample.getPackage().getName())){
                        newGenericType = generate(genericType, jPackage);
                    } else{
                        newGenericType = (new JCodeModel()).ref(genericType.getCanonicalName());
                    }
                    
                    JClass fieldClass = (new JCodeModel()).ref("java.util.Map");
                    fieldClass = fieldClass.narrow(String.class);
                    fieldClass = fieldClass.narrow(newGenericType);
                    jField = jClass.field(JMod.PROTECTED, fieldClass, originalField.getName());                        
                } else{
                    jField = jClass.field(JMod.PROTECTED, originalClass, originalField.getName());
                }
                
                JVar param = constructor.param(jField.type(), jField.name()+"Init");
                constructorBlock = constructorBlock.assign(jField, param);
                
                JMethod getMethod = jClass.method(JMod.PUBLIC, jField.type(), "get" + capitalizeFirst(jField.name()));
                getMethod.body()._return(jField);
                
                JMethod setMethod = jClass.method(JMod.PUBLIC, void.class, "set" + capitalizeFirst(jField.name()));
                JVar setParam = setMethod.param(jField.type(), jField.name()+"Value");
                setMethod.body().assign(jField, setParam);
                
            }
            return jClass;
        } catch (JClassAlreadyExistsException ex ) {
            return ex.getExistingClass();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BeanGenerator.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
    private static String capitalizeFirst(String word){
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }
    
    private static String extractGenericType(Type type){
        String typeString = type.toString();
        return typeString.substring(typeString.indexOf("<")+1, typeString.indexOf(">"));
    }
    
}
