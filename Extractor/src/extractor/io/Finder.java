/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.io;

import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author Meng
 */
public class Finder {
    
    public static Path find(Path base, String name){
        File start = base.toFile();
        for(File f : start.listFiles()){           
            if(f.getName().equals(name)){
                return f.toPath();
            } else{
                if(f.isDirectory()){
                    Path result = find(base.resolve(f.getName()), name);
                    if(result!=null){
                        return result;
                    }
                }
            }
        }
        return null;
    }
}
