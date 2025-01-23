package homeappliance.web;

import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import homeappliance.HomeApplianceDAO;
import login.web.LoginSessionManager;
import homeappliance.HomeAppliance;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * The RootHandler class implements the HttpHandler interface and is responsible
 * for handling requests to the root endpoint of the application. It generates
 * a dynamic web page (home page) that displays navigation links, search functionality,
 * and search results for products.
 * 
 * Key features:
 * - Displays navigation options based on user roles (Admin, Customer, Guest).
 * - Provides a search bar for querying products by keyword.
 * - Displays product search results and a welcome message.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class RootHandler implements HttpHandler {
	
	/**
     * Default constructor for RootHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public RootHandler() {
        // No specific initialisation required
    }
    
    private HomeApplianceDAO products = new HomeApplianceDAO();
    
    /**
     * Handles incoming HTTP requests to the root endpoint. 
     * Generates an HTML response based on the user's session and search queries.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while processing the request.
     */
    @Override
    public void handle(HttpExchange he) throws IOException {
    	String sessionId = getSessionIdFromCookie(he);
        LoginSessionManager.UserSession session = LoginSessionManager.getSession(sessionId);
    	
    	String searchQuery = null;
        ArrayList<HomeAppliance> searchResults = new ArrayList<>();
        
        Map<String, String> params = parseQueryString(he.getRequestURI().getQuery());
        if (params.containsKey("search")) {
            searchQuery = params.get("search");
            
            
            if (searchQuery != null && !searchQuery.isEmpty()) {
                searchResults = products.searchProductsByKeyword(searchQuery);
            } else {
                searchResults = products.findAllProducts();
            }
        }

        he.sendResponseHeaders(200, 0);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));
        out.write("<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Appliance Store</title>" +
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
                "            <ul class='navbar-nav mr-auto'>");

        // Common links visible to all users
        out.write("                <li class='nav-item active'>" +
                  "                    <a class='nav-link' href='/products'>Products</a>" +
                  "                </li>");

        if (session != null) {
            if ("Admin".equals(session.getRole())) {
                // Links for admin
                out.write("                <li class='nav-item'>" +
                          "                    <a class='nav-link' href='/addproduct'>Add Product</a>" +
                          "                </li>" +
                          "                <li class='nav-item'>" +
                          "                    <a class='nav-link' href='/customers'>View Customers</a>" +
                          "                </li>" +
                          "                <li class='nav-item'>" +
                          "                    <a class='nav-link' href='/addcustomer'>Add Customer</a>" +
                          "                </li>" +
            			  "                <li class='nav-item'>" +
            			  "                    <a class='nav-link' href='/admindashboard'>Admin Dashboard</a>" +
            			  "                </li>");
            } else if ("Customer".equals(session.getRole())) {
                // Links for customers
                out.write("                <li class='nav-item'>" +
                          "                    <a class='nav-link' href='/customerdashboard'>Customer Dashboard</a>" +
                          "                </li>");
            }
            // User is logged in
            out.write("                <li class='nav-item'>" +
                      "                    <a class='nav-link' href='/logout'>Logout</a>" +
                      "                </li>" +
                      "                <li class='nav-item'>" +
                      "                    <span class='navbar-text'>Logged in as: " + session.getUsername() + "</span>" +
                      "                </li>");
        } else {
            // Links for not logged in users
            out.write("                <li class='nav-item'>" +
                      "                    <a class='nav-link' href='/login'>Login</a>" +
                      "                </li>" +
                      "                <li class='nav-item'>" +
                      "                    <a class='nav-link' href='/signup'>Sign Up</a>" +
                      "                </li>");
        }

        // Search bar for all states
        out.write("            </ul>" +
                  "            <form class='form-inline ml-auto' method='get' action='/'>" +
                  "                <input class='form-control mr-sm-2' type='search' placeholder='Search for product' aria-label='Search' name='search'>" +
                  "                <button class='btn btn-outline-success my-2 my-sm-0' type='submit'>Search</button>" +
                  "            </form>" +
                  "        </div>" +
                  "    </nav>" +
                  "    <div class='container mt-5'>");


        // Display search results if a search query is present
        if (searchQuery != null && !searchQuery.isEmpty()) {
            out.write("<h2 class='mb-4'>Search Results for: " + searchQuery + "</h2>");
            if (!searchResults.isEmpty()) {
                out.write("<table class='table table-striped'>" +
                        "    <thead class='thead-dark'>" +
                        "        <tr>" +
                        "            <th>ID</th>" +
                        "            <th>SKU</th>" +
                        "            <th>Description</th>" +
                        "            <th>Category</th>" +
                        "            <th>Price</th>" +
                        "        </tr>" +
                        "    </thead>" +
                        "    <tbody>");
                for (HomeAppliance appliance : searchResults) {
                    out.write("<tr>" +
                            "    <td>" + appliance.getId() + "</td>" +
                            "    <td>" + appliance.getSku() + "</td>" +
                            "    <td>" + appliance.getDescription() + "</td>" +
                            "    <td>" + appliance.getCategory() + "</td>" +
                            "    <td>£" + appliance.getPrice() + "</td>" +
                            "</tr>");
                }
                out.write("</tbody></table>");
            } else {
                out.write("<p>No results found for '" + searchQuery + "'</p>");
            }
        } else {
            // Welcome message and options
            out.write("<h1 class='text-center mb-4'>Sonic Home Appliances</h1>" +
                    "        <p class='text-center'>Manage and explore products and customers</p>" +
                    "        <div class='list-group'>" +
                    "            <a href='/products' class='list-group-item list-group-item-action d-flex align-items-center text-white bg-primary rounded my-2 shadow' style='transition: transform .2s;'>" +
                    "                <i class='fas fa-shopping-cart fa-2x mr-3'></i> View All Products" +
                    "                <p class='small text-white ml-auto'>Explore our range of products</p>" +
                    "            </a>" +
                    "            <a href='/addproduct' class='list-group-item list-group-item-action d-flex align-items-center text-white bg-secondary rounded my-2 shadow' style='transition: transform .2s;'>" +
                    "                <i class='fas fa-plus fa-2x mr-3'></i> Add Product" +
                    "                <p class='small text-white ml-auto'>Add new products (Login as Admin)</p>" +
                    "            </a>" +
                    "            <a href='/customers' class='list-group-item list-group-item-action d-flex align-items-center text-white bg-success rounded my-2 shadow' style='transition: transform .2s;'>" +
                    "                <i class='fas fa-users fa-2x mr-3'></i> View All Customers" +
                    "                <p class='small text-white ml-auto'>See all registered customers (Login as Admin)</p>" +
                    "            </a>" +
                    "            <a href='/addcustomer' class='list-group-item list-group-item-action d-flex align-items-center text-white bg-info rounded my-2 shadow' style='transition: transform .2s;'>" +
                    "                <i class='fas fa-user-plus fa-2x mr-3'></i> Add Customer" +
                    "                <p class='small text-white ml-auto'>Register new customers (Login as Admin)</p>" +
                    "            </a>" +
                    "        </div>"
                );
        }

        out.write("</div></body></html>");
        out.close();
    }
    
    /**
     * Extracts the session ID from the "Cookie" header of an HTTP request.
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
    
    /**
     * Parses a query string into a map of key-value pairs.
     * 
     * @param query the query string to parse.
     * @return a map where keys are parameter names and values are parameter values.
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
}