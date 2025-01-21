package login.web;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import users.Users;
import users.UsersDAO;

/**
 * Handles the login form submission process.
 * This class processes the credentials submitted through the login form, authenticates the user,
 * and establishes a session if the credentials are valid.
 * 
 * Login Details (Passwords saved as hashed strings):
 * ADMIN Username: admin | Password: 123456
 * CUSTOMER Username: northernsteakhouse | Password: 123456
 * All user details can be viewed via the menu system. Passwords for all users is 123456.
 * 
 * Features:
 * - Validates user credentials against the database.
 * - Creates a session and sets a session cookie upon successful login.
 * - Redirects users to the appropriate dashboard based on their role (Admin or Customer).
 * - Redirects back to the login page with an error if authentication fails.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ProcessLoginHandler implements HttpHandler {
	
	/**
     * Default constructor for ProcessLoginHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ProcessLoginHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles the HTTP POST request for processing login form submissions.
     * Reads and parses the form data, authenticates the user, and redirects them to their respective dashboard.
     * 
     * @param exchange the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs during input/output operations.
     */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
	    if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
	        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
	        BufferedReader br = new BufferedReader(isr);
	        String formData = br.readLine();
	        Map<String, String> parsedData = parseFormData(formData);

	        String username = parsedData.get("username");
	        String password = parsedData.get("password");

	        try {
	            UsersDAO dao = new UsersDAO();
	            Users user = dao.findUserByUsername(username);

	            // Authenticate the user
	            if (dao.authenticate(username, password)) {
	                String sessionId = LoginSessionManager.createSession(user);  

	                // Set the session ID cookie in the response
	                exchange.getResponseHeaders().set("Set-Cookie", "sessionId=" + sessionId + "; HttpOnly; Path=/");

	                // Redirect users to the correct dashboard based on their role
	                if ("Admin".equals(user.getRole())) {
	                    exchange.getResponseHeaders().set("Location", "/admindashboard");
	                } else if ("Customer".equals(user.getRole())) {
	                    exchange.getResponseHeaders().set("Location", "/customerdashboard");
	                } else {
	                    // Handle other roles or default case
	                    exchange.getResponseHeaders().set("Location", "/login?error=role");
	                }

	                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1); // Redirect to appropriate dashboard
	            } else {
	                exchange.getResponseHeaders().set("Location", "/login?error=true");
	                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1); // Redirect back to login
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
	        } finally {
	            exchange.close();
	        }
	    }
	}
	
    /**
     * Parses form data from a URL-encoded string into a map of key-value pairs.
     * 
     * @param formData the URL-encoded form data string.
     * @return a map containing the parsed form fields and their corresponding values.
     * @throws UnsupportedEncodingException if the character encoding is not supported.
     */
    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            parameters.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return parameters;
    }
}