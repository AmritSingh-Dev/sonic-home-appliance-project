package homeappliance.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import homeappliance.ApplianceItem;
import homeappliance.ApplianceItemDAO;
import homeappliance.Basket;
import login.web.LoginSessionManager;
import users.Order;
import users.OrderDAO;
import users.UsersDAO;

/**
 * The BasketHandler class handles HTTP requests related to the shopping basket.
 * It manages the basket's content, supports adding and removing items, and 
 * facilitates the checkout process by placing an order and clearing the 
 * basket upon successful checkout.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class BasketHandler implements HttpHandler {
	
	/**
     * Default constructor for BasketHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public BasketHandler() {
        // No specific initialisation required
    }

    private final ApplianceItemDAO applianceItemDAO = new ApplianceItemDAO();

    /**
     * Handles HTTP requests for managing a user's shopping basket.
     *
     * This method processes both GET and POST requests:
     * - GET: Displays the current contents of the user's basket.
     * - POST: Handles actions like adding items to the basket, removing items, or checking out.
     *
     * If no basket is associated with the user's session, a 400 Bad Request response is sent.
     *
     * @param he the {@link HttpExchange} object representing the HTTP request and response
     * @throws IOException if an I/O error occurs while processing the request or generating the response
     */
    @Override
    public void handle(HttpExchange he) throws IOException {
    	
        // Get the session & basket
        String sessionId = getSessionIdFromCookie(he);
        LoginSessionManager.UserSession session = LoginSessionManager.getSession(sessionId);
        Basket basket = (session != null) ? session.getBasket() : null;

        if (basket == null) {
            he.sendResponseHeaders(400, -1);  // 400 Bad Request, no basket
            he.close();
            return;
        }

        // Handle POST (add to basket, remove item, or checkout)
        if ("POST".equalsIgnoreCase(he.getRequestMethod())) {
            handlePostRequest(he, session, basket);
            return;
        }

        // Otherwise handle GET = show current basket
        handleGetRequest(he, basket);
    }

    /**
     * Handles POST requests for managing the shopping basket.
     *
     * This method processes various POST actions, including:
     * - Reading POST data and parsing query parameters.
     * - Checking out the basket if the "checkout" parameter is present:
     *   - Creates a new order based on the basket contents.
     *   - Saves the order to the database.
     *   - Clears the basket after successful order creation.
     *   - Redirects the user to the order confirmation page.
     *
     * @param he the {@link HttpExchange} object representing the HTTP request and response
     * @param session the user's session containing basket and user-related data
     * @param basket the {@link Basket} object representing the user's current shopping basket
     * @throws IOException if an I/O error occurs while processing the request or generating the response
     */
    private void handlePostRequest(HttpExchange he, LoginSessionManager.UserSession session, Basket basket)
            throws IOException {
        String requestBody = readRequestBody(he);
        Map<String, String> params = parseQueryString(requestBody);

        if (params.containsKey("checkout")) {
            int totalPrice = basket.getTotalPrice();
            int userId = session.getUserId();

            Order order = new Order(0, new UsersDAO().findUser(userId), totalPrice, null);
            OrderDAO orderDAO = new OrderDAO();

            if (orderDAO.addOrder(order)) {
                basket.clear();

                he.getResponseHeaders().add("Location", "/orderconfirmation");
                he.sendResponseHeaders(302, -1);  
                he.close();
                return;
            }
        }

        if (params.containsKey("applianceId")) {
            try {
                int applianceId = Integer.parseInt(params.get("applianceId"));
                ApplianceItem item = applianceItemDAO.findApplianceItem(applianceId);
                if (item != null) {
                    basket.addItem(item);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid applianceId: " + params.get("applianceId"));
            }
        }

        if (params.containsKey("removeId")) {
            try {
                int removeId = Integer.parseInt(params.get("removeId"));
                basket.removeItem(removeId);
            } catch (NumberFormatException e) {
                System.err.println("Invalid removeId: " + params.get("removeId"));
            }
        }

        he.getResponseHeaders().add("Cache-Control", "no-cache, no-store, must-revalidate");
        he.getResponseHeaders().add("Location", "/basket");
        he.sendResponseHeaders(302, -1); 
        he.close(); 
    }

    /**
     * Handles GET requests to display the current contents of the user's shopping basket.
     *
     * This method generates an HTML response showing:
     * - The list of items currently in the user's basket, including details such as ID, brand, model, price, and quantity.
     * - The total price of items in the basket.
     * - Actions to remove individual items or proceed to checkout.
     * - Links for continuing shopping or placing an order.
     *
     * If the basket is empty, it shows a message indicating that the basket is empty.
     *
     * @param he the {@link HttpExchange} object representing the HTTP request and response
     * @param basket the {@link Basket} object representing the user's current shopping basket
     * @throws IOException if an I/O error occurs while processing the request or generating the response
     */
    private void handleGetRequest(HttpExchange he, Basket basket) throws IOException {
        he.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        he.sendResponseHeaders(200, 0);

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(he.getResponseBody(), StandardCharsets.UTF_8))) {
            out.write(
                "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <title>Your Basket</title>" +
                "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\" crossorigin=\"anonymous\">" +
                "</head>" +
                "<body>" +
                "    <nav class='navbar navbar-expand-lg navbar-light bg-light'>" +
                "        <a class='navbar-brand' href='/'>Appliance Store</a>" +
                "        <button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarNav' aria-controls='navbarNav' aria-expanded='false' aria-label='Toggle navigation'>" +
                "            <span class='navbar-toggler-icon'></span>" +
                "        </button>" +
                "        <div class='collapse navbar-collapse' id='navbarNav'>" +
                "            <ul class='navbar-nav'>" +
                "                <li class='nav-item'><a class='nav-link' href='/products'>Products</a></li>" +
                "                <li class='nav-item'><a class='nav-link' href='/basket'>Basket</a></li>" +
                "                <li class='nav-item'><a class='nav-link' href='/logout'>Logout</a></li>" +
                "            </ul>" +
                "        </div>" +
                "    </nav>" +
                "    <div class='container mt-5'>" +
                "        <h1>Your Basket</h1>"
            );

            Map<ApplianceItem, Integer> quantities = basket.getItemsWithQuantities();
            ArrayList<ApplianceItem> items = new ArrayList<>(quantities.keySet());
            int totalPrice = basket.getTotalPrice();

            if (items.isEmpty()) {
                out.write("<p>Your basket is empty.</p>");
            } else {
                out.write(
                    "<table class='table table-bordered'>" +
                    "    <thead class='thead-light'>" +
                    "        <tr>" +
                    "            <th>ID</th>" +
                    "            <th>Brand</th>" +
                    "            <th>Model</th>" +
                    "            <th>Price</th>" +
                    "            <th>Quantity</th>" +
                    "            <th>Action</th>" +
                    "        </tr>" +
                    "    </thead>" +
                    "    <tbody>"
                );

                for (ApplianceItem item : items) {
                    int quantity = quantities.get(item);
                    out.write(String.format(
                        "        <tr>" +
                        "            <td>%d</td>" +
                        "            <td>%s</td>" +
                        "            <td>%s</td>" +
                        "            <td>£%d</td>" +
                        "            <td>%d</td>" +
                        "            <td>" +
                        "                <form method='post' action='/basket' style='display:inline;'>" +
                        "                    <input type='hidden' name='removeId' value='%d' />" +
                        "                    <button type='submit' class='btn btn-danger btn-sm'>Remove</button>" +
                        "                </form>" +
                        "            </td>" +
                        "        </tr>",
                        item.getId(), item.getBrand(), item.getModel(),
                        item.getHomeAppliance().getPrice(), quantity,
                        item.getId()
                    ));
                }

                out.write("    </tbody></table>");
                out.write(String.format("<h3>Total Price: £%d</h3>", totalPrice));
            }

            out.write(
                "        <a href='/products' class='btn btn-secondary'>Continue Shopping</a>" +
                "        <form method='post' action='/basket' style='display:inline;'>" +
                "            <button type='submit' class='btn btn-success' name='checkout'>Place Order</button>" +
                "        </form>" +
                "    </div>" +
                "</body></html>"
            );
        }
    }

    /**
     * Reads the body of an HTTP request and returns it as a string.
     *
     * This method reads the raw data sent in the request body using UTF-8 encoding
     * and appends it line by line into a {@link StringBuilder}.
     *
     * Useful for handling POST requests where the body contains form data or JSON payloads.
     *
     * @param he the {@link HttpExchange} object representing the HTTP request
     * @return a {@link String} containing the entire body of the HTTP request
     * @throws IOException if an I/O error occurs while reading the request body
     */
    private String readRequestBody(HttpExchange he) throws IOException {
        try (
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr)
        ) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    /**
     * Extracts the session ID from the "Cookie" header of an HTTP request.
     *
     * This method searches for a cookie named "sessionId" in the "Cookie" header
     * and returns its value if found. If no "sessionId" cookie is present, or if
     * the "Cookie" header is missing, it returns {@code null}.
     *
     * Useful for managing user sessions in web applications.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request
     * @return the session ID as a {@link String} if the "sessionId" cookie is found;
     *         {@code null} if no such cookie exists or if the header is missing
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
     * This method processes a query string in the format {@code key1=value1&key2=value2}
     * and converts it into a {@link Map} where each key corresponds to a parameter name,
     * and each value corresponds to the parameter's value. If a parameter has no value
     * (e.g., {@code key1=}), it is assigned an empty string.
     *
     *
     * @param query the query string to parse (e.g., {@code "key1=value1&key2=value2"}); 
     *              may be {@code null} or empty
     * @return a {@link Map} containing the parsed key-value pairs; returns an empty map
     *         if the input is {@code null} or empty
     */
    private Map<String, String> parseQueryString(String query) {
        Map<String, String> result = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                String key = keyValue[0];
                String value = "";
                if (keyValue.length == 2) {
                    // URL-decode if needed
                    value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                }
                result.put(key, value);
            }
        }
        return result;
    }
}
