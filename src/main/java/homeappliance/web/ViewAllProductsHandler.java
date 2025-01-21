package homeappliance.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import homeappliance.ApplianceItem;
import homeappliance.ApplianceItemDAO;
import homeappliance.HomeAppliance;
import homeappliance.HomeApplianceDAO;
import login.web.LoginSessionManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The ViewAllProductsHandler class implements the HttpHandler interface
 * and is responsible for handling HTTP requests to view and manage products,
 * as well as handling "Add To Basket" form submissions (POST).
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class ViewAllProductsHandler implements HttpHandler {

	    private HomeApplianceDAO products = new HomeApplianceDAO();
	    private ApplianceItemDAO applianceItemDAO = new ApplianceItemDAO();
	    
	    
		/**
	     * Default constructor for ViewAllProductsHandler.
	     * Initialises a new instance of the handler without any additional setup.
	     */
	    public ViewAllProductsHandler() {
	        // No specific initialisation required
	    }
	    
	    /**
	     * Handles incoming HTTP requests for the specified endpoint.
	     *
	     * This method determines the HTTP request method (GET or POST) and delegates
	     * the request to the appropriate handler:
	     * 
	     * If the method is POST, it calls {@code handleAddToBasket(HttpExchange)} to process
	     * the "Add To Basket" form submission.
	     * If the method is GET, it calls {@code handleShowProducts(HttpExchange)} to display
	     * the list of products.
	     * 
	     *
	     * @param he the {@link HttpExchange} object representing the HTTP request and response
	     * @throws IOException if an I/O error occurs during the handling of the request
	     */
	    @Override
	    public void handle(HttpExchange he) throws IOException {
	        try {
	            String method = he.getRequestMethod();
	
	            // If the request is POST, assume it's the "Add To Basket" form submission.
	            if ("POST".equalsIgnoreCase(method)) {
	                handleAddToBasket(he);
	                return; 
	            }
	
	            // Otherwise, handle GET = show the list of products
	            handleShowProducts(he);
	
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            he.sendResponseHeaders(500, 0);
	            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()))) {
	                out.write("Internal Server Error: " + ex.getMessage());
	            }
	        }
	    }
	
	    /**
	     * Handles the HTTP GET request to display the list of products.
	     *
	     * Features:
	     * - Parses query parameters for search, filtering, and sorting.</li>
	     * - Fetches the appropriate products and categories based on the parameters.</li>
	     * - Dynamically generates an HTML response, including filtering and sorting options.</li>
	     * - Adjusts navigation bar and actions based on user session and role (Admin/Customer).</li>
	     *
	     * @param he the {@link HttpExchange} object representing the HTTP request and response.
	     * @throws IOException if an error occurs while reading the request or writing the response.
	     */
	    private void handleShowProducts(HttpExchange he) throws IOException {
	        // Parse user session
	        String sessionId = getSessionIdFromCookie(he);
	        LoginSessionManager.UserSession session = LoginSessionManager.getSession(sessionId);
	        boolean isAdmin = (session != null && "Admin".equals(session.getRole()));
	
	        // Parse query parameters for filtering/sorting
	        Map<String, String> params = parseQueryString(he.getRequestURI().getQuery());
	        String searchQuery = params.get("search");
	        String filterValue = params.get("filterValue");
	        String category = params.get("category");
	        String sortType = params.get("sortType");
	
	        // Fetch all products and items
	        List<HomeAppliance> appliances;
	        if (category != null && !category.isEmpty()) {
	            appliances = products.filterProductsByCategory(category);
	        } else if (filterValue != null && !filterValue.isEmpty()) {
	            appliances = products.searchProductsByAttribute("description", filterValue);
	        } else {
	            appliances = products.findAllProducts();
	        }
	
	        List<ApplianceItem> applianceItems = applianceItemDAO.findAllApplianceItems();
	
	        // Apply sorting if requested
	        if (sortType != null && !sortType.isEmpty()) {
	            switch (sortType) {
	                case "priceAsc":
	                    appliances = products.productsSortedByPrice(true);
	                    break;
	                case "priceDesc":
	                    appliances = products.productsSortedByPrice(false);
	                    break;
	                case "warrantyAsc":
	                    applianceItems = applianceItemDAO.itemsSortedByWarrantyYears(true);
	                    appliances = syncAppliancesWithItems(appliances, applianceItems);
	                    break;
	                case "warrantyDesc":
	                    applianceItems = applianceItemDAO.itemsSortedByWarrantyYears(false);
	                    appliances = syncAppliancesWithItems(appliances, applianceItems);
	                    break;
	            }
	        }
	
	        // If sorting by warranty, filter out any appliances not in applianceItems
	        if (sortType != null && sortType.startsWith("warranty")) {
	            List<Integer> applianceIds = applianceItems.stream()
	                    .map(ai -> ai.getHomeAppliance().getId())
	                    .collect(Collectors.toList());
	            appliances = appliances.stream()
	                    .filter(ap -> applianceIds.contains(ap.getId()))
	                    .collect(Collectors.toList());
	        }
	
	        he.sendResponseHeaders(200, 0);
	        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()))) {
	
	            // Get all categories for the dropdown
	            List<String> categories = products.findAllCategories();
	
	            out.write(
	            	    "<!DOCTYPE html>" +
	            	    "<html lang='en'>" +
	            	    "<head>" +
	            	    "  <meta charset='UTF-8'>" +
	            	    "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
	            	    "  <title>Products</title>" +
	            	    "  <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css' crossorigin='anonymous'>" +
	            	    "</head>" +
	            	    "<body>" +
	            	    "<nav class='navbar navbar-expand-lg navbar-light bg-light'>" +
	            	    "  <a class='navbar-brand' href='/'>Home</a>" +
	            	    "  <button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarNav' aria-controls='navbarNav' aria-expanded='false' aria-label='Toggle navigation'>" +
	            	    "    <span class='navbar-toggler-icon'></span>" +
	            	    "  </button>" +
	            	    "  <div class='collapse navbar-collapse' id='navbarNav'>" +
	            	    "    <ul class='navbar-nav mr-auto'>"
	            	);
	
	            // Dynamic navigation based on user role
	            if (session != null) {
	                if ("Admin".equals(session.getRole())) {
	                	out.write(
	                		    "      <li class='nav-item'><a class='nav-link' href='/addproduct'>Add Product</a></li>" +
	                		    "      <li class='nav-item'><a class='nav-link' href='/customers'>View Customers</a></li>" +
	                		    "      <li class='nav-item'><a class='nav-link' href='/addcustomer'>Add Customer</a></li>" +
	                		    "      <li class='nav-item'><a class='nav-link' href='/admindashboard'>Admin Dashboard</a></li>" +
	                		    "      <li class='nav-item'><a class='nav-link' href='/basket'>Basket</a></li>"
	                		);
	                } else if ("Customer".equals(session.getRole())) {
	                	out.write(
	                		    "      <li class='nav-item'><a class='nav-link' href='/customerDashboard'>Customer Dashboard</a></li>" +
	                		    "      <li class='nav-item'><a class='nav-link' href='/basket'>Basket</a></li>"
	                		);

	                }
	                out.write(
	                	    "      <li class='nav-item'><a class='nav-link' href='/logout'>Logout</a></li>" +
	                	    "      <li class='nav-item'><span class='navbar-text'>Logged in as: " + session.getUsername() + "</span></li>"
	                	);

	            } else {
	            	out.write(
	            		    "      <li class='nav-item'><a class='nav-link' href='/login'>Login</a></li>" +
	            		    "      <li class='nav-item'><a class='nav-link' href='/signup'>Sign Up</a></li>"
	            		);
	            }
	
	            out.write(
	            	    "    </ul>" +
	            	    "    <form class='form-inline ml-auto' method='get' action='/'>" +
	            	    "      <input class='form-control mr-sm-2' type='search' placeholder='Search for product' aria-label='Search' name='search'>" +
	            	    "      <button class='btn btn-outline-success my-2 my-sm-0' type='submit'>Search</button>" +
	            	    "    </form>" +
	            	    "  </div>" +
	            	    "</nav>" +
	            	    "<!-- Main container -->" +
	            	    "<div class='container mt-5'>" +
	            	    "  <h1 class='mb-4'>Products</h1>" +
	            	    "<!-- Filter by category -->" +
	            	    "  <div class='row mb-3'>" +
	            	    "    <div class='col-md-6'>" +
	            	    "      <form action='/products' method='get'>" +
	            	    "        <select name='category' class='form-control mb-2'>" +
	            	    "          <option value=''>Filter by category...</option>"
	            	);
	            for (String cat : categories) {
	                boolean isSelected = (category != null && cat.equals(category));
	                out.write("<option value='" + cat + "'" + (isSelected ? " selected" : "") + ">" + cat + "</option>");
	            }
	            out.write(
	            	    "        </select>" +
	            	    "        <button type='submit' class='btn btn-secondary mb-2'>Filter</button>" +
	            	    "      </form>" +
	            	    "    </div>" +
	            	    "    <!-- Sort by ... -->" +
	            	    "    <div class='col-md-6'>" +
	            	    "      <form action='/products' method='get'>" +
	            	    "        <select name='sortType' class='form-control mb-2'>" +
	            	    "          <option value=''>Sort by...</option>" +
	            	    "          <option value='priceAsc'>Price Low to High</option>" +
	            	    "          <option value='priceDesc'>Price High to Low</option>" +
	            	    "          <option value='warrantyAsc'>Warranty Years Low to High</option>" +
	            	    "          <option value='warrantyDesc'>Warranty Years High to Low</option>" +
	            	    "        </select>" +
	            	    "        <button type='submit' class='btn btn-info mb-2'>Sort</button>" +
	            	    "      </form>" +
	            	    "    </div>" +
	            	    "  </div>" +
	            	    "<!-- Table of products -->" +
	            	    "  <div class='table-responsive'>" +
	            	    "    <table class='table table-striped'>" +
	            	    "      <thead class='thead-dark'>" +
	            	    "        <tr>" +
	            	    "          <th>ID</th>" +
	            	    "          <th>Brand</th>" +
	            	    "          <th>Model</th>" +
	            	    "          <th>Warranty</th>" +
	            	    "          <th>SKU</th>" +
	            	    "          <th>Description</th>" +
	            	    "          <th>Category</th>" +
	            	    "          <th>Price</th>" +
	            	    "          <th>Purchase</th>"
	            	);

	            	// Here is the admin-specific column if needed
	            	if (isAdmin) {
	            	    out.write("          <th>Admin Actions</th>");
	            	}

	            	// Now close the row and continue
	            	out.write(
	            	    "        </tr>" +
	            	    "      </thead>" +
	            	    "      <tbody>"
	            	);

	            if (isAdmin) {
	                out.write("      <th>Admin Actions</th>");
	            }
	            out.write("        </tr>");
	            out.write("      </thead>");
	            out.write("      <tbody>");
	
	            for (HomeAppliance appliance : appliances) {
	                ApplianceItem matchingItem = applianceItems.stream()
	                    .filter(ai -> ai.getHomeAppliance().getId() == appliance.getId())
	                    .findFirst()
	                    .orElse(null);
	
	                String brand = (matchingItem != null && matchingItem.getBrand() != null)
	                        ? matchingItem.getBrand() : "Not Specified";
	                String model = (matchingItem != null && matchingItem.getModel() != null)
	                        ? matchingItem.getModel() : "Not Specified";
	                String warranty = (matchingItem != null)
	                        ? matchingItem.getWarrantyYears() + " years"
	                        : "Not Specified";
	
	                out.write(
	                	    "<tr>" +
	                	    "<td>" + appliance.getId() + "</td>" +
	                	    "<td>" + brand + "</td>" +
	                	    "<td>" + model + "</td>" +
	                	    "<td>" + warranty + "</td>" +
	                	    "<td>" + appliance.getSku() + "</td>" +
	                	    "<td>" + appliance.getDescription() + "</td>" +
	                	    "<td>" + appliance.getCategory() + "</td>" +
	                	    "<td>£" + appliance.getPrice() + "</td>" +
	                	    "<!-- Purchase form (POST to the same handler, but we check for POST in handleAddToBasket) -->" +
	                	    "<td>" +
	                	    "  <form method='post' action='/basket'>" +
	                	    "    <input type='hidden' name='applianceId' value='" + appliance.getId() + "' />" +
	                	    "    <button type='submit' class='btn btn-primary btn-sm' style='font-size: 12px; padding: 2px 6px;'>Add To Basket</button>" +
	                	    "  </form>" +
	                	    "</td>"
	                	);
	
	                // Admin actions
	                if (isAdmin) {
	                	out.write(
	                		    "<td style='white-space: nowrap; width: 200px;'>" +
	                		    "  <a href='/delete?id=" + appliance.getId() + "' " +
	                		    "     class='btn btn-danger btn-sm' " +
	                		    "     style='font-size: 12px; padding: 2px 6px; margin-right: 5px;'>Delete</a>" +
	                		    "  <a href='/updateproduct?id=" + appliance.getId() + "' " +
	                		    "     class='btn btn-primary btn-sm' " +
	                		    "     style='font-size: 12px; padding: 2px 6px;'>Update</a>" +
	                		    "</td>"
	                		);
	                }
	                out.write("</tr>");
	            }
	
	            out.write("      </tbody>");
	            out.write("    </table>");
	            out.write("  </div>");
	
	            // Back to menu link
	            out.write("  <a href='/' class='btn btn-secondary mt-3'>Back to Menu</a>");
	            out.write("</div>"); // .container
	            out.write("</body>");
	            out.write("</html>");
	        }
	    }
	
	    /**
	     * Handles a POST request to add an appliance to the user's basket.
	     * Extracts the `applianceId` parameter from the POST body and performs necessary processing.
	     * Redirects the client to the products page after handling the request.
	     *
	     * @param he the {@link HttpExchange} object representing the HTTP request and response.
	     * @throws IOException if an I/O error occurs while processing the request or sending the response.
	     */
	    private void handleAddToBasket(HttpExchange he) throws IOException {
	        Map<String, String> postData = parsePostBody(he);
	
	        String applianceIdStr = postData.get("applianceId");
	        if (applianceIdStr != null) {
	            try {
	                int applianceId = Integer.parseInt(applianceIdStr);
	
	                System.out.println("Added appliance ID " + applianceId + " to the basket.");
	
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid applianceId: " + applianceIdStr);
	            }
	        }
	
	        he.getResponseHeaders().set("Location", "/products");
	        he.sendResponseHeaders(303, -1);
	        he.close();
	    }
	
	    /**
	     * Parses the POST body of an HTTP request into a map of key-value pairs.
	     * This method assumes the POST body contains form data in the standard format:
	     * {@code key1=value1&key2=value2}. Both keys and values are URL-decoded.
	     *
	     * @param he the {@link HttpExchange} object representing the HTTP request.
	     * @return a map containing the key-value pairs from the POST body.
	     *         If the POST body is empty, an empty map is returned.
	     * @throws IOException if an I/O error occurs while reading the request body.
	     */
	    private Map<String, String> parsePostBody(HttpExchange he) throws IOException {
	        Map<String, String> result = new HashMap<>();
	
	        try (
	            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8);
	            BufferedReader br = new BufferedReader(isr)
	        ) {
	            StringBuilder sb = new StringBuilder();
	            String line;
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	            String[] pairs = sb.toString().split("&");
	            for (String pair : pairs) {
	                String[] kv = pair.split("=", 2);
	                if (kv.length == 2) {
	                    String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
	                    String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
	                    result.put(key, value);
	                }
	            }
	        }
	        return result;
	    }
	
	    /**
	     * Synchronises a list of home appliances with their corresponding appliance items.
	     * For each appliance item in the sorted list, it finds the matching home appliance
	     * (by comparing IDs) and builds a new list containing only the matching appliances.
	     *
	     * @param appliances the list of {@link HomeAppliance} objects to synchronize.
	     * @param sortedItems the sorted list of {@link ApplianceItem} objects to use for synchronization.
	     * @return a list of {@link HomeAppliance} objects that match the IDs of the appliance items in the sorted list.
	     *         If no match is found for an item, it is excluded from the resulting list.
	     */
	    private List<HomeAppliance> syncAppliancesWithItems(
	            List<HomeAppliance> appliances, List<ApplianceItem> sortedItems) {
	        return sortedItems.stream()
	                .map(item -> appliances.stream()
	                        .filter(ap -> ap.getId() == item.getHomeAppliance().getId())
	                        .findFirst()
	                        .orElse(null))
	                .filter(Objects::nonNull)
	                .collect(Collectors.toList());
	    }
	
	    /**
	     * Extracts the session ID from the "Cookie" header of an HTTP request.
	     * It searches for a cookie named "sessionId" and returns its value if found.
	     *
	     * @param exchange the {@link HttpExchange} object containing the HTTP request and headers.
	     * @return the session ID as a string if the "sessionId" cookie is present and valid;
	     *         returns null if no "sessionId" cookie is found or if the cookie header is missing.
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
	     * Parses a query string (e.g., "key1=value1&key2=value2") into a map of key-value pairs.
	     * Each key and value are URL-decoded to handle encoded characters such as spaces ("+") and special symbols.
	     * If a key has no associated value, it is added to the map with an empty string as the value.
	     *
	     * @param query the query string to parse (e.g., "key1=value1&key2=value2"); may be null or empty.
	     * @return a map containing the key-value pairs from the query string.
	     *         Returns an empty map if the input is null or empty.
	     */
	    private Map<String, String> parseQueryString(String query) {
	        Map<String, String> result = new HashMap<>();
	        if (query != null && !query.isEmpty()) {
	            String[] pairs = query.split("&");
	            for (String pair : pairs) {
	                String[] keyValue = pair.split("=", 2);
	                if (keyValue.length == 2) {
	                    String decodedKey = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
	                    String decodedValue = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
	                    result.put(decodedKey, decodedValue);
	                } else if (keyValue.length == 1) {
	                    // decode the key, value is empty
	                    String decodedKey = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
	                    result.put(decodedKey, "");
	                }
	            }
	        }
	        return result;
	    }

}
