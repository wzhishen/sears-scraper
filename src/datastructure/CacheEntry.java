package datastructure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Class for a cache entry.
 * @author Zhishen Wen
 * @version Sep 19, 2013
 */
public class CacheEntry implements Serializable {
    
    /** assigned serial number */
    private static final long serialVersionUID = 1001L;
    
    /** total number of pages for this cache entry */
    private String total;
    
    /** hashtable for mapping each page to a list of Items */
    private HashMap<Integer, List<Item>> result;
    
    /**
     * Constructor for CacheEntry.
     * @param total Total number of pages for this cache entry.
     * @param result Hashtable for mapping each page to a list 
     * of Items for this cache entry.
     */
    public CacheEntry(String total, HashMap<Integer, List<Item>> result) {
        this.total = total;
        this.result = result;
    }
    
    /**
     * Getter for field total.
     * @return Value of field total.
     */
    public String getTotal() {
        return total;
    }
    
    /**
     * Setter for field total.
     * @param total The value to be set
     * to field total.
     */
    public void setTotal(String total) {
        this.total = total;
    }
    
    /**
     * Getter for field result.
     * @return Value of field result.
     */
    public HashMap<Integer, List<Item>> getresult() {
        return result;
    }
    
    /**
     * Setter for field result.
     * @param result The value to be set
     * to field result.
     */
    public void setresult(HashMap<Integer, List<Item>> result) {
        this.result = result;
    }
    
}
