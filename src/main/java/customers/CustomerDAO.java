package customers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The CustomerDAO class provides methods for managing customer records in an SQLite database.
 * This class implements various methods such as adding, retrieving, updating, and deleting customers, 
 * as well as fetching specific customer IDs based on attributes.
 * 
 * The `customer` table stores customer details, including their ID, business name, address, 
 * telephone number, and email address.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class CustomerDAO { 
	
	/**
     * Default constructor for CustomerDAO.
     * Initialises a new instance of the class without any additional setup.
     */
    public CustomerDAO() {
        // No specific initialisation required
    }

	/**
     * Establishes a connection to the SQLite appliance database (which is stored in root of this project).
     * A 'customer' table is created if it does not already exist. The table stores 
     * customer information.
     * 
     * @return a Connection object to the SQLite database
     */
	protected Connection connect() {
	    Connection conn = null;
	    
	    try {
	        conn = DriverManager.getConnection("jdbc:sqlite:appliance.db");
	        String table = "CREATE TABLE IF NOT EXISTS customer (" +
	                       "customerId INTEGER PRIMARY KEY AUTOINCREMENT, " +
	                       "businessName TEXT, " +
	                       "address TEXT, " +
	                       "telephoneNumber TEXT, " +
	                       "emailAddress TEXT)";
	        try (PreparedStatement preStatement = conn.prepareStatement(table)) {
	            preStatement.execute();
	        }  // Automatically closes the PreparedStatement
	    } catch(SQLException ex) {
	        ex.printStackTrace();
	    }
	    return conn;
	}
	
	/**
     * Retrieves all customer records from the database.
     * 
     * @return an ArrayList of Customer objects, or an empty list if no records are found
     */
	public ArrayList<Customer> findAllCustomers() {
		String query = "SELECT * FROM customer;";
		ArrayList<Customer> customers = new ArrayList<>();
		try (Connection conn = this.connect();
				PreparedStatement preStatement = conn.prepareStatement(query);
				ResultSet result = preStatement.executeQuery()){
					while (result.next()) {
						Customer cust = new Customer(
								result.getString("businessName"), 
								Address.fromString(result.getString("address")), 
								result.getString("telephoneNumber"),
								result.getString("emailAddress")
						);
						cust.setCustomerID(result.getInt("customerId"));
						customers.add(cust);
					}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return customers;
	}
	
	 /**
     * Finds a specific customer record by its unique ID.
     * 
     * @param customerID the ID of the customer to retrieve
     * @return the Customer object if found, or null if not found
     */
	public Customer findCustomer(int customerID) {
		String query = "SELECT * FROM customer WHERE customerId = ?";
		try (Connection conn = this.connect();
				PreparedStatement preStatement = conn.prepareStatement(query)){
			preStatement.setInt(1, customerID);
			ResultSet result = preStatement.executeQuery();
			if (result.next()) {
				Customer cust = new Customer(
						result.getString("businessName"), 
						Address.fromString(result.getString("address")), 
						result.getString("telephoneNumber"),
						result.getString("emailAddress")
				);
				cust.setCustomerID(result.getInt("customerId"));
				return cust;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
     * Deletes a customer record from the database by its unique ID.
     * 
     * @param customerID the ID of the customer to delete
     * @return true if the deletion is successful, false otherwise
     */
	public boolean deleteCustomer(int customerID) {
		String query = "DELETE FROM customer WHERE customerId = ?";
		try (Connection conn = this.connect();
				PreparedStatement preStatement = conn.prepareStatement(query)){
			preStatement.setInt(1, customerID);
			int rows = preStatement.executeUpdate();
			return rows > 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
     * Updates an existing customer record in the database with new details.
     * 
     * @param customer the Customer object containing the updated details
     * @return true if the update is successful, false otherwise
     */
	public boolean updateCustomer(Customer customer) {
		String query = "UPDATE customer SET businessName = ?, address = ?, telephoneNumber = ?, emailAddress = ? WHERE customerId = ?";
		try (Connection conn = this.connect();
				PreparedStatement preStatement = conn.prepareStatement(query)){
			preStatement.setString(1, customer.getBusinessName());
			preStatement.setString(2, customer.getAddress().toString());
			preStatement.setString(3, customer.getTelephoneNumber());
			preStatement.setString(4, customer.getEmailAddress());
			preStatement.setInt(5, customer.getCustomerID());
			int rows = preStatement.executeUpdate();
			return rows > 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
    /**
     * Adds a new customer record to the database.
     * 
     * @param customer the Customer object to be added
     * @return true if the addition is successful, false otherwise
     */
	public boolean addCustomer(Customer customer) {
		String query = "INSERT INTO customer(businessName, address, telephoneNumber, emailAddress) VALUES(?, ?, ?, ?)";
		try (Connection conn = this.connect();
				PreparedStatement preStatement = conn.prepareStatement(query)){
			preStatement.setString(1, customer.getBusinessName());
			preStatement.setString(2, customer.getAddress().toString());
			preStatement.setString(3, customer.getTelephoneNumber());
			preStatement.setString(4, customer.getEmailAddress());
			int rows = preStatement.executeUpdate();
			return rows > 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;

	}
	
	/**
     * Retrieves the ID of a customer by their business name and email address.
     * 
     * @param businessName the business name of the customer
     * @param emailAddress the email address of the customer
     * @return the customer ID if found, or -1 if no matching record is found
     */
	public int getCustomerId(String businessName, String emailAddress) {
	    String query = "SELECT customerId FROM customer WHERE businessName = ? AND emailAddress = ?";
	    try (Connection conn = this.connect();
	         PreparedStatement preStatement = conn.prepareStatement(query)) {
	        preStatement.setString(1, businessName);
	        preStatement.setString(2, emailAddress);
	        ResultSet resultSet = preStatement.executeQuery();
	        if (resultSet.next()) {
	            return resultSet.getInt("customerId");
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return -1; // Return -1 if no customer is found
	}
	
}
