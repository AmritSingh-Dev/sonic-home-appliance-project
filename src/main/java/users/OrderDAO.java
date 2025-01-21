package users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDAO is a Data Access Object (DAO) class that provides methods 
 * for managing Order records in an SQLite database. 
 * This class includes operations for adding orders, initialising the database table, 
 * and ensuring data integrity through foreign key relationships with the `users` table.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class OrderDAO {

	/**
     * Default constructor for OrderDAO.
     * Initialises a new instance of the class without any additional setup.
     */
    public OrderDAO() {
        // No specific initialisation required
    }
	
	  /**
     * Establishes a connection to the SQLite database and creates the `orders` table 
     * if it does not already exist.
     * 
     * @return a Connection object to the SQLite database
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:appliance.db");
            if (conn != null) {
                // Creating orders table if it does not exist
                String ordersTable = "CREATE TABLE IF NOT EXISTS orders (" + 
                                     "orderId INTEGER PRIMARY KEY AUTOINCREMENT, " +  
                                     "userId INTEGER, " + 
                                     "totalPrice INTEGER, " +
                                     "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + 
                                     "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE)";
                try (PreparedStatement preStatement = conn.prepareStatement(ordersTable)) {
                    preStatement.execute();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }
    
    /**
     * Adds a new order to the database. The order must have a valid `user` object 
     * with an associated `userId`. If the creation is successful, the `orderId` 
     * of the provided Order object is updated with the generated ID from the database.
     * 
     * @param order the Order object containing order details
     * @return true if the order was successfully added, false otherwise
     * @throws IllegalArgumentException if the `user` in the order is null
     */
    public boolean addOrder(Order order) {
        String query = "INSERT INTO orders (userId, totalPrice) VALUES (?, ?)"; // Correct the SQL statement.
        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Check if the user object is null to avoid NullPointerException
            if (order.getUser() == null) {
                throw new IllegalArgumentException("Order must have a valid User.");
            }

            // Get userId from the User object within the Order
            preStatement.setInt(1, order.getUser().getUserId());
            preStatement.setInt(2, order.getTotalPrice());

            int rows = preStatement.executeUpdate();

            // Get the generated orderId
            if (rows > 0) {
                try (ResultSet generatedKeys = preStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);
                        order.setOrderId(orderId);  // Set the generated orderId in the order object
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a list of orders associated with a specific user ID.
     * This method queries the `orders` table in the database to fetch all orders
     * placed by the specified user. Each order includes details such as the 
     * order ID, total price, and the timestamp when the order was created.
     * 
     * @param userId the ID of the user whose orders are to be retrieved
     * @return a list of `Order` objects associated with the user; 
     *         an empty list if no orders are found or an error occurs
     */
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT orderId, userId, totalPrice, createdAt FROM orders WHERE userId = ?";
        UsersDAO usersDAO = new UsersDAO(); // Use the existing UsersDAO to fetch user details

        try (Connection conn = this.connect();
             PreparedStatement preStatement = conn.prepareStatement(query)) {

            preStatement.setInt(1, userId);

            try (ResultSet resultSet = preStatement.executeQuery()) {
                Users user = usersDAO.findUser(userId); // Fetch the full Users object
                if (user == null) {
                    throw new SQLException("No user found with userId: " + userId);
                }

                while (resultSet.next()) {
                    int orderId = resultSet.getInt("orderId");
                    int totalPrice = resultSet.getInt("totalPrice");
                    Timestamp createdAt = resultSet.getTimestamp("createdAt"); // Retrieve createdAt

                    // Create the Order object with the full Users object and createdAt
                    Order order = new Order(orderId, user, totalPrice, createdAt);
                    orders.add(order);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return orders;
    }

}
