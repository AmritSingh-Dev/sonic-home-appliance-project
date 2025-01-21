package homeappliance.web;

import com.sun.net.httpserver.HttpServer;
import customers.web.ViewAllCustomersHandler;
import login.web.AdminDashboardHandler;
import login.web.CustomerDashboardHandler;
import login.web.LoginHandler;
import login.web.ProcessLoginHandler;
import login.web.ProcessSignupHandler;
import login.web.SignupHandler;
import users.web.DeleteUserHandler;
import users.web.OrderConfirmationHandler;
import users.web.ProcessUpdateUserRoleHandler;
import users.web.UpdateUserRoleHandler;
import users.web.ViewAllUsersHandler;
import users.web.ViewOrdersHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import customers.web.ProcessAddCustomerHandler;
import customers.web.ProcessUpdateCustomerHandler;
import customers.web.UpdateCustomerHandler;
import customers.web.AddCustomerHandler;
import customers.web.DeleteCustomerHandler;
import login.web.LogoutHandler;

/**
 * It sets up the server, connects it to a specified port, and registers handlers
 * for various endpoints. Each endpoint maps to a specific handler class
 * The Main class serves as the entry point for the HTTP server application.
 * responsible for processing HTTP requests and generating responses.
 * 
 * Key functionalities of the Main class:
 * - Initialises an HTTP server on a specified port (default: 8080).
 * - Registers contexts for various endpoints to handle HTTP requests.
 * - Starts the server and begins listening for incoming connections.
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class Main {
	
	/**
     * Default constructor for Main.
     * Initialises a new instance of the class without any additional setup.
     */
    public Main() {
        // No specific initialisation required
    }
	
	/** Port number on which the server listens. */
	 static final private int PORT = 8080;

	 /**
     * The main method initialises and starts the HTTP server.
     * Registers all the handlers for various endpoints.
     * 
     * 
     * @param args command-line arguments (not used).
     * @throws IOException if the server fails to start or bind to the port.
     */
	  public static void main(String[] args) throws IOException {

	    HttpServer server = HttpServer.create(new InetSocketAddress(PORT),0);
	    server.createContext("/", new RootHandler() ); 
	    server.createContext("/products", new ViewAllProductsHandler() );
	    server.createContext("/delete", new DeleteHandler() );
	    server.createContext("/updateproduct", new UpdateProductHandler() );
	    server.createContext("/processupdateproduct", new ProcessUpdateProductHandler() ); 
	    server.createContext("/addproduct", new AddProductHandler());
	    server.createContext("/processaddproduct", new ProcessAddProductHandler());
	    server.createContext("/basket", new BasketHandler());
	    
	    server.createContext("/customers", new ViewAllCustomersHandler());
	    server.createContext("/deletecustomer", new DeleteCustomerHandler());
	    server.createContext("/updatecustomer", new UpdateCustomerHandler());
	    server.createContext("/processupdatecustomer", new ProcessUpdateCustomerHandler());
	    server.createContext("/addcustomer", new AddCustomerHandler());
	    server.createContext("/processaddcustomer", new ProcessAddCustomerHandler());
	    
	    server.createContext("/login", new LoginHandler());
	    server.createContext("/processlogin", new ProcessLoginHandler());
	    server.createContext("/admindashboard", new AdminDashboardHandler());
	    server.createContext("/customerdashboard", new CustomerDashboardHandler());
	    server.createContext("/logout", new LogoutHandler());
	    server.createContext("/signup", new SignupHandler());
	    server.createContext("/processsignup", new ProcessSignupHandler());
	    
	    server.createContext("/users", new ViewAllUsersHandler());
	    server.createContext("/updateuserrole", new UpdateUserRoleHandler());
	    server.createContext("/processupdateuserrole", new ProcessUpdateUserRoleHandler());
	    server.createContext("/deleteuser", new DeleteUserHandler());
	    server.createContext("/orderconfirmation", new OrderConfirmationHandler());
	    server.createContext("/orders", new ViewOrdersHandler());


	    
	    server.setExecutor(null);
	    server.start();
	    System.out.println("The server is listening on port " + PORT);

	    
	  }

}
