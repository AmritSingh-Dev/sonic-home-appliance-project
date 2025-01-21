package users.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

/**
 * Handles the display of the order confirmation page.
 * This handler is responsible for presenting a confirmation message to the user after a successful order placement.
 * 
 * Features:
 * - Displays a thank-you message for the user.
 * - Provides a link to continue shopping by redirecting the user back to the products page.
 * - Uses Bootstrap for styling to ensure a visually appealing interface.
 * 
 * HTML Output:
 * - A structured HTML page with a thank-you message and a "Continue Shopping" button.
 * 
 * Navigation:
 * - Redirects the user to the products page via the provided button.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class OrderConfirmationHandler implements HttpHandler {
	
	/**
     * Default constructor for OrderConfirmationHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public OrderConfirmationHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles the HTTP request to display the order confirmation page.
     * Generates an HTML response containing a confirmation message and a link to continue shopping.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while writing the response.
     */
    @Override
    public void handle(HttpExchange he) throws IOException {
    	
        he.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        he.sendResponseHeaders(200, 0);

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody(), "UTF-8"))) {
            out.write(
                "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <title>Order Confirmation</title>" +
                "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\" crossorigin=\"anonymous\">" +
                "</head>" +
                "<body>" +
                "    <div class='container mt-5'>" +
                "        <h1>Your Order Has Been Placed Successfully!</h1>" +
                "        <p>Thank you for your purchase. Your order will be processed shortly.</p>" +
                "        <a href='/products' class='btn btn-primary'>Continue Shopping</a>" +
                "    </div>" +
                "</body>" +
                "</html>"
            );
        }
    }
}
