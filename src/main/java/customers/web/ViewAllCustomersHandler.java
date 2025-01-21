package customers.web;

import java.io.OutputStreamWriter;
import com.sun.net.httpserver.HttpHandler;
import customers.Customer;
import customers.CustomerDAO;
import com.sun.net.httpserver.HttpExchange;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.IOException;
import login.web.LoginSessionManager;

/**
 * Handles HTTP requests to display a list of all customers.
 * Only accessible by administrators. Redirects unauthorised users to the login page.
 * Generates an HTML table displaying customer details with options for updating or deleting customers.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ViewAllCustomersHandler implements HttpHandler{
	
	 /**
     * Default constructor for ViewAllCustomersHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ViewAllCustomersHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles the HTTP request to display a list of customers.
     * Checks if the user has administrative privileges before proceeding.
     * Generates an HTML page with a table of customer details and admin options.
     * 
     * @param he the HttpExchange object containing details about the request and response.
     * @throws IOException if an I/O error occurs during processing.
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
		  	
		    he.sendResponseHeaders(200,0);
		    BufferedWriter out = new BufferedWriter(  
		        new OutputStreamWriter(he.getResponseBody() ));
		    
		    CustomerDAO customers = new CustomerDAO();
		    try{
			    ArrayList<Customer> cust = customers.findAllCustomers();
			   
		    out.write(
		    	    "<!DOCTYPE html>" +
		    	    "<html lang='en'>" +
		    	    "<head>" +
		    	    "    <meta charset='UTF-8'>" +
		    	    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
		    	    "    <title>Customers</title>" +
		    	    "    <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css' integrity='sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2' crossorigin='anonymous'>" +
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
		    	    "    <div class='container mt-5'>" +
		    	    "        <h1 class='mb-4'>Customers</h1>" +
		    	    "        <table class='table table-striped'>" +
		    	    "            <thead class='thead-dark'>" +
		    	    "                <tr>" +
		    	    "                    <th>Customer ID</th>" +
		    	    "                    <th>Business Name</th>" +
		    	    "                    <th>Address</th>" +
		    	    "                    <th>Telephone Number</th>" +
		    	    "                    <th>Email Address</th>" +
		    	    "                    <th>Admin Options</th>" +
		    	    "                </tr>" +
		    	    "            </thead>" +
		    	    "            <tbody>");
	
		    	for (Customer d : cust) {
		    	    out.write(
		    	        "<tr>" +
		    	        "    <td>" + d.getCustomerID() + "</td>" +
		    	        "    <td>" + d.getBusinessName() + "</td>" +
		    	        "    <td>" + d.getAddress() + "</td>" +
		    	        "    <td>" + d.getTelephoneNumber() + "</td>" +
		    	        "    <td>" + d.getEmailAddress() + "</td>" +
		    	        "    <td>" +
		    	        "        <a href='/deletecustomer?id=" + d.getCustomerID() + "' class='btn btn-danger btn-sm'>Delete</a> " + // Styled as a red button for delete
		    	        "        <a href='/updatecustomer?id=" + d.getCustomerID() + "' class='btn btn-primary btn-sm'>Update</a> " + // Styled as a blue button for update
		    	        "    </td>" +
		    	        "</tr>"
		    	    );
		    	}
	
		    	out.write(
		    	    "</tbody>" +
		    	    "</table>" +
		    	    "<a href='/' class='btn btn-secondary mt-3'>Back to Menu</a>" + // Consistent button styling
		    	    "</div>" +
		    	    "</body>" +
		    	    "</html>");
	     }catch(IOException ex){
	    	 ex.printStackTrace();
	    }
	    out.close();
	
	  }
	  
	    /**
	     * Retrieves the session ID from the HTTP cookies.
	     * 
	     * @param exchange the HttpExchange object containing the request details.
	     * @return the session ID if found, otherwise null.
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