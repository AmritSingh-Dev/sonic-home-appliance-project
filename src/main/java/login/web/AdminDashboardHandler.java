package login.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import homeappliance.HomeAppliance;
import homeappliance.HomeApplianceDAO;
import login.web.LoginSessionManager.UserSession;

/**
 * Admin dashboard class which loads when a admin user logs in, allowing administrators to manage users, products, and customers.
 * The dashboard also supports search functionality for products.
 * Only accessible by users with admin privileges.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class AdminDashboardHandler implements HttpHandler {
	
	/**
     * Default constructor for AdminDashboardHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public AdminDashboardHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles HTTP requests to display the Admin Dashboard.
     * Validates if the user has admin privileges and displays appropriate options.
     * 
     * @param exchange the HttpExchange object containing request and response details.
     * @throws IOException if an I/O error occurs while writing the response.
     */
	private HomeApplianceDAO products = new HomeApplianceDAO();
	
	@Override
    public void handle(HttpExchange exchange) throws IOException {
        String sessionId = getSessionIdFromCookie(exchange);
        UserSession session = LoginSessionManager.getSession(sessionId);
        
        // Redirect unauthorised users to the login page
        if (session != null && "Admin".equals(session.getRole())) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody()));
            exchange.sendResponseHeaders(200, 0);
            
            String searchQuery = null;
            ArrayList<HomeAppliance> searchResults = new ArrayList<>();
            // Handle search functionality
            Map<String, String> params = parseQueryString(exchange.getRequestURI().getQuery());
            if (params.containsKey("search")) {
                searchQuery = params.get("search");
                
                
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    searchResults = products.searchProductsByKeyword(searchQuery);
                } else {
                    searchResults = products.findAllProducts();
                }
            }
            
            out.write(
                    "<!DOCTYPE html>" +
                    "<html lang='en'>" +
                    "<head>" +
                    "    <meta charset='UTF-8'>" +
                    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "    <title>Admin Dashboard</title>" +
                    "    <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css'>" +
                    "    <style>" +
                    "        .dashboard-card {" +
                    "            border-radius: 15px;" +
                    "            transition: transform 0.2s;" +
                    "        }" +
                    "        .dashboard-card:hover {" +
                    "            transform: scale(1.05);" +
                    "            box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.2);" +
                    "        }" +
                    "        .icon {" +
                    "            font-size: 40px;" +
                    "            margin-bottom: 15px;" +
                    "        }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container-fluid p-0'>" +
                    "        <nav class='navbar navbar-expand-lg navbar-light bg-light'>" +
                    "            <a class='navbar-brand d-flex align-items-center' href='#'>" +
                    "                <span>Admin Dashboard</span>" +
                    "            </a>" +
                    "            <button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarNav' aria-controls='navbarNav' aria-expanded='false' aria-label='Toggle navigation'>" +
                    "                <span class='navbar-toggler-icon'></span>" +
                    "            </button>" +
                    "            <div class='collapse navbar-collapse' id='navbarNav'>" +
                    "                <ul class='navbar-nav mr-auto'>" +
                    "                    <li class='nav-item active'>" +
                    "                        <a class='nav-link' href='/'>Home <span class='sr-only'>(current)</span></a>" +
                    "                    </li>" +
                    "                    <li class='nav-item'>" +
                    "                        <a class='nav-link' href='/logout'>Logout</a>" +
                    "                    </li>" +
                    "                    <span class='navbar-text ml-auto'>" +
                    "                        Logged in as: " + session.getUsername() +
                    "                    </span>" +
                    "                </ul>" +
                    "                <form class='form-inline ml-auto' method='get' action='/'>" +
            	    "                    <input class='form-control mr-sm-2' type='search' placeholder='Search for product' aria-label='Search' name='search'>" +
            	    "                    <button class='btn btn-outline-success my-2 my-sm-0' type='submit'>Search</button>" +
            	    "                </form>" +
                    "            </div>" +
                    "        </nav>" +
                    "    </div>" +
                    "    <div class='container mt-5'>" +
                    "        <h1 class='mt-4 text-center'>Welcome to the Admin Dashboard</h1>" +
                    "        <div class='row mt-4'>" +
                    "            <div class='col-md-12 mb-4'>" +
                    "                <a href='/users' class='text-decoration-none'>" +
                    "                    <div class='card text-center text-white bg-danger bg-gradient dashboard-card'>" +
                    "                        <div class='card-body'>" +
                    "                            <div class='icon'><i class='fas fa-users'></i></div>" +
                    "                            <h5 class='card-title'>View Users & Update Roles</h5>" +
                    "                        </div>" +
                    "                    </div>" +
                    "                </a>" +
                    "            </div>" +
                    "            <div class='col-md-6 mb-4'>" +
                    "                <a href='/products' class='text-decoration-none'>" +
                    "                    <div class='card text-center text-white bg-success bg-gradient dashboard-card'>" +
                    "                        <div class='card-body'>" +
                    "                            <div class='icon'><i class='fas fa-box'></i></div>" +
                    "                            <h5 class='card-title'>View Products</h5>" +
                    "                        </div>" +
                    "                    </div>" +
                    "                </a>" +
                    "            </div>" +
                    "            <div class='col-md-6 mb-4'>" +
                    "                <a href='/addproduct' class='text-decoration-none'>" +
                    "                    <div class='card text-center text-white bg-success bg-gradient dashboard-card'>" +
                    "                        <div class='card-body'>" +
                    "                            <div class='icon'><i class='fas fa-plus'></i></div>" +
                    "                            <h5 class='card-title'>Add Products</h5>" +
                    "                        </div>" +
                    "                    </div>" +
                    "                </a>" +
                    "            </div>" +
                    "            <div class='col-md-6 mb-4'>" +
                    "                <a href='/customers' class='text-decoration-none'>" +
                    "                    <div class='card text-center text-white bg-secondary bg-gradient dashboard-card'>" +
                    "                        <div class='card-body'>" +
                    "                            <div class='icon'><i class='fas fa-address-book'></i></div>" +
                    "                            <h5 class='card-title'>View Customers</h5>" +
                    "                        </div>" +
                    "                    </div>" +
                    "                </a>" +
                    "            </div>" +
                    "            <div class='col-md-6 mb-4'>" +
                    "                <a href='/addcustomer' class='text-decoration-none'>" +
                    "                    <div class='card text-center text-white bg-secondary bg-gradient dashboard-card'>" +
                    "                        <div class='card-body'>" +
                    "                            <div class='icon'><i class='fas fa-user-plus'></i></div>" +
                    "                            <h5 class='card-title'>Add Customers</h5>" +
                    "                        </div>" +
                    "                    </div>" +
                    "                </a>" +
                    "            </div>" +
                    "        </div>" +
                    "    </div>" +
                    "    <script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>" +
                    "</body>" +
                    "</html>");
            
            if (searchQuery != null && !searchQuery.isEmpty() && !searchResults.isEmpty()) {
                out.write("<h2 class='mb-4'>Search Results for: " + searchQuery + "</h2>" +
                          "<table class='table'>" +
                          "  <thead class='thead-dark'>" +
                          "    <tr>" +
                          "      <th scope='col'>ID</th>" +
                          "      <th scope='col'>SKU</th>" +
                          "      <th scope='col'>Description</th>" +
                          "      <th scope='col'>Category</th>" +
                          "      <th scope='col'>Price</th>" +
                          "    </tr>" +
                          "  </thead>" +
                          "  <tbody>");
                for (HomeAppliance appliance : searchResults) {
                    out.write(String.format(
                        "<tr>" +
                        "  <td>%d</td>" +
                        "  <td>%s</td>" +
                        "  <td>%s</td>" +
                        "  <td>%s</td>" +
                        "  <td>%.2f</td>" +
                        "</tr>", 
                        appliance.getId(), appliance.getSku(), appliance.getDescription(), appliance.getCategory(), appliance.getPrice()));
                }
                out.write("  </tbody>" +
                          "</table>");
            } else if (searchQuery != null && !searchQuery.isEmpty()) {
                out.write("<p>No results found for '" + searchQuery + "'</p>");
            }

            out.write("    </div>" +
                    "</body>" +
                    "</html>");
            out.close();
        } else {
            // Redirect to login or deny access
            exchange.getResponseHeaders().set("Location", "/login");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        }
    }
	
    /**
     * Parses a query string into a map of key-value pairs.
     * Supports standard URL-encoded query strings in the format key=value&key2=value2.
     * 
     * @param query The query string from the URL.
     * @return A map containing the parsed query parameters and their values.
     *         If a parameter has no value (e.g., key=), an empty string is used as the value.
     */
	 private Map<String, String> parseQueryString(String query) {
	        Map<String, String> result = new HashMap<>();
	        if (query != null && !query.isEmpty()) {
	            String[] pairs = query.split("&");
	            for (String pair : pairs) {
	                String[] keyValue = pair.split("=", 2);
	                if (keyValue.length == 2) {
	                    result.put(keyValue[0], keyValue[1]);
	                } else if (keyValue.length == 1) {
	                    result.put(keyValue[0], "");
	                }
	            }
	        }
	        return result;
	    }
	 
    /**
     * Extracts the session ID from the "Cookie" header in an HTTP request.
     * 
     * @param exchange The HttpExchange object representing the HTTP request.
     * @return The session ID if present, or null if not found.
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
