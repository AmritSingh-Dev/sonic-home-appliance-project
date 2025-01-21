/**
 * Handles HTTP requests to display the update customer form.
 * Validates the user session to ensure administrative privileges before allowing access.
 * Generates an HTML form pre-filled with the customer data for editing.
 * 
 * @author Amrit Singh | 24852631
 */
package customers.web;

import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpHandler;

import login.web.LoginSessionManager;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Handles HTTP requests to display the update customer form.
 * Validates the user session to ensure administrative privileges before allowing access.
 * Generates an HTML form pre-filled with the customer data for editing.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class UpdateCustomerHandler implements HttpHandler {
	
	 /**
     * Default constructor for UpdateCustomerHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public UpdateCustomerHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles the HTTP request to display the customer update form.
     * Only accessible by administrators. Redirects non-admin users to the login page.
     * 
     * @param he the HttpExchange object containing details about the request and response.
     * @throws IOException if an I/O error occurs.
     */
    public void handle(HttpExchange he) throws IOException {
    	String sessionId = getSessionIdFromCookie(he);
        LoginSessionManager.UserSession session = LoginSessionManager.getSession(sessionId);

        // Check if user is logged in and is an admin (Page is only accessible for admin and not customers)
        if (session == null || !"Admin".equals(session.getRole())) {
            he.getResponseHeaders().set("Location", "/login");
            he.sendResponseHeaders(302, 0);
            he.getResponseBody().close();
            return;
        }
    	
        he.sendResponseHeaders(200, 0);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));
        
        // Extract customer ID from query parameters
        String query = he.getRequestURI().getQuery();
        String custId = null;
        if (query != null && query.contains("id=")) {
        	custId = query.split("=")[1];
        }

       
        out.write(
            "<html>" +
            "<head><title>Update Customer</title>" +
            "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\" crossorigin=\"anonymous\">" +
            "</head>" +
            "<body>" +
            "    <nav class='navbar navbar-expand-lg navbar-light bg-light'>" +
            "        <a class='navbar-brand' href='/'>" +
            "        </a>" +
            "        <button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarNav' aria-controls='navbarNav' aria-expanded='false' aria-label='Toggle navigation'>" +
            "            <span class='navbar-toggler-icon'></span>" +
            "        </button>" +
            "        <div class='collapse navbar-collapse' id='navbarNav'>" +
            "            <ul class='navbar-nav mr-auto'>" +
            "                <li class='nav-item active'>" +
            "                    <a class='nav-link' href='/products'>Products</a>" +
            "                </li>" +
            "                <li class='nav-item'>" +
            "                    <a class='nav-link' href='/addproduct'>Add Product</a>" +
            "                </li>" +
            "                <li class='nav-item'>" +
            "                    <a class='nav-link' href='/customers'>View Customers</a>" +
            "                </li>" +
            "                <li class='nav-item'>" +
            "                    <a class='nav-link' href='/addcustomer'>Add Customer</a>" +
            "                </li>" +
            "                </li>" +
			"                <li class='nav-item'>" +
			"                    <a class='nav-link' href='/admindashboard'>Admin Dashboard</a>" +
			"                </li>" +
            "                <li class='nav-item'>" +
            "                    <a class='nav-link' href='/logout'>Logout</a>" +
            "                </li>" +
            "                <li class='nav-item'>" +
            "                    <span class='navbar-text'>Logged in as: " + session.getUsername() + "</span>" +
            "                </li>" +
            "            </ul>" +
            "        </div>" +
            "    </nav>" +
            "<div class=\"container\">" +
            
            //Update custom form
            "<h1>Update Customer</h1>" +
            "<form method=\"POST\" action=\"/processupdatecustomer\">" +
            "    <input type=\"hidden\" name=\"id\" value=\"" + custId + "\" />" + // Hidden field for the product incremental ID
            "    <div class=\"form-group\">" +
            "    <div class=\"form-group\">" +
            "        <label for=\"name\">Enter Updated Business Name:</label>" +
            "        <input type='text' id='name' name='name' class='form-control' required>" +
            "            </div>" +
    	    "            <div class='form-group'>" +
    	    "                <label>Enter Updated Address:</label>" +
    	    "                <input type='text' id='addressLine0' name='addressLine0' class='form-control' placeholder='Street' required>" +
    	    "                <input type='text' id='addressLine1' name='addressLine1' class='form-control' placeholder='Town' required>" +
    	    "                <input type='text' id='addressLine2' name='addressLine2' class='form-control' placeholder='City' required>" +
    	    "                <input type='text' id='country' name='country' class='form-control' placeholder='Country' required>" +
    	    "                <input type='text' id='postCode' name='postCode' class='form-control' placeholder='Postcode' required>" +
    	    "            </div>" +
            "    <div class=\"form-group\">" +
            "        <label for=\"telephone\">Enter Updated Telephone Number:</label>" +
            "        <input type=\"text\" id=\"telephone\" name=\"telephone\" class=\"form-control\" required>" +
            "    </div>" +
            "    <div class=\"form-group\">" +
            "        <label for=\"email\">Enter Updated Email:</label>" +
            "        <input type=\"text\" id=\"email\" name=\"email\" class=\"form-control\" required>" +
            "    </div>" +
            "    <button type=\"submit\" class=\"btn btn-primary\">Update Customer</button>" +
            "</form>" +
            "</div>" +
            "</body>" +
            "</html>"
        );

        out.close();
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
}
