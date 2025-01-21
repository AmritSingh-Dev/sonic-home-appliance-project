package users.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import login.web.LoginSessionManager;
import users.Order;
import users.OrderDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * The ViewOrdersHandler class handles HTTP requests to display the logged-in user's order history.
 * It retrieves orders for the current user from the database and generates an HTML response 
 * displaying the order details in a table format.
 * 
 * Key Features:
 * - Ensures the user is authenticated before accessing the page.
 * - Retrieves the user's orders using the OrderDAO.
 * - Displays the order details, including Order ID, Total Price (with £), and Date.
 * - Redirects unauthenticated users to the login page.
 * - Provides a link to navigate back to the customer dashboard.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ViewOrdersHandler implements HttpHandler {

	/**
     * Default constructor for ViewOrdersHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ViewOrdersHandler() {
        // No specific initialisation required
    }
	
	/**
     * Handles the HTTP request to display the logged-in user's order history.
     * If the user is not logged in, they are redirected to the login page.
     * 
     * @param he the HttpExchange object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while writing the response.
     */
    @Override
    public void handle(HttpExchange he) throws IOException {
        String sessionId = getSessionIdFromCookie(he);
        LoginSessionManager.UserSession session = LoginSessionManager.getSession(sessionId);

        if (session == null) {
            he.getResponseHeaders().set("Location", "/login");
            he.sendResponseHeaders(302, 0);
            he.getResponseBody().close();
            return;
        }

        int userId = session.getUserId();
        OrderDAO orderDAO = new OrderDAO();
        List<Order> orders = orderDAO.getOrdersByUserId(userId);

        he.sendResponseHeaders(200, 0);

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody()))) {
            out.write(
                "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>My Orders</title>" +
                "    <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css'>" +
                "</head>" +
                "<body>" +
                "    <nav class='navbar navbar-expand-lg navbar-light bg-light'>" +
                "        <a class='navbar-brand' href='/'>Amrit's Home Appliance Store</a>" +
                "        <div class='collapse navbar-collapse'>" +
                "            <ul class='navbar-nav'>" +
                "                <li class='nav-item'><a class='nav-link' href='/products'>Products</a></li>" +
                "                <li class='nav-item'><a class='nav-link' href='/logout'>Logout</a></li>" +
                "            </ul>" +
                "            <span class='navbar-text ml-auto'>Logged in as: " + session.getUsername() + "</span>" +
                "        </div>" +
                "    </nav>" +
                "    <div class='container mt-5'>" +
                "        <h1 class='mb-4'>My Orders</h1>" +
                "        <table class='table table-striped'>" +
                "            <thead class='thead-dark'>" +
                "                <tr>" +
                "                    <th>Order ID</th>" +
                "                    <th>Total Price</th>" +
                "                    <th>Date</th>" +
                "                </tr>" +
                "            </thead>" +
                "            <tbody>");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (orders == null || orders.isEmpty()) {
                out.write("<tr><td colspan='3'>No orders found</td></tr>");
            } else {
                for (Order order : orders) {
                    String formattedDate = (order.getDate() != null) ? sdf.format(order.getDate()) : "N/A";
                    out.write(
                        "<tr>" +
                        "    <td>" + order.getOrderId() + "</td>" +
                        "    <td>£" + order.getTotalPrice() + "</td>" +
                        "    <td>" + formattedDate + "</td>" +
                        "</tr>");
                }
            }

            out.write(
                "            </tbody>" +
                "        </table>" +
                "        <a href='/customerdashboard' class='btn btn-secondary mt-3'>Back to Dashboard</a>" +
                "    </div>" +
                "</body>" +
                "</html>");
        }
    }

    /**
     * Retrieves the session ID from the "Cookie" header of the HTTP request.
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
