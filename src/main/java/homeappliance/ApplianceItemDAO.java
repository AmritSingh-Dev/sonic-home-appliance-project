package homeappliance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * ApplianceItemDAO is a Data Access Object (DAO) class that provides methods 
 * for managing ApplianceItem records in an SQLite database. 
 * This class includes operations for adding, retrieving, updating, and deleting ApplianceItem objects 
 * as well as sorting and querying records by specific attributes.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class ApplianceItemDAO {
	
	/**
     * Default constructor for ApplianceItemDAO.
     * Initialises a new instance of the class without any additional setup.
     */
    public ApplianceItemDAO() {
        // No specific initialisation required
    }
	
	/**
     * Establishes a connection to the SQLite appliance database (which is stored in root of this project).
     * A 'applianceItem' table is created if it does not already exist. The table stores 
     * ApplianceItem information and is associated with the 'appliance' table via 
     * a foreign key.
     * 
     * @return a Connection object to the SQLite database
     */
	private Connection connect() {
	    Connection conn = null;
	    
	    try {
	        conn = DriverManager.getConnection("jdbc:sqlite:appliance.db"); //Establish connection to DB specified by file path 
	        String table = "CREATE TABLE IF NOT EXISTS applianceItem (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "homeApplianceId INTEGER NOT NULL, " +
                    "warrantyYears INTEGER, " +
                    "brand TEXT, " +
                    "model TEXT, " +
                    "FOREIGN KEY (homeApplianceId) REFERENCES appliance(id) ON DELETE CASCADE)";
	        try (PreparedStatement preStatement = conn.prepareStatement(table)) {  // Use try-with-resources to ensure that the PreparedStatement is closed
	            preStatement.execute(); // Executes the SQL statement without any parameters
	        }
	    } catch(SQLException ex) {
	        ex.printStackTrace();
	    }
	    return conn;
	}
	
		/**
	     * This method adds a new ApplianceItem to the database.
	     * 
	     * @param item - the ApplianceItem object to be added
	     * @return true if the addition is successful, false otherwise
	     */
		public boolean addApplianceItem(ApplianceItem item) {
	        String query = "INSERT INTO applianceItem (homeApplianceId, warrantyYears, brand, model) VALUES (?, ?, ?, ?)";
	        try (Connection conn = this.connect();
	             PreparedStatement preStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	            preStatement.setInt(1, item.getHomeAppliance().getId());
	            preStatement.setInt(2, item.getWarrantyYears());
	            preStatement.setString(3, item.getBrand());
	            preStatement.setString(4, item.getModel());
	            int rows = preStatement.executeUpdate();
	            if (rows > 0) {
	                try (ResultSet generatedKeys = preStatement.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        item.setId(generatedKeys.getInt(1));
	                    }
	                }
	                return true;
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return false;
	    }
		
		 /**
	     * Reads and retrieves all ApplianceItem records from the database.
	     * 
	     * @return an ArrayList of ApplianceItem objects
	     */
		public ArrayList<ApplianceItem> findAllApplianceItems() {
		    String query = "SELECT * FROM applianceItem";
		    ArrayList<ApplianceItem> items = new ArrayList<>();
		    try (Connection conn = this.connect();
		         PreparedStatement preStatement = conn.prepareStatement(query);
		         ResultSet result = preStatement.executeQuery()) {
		        HomeApplianceDAO homeApplianceDAO = new HomeApplianceDAO();
		        while (result.next()) {
		            HomeAppliance homeAppliance = homeApplianceDAO.findProduct(result.getInt("homeApplianceId"));
		            if (homeAppliance != null) {
		                ApplianceItem item = new ApplianceItem(
		                    homeAppliance,
		                    result.getInt("warrantyYears"),
		                    result.getString("brand"),
		                    result.getString("model")
		                );
		                item.setId(result.getInt("id"));
		                items.add(item);
		            } else {
		                System.out.println("Warning: No HomeAppliance found for ID " + result.getInt("homeApplianceId"));
		            }
		        }
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    }
		    return items;
		}

	    
	    /**
	     * Reads and retrieves an ApplianceItem from the database by its unique ID.
	     * 
	     * @param id - the ID of the ApplianceItem to retrieve
	     * @return the ApplianceItem object if found, otherwise null
	     */
	    public ApplianceItem findApplianceItem(int id) {
	        String query = "SELECT * FROM applianceItem WHERE id = ?";
	        try (Connection conn = this.connect();
	             PreparedStatement preStatement = conn.prepareStatement(query)) {
	            preStatement.setInt(1, id);
	            ResultSet result = preStatement.executeQuery();
	            if (result.next()) {
	                HomeApplianceDAO homeApplianceDAO = new HomeApplianceDAO();
	                HomeAppliance homeAppliance = homeApplianceDAO.findProduct(result.getInt("homeApplianceId"));
	                ApplianceItem item = new ApplianceItem(
	                        homeAppliance,
	                        result.getInt("warrantyYears"),
	                        result.getString("brand"),
	                        result.getString("model")
	                );
	                item.setId(result.getInt("id"));
	                return item;
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return null;
	    }
	    
	    /**
	     * Updates an existing ApplianceItem in the database which is retrieved by its unique ID.
	     * 
	     * @param item - the ApplianceItem object containing updated details
	     * @return true if the update is successful, false otherwise
	     */
	    public boolean updateApplianceItem(ApplianceItem item) {
	        String query = "UPDATE applianceItem SET homeApplianceId = ?, warrantyYears = ?, brand = ?, model = ? WHERE id = ?";
	        try (Connection conn = this.connect();
	             PreparedStatement preStatement = conn.prepareStatement(query)) {
	            preStatement.setInt(1, item.getHomeAppliance().getId());
	            preStatement.setInt(2, item.getWarrantyYears());
	            preStatement.setString(3, item.getBrand());
	            preStatement.setString(4, item.getModel());
	            preStatement.setInt(5, item.getId());
	            int rows = preStatement.executeUpdate();
	            return rows > 0;
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return false;
	    }
	    
	    /**
	     * Deletes an ApplianceItem from the database by its unique ID.
	     * 
	     * @param id - the ID of the ApplianceItem to delete
	     * @return true if the deletion is successful, false otherwise
	     */
	    public boolean deleteApplianceItem(int id) {
	        String query = "DELETE FROM applianceItem WHERE id = ?";
	        try (Connection conn = this.connect();
	             PreparedStatement preStatement = conn.prepareStatement(query)) {
	            preStatement.setInt(1, id);
	            int rows = preStatement.executeUpdate();
	            return rows > 0;
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return false;
	    }
	    
	    /**
	     * Retrieves ApplianceItem records sorted by warranty years in either ascending 
	     * or descending order.
	     * 
	     * @param ascending true to sort in ascending order, false for descending order
	     * @return an ArrayList of sorted ApplianceItem objects
	     */
	    public ArrayList<ApplianceItem> itemsSortedByWarrantyYears(boolean ascending) {
	        String order = ascending ? "ASC" : "DESC";
	        String query = "SELECT * FROM applianceItem ORDER BY warrantyYears " + order;
	        ArrayList<ApplianceItem> items = new ArrayList<>();
	        HomeApplianceDAO homeApplianceDAO = new HomeApplianceDAO(); // Assuming you have access to HomeApplianceDAO

	        try (Connection conn = this.connect();
	             PreparedStatement preStatement = conn.prepareStatement(query);
	             ResultSet result = preStatement.executeQuery()) {
	            while (result.next()) {
	                int homeApplianceId = result.getInt("homeApplianceId");
	                HomeAppliance homeAppliance = homeApplianceDAO.findProduct(homeApplianceId);
	                if (homeAppliance == null) {
	                    homeAppliance = new HomeAppliance("N/A", "No Description", "No Category", 0);  // Fallback for missing data
	                }
	                
	                ApplianceItem item = new ApplianceItem(
	                    homeAppliance,
	                    result.getInt("warrantyYears"),
	                    result.getString("brand"),
	                    result.getString("model")
	                );
	                item.setId(result.getInt("id"));
	                items.add(item);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return items;
	    }

}
