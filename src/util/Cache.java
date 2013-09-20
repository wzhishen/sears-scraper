package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import datastructure.CacheEntry;
import datastructure.Item;

/**
 * Class for the query result cache.
 * @author Zhishen Wen
 * @version Sep 19, 2013
 */
public class Cache {
    
    /** cache mapping each keyword to a CacheEntry */
    private static HashMap<String, CacheEntry> cache;
    
    /** the name for the cache file */
    private static final String CACHE_FILE_NAME = "cache.dat";
    
    /** Private constructor for Cache. */
    private Cache() { }
    
    /**
     * Initializes this cache. Reads in data if
     * cache exists; creates new one otherwise.
     */
    @SuppressWarnings("unchecked")
    public static void init() {
        File f = new File(CACHE_FILE_NAME);
        try {
            if (f.exists()) {
                msg("[INFO] Cache file found.");
                ObjectInputStream oin = new ObjectInputStream(
                        new FileInputStream(f));
                cache = (HashMap<String, CacheEntry>) oin.readObject();
                oin.close();
                msg("[INFO] Load cache successfully.");
            }
            else {
                msg("[INFO] Cache file not found.");
                f.createNewFile();
                msg("[INFO] Create new cache successfully.");
            }
        }
        catch (ClassNotFoundException e) {
            msg("\n[WARN] Cache file format wrong. Now query without cache.");
            cache = null;
        }
        catch (IOException e) {
            msg("\n[WARN] Unable to access cache file. Now query without cache.");
            cache = null;
        }
        if (cache == null)
            cache = new HashMap<String, CacheEntry>();
    }
    
    /**
     * Adds a new CacheEntry to the existing cache.
     * @param keyword The keyword to be matched.
     * @param entry The CacheEntry object to be added.
     * @throws IllegalStateException If Cache.init has
     * not yet been called.
     */
    public static void addCacheEntry(String keyword, CacheEntry entry) {
        if (cache == null)
            throw new IllegalStateException("[ERROR] Cache has not initialized yet.");
        cache.put(keyword, entry);
        File f = new File(CACHE_FILE_NAME);
        try {
            ObjectOutputStream oout = new ObjectOutputStream(
                    new FileOutputStream(f));
            oout.writeObject(cache);
            oout.reset();
            oout.flush();
            oout.close();
            msg("[INFO] Results for '" + keyword + "' cached.");
        }
        catch (IOException e) {
            msg("[WARN] Unable to write to cache file. Abort write.");
        }
    }
    
    /**
     * Determines whether the existing cache contains an entry
     * with both this keyword and page number.
     * @param keyword The keyword to be checked.
     * @param pageNum The page number to be checked.
     * @return True if the existing cache contains an entry
     * with both this keyword and page number; false otherwise.
     */
    public static boolean containsEntry(String keyword, int pageNum) {
        if (!containsKeyword(keyword))
            return false;
        if (!cache.get(keyword).getresult().containsKey(pageNum))
            return false;
        return true;
    }
    
    /**
     * Determines whether the existing cache contains an entry
     * with this keyword.
     * @param keyword The keyword to be checked.
     * @return True if the existing cache contains an entry
     * with this keyword; false otherwise.
     */
    public static boolean containsKeyword(String keyword) {
        return cache.containsKey(keyword);
    }
    
    /**
     * Gets a List of Items given this keyword and page number.
     * @param keyword The keyword to be matched.
     * @param pageNum The page number to be matched.
     * @return A List of Items given this keyword and page 
     * number; null if no matches found.
     */
    public static List<Item> getItemsList(String keyword, int pageNum) {
        if (!containsEntry(keyword, pageNum))
            return null;
        return cache.get(keyword).getresult().get(pageNum);
    }
    
    /**
     * Gets the total number of Items given this keyword.
     * @param keyword The keyword to be matched.
     * @return The total number of Items given this keyword;
     * empty String if no matches found.
     */
    public static String getItemsTotalNum(String keyword) {
        if (!containsKeyword(keyword))
            return "";
        return cache.get(keyword).getTotal();
    }
    
    /**
     * Gets the result mapping page number to a List of Items.
     * @param keyword The keyword to be matched.
     * @return the result mapping page number to a List of Items;
     * null if no matches found.
     */
    public static HashMap<Integer, List<Item>> getItemsResult(String keyword) {
        if (!containsKeyword(keyword)) return null;
        return cache.get(keyword).getresult();
    }
    
    //------------------- private helpers -------------------
    
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
