package homeappliance.web;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import homeappliance.HomeApplianceDAO;
import login.web.LoginSessionManager;
import homeappliance.HomeAppliance;

/**
 * The ProcessAddProductHandler class processes HTTP POST requests to add a new product.
 * This handler verifies the user's session and role, extracts form data from the request,
 * and adds the new product to the database. It supports adding a new category or using an existing category.
 *
 * Features:
 * - Validates that the user is logged in and is an admin.
 * - Parses the form data sent in the request body.
 * - Adds a new product to the database with dynamic or predefined categories.
 * - Displays a success or failure message after processing the request.
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ProcessAddProductHandler implements HttpHandler {
	
	/**
     * Default constructor for ProcessAddProductHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ProcessAddProductHandler() {
        // No specific initialisation required
    }
	
	/**
     * Handles the HTTP request for processing a new product addition.
     * Verifies user authentication, parses form data, and adds the product to the database.
     * Responds with a success or failure message.
     *
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while handling the request or response.
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

        try {
            Map<String, String> formData = parseFormData(input.toString());
            
            // Extract individual fields
            String sku = formData.get("sku");
            String description = formData.get("description");
            String category = formData.get("category");
            String newCategory = formData.get("newCategory");
            int price = Integer.parseInt(formData.get("price"));

            // Use newCategory if provided, otherwise use the selected category
            if ("new".equals(category) && newCategory != null && !newCategory.isEmpty()) {
                category = newCategory;
            }

            // Create the HomeAppliance object
            HomeAppliance newProduct = new HomeAppliance(sku, description, category, price);

            // Insert into the database
            HomeApplianceDAO dao = new HomeApplianceDAO();
            boolean success = dao.addProduct(newProduct);

            // Prepare the response
            String response = success ? "Product added successfully!" : "Failed to add product.";

            he.sendResponseHeaders(200, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));
            out.write("<html><body><h1>" + response + "</h1><a href=\"/products\">View Products</a></body></html>");
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    /**
     * Parses form data and formats into a map of key-value pairs.
     *
     * @param formData the raw form data as a String.
     * @return a Map containing the parsed key-value pairs.
     */
    private Map<String, String> parseFormData(String formData) {
        Map<String, String> result = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
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