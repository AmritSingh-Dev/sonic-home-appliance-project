package homeappliance;

import java.util.HashMap;
import java.util.Map;

/**
 * The Basket class is for managing ApplianceItem objects and their quantities.
 * This class provides functionalities to add items, remove items, retrieve the current basket contents,
 * calculate the total price, and clear the basket.
 * 
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class Basket {
	
	/**
     * Default constructor for Basket.
     * Initialises a new instance of the class without any additional setup.
     */
    public Basket() {
        // No specific initialisation required
    }
	
    private Map<ApplianceItem, Integer> items = new HashMap<>();

    /**
     * Adds an ApplianceItem to the basket. If the item already exists in the basket, 
     * its quantity is incremented by 1.
     * 
     * @param item the ApplianceItem to add to the basket
     */
    public void addItem(ApplianceItem item) {
        int currentQuantity = items.getOrDefault(item, 0);
        items.put(item, currentQuantity + 1);
    }

    /**
     * Removes one instance of an ApplianceItem from the basket based on its ID. 
     * If the item's quantity is greater than 1, it decrements the quantity by 1. 
     * Otherwise, it removes the item from the basket entirely.
     * 
     * If the item is not found in the basket, an informational message is displayed.
     * 
     * @param itemId the ID of the ApplianceItem to remove
     */
    public void removeItem(int itemId) {
        ApplianceItem itemToRemove = null;
        for (ApplianceItem item : items.keySet()) {
            if (item.getId() == itemId) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            int quantity = items.get(itemToRemove);
            if (quantity > 1) {
                items.put(itemToRemove, quantity - 1);
            } else {
                items.remove(itemToRemove);
            }
        } else {
            System.out.println("Item with ID " + itemId + " not found in the basket.");
        }
    }

    /**
     * Retrieves a map of all items in the basket along with their quantities.
     * 
     * @return a map where the keys are ApplianceItem objects and the values are their quantities
     */
    public Map<ApplianceItem, Integer> getItemsWithQuantities() {
        return items;
    }

    /**
     * Calculates the total price of all items in the basket. The total price is determined
     * by summing the price of each item multiplied by its quantity.
     * 
     * @return the total price of items in the basket
     */
    public int getTotalPrice() {
        return items.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getHomeAppliance().getPrice() * entry.getValue())
                .sum();
    }
    
    /**
     * Clears all items from the basket, effectively resetting it to an empty state.
     */
    public void clear() {
        items.clear(); 
    }
}