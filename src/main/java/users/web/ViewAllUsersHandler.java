package users.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import login.web.LoginSessionManager;
import users.Users;
import users.UsersDAO;

/**
 * Handles HTTP requests to display a list of all users in the system.
 * This handler is restricted to administrators and provides options to manage users, such as updating roles or deleting accounts.
 * 
 * Features:
 * - Verifies that the user is logged in as an admin.
 * - Retrieves all users from the database and displays them in a table format.
 * - Provides admin options for each user (e.g., delete or update role).
 * - Redirects unauthorised users to the login page.
 * 
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ViewAllUsersHandler implements HttpHandler{
	
	/**
     * Default constructor for ViewAllUsersHandler.
     * Initialises a new instance of the handler without any additional setup.
     */
    public ViewAllUsersHandler() {
        // No specific initialisation required
    }
	
    /**
     * Handles the HTTP request to display all users.
     * Validates that the current user is an admin and fetches the list of users from the database.
     * Generates an HTML table with user details and admin options.
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
		  	
		    he.sendResponseHeaders(200,0);
		    BufferedWriter out = new BufferedWriter(  
		        new OutputStreamWriter(he.getResponseBody() ));
		    
		    UsersDAO users = new UsersDAO();
		    try{
			    ArrayList<Users> user = users.findAllUsers();
			   
			    out.write(
			    	    "<!DOCTYPE html>" +
			    	    "<html lang='en'>" +
			    	    "<head>" +
			    	    "    <meta charset='UTF-8'>" +
			    	    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
			    	    "    <title>Users</title>" +
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
			    	    "        <h1 class='mb-4'>Users</h1>" +
			    	    "        <table class='table table-striped'>" +
			    	    "            <thead class='thead-dark'>" + 
			    	    "                <tr>" +
			    	    "                    <th>User ID</th>" +
			    	    "                    <th>Username</th>" +
			    	    "                    <th>Crypted Password</th>" +
			    	    "                    <th>Role</th>" +
			    	    "                    <th>Admin Options</th>" +
			    	    "                </tr>" +
			    	    "            </thead>" +
			    	    "            <tbody>");
		
			    	for (Users d : user) {
			    	    out.write(
			    	        "<tr>" +
			    	        "    <td>" + d.getUserId() + "</td>" +
			    	        "    <td>" + d.getUsername() + "</td>" +
			    	        "    <td>" + d.getPassword() + "</td>" +
			    	        "    <td>" + d.getRole() + "</td>" +
			    	        "    <td>" +
			    	        "        <a href='/deleteuser?id=" + d.getUserId() + "' class='btn btn-danger btn-sm'>Delete</a> " + 
			    	        "        <a href='/updateuserrole?id=" + d.getUserId() + "' class='btn btn-primary btn-sm'>Update Role</a> " + 
			    	        "    </td>" +
			    	        "</tr>"
			    	    );
			    	}
		
			    	out.write(
			    	    "</tbody>" +
			    	    "</table>" +
			    	    "<a href='/' class='btn btn-secondary mt-3'>Back to Menu</a>" +
			    	    "</div>" +
			    	    "</body>" +
			    	    "</html>");
		     }catch(IOException ex){
		    	 ex.printStackTrace();
		    }
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
