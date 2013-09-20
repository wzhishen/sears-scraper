package datastructure;

import java.io.Serializable;

/**
 * Class for a single product item.
 * @author Zhishen Wen
 * @version Sep 19, 2013
 */
public class Item implements Serializable {
    
    /** assigned serial number */
    private static final long serialVersionUID = 1000L;
    
    /** the name of this item */
    private String name;
    
    /** the price of this item */
    private String price;
    
    /** the vendor of this item */
    private String vendor;
    
    /**
     * Constructor for Item
     * @param name The name of this item.
     * @param price The price of this item.
     * @param vendor The vendor of this item.
     */
    public Item(String name, String price, String vendor) {
        this.name = name;
        this.price = price;
        this.vendor = vendor;
    }

    /**
     * Getter for field name.
     * @return Value of field name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Setter for field name.
     * @param name The value to be set
     * to field name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Getter for field price.
     * @return Value of field price.
     */
    public String getPrice() {
        return price;
    }
    
    /**
     * Setter for field price.
     * @param name The value to be set
     * to field price.
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Getter for field vendor.
     * @return Value of field vendor.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Setter for field vendor.
     * @param name The value to be set
     * to field vendor.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
}
