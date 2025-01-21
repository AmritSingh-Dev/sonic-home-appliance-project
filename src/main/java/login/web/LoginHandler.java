package login.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * The LoginHandler class is responsible for displaying the login page.
 * This handler serves the login form as an HTML page to the user which is stylised with bootstrap.
 * 
 * Login Details (Passwords saved as hashed strings):
 * ADMIN Username: admin | Password: 123456
 * CUSTOMER Username: northernsteakhouse | 123456
 * All user details can be viewed via the menu system. Passwords for all users is 123456.
 * 
 * Features:
 * - Displays a login form with fields for username and password.
 * - Provides navigation links to other parts of the application, such as the home page and product page.
 * - The form submission is processed by `/processlogin` (ProcessLoginHandler).
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class LoginHandler implements HttpHandler {
	
	/**
     * Default constructor for LoginHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public LoginHandler() {
        // No specific initialisation required
    }
	
	
	/**
     * Handles HTTP GET requests to serve the login page.
     * Generates an HTML response containing the login form and navigation links.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while writing the response.
     */
    public void handle(HttpExchange he) throws IOException {
        he.sendResponseHeaders(200, 0);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()));

        out.write(
        	    "<!DOCTYPE html>" +
        	    "<html lang='en'>" +
        	    "<head>" +
        	    "    <meta charset='UTF-8'>" +
        	    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
        	    "    <title>Login</title>" +
        	    "    <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css'>" +
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
	            "                    <a class='nav-link' href='/'>Home</a>" +
	            "                </li>" +
	            "                <li class='nav-item active'>" +
	            "                    <a class='nav-link' href='/products'>Products</a>" +
	            "                </li>" +
	            "                <li class='nav-item'>" +
                "                    <a class='nav-link' href='/login'>Login</a>" +
                "                </li>" +
                "                <li class='nav-item'>" +
                "                    <a class='nav-link' href='/signup'>Sign Up</a>" +
                "                </li>" +
	            "            </ul>" +
	            "        </div>" +
	            "    </nav>" +
        	    "    <div class='container mt-5'>" +
        	    "        <h2>Login</h2>" +
        	    "        <form method='POST' action='/processlogin'>" +
        	    "            <div class='form-group'>" +
        	    "                <label for='username'>Username:</label>" +
        	    "                <input type='text' id='username' name='username' class='form-control' required>" +
        	    "            </div>" +
        	    "            <div class='form-group'>" +
        	    "                <label for='password'>Password:</label>" +
        	    "                <input type='password' id='password' name='password' class='form-control' required>" +
        	    "            </div>" +
        	    "            <button type='submit' class='btn btn-primary'>Login</button>" +
        	    "        </form>" +
        	    "    </div>" +
        	    "</body>" +
        	    "</html>"
        	);
        	out.close();
    }
}