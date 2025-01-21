package users.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import login.web.LoginSessionManager;

/**
 * Handles the HTTP request to display a form for updating a user's role.
 * This handler ensures that only administrators can access the page to update user roles.
 * The form allows the admin to select a new role (Admin or Customer) for a specific user and submit the changes.
 * 
 * Features:
 * - Verifies administrative privileges before granting access to the form.
 * - Extracts the user ID from the query parameters in the URL.
 * - Generates an HTML form to update the user's role.
 * - Redirects unauthorised users to the login page.
 * 
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class UpdateUserRoleHandler implements HttpHandler {
	
	/**
     * Default constructor for UpdateUserRoleHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public UpdateUserRoleHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles the HTTP request to display the role update form.
     * Validates that the user is logged in and has administrative privileges.
     * Generates an HTML form to update the role of a specific user.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while writing the response.
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
        String usrId = null;
        if (query != null && query.contains("id=")) {
        	usrId = query.split("=")[1];
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
            "<h1>Update User Role</h1>" +
            "<form method=\"POST\" action=\"/processupdateuserrole\">" +
            "    <input type=\"hidden\" name=\"id\" value=\"" + usrId + "\" />" + // Hidden field for the product ID
            "    <div class=\"form-group\">" +
            "    <div class=\"form-group\">" +
            "        <label for=\"name\">Select New Role:</label>" +
            "        <select id=\"role\" name=\"role\" class=\"form-control\" required>" +
            "		     <option value=\"Admin\">Admin</option>" +
            "		     <option value=\"Customer\">Customer</option>" +
            "        </select>" +
            "    </div>" +
            "    <button type=\"submit\" class=\"btn btn-primary\">Update Role</button>" +
            "</form>" +
            "</div>" +
            "</body>" +
            "</html>"
        );

        out.close();
    }
    
    /**
     * Retrieves the session ID from the HTTP request cookies.
     * 
     * @param exchange the HttpExchange object representing the HTTP request and response.
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