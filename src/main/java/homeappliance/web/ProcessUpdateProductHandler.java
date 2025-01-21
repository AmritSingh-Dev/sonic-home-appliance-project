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
 * The ProcessUpdateProductHandler class handles HTTP POST requests to update an existing product.
 * This handler validates the user's session and role, extracts form data, and updates the product details
 * in the database. It ensures only admin users can perform this operation.
 *
 * Features:
 * - Validates that the user is logged in and has admin privileges.
 * - Parses form data from the request body to extract product details.
 * - Updates the product information in the database.
 * - Displays a success or failure message after processing the request.
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ProcessUpdateProductHandler implements HttpHandler {
	
	/**
     * Default constructor for ProcessUpdateProductHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ProcessUpdateProductHandler() {
        // No specific initialisation required
    }
	
	
    /**
     * Handles the HTTP request for updating a product.
     * Validates the user as an admin, parses form data, updates the product in the database,
     * and responds with a success or failure message.
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

        // Parse the form data into a Map
        try {
            Map<String, String> formData = parseFormData(input.toString());
            
            // Extract individual fields
            int id = Integer.parseInt(formData.get("id"));
            String sku = formData.get("sku");
            String description = formData.get("description");
            String category = formData.get("category");
            int price = Integer.parseInt(formData.get("price"));
            
            
            // Create the HomeAppliance object
            HomeAppliance updatedProduct = new HomeAppliance(sku, description, category, price);
            updatedProduct.setId(id);
            
            // Update the database record
            HomeApplianceDAO dao = new HomeApplianceDAO();
            boolean success = dao.updateProduct(updatedProduct);
            
            String response = success ? "Product updated successfully!" : "Failed to update product.";
            
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
     * @throws UnsupportedEncodingException if the encoding for URL decoding is not supported.
     */
    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
            	String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8"); // Decode the value
                result.put(key, value);
            }
        }
        return result;
    }
}
