package customers.web;

import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpHandler;
import login.web.LoginSessionManager;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Handles the HTTP GET requests to display a form for adding a new customer.
 * 
 * Features:
 * - Verifies if the user is logged in and has admin privileges.
 * - Displays an HTML form to collect customer information such as business name, 
 *   address, telephone number, and email.
 * - Redirects to the login page if the user is not authorised.
 * 
 * This handler is part of the server-side implementation for managing customer
 * information in the appliance store.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class AddCustomerHandler implements HttpHandler {
	
	 /**
     * Default constructor for AddCustomerHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public AddCustomerHandler() {
        // No specific initialisation required
    }
    
    /**
     * Handles the HTTP GET request to display the add customer form.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while handling the request.
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
        
        he.sendResponseHeaders(200, 0);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));

        out.write(
        	    "<!DOCTYPE html>" +
        	    "<html lang='en'>" +
        	    "<head>" +
        	    "    <meta charset='UTF-8'>" +
        	    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
        	    "    <title>Add New Customer</title>" +
        	    "    <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css' crossorigin='anonymous'>" +
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
        	    "    <div class='container mt-5'>" +
        	    "        <h1 class='mb-4'>Add a New Customer</h1>" +
        	    "        <form method='POST' action='/processaddcustomer'>" +
        	    "            <div class='form-group'>" +
        	    "                <label for='name'>Business Name:</label>" +
        	    "                <input type='text' id='name' name='name' class='form-control' placeholder='Enter business name' required>" +
        	    "            </div>" +
        	    "            <div class='form-group'>" +
        	    "                <label>Customer Address:</label>" +
        	    "                <input type='text' id='addressLine0' name='addressLine0' class='form-control' placeholder='Street' required>" +
        	    "                <input type='text' id='addressLine1' name='addressLine1' class='form-control' placeholder='Town' required>" +
        	    "                <input type='text' id='addressLine2' name='addressLine2' class='form-control' placeholder='City' required>" +
        	    "                <input type='text' id='country' name='country' class='form-control' placeholder='Country' required>" +
        	    "                <input type='text' id='postCode' name='postCode' class='form-control' placeholder='Postcode' required>" +
        	    "            </div>" +
        	    "            <div class='form-group'>" +
        	    "                <label for='telephonenumber'>Telephone Number:</label>" +
        	    "                <input type='text' id='telephonenumber' name='telephonenumber' class='form-control' placeholder='Enter telephone number' required>" +
        	    "            </div>" +
        	    "            <div class='form-group'>" +
        	    "                <label for='email'>Email Address:</label>" +
        	    "                <input type='email' id='email' name='email' class='form-control' placeholder='Enter email address' required>" +
        	    "            </div>" +
        	    "            <button type='submit' class='btn btn-primary'>Add Customer</button>" +
        	    "        </form>" +
        	    "    </div>" +
        	    "</body>" +
        	    "</html>");

        out.close();
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
}