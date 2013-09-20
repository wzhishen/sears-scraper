package util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import datastructure.Item;

/**
 * Class for the DOM parser.
 * @author Zhishen Wen
 * @version Sep 20, 2013
 */
public class DOMParser {
        
        /** the DOM Document to be parsed */
        private Document doc;
        
        /**
         * Reads in a DOM Document to be parsed.
         * @param doc The DOM Document to be parsed.
         */
        public void parse(Document doc) {
            this.doc = doc;
        }
        
        /**
         * Gets a List of Items for this Document.
         * @return A List of Items; an empty List if no matches found.
         */
        public List<Item> getAllItems() {
            Elements items = doc.getElementsByAttributeValue("class", "cardInner");
            List<Item> itemList = new ArrayList<Item>();
            for (Element item : items) {
                // extracts product name/title
                String name = item.getElementsByAttributeValue("class", "cardProdTitle").get(0).text();
                
                // extracts product price
                Elements pricingInfo = item.getElementsByAttributeValueContaining("class", "price_v2");
                String price = pricingInfo.size() == 0 ? pricingInfo.text() : pricingInfo.get(0).text();
                
                // extract vendor name
                String seller = item.getElementsByAttributeValue("id", "mrkplc").text();
                // normalizes the String
                seller = seller
                        .replaceAll("[^(\\x20-\\x7F)]*", "")
                        .replaceAll("\\|", "")
                        .replaceAll("Sold by", "")
                        .replaceAll("ShopYourWay Guarantee Seller", "")
                        .replaceAll("Marketplace Item", "")
                        .replaceAll("learn more", "")
                        .trim();
                String vendor = seller.isEmpty() ? "Sears" : seller;
                
                itemList.add(new Item(name, price, vendor));
            }
            return itemList;
        }
        
        /**
         * Gets the total number of Items found.
         * @return The total number of Items found;
         * an empty String if no matches found.
         */
        public String getItemsTotalNum() {
            try {
                String itemsTotalInfo = doc.getElementById("nmbProdItems").text();
                return itemsTotalInfo.split("of")[1].substring(1);
            }
            catch (NullPointerException e) {
                return "";
            }
        }
        
}
