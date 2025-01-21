package login.web;

import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handles user logout by ending their session and redirecting them to the home page.
 * This handler is responsible for clearing the session ID from the session manager
 * and invalidating the session cookie.
 * 
 * Features:
 * - Ends user sessions securely.
 * - Clears the session cookie to ensure proper logout.
 * - Redirects users to the home page after logout.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class LogoutHandler implements HttpHandler {
	
	/**
     * Default constructor for LogoutHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public LogoutHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles HTTP requests to log out a user.
     * Ends the user's session and clears their session cookie, then redirects to the home page.
     * 
     * @param exchange the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs during the process.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    	
        // Retrieve session ID from cookie
        String sessionId = getSessionIdFromCookie(exchange);

        // End the session if it exists
        if (sessionId != null) {
            LoginSessionManager.endSession(sessionId);
            clearSessionCookie(exchange);
        }

        // Redirect to the login page after logout
        exchange.getResponseHeaders().set("Location", "/");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
    
    /**
     * Retrieves the session ID from the user's cookies.
     * 
     * @param exchange the HttpExchange object containing the request headers.
     * @return the session ID if present, or `null` if not found.
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
     * Clears the session ID cookie by setting its value to empty and its max-age to 0.
     * This ensures that the browser removes the cookie immediately.
     * 
     * @param exchange the HttpExchange object to modify response headers.
     */
    private void clearSessionCookie(HttpExchange exchange) {
        // Set the 'sessionId' cookie to an empty value and max-age to 0 to delete it
        exchange.getResponseHeaders().add("Set-Cookie", "sessionId=; Path=/; HttpOnly; Max-Age=0");
    }
}