package login.web;

import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Handles the HTTP GET requests for the Signup page.
 * This handler serves an HTML signup form for new customers to register their details.
 * The form submission is processed by the `/processsignup` endpoint.
 * 
 * Features:
 * - Displays a user-friendly signup form styled with Bootstrap.
 * - Captures customer details such as business name, address, telephone, email, and login credentials.
 * - The form is submitted via POST to the `/processsignup` handler.
 * 
 * Navigation:
 * - The top navigation bar provides a link to the home page.
 * - A clear and responsive layout for ease of use on various devices.
 * 
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class SignupHandler implements HttpHandler {
	
	/**
     * Default constructor for SignupHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public SignupHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles HTTP GET requests to serve the signup page.
     * Generates an HTML response containing a signup form for new customers.
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
                "    <title>Signup</title>" +
                "    <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css'>" +
                "</head>" +
                "<body>" +
                "    <nav class='navbar navbar-expand-lg navbar-light bg-light'>" +
                "        <a class='navbar-brand' href='/'>Home Appliance Store</a>" +
                "    </nav>" +
                "    <div class='container mt-5'>" +
                "        <h1>Signup</h1>" +
                "        <form method='POST' action='/processsignup'>" +
                "            <h3>Customer Details</h3>" +
                "            <div class='form-group'>" +
                "                <label for='name'>Business Name:</label>" +
                "                <input type='text' id='name' name='name' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='addressLine0'>Street Address:</label>" +
                "                <input type='text' id='addressLine0' name='addressLine0' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='addressLine1'>Town:</label>" +
                "                <input type='text' id='addressLine1' name='addressLine1' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='addressLine2'>City:</label>" +
                "                <input type='text' id='addressLine2' name='addressLine2' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='country'>Country:</label>" +
                "                <input type='text' id='country' name='country' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='postCode'>Postcode:</label>" +
                "                <input type='text' id='postCode' name='postCode' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='telephone'>Telephone Number:</label>" +
                "                <input type='text' id='telephone' name='telephone' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='email'>Contact Email Address:</label>" +
                "                <input type='email' id='email' name='email' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='username'>Login Username:</label>" +
                "                <input type='text' id='username' name='username' class='form-control' required>" +
                "            </div>" +
                "            <div class='form-group'>" +
                "                <label for='password'>Password:</label>" +
                "                <input type='password' id='password' name='password' class='form-control' required>" +
                "            </div>" +
                "            <button type='submit' class='btn btn-primary'>Signup</button>" +
                "        </form>" +
                "    </div>" +
                "</body>" +
                "</html>"
            );
            out.close();
    }
}