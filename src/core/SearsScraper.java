package core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import datastructure.CacheEntry;
import datastructure.Item;

import util.Cache;
import util.DOMParser;
import util.HTTPClient;

/**
 * Main class for Sears.com text scraper.
 * @author Zhishen Wen
 * @version Sep 20, 2013
 */
public class SearsScraper {
    
    /**
     * Runs Sears.com text scraper.
     * @param args Command line args.
     */
    public static void main(String[] args) {
        title();
        if (args.length == 0 || args.length > 2) {
            msg("[ERROR] Invalid number of arguments.");
            usage();
            return;
        }
        
        HTTPClient client = new HTTPClient();
        DOMParser parser = new DOMParser();
        String keyword;
        int pageNum = 1;
        
        // normalizes keyword
        try {
            args[0] = args[0].trim();
            if (args[0].isEmpty()) {
                msg("[INFO] Invalid value of keyword.");
                return;
            }
            keyword = URLEncoder.encode(args[0], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //unsupported UTF-8 encoding, just uses original keyword
            keyword = args[0];
        }
        
        // handles query type #1
        if (args.length == 1) {
            Cache.init();
            String totalNum;
            // cached entries found: gets result from local cache
            if (Cache.containsEntry(keyword, pageNum)) {
                msg("[INFO] Cached results found. Query locally from cache...");
                totalNum = Cache.getItemsTotalNum(keyword);
            }
            // cached entries not found: gets result from remote server
            else {
                msg("[INFO] Query remotely to 'Sears.com'...");
                try {
                    Document doc = client.getDOMTree(keyword, pageNum);
                    parser.parse(doc);
                    totalNum = parser.getItemsTotalNum();
                    cacheResult(keyword, pageNum, totalNum, parser); 
                }
                catch (IOException e) {
                    msg("[ERROR] Unable to connect to 'Sears.com'.");
                    return;
                }
            }
            // displays result to console
            if (totalNum.isEmpty())
                msg("\nNo entry matched.");
            else
                msg("\nTotal number of '" + args[0] + "': " + totalNum);
        }
        
        // handles query type #2
        else if (args.length == 2) {
            try {
                pageNum = Integer.parseInt(args[1]);
                if (pageNum <= 0) {
                    msg("[ERROR] Non-positive value for the second argument.");
                    usage();
                    return;
                }
            }
            catch (NumberFormatException e) {
                msg("[ERROR] Invalid value for the second argument.");
                usage();
                return;
            }
            Cache.init();
            String totalNum;
            List<Item> itemsList = null;
            // cached entries found: gets result from local cache
            if (Cache.containsEntry(keyword, pageNum)) {
                msg("[INFO] Cached results found. Query locally from cache...");
                totalNum = Cache.getItemsTotalNum(keyword);
                itemsList = Cache.getItemsList(keyword, pageNum);
            }
            // cached entries not found: gets result from remote server
            else {
                msg("[INFO] Query remotely to 'Sears.com'...");
                try {
                    Document doc = client.getDOMTree(keyword, pageNum);
                    parser.parse(doc);
                    totalNum = parser.getItemsTotalNum();
                    itemsList = parser.getAllItems();
                    cacheResult(keyword, pageNum, totalNum, parser);
                }
                catch (SocketTimeoutException e) {
                    msg("[WARN] Socket timeout, please try again.");
                    return;
                }
                catch (IOException e) {
                    msg("[ERROR] Unable to connect to 'Sears.com'.");
                    return;
                }
            }
            // displays result to console
            if (itemsList == null || itemsList.isEmpty()) {
                msg("\nNo entry matched.");
            }
            else {
                int viewItemsNum = HTTPClient.VALUE_VIEW_ITEMS;
                String span = "Items " + 
                       ((pageNum - 1) * viewItemsNum + 1) + "-" + 
                       ((pageNum - 1) * viewItemsNum + itemsList.size()) + 
                       " of " + totalNum + " for '" + args[0] + "'";
                msg("\n" + span);
                msg("======================================================");
                for (int i = 0; i < itemsList.size(); ++i) {
                    Item item = itemsList.get(i);
                    msg("- " + ((pageNum - 1) * viewItemsNum + i + 1) + " -");
                    msg(" [NAME]   " + item.getName());
                    msg(" [PRICE]  " + item.getPrice());
                    msg(" [VENDOR] " + item.getVendor());
                    msg("");
                }
                msg("======================================================");
                msg(span);
            }
        }
    }
    
    //------------------- private helpers -------------------
    
    /**
     * Caches return results.
     * @param keyword The keyword to be searched.
     * @param pageNum The page number to be searched.
     * @param totalNum The total number of pages found.
     * @param parser The DOM parser object.
     */
    private static void cacheResult(String keyword, int pageNum, String totalNum, DOMParser parser) {
        List<Item> itemList;
        HashMap<Integer, List<Item>> result;
        try {
            itemList = parser.getAllItems();
        }
        catch (NullPointerException e) {
            itemList = null;
        }
        if (Cache.containsKeyword(keyword))
            result = Cache.getItemsResult(keyword);
        else
            result = new HashMap<Integer, List<Item>>();
        result.put(pageNum, itemList);
        CacheEntry entry = new CacheEntry(totalNum, result);
        Cache.addCacheEntry(keyword, entry);
    }
    
    /**
     * Prints app title.
     */
    private static void title() {
        msg("-----------------------------------");
        msg("|          SEARS SCRAPER          |");
        msg("-----------------------------------");
    }
    
    /**
     * Prints app usage.
     */
    private static void usage() {
        msg("");
        msg("    Usage : SearsScraper <keyword> [<pageNum>]");
        msg("<keyword> : The keyword to be searched on 'Sears.com'.");
        msg("<pageNum> : (Optional) The page number (positive) to");
        msg("            customize result display. With this argument");
        msg("            detailed item information will be displayed.");
    }
    
    /**
     * Prints a message to console, with a newline
     * char appended.
     * @param msg Message to be displayed.
     */
    private static void msg(Object msg) {
        msg(msg, true);
    }
    
    /**
     * Prints a message to console.
     * @param msg Message to be displayed.
     * @param nl True if a newline char is appended
     * to this message; false otherwise.
     */
    private static void msg(Object msg, boolean nl) {
        System.out.print(msg);
        if (nl) System.out.print("\n");
    }
}
