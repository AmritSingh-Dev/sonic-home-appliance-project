package users;

/**
 * The Users constructor class creates a user attributes in the system. Each user has an ID, 
 * username, password, role, and optionally an associated customer ID. 
 * This class provides getter and setter methods for accessing and modifying 
 * user details, as well as a custom `toString` method for generating a string 
 * representation of the user.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class Users {
	
	/**
     * Unique identifier for the user.
     */
	private int userId;
	
    /**
     * The username of the user.
     */
	private String username;
	
    /**
     * The password of the user.
     */
	private String password;
	
    /**
     * The role of the user (e.g., admin, customer).
     */
	private String role;
	
    /**
     * The associated customer ID, if applicable. Null if the user is not associated with a customer.
     */
	private Integer customerId;
	
	/**
     * Constructs a new Users object with the specified details.
     * 
     * @param username the username of the user
     * @param password the password of the user
     * @param role the role of the user
     * @param customerId the associated customer ID, or null if not applicable
     */
	public Users(String username, String password, String role, Integer customerId) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.customerId = customerId;
	}
	
	 /**
     * Returns the unique ID of the user.
     * 
     * @return the user ID
     */
	public int getUserId() {
		return userId;
	}
	
	  /**
     * Returns the username of the user.
     * 
     * @return the username
     */
	public String getUsername() {
		return username;
	}
	
	/**
     * Returns the password of the user.
     * 
     * @return the password
     */
	public String getPassword() {
		return password;
	}
	
	 /**
     * Returns the role of the user.
     * 
     * @return the role
     */
	public String getRole() {
		return role;
	}
	
	 /**
     * Returns the associated customer ID, if applicable.
     * 
     * @return the customer ID, or null if not applicable
     */
	public Integer getCustomerId() {
		return customerId;
	}
	
	/**
     * Sets the unique ID of the user.
     * 
     * @param userId the new user ID to set
     */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	 /**
     * Sets the username of the user.
     * 
     * @param username the new username to set
     */
	public void setUsername(String username) {
		this.username = username;
	}
	
	 /**
     * Sets the password of the user.
     * 
     * @param password the new password to set
     */
	public void setPassword(String password) {
		this.password = password;
	}
	
	 /**
     * Sets the role of the user.
     * 
     * @param role the new role to set
     */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
     * Sets the associated customer ID for the user.
     * 
     * @param customerId the new customer ID to set, or null if not applicable
     */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	 /**
     * Provides a string representation of the Users object, excluding the customer ID.
     * 
     * @return a formatted string containing the user ID, username, password, and role
     */
	@Override
	public String toString() {
		return "ID: " + userId + ", Username: " + username + ", Password: " + password + ", Role: " + role;
	}
	
}
