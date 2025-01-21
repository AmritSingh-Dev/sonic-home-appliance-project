package customers.web;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.sun.net.httpserver.HttpHandler;
import customers.Address;
import customers.Customer;
import customers.CustomerDAO;
import login.web.LoginSessionManager;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles HTTP requests to delete a customer record.
 * It validates user session for admin privileges before allowing deletion operations.
 * It provides feedback to the user about the outcome of the delete operation.
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ProcessAddCustomerHandler implements HttpHandler {
	
	 /**
     * Default constructor for ProcessAddCustomerHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ProcessAddCustomerHandler() {
        // No specific initialisation required
    }
	
    /**
     * Processes the HTTP request to delete a customer.
     * Requires administrative privileges to proceed with the deletion.
     * Responds with appropriate HTML based on the outcome of the delete operation.
     * 
     * @param he the HttpExchange object containing details about the request and response.
     * @throws IOException if an I/O error occurs.
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
        	String customerName = formData.get("name");
        	String street = formData.get("addressLine0");
            String town = formData.get("addressLine1");
            String city = formData.get("addressLine2");
            String country = formData.get("country");
            String postcode = formData.get("postCode");
            Address address = new Address(street, town, city, country, postcode);

        	String telephoneNumber = formData.get("telephonenumber");
        	String emailAddress = formData.get("email");
        	
        	Customer newCustomer = new Customer(customerName, address, telephoneNumber, emailAddress);
        	
        	// Insert into the database
        	CustomerDAO dao = new CustomerDAO();
        	boolean success = dao.addCustomer(newCustomer);
        	
        	String response = success ? "Customer added successfully!" : "Failed to add customer.";
        	
        	he.sendResponseHeaders(200, 0);
        	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));
        	out.write("<html><body><h1>" + response + "</h1><a href=\"/customers\">View Customers</a></body></html>");
        	out.close();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
    
    /**
     * Retrieves the session ID from the HTTP cookies.
     * 
     * @param exchange the HttpExchange object containing the request details.
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
     * Parses form data from a URL-encoded string.
     * 
     * @param formData the raw form data string.
     * @return a map containing key-value pairs of form data.
     */
    private Map<String, String> parseFormData(String formData) {
    	Map<String, String> result = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);  // Ensure to split into only two parts: key and value
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    result.put(key, value);
                } catch (UnsupportedEncodingException ex) {
                    throw new AssertionError("UTF-8 is a required encoding support", ex);
                }
            }
        }
        return result;
    }
}