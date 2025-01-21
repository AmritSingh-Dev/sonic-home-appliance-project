package users.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import login.web.LoginSessionManager;
import users.Users;
import users.UsersDAO;

/**
 * Handles the HTTP request to delete a user from the system.
 * This handler is accessible only to admin users and performs user deletion operations based on the provided user ID.
 * 
 * Features:
 * - Validates if the logged-in user has admin privileges before proceeding.
 * - Deletes the user from the database and displays the deleted user's details.
 * - Displays appropriate error messages if the user ID is invalid or the operation fails.
 * 
 * HTML Output:
 * - Displays a confirmation message and details of the deleted user on success.
 * - Displays an error message on failure or if the user is not found.
 * 
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class DeleteUserHandler implements HttpHandler {
	
	/**
     * Default constructor for DeleteUserHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public DeleteUserHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles the HTTP request to delete a user.
     * Validates the session for admin privileges and processes the user deletion based on the provided user ID.
     * 
     * @param he the HttpExchange object containing the HTTP request and response.
     * @throws IOException if an I/O error occurs while handling the request.
     */
    @Override
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
        
        he.sendResponseHeaders(200, 0);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));

        try {
            Map<String, String> params = parseQueryString(he.getRequestURI().getQuery());
            int id = Integer.parseInt(params.get("id"));

            UsersDAO dao = new UsersDAO();

            Users deletedUser = dao.findUser(id);
            boolean success = dao.deleteUser(id);

            if (success && deletedUser != null) {
                // Write the response HTML
                out.write(
                    "<html>" +
                    "<head> <title>Sonic Home Appliances</title>" +
                    "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\" " +
                    "integrity=\"sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\" crossorigin=\"anonymous\">" +
                    "</head>" +
                    "<body>" +
                    "<h1>User Deleted Successfully</h1>" +
                    "<table class=\"table\">" +
                    "<thead>" +
                    "  <tr>" +
                    "    <th>ID</th>" +
                    "    <th>Username</th>" +
                    "    <th>Password</th>" +
                    "    <th>Role</th>" +
                    "  </tr>" +
                    "</thead>" +
                    "<tbody>" +
                    "  <tr>" +
                    "    <td>" + deletedUser.getUserId() + "</td>" +
                    "    <td>" + deletedUser.getUsername() + "</td>" +
                    "    <td>" + deletedUser.getPassword() + "</td>" +
                    "    <td>" + deletedUser.getRole() + "</td>" +
                    "  </tr>" +
                    "</tbody>" +
                    "</table>" +
                    "<a href=\"/\">Back to Menu</a>" +
                    "</body>" +
                    "</html>");
                out.close();
            } else {
                // If deletion failed or product was not found
                out.write(
                    "<html>" +
                    "<head><title>Error</title></head>" +
                    "<body>" +
                    "<h1>Failed to delete user. User not found or an error occurred.</h1>" +
                    "<a href=\"/\">Back to Homepage</a>" +
                    "</body>" +
                    "</html>");
                out.close();
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // Send error response
            out.write(
                "<html>" +
                "<head><title>Error</title></head>" +
                "<body>" +
                "<h1>Error processing request</h1>" +
                "<p>" + ex.getMessage() + "</p>" +
                "<a href=\"/\">Back to Menu</a>" +
                "</body>" +
                "</html>"
            );
        
            out.close();
        }
    }
    
    /**
     * Retrieves the session ID from the request's cookies.
     * 
     * @param exchange the HttpExchange object containing the HTTP request.
     * @return the session ID if found, otherwise null.
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
     * Parses a query string into a Map of key-value pairs.
     * 
     * @param query the query string from the URL.
     * @return a map containing the query parameters and their values.
     */
    private Map<String, String> parseQueryString(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1) {
                result.put(keyValue[0], "");
            }
        }
        return result;
    }
}