package customers.web;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.sun.net.httpserver.HttpHandler;

import customers.Address;
import customers.Customer;
import customers.CustomerDAO;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import login.web.LoginSessionManager;

/**
 * Handles HTTP requests to process updates to an existing customer record.
 * Validates the user session to ensure administrative privileges before allowing the operation.
 * Parses form data from the request to update the customer in the database.
 * Provides feedback to the user about the success or failure of the operation.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class ProcessUpdateCustomerHandler implements HttpHandler {
	
	 /**
     * Default constructor for ProcessUpdateCustomerHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ProcessUpdateCustomerHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles an HTTP request to update customer information.
     * 
     * This method processes HTTP requests received by the server. It performs the following tasks:
     * - Validates the session to ensure the user is logged in and has administrative privileges.
     * - Parses form data submitted through the request body to extract customer details.
     * - Updates the customer's information in the database using the provided form data.
     * - Sends an appropriate HTML response indicating the success or failure of the update operation.
     * 
     * If the user is not logged in as an administrator, the method redirects the request to the login page.
     * 
     * @param he the {@link HttpExchange} object that represents the HTTP request and response.
     * @throws IOException if an I/O error occurs while processing the request or sending the response.
     */
    public void handle(HttpExchange he) throws IOException {
    	
    	String sessionId = getSessionIdFromCookie(he);
        LoginSessionManager.UserSession session = LoginSessionManager.getSession(sessionId);

        // Check if user is logged in and is an admin (need to be logged in as admin to view this page)
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

        // Parse the form data into a map
        try {
            Map<String, String> formData = parseFormData(input.toString());
            
            // Extract individual form fields and store in the map
            int customerId = Integer.parseInt(formData.get("id"));
            String businessName = formData.get("name");
            String street = formData.get("addressLine0");
            String town = formData.get("addressLine1");
            String city = formData.get("addressLine2");
            String country = formData.get("country");
            String postcode = formData.get("postCode");
            Address address = new Address(street, town, city, country, postcode);
            String telephoneNumber = formData.get("telephone");
            String emailAddress = formData.get("email");
            
            
            // Create the HomeAppliance object
            Customer updatedCust = new Customer(businessName, address, telephoneNumber, emailAddress);
            updatedCust.setCustomerID(customerId);
            
            // Update the database with new form entries
            CustomerDAO dao = new CustomerDAO();
            boolean success = dao.updateCustomer(updatedCust);
            
            String response = success ? "Customer updated successfully!" : "Failed to update customer.";
            
           
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
     * @throws UnsupportedEncodingException if UTF-8 encoding is not supported.
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