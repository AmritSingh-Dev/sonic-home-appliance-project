package users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

/**
 * The UsersDAO class provides data access object methods for managing user records in the SQLite database.
 * It supports operations such as adding, retrieving, updating, and deleting users, as well as 
 * authentication and role management. 
 * 
 * Users are stored in the `users` table, which includes columns for user ID, username, 
 * hashed password, role, and an optional associated customer ID.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class UsersDAO {
	
	/**
     * Default constructor for UsersDAO.
     * Initialises a new instance of the class without any additional setup.
     */
    public UsersDAO() {
        // No specific initialisation required
    }
	
	 /**
     * Establishes a connection to the SQLite database and creates the `users` table if it does not already exist.
     * 
     * @return a Connection object to the SQLite database
     */
	private Connection connect() {
	    Connection conn = null;
	    try {
	        conn = DriverManager.getConnection("jdbc:sqlite:appliance.db");
	        if (conn != null) {
	            String table = "CREATE TABLE IF NOT EXISTS users (" + 
	                           "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +  
	                           "username TEXT UNIQUE, " + 
	                           "password TEXT, " + 
	                           "role TEXT, " +
	                           "customerId INTEGER, " +
	                           "FOREIGN KEY (customerId) REFERENCES customer(customerId) ON DELETE CASCADE)";
	            try (PreparedStatement preStatement = conn.prepareStatement(table)) {
	                preStatement.execute();
	            }
	        }
	    } catch(SQLException ex) {
	        ex.printStackTrace();
	    }
	    return conn;
	}
	
	/**
     * Adds a new user to the database with a hashed password.
     * 
     * @param user the Users object containing user details
     * @return true if the user was added successfully, false otherwise
     */
	public boolean addUser(Users user) {
		String query = "INSERT INTO users(username, password, role, customerId) VALUES(?, ?, ?, ?)";
	    try (Connection conn = this.connect();
	         PreparedStatement preStatement = conn.prepareStatement(query)) {

	        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

	        preStatement.setString(1, user.getUsername());
	        preStatement.setString(2, hashedPassword);
	        preStatement.setString(3, user.getRole());
	        // Handle nullable customerId
	        if (user.getCustomerId() == null) {
	            preStatement.setNull(4, java.sql.Types.INTEGER);
	        } else {
	            preStatement.setInt(4, user.getCustomerId());
	        }
	        int rows = preStatement.executeUpdate();
	        return rows > 0;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}
	
	/**
     * Retrieves all user records from the database.
     * 
     * @return an ArrayList of Users objects, or an empty list if no users are found
     */
	public ArrayList<Users> findAllUsers() {
		String query = "SELECT * FROM users;";
		ArrayList<Users> users = new ArrayList<>();
		try (Connection conn = this.connect();
				PreparedStatement preStatement = conn.prepareStatement(query);
				ResultSet result = preStatement.executeQuery()){
					while (result.next()) {
						Users user = new Users(
								result.getString("username"),
								result.getString("password"),
								result.getString("role"),
								result.getInt("customerId")
						);
						user.setUserId(result.getInt("userId"));
						users.add(user);
					}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return users;
	}
	
	/**
     * Retrieves a user record by their unique user ID.
     * 
     * @param userId the ID of the user to retrieve
     * @return the Users object if found, or null if not found
     */
	public Users findUser(int userId) {
		String query = "SELECT * FROM users WHERE userId = ?";
		try (Connection conn = this.connect();
				PreparedStatement preStatement = conn.prepareStatement(query)){
			preStatement.setInt(1, userId);
			ResultSet result = preStatement.executeQuery();
			if (result.next()) {
				Users user = new Users(
						result.getString("username"),
						result.getString("password"),
						result.getString("role"),
						result.getInt("customerId")
				);
				user.setUserId(result.getInt("userId"));
				return user;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
     * Deletes a user record from the database by their user ID.
     * 
     * @param userId the ID of the user to delete
     * @return true if the deletion is successful, false otherwise
     */
	public boolean deleteUser(int userId) {
		String query = "DELETE FROM users WHERE userId = ?";
	    try (Connection conn = this.connect();
	         PreparedStatement preStatement = conn.prepareStatement(query)) {
	        preStatement.setInt(1, userId);
	        int rows = preStatement.executeUpdate();
	        return rows > 0;
	    } catch (SQLException ex) {
	        System.out.println("Error deleting user: " + ex.getMessage());
	        return false;
	    }
	}
	
	/**
     * Updates an existing user record in the database with new details.
     * 
     * @param user the Users object containing updated user details
     * @return true if the update is successful, false otherwise
     */
	public boolean updateUser(Users user) {
		String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		
		String query = "UPDATE users SET username = ?, password = ?, role = ?, customerId = ? WHERE userId = ?";
		try (Connection conn = this.connect();
				PreparedStatement preStatement = conn.prepareStatement(query)){
			preStatement.setString(1, user.getUsername());
			preStatement.setString(2, hashedPassword);
			preStatement.setString(3, user.getRole());
			preStatement.setObject(4, user.getCustomerId()); //setObject to handle null
			preStatement.setInt(5, user.getUserId());
			int rows = preStatement.executeUpdate();
			return rows > 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
     * Authenticates a user by their username and password.
     * 
     * @param username the username of the user
     * @param password the plaintext password to check
     * @return true if authentication is successful, false otherwise
     */
	public boolean authenticate(String username, String password) {
	    String query = "SELECT password FROM users WHERE username = ?";
	    try (Connection conn = this.connect();
	         PreparedStatement preStatement = conn.prepareStatement(query)) {
	        preStatement.setString(1, username);
	        ResultSet result = preStatement.executeQuery();

	        if (result.next()) {
	            String storedPassword = result.getString("password");
	            return BCrypt.checkpw(password, storedPassword);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}
	
	/**
     * Retrieves the role of a user by their username.
     * 
     * @param username the username of the user
     * @return the role of the user if found, or null if the user is not found
     */
	public String getUserRole(String username) {
	    String query = "SELECT role FROM users WHERE username = ?";
	    try (Connection conn = this.connect();
	         PreparedStatement preStatement = conn.prepareStatement(query)) {
	        preStatement.setString(1, username);
	        ResultSet result = preStatement.executeQuery();
	        if (result.next()) {
	            return result.getString("role");
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return null; // Return null if user is not found or in case of an error
	}
	
	/**
     * Updates the role of a user by their user ID.
     * 
     * @param userId the ID of the user to update
     * @param newRole the new role to set for the user
     * @return true if the role is updated successfully, false otherwise
     */
	public boolean updateUserRole(int userId, String newRole) {
	    String query = "UPDATE users SET role = ? WHERE userId = ?";
	    try (Connection conn = this.connect();
	         PreparedStatement preStatement = conn.prepareStatement(query)){
	        preStatement.setString(1, newRole);
	        preStatement.setInt(2, userId);
	        int rows = preStatement.executeUpdate();
	        return rows > 0;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}
	
	 /**
     * Retrieves a user record by their username.
     * 
     * @param username the username of the user
     * @return the Users object if found, or null if not found
     */
	public Users findUserByUsername(String username) {
	    String query = "SELECT * FROM users WHERE username = ?";
	    try (Connection conn = this.connect();
	         PreparedStatement preStatement = conn.prepareStatement(query)) {
	        preStatement.setString(1, username);
	        ResultSet result = preStatement.executeQuery();
	        if (result.next()) {
	            Users user = new Users(
	                    result.getString("username"),
	                    result.getString("password"),
	                    result.getString("role"),
	                    result.getInt("customerId")
	            );
	            user.setUserId(result.getInt("userId"));
	            return user;
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 * Checks if a given username already exists in the database.
	 * 
	 *
	 * @param username the username to check in the database.
	 * @return true if the username exists, false otherwise.
	 */
	public boolean isUsernameExist(String username) {
	    String query = "SELECT COUNT(username) FROM users WHERE username = ?";
	    try (Connection conn = this.connect();
	         PreparedStatement preStatement = conn.prepareStatement(query)) {
	        preStatement.setString(1, username);
	        ResultSet rs = preStatement.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; 
	}

	
}
