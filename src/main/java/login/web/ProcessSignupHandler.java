package login.web;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import com.sun.net.httpserver.HttpHandler;

import customers.Address;
import customers.Customer;
import customers.CustomerDAO;
import users.Users;
import users.UsersDAO;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the signup process for new customers. 
 * The handler processes form data submitted by the user, validates the data, 
 * creates a new customer and user account, and logs the user in if successful.
 * 
 * Features:
 * - Accepts customer and user details from a signup form.
 * - Creates a new customer in the database.
 * - Creates a corresponding user account with "Customer" role.
 * - Automatically logs the user in upon successful signup.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ProcessSignupHandler implements HttpHandler {
	
	/**
     * Default constructor for ProcessSignupHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ProcessSignupHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles the signup process.
     * Accepts form data, creates a new customer and user in the database, 
     * and redirects the user to the customer dashboard on success.
     * 
     * @param he the HttpExchange object containing the HTTP request and response details.
     * @throws IOException if an I/O error occurs during the process.
     */
	public void handle(HttpExchange he) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(he.getRequestBody()));
        String line;
        StringBuilder input = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            input.append(line);
        }

        // Parse the form data into a Map
        try {
            Map<String, String> formData = parseFormData(input.toString());
            String businessName = formData.get("name");
            String street = formData.get("addressLine0");
            String town = formData.get("addressLine1");
            String city = formData.get("addressLine2");
            String country = formData.get("country");
            String postcode = formData.get("postCode");
            String telephone = formData.get("telephone");
            String email = formData.get("email");

            // Creates address object
            Address address = new Address(street, town, city, country, postcode);
            
            String username = formData.get("username");
            String password = formData.get("password");
      
            
            Customer newCustomer = new Customer(businessName, address, telephone, email);
            CustomerDAO custdao = new CustomerDAO();
            boolean custSuccess = custdao.addCustomer(newCustomer);
            
            if (custSuccess) {
            	int customerId = custdao.getCustomerId(businessName, email);
            	
            	if (customerId > 0) {
            		Users newUser = new Users(username, password, "Customer", customerId);
            		
            		// Add the user to the database
            		UsersDAO dao = new UsersDAO();
            		boolean success = dao.addUser(newUser);
            		
            		if (success) {
            			// Log the user in by creating a session
            			String sessionId = LoginSessionManager.createSession(newUser);
            			
            			// Set the session ID in a cookie
            			he.getResponseHeaders().add("Set-Cookie", "sessionId=" + sessionId + "; HttpOnly; Path=/");
            			
            			// Redirect to the customer dashboard
            			he.getResponseHeaders().set("Location", "/customerdashboard");
            			he.sendResponseHeaders(302, -1);
            		} else {
                        // Handle failure to fetch customerId
                        he.sendResponseHeaders(200, 0);
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));
                        out.write("<html><body><h1>Failed to retrieve customer details.</h1><a href='/signup'>Try Again</a></body></html>");
                        out.close();
                    }
        		} else {
        			// Handle failure (e.g., username already exists)
        			he.sendResponseHeaders(200, 0);
        			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));
        			out.write("<html><body><h1>Failed to create account. Username may already exist.</h1><a href='/signup'>Try Again</a></body></html>");
        			out.close();
        		}
         
            	}

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Parses form data into a Map.
     * 
     * @param formData the raw form data as a string.
     * @return a Map containing key-value pairs of form data.
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