package homeappliance.web;

import java.io.OutputStreamWriter;
import java.util.List;

import com.sun.net.httpserver.HttpHandler;

import homeappliance.HomeApplianceDAO;
import login.web.LoginSessionManager;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * The AddProductHandler class handles HTTP requests for the "Add Product" functionality.
 * This class checks user authentication and authorisation to ensure that only 
 * admit users can access the "Add Product" page. It dynamically generates an HTML 
 * form for adding a new product to the system.
 *
 * Key Features:
 * - Verifies the user's session and role (Admin).
 * - Redirects unauthorised users to the login page.
 * - Dynamically populates the product category dropdown based on database entries.
 * - Allows entering a new category if desired.
 * - Renders an HTML form for adding a new product with fields for SKU, description, category, and price.
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class AddProductHandler implements HttpHandler {
	
	 /**
     * Default constructor for AddProductHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public AddProductHandler() {
        // No specific initialisation required
    }
	
	 /**
     * Handles HTTP GET requests to render the "Add Product" page.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs during the response generation.
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
        
        // Fetch all product categories from the database
        HomeApplianceDAO dao = new HomeApplianceDAO();
        List<String> categories = dao.findAllCategories();

        he.sendResponseHeaders(200, 0);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));

        // Output the HTML form for adding a new product
        out.write(
            "<!DOCTYPE html>" +
            "<html lang='en'>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <title>Add New Product</title>" +
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
            "        <h1 class='mb-4'>Add a New Product</h1>" +
            "        <form method='POST' action='/processaddproduct'>" +
            "            <div class='form-group'>" +
            "                <label for='sku'>SKU:</label>" +
            "                <input type='text' id='sku' name='sku' class='form-control' placeholder='Enter SKU' required>" +
            "            </div>" +
            "            <div class='form-group'>" +
            "                <label for='description'>Description:</label>" +
            "                <textarea id='description' name='description' class='form-control' placeholder='Describe the product' required></textarea>" +
            "            </div>" +
            "            <div class='form-group'>" +
            "                <label for='category'>Category:</label>" +
            "                <select id='category' name='category' class='form-control' onchange='toggleNewCategoryField()' required>" +
            "                    <option value=''>Select an existing category</option>");

        // Populate dropdown with existing categories
        for (String category : categories) {
            out.write("<option value='" + category + "'>" + category + "</option>");
        }
        
        // Complete the form with options for entering a new category and price
        out.write(
            "                    <option value='new'>Enter New Category</option>" +
            "                </select>" +
            "                <input type='text' id='newCategory' name='newCategory' class='form-control mt-2' placeholder='Enter new category' style='display:none;'>" +
            "                <script>" +
            "                    function toggleNewCategoryField() {" +
            "                        var categorySelect = document.getElementById('category');" +
            "                        var newCategoryField = document.getElementById('newCategory');" +
            "                        if (categorySelect.value === 'new') {" +
            "                            newCategoryField.style.display = 'block';" +
            "                        } else {" +
            "                            newCategoryField.style.display = 'none';" +
            "                        }" +
            "                    }" +
            "                </script>" +
            "            </div>" +
            "            <div class='form-group'>" +
            "                <label for='price'>Price:</label>" +
            "                <input type='number' id='price' name='price' class='form-control' placeholder='Enter price' step='1' required>" +
            "            </div>" +
            "            <button type='submit' class='btn btn-primary'>Add Product</button>" +
            "        </form>" +
            "    </div>" +
            "</body>" +
            "</html>");

        out.close();
    }
    
    /**
     * Extracts the session ID from the "Cookie" header of the HTTP request.
     * 
     * @param exchange the HttpExchange object representing the HTTP request.
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
}


