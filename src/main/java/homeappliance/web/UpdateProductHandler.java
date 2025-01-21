package homeappliance.web;

import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpHandler;

import login.web.LoginSessionManager;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * The UpdateProductHandler class handles HTTP GET requests to display a form
 * for updating an existing product. This form is pre-populated with product details.
 * 
 * Features:
 * - Validates the user's session and ensures the user is an admin.
 * - Displays an HTML form to update product details.
 * - Prepares the product ID and necessary fields for submission.
 * 
 * This handler is part of the server-side implementation for managing
 * products in the appliance store.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class UpdateProductHandler implements HttpHandler {
	
	/**
     * Default constructor for UpdateProductHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public UpdateProductHandler() {
        // No specific initialisation required
    }
	
	
    /**
     * Handles the HTTP GET request to display the product update form.
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
        
        String query = he.getRequestURI().getQuery();
        String productId = null;
        if (query != null && query.contains("id=")) {
        	productId = query.split("=")[1];
        }

        out.write("<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Update Product</title>" +
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
                "        <h1>Update Product</h1>" +
                "        <form method='POST' action='/processupdateproduct'>" +
                "            <input type='hidden' name='id' value='" + (productId != null ? productId : "") + "'>" +
                "            <div class='form-group'>" +
                "                <label for='sku'>SKU:</label>" +
                "                <input type='text' class='form-control' id='sku' name='sku'>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='description'>Description:</label>" +
                "                <input type='text' class='form-control' id='description' name='description'>" +
                "            </div>" +
                "    		 <div class=\"form-group\">" +
                "        		 <label for=\"category\">Enter Updated Category:</label>" +
                "        		 <input type=\"text\" id=\"category\" name=\"category\" class=\"form-control\" required>" +
                "    		 </div>" +
                "            <div class='form-group'>" +
                "                <label for='price'>Price:</label>" +
                "                <input type='number' class='form-control' id='price' name='price'>" +
                "            </div>" +
                "            <button type='submit' class='btn btn-primary'>Update</button>" +
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
