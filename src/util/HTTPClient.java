package util;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Class for the HTTP client
 * @author Zhishen Wen
 * @version Sep 20, 2013
 */
public class HTTPClient {
    
    /* a list of predefined URL parameters used by Sears.com */
    
    /** the main body of query to Sears */
    private final static String HOST_QUERY          = "http://www.sears.com/search=";
    
    /** the page number for result display */
    private final static String PARAM_PAGE_NUM      = "pageNum=";
    
    /** the specific levels for this item defined for Sears product division */
    private final static String PARAM_LEVELS        = "levels=";
    
    /** the key-value pair indicating a redirect URL to specific levels */
    private final static String PARAM_REDIRECT_TYPE = "redirectType=BRAT_RULE";
    
    /** the items to be viewed per page */
    private final static String PARAM_VIEW_ITEMS    = "viewItems=";
    
    /** the value for items to be viewed per page */
    public  final static int    VALUE_VIEW_ITEMS    = 50;
    
    /**
     * Gets a DOM Document root from Sears.com, given this
     * keyword and page number.
     * @param keyword The keyword to be searched.
     * @param pageNum The page number to searched.
     * @return A DOM Document representing the retrieved
     * web page.
     * @throws IOException If connection to Sears.com fails.
     */
    public Document getDOMTree(String keyword, int pageNum) 
           throws IOException {
        return getDOMTree(keyword, pageNum, 
                          HOST_QUERY       + keyword          + "?" + 
                          PARAM_VIEW_ITEMS + VALUE_VIEW_ITEMS + "&" +
                          PARAM_PAGE_NUM   + pageNum);
    }
    
    //------------------- private helpers -------------------
    
    /**
     * Helpers for getDOMTree. Gets a DOM Document from Sears.com.
     * @param keyword The keyword to be searched.
     * @param pageNum The page number to be searched.
     * @param query The query to be searched, used for recursion calls.
     * @return A DOM Document representing the retrieved web page.
     * @throws IOException If connection to Sears.com fails.
     */
    private Document getDOMTree(String keyword, int pageNum, String query) 
            throws IOException {
        String body = Jsoup.connect(query).execute().body();
        if (body.contains(PARAM_REDIRECT_TYPE)) {
            return getDOMTree(keyword + "%20item", pageNum);
        }
        else if (body.contains(PARAM_LEVELS)) {
            int begIndex = body.indexOf(PARAM_LEVELS) + PARAM_LEVELS.length();
            int endIndex = body.indexOf("&", begIndex);
            String levels = "";
            if (begIndex != -1 && endIndex != -1)
                levels = body.substring(begIndex, endIndex);
            try {
                return Jsoup.connect(query + "&" + PARAM_LEVELS + levels).get(); 
            }
            catch (MalformedURLException e) {
                return Jsoup.connect(query).get();
            }
        }
        else {
            return Jsoup.connect(query).get();
        }
    }

}
