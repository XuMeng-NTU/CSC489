/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.io;

import exception.DataException;
import exception.SettingsException;
import extractor.application.Controller;
import extractor.data.repr.THSUnsignedInteger;
import extractor.settings.definitions.Definition;
import extractor.settings.definitions.Definitions;
import extractor.settings.mappings.Mapping;
import extractor.settings.mappings.Mappings;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Meng
 */

public class Reader {
    
    public static List<Map<String, Object>> read(String code){
        try {
            
            InputStream data = new FileInputStream(Finder.find(Controller.getInstance().getSettings().lookupData("ORIGINAL"), code + ".day").toFile());
            Map<String, Object> meta = readByDefinitions(((Definitions)Controller.getInstance().getSettings().lookupComponent("DEFINITION", "FILE_META")), data);

            int columnNum = ((THSUnsignedInteger) (meta.get("COLUMN_NUM"))).intValue();

            Map<String, Definition> contentDefinitions = new LinkedHashMap<>();

            for (int i = 0; i < columnNum; i++) {
                Map<String, Object> columnMeta = readByDefinitions(((Definitions)Controller.getInstance().getSettings().lookupComponent("DEFINITION", "COLUMN_META")), data);

                String columnID = columnMeta.get("COLUMN_ID").toString();

                int size = ((THSUnsignedInteger) columnMeta.get("LENGTH")).intValue();

                boolean reverse;
                String name;
                Class repr;
                String desc = "unused";
                boolean skip;
                
                Mapping map = ((Mappings)Controller.getInstance().getSettings().lookupComponent("MAPPING", "DAILY")).getMapping().get(columnID);
                if (map == null) {
                    name = columnID;
                    repr = String.class;
                    skip = true;
                } else {
                    name = map.getSymbol();
                    repr = map.getRepr();
                    skip = false;
                }

                if (repr.equals(String.class)) {
                    reverse = false;
                } else{
                    reverse = true;
                }

                Definition definition = new Definition(name, desc, size, repr, reverse, skip);
                contentDefinitions.put(name, definition);                
            }
      
            Definitions content = new Definitions();
            content.setDefinition(contentDefinitions);       
            
            int i;
            int rowNum = ((THSUnsignedInteger) (meta.get("RECORD_NUM"))).intValue();

            List<Map<String, Object>> records = new ArrayList<>();
            for (i = 0; i < rowNum; i++) {            
                Map<String, Object> record = readByDefinitions(content, data);
                records.add(record);
            }

            return records;            

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException(ex);
        }    
    }
    
    private static Map<String, Object> readByDefinitions(Definitions definitions, InputStream file) {

        Map<String, Object> result = new LinkedHashMap<>();

        Set<Map.Entry<String, Definition>> definitionSet = definitions.getDefinition().entrySet();
        Iterator<Map.Entry<String, Definition>> iterator = definitionSet.iterator();

        Map.Entry<String, Definition> temp;

        while (iterator.hasNext()) {
            temp = iterator.next();
            if (!temp.getValue().getSkip()) {
                result.put(temp.getKey(), readByDefinition(temp.getValue(), file));
            } else {
                readByDefinition(temp.getValue(), file);
            }
        }

        return result;
    }
    
    private static Object readByDefinition(Definition definition, InputStream file){
        try {
            byte[] bytes = new byte[definition.getSize()];
            file.read(bytes);
            
            if(definition.getReverse()){
                bytes = reverseBytes(bytes);
            }
            
            return definition.getRepr().getConstructor(bytes.getClass()).newInstance(bytes);
            
        } catch (IOException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Definition.class.getName()).log(Level.SEVERE, null, ex);
            throw new SettingsException(ex);
        }
    }
    
    private static byte[] reverseBytes(byte[] bytes){
        byte[] reversedBytes = new byte[bytes.length];
        for(int m=0, n=bytes.length-1;n>=0;m++, n--){
            reversedBytes[m] = bytes[n];
        }        
        return reversedBytes;
    }        

}
