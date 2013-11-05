/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.ticker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Meng
 */
public class TickerRetriever {
    
    public static final String FOLDER = "D:\\同花顺软件\\同花顺\\history\\shase\\day\\";
    
    public static List<String> retrieve(String base){
        
        List<String> result = new ArrayList<>();
        
        File start = new File(base);
        for(File f : start.listFiles()){
            int indicator = f.getName().indexOf(".day");
            if(indicator!=-1){
                if(f.getName().startsWith("6")){
                    result.add(f.getName().substring(0,indicator));
                }
            } else{
                if(f.isDirectory()){
                    result.addAll(retrieve(f.getAbsolutePath()));
                }
            }
        }      
        result.remove("600015");
        result.remove("600028");
        return result;
    }
    
    public static void main(String[] args){
        System.out.println(retrieve(FOLDER));
    }
}
