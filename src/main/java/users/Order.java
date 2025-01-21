package users;

import java.sql.Timestamp;

/**
 * The Order class represents an order placed by a user. It includes details such as 
 * the order ID, the user who placed the order, and the total price of the order.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class Order {
	
    /**
     * The unique ID for the order.
     */
    private int orderId;
    
    /**
     * The user who placed the order.
     */
    private Users user;
    
    /**
     * The total price of the order.
     */
    private int totalPrice;
    
    /**
     * The date when the order was created.
     */
    private Timestamp date; 
    
    /**
     * Constructs a new Order with the specified details.
     * 
     * @param orderId - the unique ID of the order
     * @param user - the user who placed the order
     * @param totalPrice  the total price of the order
     * @param date - the date when the order was created
     */
    public Order(int orderId, Users user, int totalPrice, Timestamp date) {
        this.orderId = orderId;
        this.user = user;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    /**
     * Returns the unique ID of the order.
     * 
     * @return the order ID
     */
    public int getOrderId() {
        return orderId;
    }
    
    /**
     * Returns the user who placed the order.
     * 
     * @return the user
     */
    public Users getUser() {
        return user;
    }
    
    /**
     * Returns the total price of the order.
     * 
     * @return the total price
     */
    public int getTotalPrice() {
        return totalPrice;
    }
    
    /**
     * Returns the date when the order was created.
     * 
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }
    
    /**
     * Sets the unique ID of the order.
     * 
     * @param orderId the new order ID
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    /**
     * Sets the user who placed the order.
     * 
     * @param user the new user
     */
    public void setUser(Users user) {
        this.user = user;
    }
    
    /**
     * Sets the total price of the order.
     * 
     * @param totalPrice the new total price
     */
    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    /**
     * Sets the date when the order was created.
     * 
     * @param date - the new date timestamp
     */
    public void setDate(Timestamp date) {
        this.date = date;
    }
}
