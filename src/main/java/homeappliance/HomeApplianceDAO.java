package homeappliance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for managing HomeAppliance records in an SQLite database.
 * Provides methods to perform various CRUD operations, search and sort functionality which is utilised by both console and web interface.
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class HomeApplianceDAO {
	
	/**
     * Default constructor for HomeApplianceDAO.
     * Initialises a new instance of the class without any additional setup.
     */
    public HomeApplianceDAO() {
        // No specific initialisation required
    }
	
    /**
     * Establishes a connection to the database and creates the appliance table if it does not already exist.
     *
     * @return a Connection object to the SQLite database
     */
    protected Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:appliance.db");
            String table = "CREATE TABLE IF NOT EXISTS appliance (" +
                           "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                           "sku TEXT NOT NULL, " +
                           "description TEXT, " +
                           "category TEXT, " +
                           "price INTEGER)";
            try (PreparedStatement preStatement = conn.prepareStatement(table)) {
                preStatement.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    /**
     * Retrieves all appliance products from the database and returns them as a list of HomeAppliance objects.
     * This can be viewed either on the console or via the products page (table) on the web interface.
     *
     * @return an ArrayList of HomeAppliance objects
     */
    public ArrayList<HomeAppliance> findAllProducts() {
        String query = "SELECT * FROM appliance;";
        ArrayList<HomeAppliance> appliances = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query);
             ResultSet result = preStatement.executeQuery()) {
            while (result.next()) {
                HomeAppliance appliance = new HomeAppliance(
                        result.getString("sku"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getInt("price")
                );
                appliance.setId(result.getInt("id"));
                appliances.add(appliance);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appliances;
    }

    /**
     * Finds a single product by its specific (and unique) ID and returns it as a HomeAppliance object.
     *
     * @param id the ID of the product to find
     * @return a HomeAppliance object if found, otherwise null
     */
    public HomeAppliance findProduct(int id) {
        String query = "SELECT * FROM appliance WHERE ID = ?";
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query)) {
            preStatement.setInt(1, id);
            ResultSet result = preStatement.executeQuery();
            if (result.next()) {
                HomeAppliance appliance = new HomeAppliance(
                        result.getString("sku"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getInt("price")
                );
                appliance.setId(result.getInt("id"));
                return appliance;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes a product from the database by its specific (and unique) ID.
     *
     * @param id the ID of the product to delete
     * @return true if the product was successfully deleted, false otherwise
     */
    public boolean deleteProduct(int id) {
        String query = "DELETE FROM appliance WHERE ID = ?";
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
     * Updates the details of an existing product in the database which updates all the attributes of the product (except the ID).
     *
     * @param appliance the HomeAppliance object containing the updated details
     * @return true if the update was successful, false otherwise
     */
    public boolean updateProduct(HomeAppliance appliance) {
        String query = "UPDATE appliance SET sku = ?, description = ?, category = ?, price = ? WHERE id = ?";
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query)) {
            preStatement.setString(1, appliance.getSku());
            preStatement.setString(2, appliance.getDescription());
            preStatement.setString(3, appliance.getCategory());
            preStatement.setInt(4, appliance.getPrice());
            preStatement.setInt(5, appliance.getId());
            int rows = preStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a new home appliance entry to the database record (in the appliance table).
     *
     * @param appliance the HomeAppliance object to add
     * @return true if the product was successfully added, false otherwise
     */
    public boolean addProduct(HomeAppliance appliance) {
        String query = "INSERT INTO appliance(sku, description, category, price) VALUES(?, ?, ?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, appliance.getSku());
            preStatement.setString(2, appliance.getDescription());
            preStatement.setString(3, appliance.getCategory());
            preStatement.setInt(4, appliance.getPrice());
            int rows = preStatement.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = preStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appliance.setId(generatedKeys.getInt(1));
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
     * Searches for products by a specified attribute and value using a LIKE SQL condition.
     *
     * @param attribute - the attribute to search by
     * @param value - the value to search for
     * @return an ArrayList of HomeAppliance objects that match the search criteria
     */
    public ArrayList<HomeAppliance> searchProductsByAttribute(String attribute, String value) {
        String query = "SELECT * FROM appliance WHERE " + attribute + " LIKE ?";
        ArrayList<HomeAppliance> appliances = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query)) {
            preStatement.setString(1, "%" + value + "%");
            ResultSet result = preStatement.executeQuery();
            while (result.next()) {
                HomeAppliance appliance = new HomeAppliance(
                        result.getString("sku"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getInt("price")
                );
                appliance.setId(result.getInt("id"));
                appliances.add(appliance);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appliances;
    }

    /**
     * Retrieves products sorted by price in either ascending or descending order.
     * This method can be utilised via the console or on the products page of the web interface (with a drop down menu).
     *
     * @param ascending true to sort by ascending price, false for descending
     * @return an ArrayList of HomeAppliance objects sorted by price
     */
    public ArrayList<HomeAppliance> productsSortedByPrice(boolean ascending) {
        String order = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM appliance ORDER BY price " + order;
        ArrayList<HomeAppliance> appliances = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query);
             ResultSet result = preStatement.executeQuery()) {
            while (result.next()) {
                HomeAppliance appliance = new HomeAppliance(
                        result.getString("sku"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getInt("price")
                );
                appliance.setId(result.getInt("id"));
                appliances.add(appliance);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appliances;
    }

    /**
     * Filters products by a specified attribute and exact value that can be accessed through the products page via a drop down menu.
     *
     * @param attribute - the attribute to filter by
     * @param value - the exact value to match
     * @return an ArrayList of HomeAppliance objects that meet the filter criteria
     */
    public ArrayList<HomeAppliance> filterProductsByAttribute(String attribute, String value) {
        String query = "SELECT * FROM appliance WHERE " + attribute + " = ?";
        ArrayList<HomeAppliance> appliances = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query)) {
            preStatement.setString(1, value);
            ResultSet result = preStatement.executeQuery();
            while (result.next()) {
                HomeAppliance appliance = new HomeAppliance(
                        result.getString("sku"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getInt("price")
                );
                appliance.setId(result.getInt("id"));
                appliances.add(appliance);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appliances;
    }

    /**
     * Filters products by category.
     *
     * @param category - the category to filter by
     * @return an ArrayList of HomeAppliance objects that belong to the specified category
     */
    public ArrayList<HomeAppliance> filterProductsByCategory(String category) {
        String query = "SELECT * FROM appliance WHERE category = ?";
        ArrayList<HomeAppliance> appliances = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query)) {
            preStatement.setString(1, category);
            ResultSet result = preStatement.executeQuery();
            while (result.next()) {
                HomeAppliance appliance = new HomeAppliance(
                        result.getString("sku"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getInt("price")
                );
                appliance.setId(result.getInt("id"));
                appliances.add(appliance);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appliances;
    }

    /**
     * Finds all distinct categories that are currently stored in the database.
     *
     * @return a List of category names
     */
    public List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM appliance ORDER BY category";
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query);
             ResultSet result = preStatement.executeQuery()) {
            while (result.next()) {
                categories.add(result.getString("category"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    /**
     * Searches for products using a keyword that may appear in the sku, description, or category.
     * This method can be utilised via the search bar which is implemented within web interface.
     *
     * @param keyword - the keyword to search for
     * @return an ArrayList of HomeAppliance objects where the keyword appears in the sku, description, or category
     */
    public ArrayList<HomeAppliance> searchProductsByKeyword(String keyword) {
        String query = "SELECT * FROM appliance WHERE sku LIKE ? OR description LIKE ? OR category LIKE ?";
        ArrayList<HomeAppliance> appliances = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query)) {
            String likeKeyword = "%" + keyword + "%";
            preStatement.setString(1, likeKeyword);
            preStatement.setString(2, likeKeyword);
            preStatement.setString(3, likeKeyword);
            ResultSet result = preStatement.executeQuery();
            while (result.next()) {
                HomeAppliance appliance = new HomeAppliance(
                        result.getString("sku"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getInt("price")
                );
                appliance.setId(result.getInt("id"));
                appliances.add(appliance);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appliances;
    }
}
