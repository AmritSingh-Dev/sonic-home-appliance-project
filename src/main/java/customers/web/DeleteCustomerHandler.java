package customers.web;

import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpHandler;
import customers.Customer;
import customers.CustomerDAO;
import login.web.LoginSessionManager;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * This class generates the the HTML code and logic to delete a customer from the database.
 * 
 * Features:
 * - Validates if the user is logged in and has admin privileges.
 * - Deletes the specified customer by their ID.
 * - Provides a success or failure response with details of the deleted customer.
 * - Redirects unauthorised users to the login page.
 * 
 * This handler is part of the server-side implementation for managing customer
 * records in the appliance store application.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class DeleteCustomerHandler implements HttpHandler {
	
	 /**
     * Default constructor for DeleteCustomerHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public DeleteCustomerHandler() {
        // No specific initialisation required
    }
	
	 /**
     * Handles the HTTP request to delete a customer.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs during the request handling.
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
            // Extract query parameters from the URL
            Map<String, String> params = parseQueryString(he.getRequestURI().getQuery());
            int id = Integer.parseInt(params.get("id"));

            CustomerDAO dao = new CustomerDAO();

            Customer deletedCustomer = dao.findCustomer(id);
            boolean success = dao.deleteCustomer(id);

            if (success && deletedCustomer != null) {
                out.write(
                    "<html>" +
                    "<head> <title>Sonic Home Appliances</title>" +
                    "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\" " +
                    "integrity=\"sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\" crossorigin=\"anonymous\">" +
                    "</head>" +
                    "<body>" +
                    "<h1>Customer Deleted Successfully</h1>" +
                    "<table class=\"table\">" +
                    "<thead>" +
                    "  <tr>" +
                    "    <th>ID</th>" +
                    "    <th>Name</th>" +
                    "    <th>Address</th>" +
                    "    <th>Telephone</th>" +
                    "    <th>Email</th>" +
                    "  </tr>" +
                    "</thead>" +
                    "<tbody>" +
                    "  <tr>" +
                    "    <td>" + deletedCustomer.getCustomerID() + "</td>" +
                    "    <td>" + deletedCustomer.getBusinessName() + "</td>" +
                    "    <td>" + deletedCustomer.getAddress() + "</td>" +
                    "    <td>" + deletedCustomer.getTelephoneNumber() + "</td>" +
                    "    <td>" + deletedCustomer.getEmailAddress() + "</td>" +
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
                    "<h1>Failed to delete customer. Customer not found or an error occurred.</h1>" +
                    "<a href=\"/\">Back to Menu</a>" +
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
     * Extracts the session ID from the request cookies.
     * 
     * @param exchange the HttpExchange object containing the request.
     * @return the session ID as a String, or null if not found.
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
     * Parses a query string into a Map.
     * 
     * @param query the query string from the URL.
     * @return a map containing the query parameters.
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