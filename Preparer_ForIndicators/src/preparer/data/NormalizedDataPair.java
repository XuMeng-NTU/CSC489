/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preparer.data;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Meng
 */
public class NormalizedDataPair extends DataPair{
    private Map<String, Map<String, Object>> params;
    
    public NormalizedDataPair(List<Map<String, Object>> input, List<Map<String, Object>> output, Map<String, Map<String, Object>> params){
        super(input, output);
        this.params = params;
    }
    
    public Map<String, Map<String, Object>> getParams(){
        return params;
    }
    
}
