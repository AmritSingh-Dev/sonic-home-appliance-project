package users.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import users.UsersDAO;
import login.web.LoginSessionManager;

/**
 * Handles the process of updating a user's role.
 * This handler ensures that only admins can update user roles, as verified through the session data.
 * It accepts form data containing the user ID and the new role, updates the database, and responds with a success or failure message.
 * 
 * Features:
 * - Verifies administrative access before processing the request.
 * - Parses form data for user ID and role from the HTTP request body.
 * - Updates the user's role in the database using the `UsersDAO`.
 * - Sends an HTML response indicating the success or failure of the operation.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ProcessUpdateUserRoleHandler implements HttpHandler {
	
	/**
     * Default constructor for ProcessUpdateUserRoleHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ProcessUpdateUserRoleHandler() {
        // No specific initialisation required
    }
	
	
    /**
     * Handles the HTTP request to update a user's role.
     * Verifies if the logged-in user is an admin before proceeding.
     * Parses the form data for user ID and role, updates the database, and generates an HTML response.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while handling the request or response.
     */
    public void handle(HttpExchange he) throws IOException {
    	
    	String sessionId = getSessionIdFromCookie(he);
        LoginSessionManager.UserSession session = LoginSessionManager.getSession(sessionId);

        // Check if user is logged in and is an admin
        if (session == null || !"Admin".equals(session.getRole())) {
            he.getResponseHeaders().set("Location", "/login");
            he.sendResponseHeaders(302, 0);
            he.getResponseBody().close();
            return;
        }
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(he.getRequestBody()));
        String line;
        StringBuilder input = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            input.append(line);
        }

        // Parse the form data into a Map
        try {
            Map<String, String> formData = parseFormData(input.toString());
            
            // Extract individual fields
            int userId = Integer.parseInt(formData.get("id"));
            String role = formData.get("role");
            
            
            // Update the database record
            UsersDAO dao = new UsersDAO();
            boolean success = dao.updateUserRole(userId, role);
            
            String response = success ? "Role updated successfully!" : "Failed to update Role.";
            
            he.sendResponseHeaders(200, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));
            out.write("<html><body><h1>" + response + "</h1><a href=\"/users\">View Users</a></body></html>");
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Retrieves the session ID from the HTTP request cookies.
     * 
     * @param exchange the HttpExchange object representing the HTTP request and response.
     * @return the session ID as a string, or null if no session ID is found.
     */
    private String getSessionIdFromCookie(HttpExchange exchange) {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                cookie = cookie.trim();
                String[] cookiePair = cookie.split("=", 2);
                if ("sessionId".equals(cookiePair[0]) && cookiePair.length > 1) {
                    return cookiePair[1];
                }
            }
        }
        return null;
    }

    /**
     * Parses form data from a URL-encoded string into a Map.
     * 
     * @param formData the URL-encoded form data string.
     * @return a Map containing key-value pairs of the form data.
     * @throws UnsupportedEncodingException if UTF-8 decoding is not supported.
     */
    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], "UTF-8"); // Decode the value
                result.put(key, value);
            }
        }
        return result;
    }
}