package login.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import homeappliance.Basket;
import users.Users;

/**
 * Manages user sessions for the application.
 * The `LoginSessionManager` handles session creation, retrieval, and termination,
 * ensuring that user-specific data, such as roles and shopping baskets, is securely maintained.
 * 
 * Features:
 * - Creates unique sessions for authenticated users.
 * - Associates sessions with user data, including roles and baskets.
 * - Provides methods to retrieve or terminate sessions.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class LoginSessionManager {
	
	/**
     * Default constructor for LoginSessionManager.
     * Initialises a new instance of the class without any additional setup.
     */
    public LoginSessionManager() {
        // No specific initialisation required
    }
	
	// A map to store active sessions, mapping session IDs to user sessions
    private final static Map<String, UserSession> sessions = new HashMap<>();
    
    /**
     * Creates a new session for the specified user.
     * Generates a unique session ID and associates it with the user's session data.
     * 
     * @param user the `Users` object representing the authenticated user.
     * @return the unique session ID for the newly created session.
     */
    public static String createSession(Users user) {
        String sessionId = UUID.randomUUID().toString();
        UserSession newSession = new UserSession(user);
        sessions.put(sessionId, newSession);
        return sessionId;
    }

    /**
     * Retrieves the user session associated with the given session ID.
     * 
     * @param sessionId the unique session ID.
     * @return the `UserSession` object associated with the session ID, or `null` if no session exists.
     */
    public static UserSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Ends the session associated with the given session ID.
     * Removes the session from the active sessions map.
     * 
     * @param sessionId the unique session ID of the session to be terminated.
     */
    public static void endSession(String sessionId) {
        sessions.remove(sessionId);
    }

    /**
     * Represents a user session, storing user-specific data.
     * Each `UserSession` contains a reference to the `Users` object for the logged-in user
     * and their shopping basket.
     */
    public static class UserSession {
        private Users userId; 
        private Basket basket;
        
        /**
         * Constructs a new `UserSession` for the specified user.
         * Initialises the user's basket.
         * 
         * @param userId the `Users` object representing the user.
         */
        public UserSession(Users userId) {
            this.userId = userId;
            this.basket = new Basket();
        }
        
        /**
         * Getter that retrieves the `Users` object associated with this session.
         * 
         * @return the `Users` object.
         */
        public Users getUser() {
            return userId;  // Get the full Users object
        }
        
        /**
         * Getter that retrieves the user ID of the logged-in user.
         * 
         * @return the user ID as an integer.
         */
        public int getUserId() {
            return userId.getUserId();  // Access userId from the Users class
        }
        
        /**
         * Getter that retrieves the username of the logged-in user.
         * 
         * @return the username as a string.
         */
        public String getUsername() {
            return userId.getUsername();  // Access username from the Users class
        }
        
        /**
         * Getter that retrieves the role of the logged-in user.
         * 
         * @return the user's role (e.g., "Admin", "Customer") as a string.
         */
        public String getRole() {
            return userId.getRole();  // Access role from the Users class
        }

        /**
         * Retrieves the shopping basket associated with this session.
         * 
         * @return the `Basket` object.
         */
        public Basket getBasket() {
            return basket;
        }
    }
}
