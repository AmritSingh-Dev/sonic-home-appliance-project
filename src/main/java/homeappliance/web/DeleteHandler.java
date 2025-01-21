package homeappliance.web;

import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import homeappliance.HomeApplianceDAO;
import login.web.LoginSessionManager;
import homeappliance.HomeAppliance;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * The DeleteHandler class handles deleting products.
 * This handler validates that the user is logged in as an Admin and deletes a product from the database 
 * based on the provided product ID in the query string.
 * 
 * Features:
 * - Verifies user session and admin role before proceeding.
 * - Extracts product ID from query parameters to locate and delete the product.
 * - Displays a confirmation message if the product is successfully deleted, including product details.
 * - Provides error handling and displays appropriate error messages for invalid or missing product data.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class DeleteHandler implements HttpHandler {
	
	/**
     * Default constructor for DeleteHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public DeleteHandler() {
        // No specific initialisation required
    }
	
	/**
     * Handles HTTP requests for deleting a product.
     * Validates the user's session and role, extracts the product ID from the query string, 
     * deletes the product from the database, and displays the result to the user.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while handling the request or response.
     */
    @Override
    public void handle(HttpExchange he) throws IOException {
    	String sessionId = getSessionIdFromCookie(he);
        LoginSessionManager.UserSession session = LoginSessionManager.getSession(sessionId);

        // Check if user is logged in and is an admin
        if (session == null || !"Admin".equals(session.getRole())) {
            // User is not an admin or not logged in, redirect to login page
            he.getResponseHeaders().set("Location", "/login");
            he.sendResponseHeaders(302, 0); // 302 is the status code for redirection
            he.getResponseBody().close();
            return;
        }
        
        he.sendResponseHeaders(200, 0);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));

        try {
            Map<String, String> params = parseQueryString(he.getRequestURI().getQuery());
            int id = Integer.parseInt(params.get("id"));

            HomeApplianceDAO dao = new HomeApplianceDAO();

            HomeAppliance deletedProduct = dao.findProduct(id);
            boolean success = dao.deleteProduct(id);

            if (success && deletedProduct != null) {
                out.write(
                    "<html>" +
                    "<head> <title>Home Appliance Store</title>" +
                    "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\" " +
                    "integrity=\"sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\" crossorigin=\"anonymous\">" +
                    "</head>" +
                    "<body>" +
                    "<h1>Product Deleted Successfully</h1>" +
                    "<table class=\"table\">" +
                    "<thead>" +
                    "  <tr>" +
                    "    <th>ID</th>" +
                    "    <th>SKU</th>" +
                    "    <th>Description</th>" +
                    "    <th>Category</th>" +
                    "    <th>Price</th>" +
                    "  </tr>" +
                    "</thead>" +
                    "<tbody>" +
                    "  <tr>" +
                    "    <td>" + deletedProduct.getId() + "</td>" +
                    "    <td>" + deletedProduct.getSku() + "</td>" +
                    "    <td>" + deletedProduct.getDescription() + "</td>" +
                    "    <td>" + deletedProduct.getCategory() + "</td>" +
                    "    <td>" + deletedProduct.getPrice() + "</td>" +
                    "  </tr>" +
                    "</tbody>" +
                    "</table>" +
                    "<a href=\"/\">Back to Menu</a>" +
                    "</body>" +
                    "</html>");
                out.close();
            } else {
                // If deletion failed or product was not found
                out.write(
                    "<html>" +
                    "<head><title>Error</title></head>" +
                    "<body>" +
                    "<h1>Failed to delete the product. Product not found or an error occurred.</h1>" +
                    "<a href=\"/\">Back to Menu</a>" +
                    "</body>" +
                    "</html>");
                out.close();
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            out.write(
                "<html>" +
                "<head><title>Error</title></head>" +
                "<body>" +
                "<h1>Error processing request</h1>" +
                "<p>" + ex.getMessage() + "</p>" +
                "<a href=\"/\">Back to Menu</a>" +
                "</body>" +
                "</html>"
            );
        
            out.close();
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
     * Parses a query string into a map of key-value pairs.
     * 
     * @param query the query string from the URL.
     * @return a Map containing query parameters as key-value pairs.
     */
    private Map<String, String> parseQueryString(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1) {
                result.put(keyValue[0], "");
            }
        }
        return result;
    }
}