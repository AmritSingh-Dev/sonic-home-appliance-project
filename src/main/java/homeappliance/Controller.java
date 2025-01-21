package homeappliance;

import java.util.Scanner;
import customers.Customer;
import customers.Address;
import customers.CustomerDAO;
import users.Users;
import users.UsersDAO;

import java.util.ArrayList;

/**
 * The Controller class is the main application menu system that provides a menu-driven interface 
 * for managing home appliances, customers, users, and appliance items. 
 * It interacts with the DAO classes to perform CRUD operations on the SQLite database 
 * and provides various menu options for user interactions.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class Controller {
	
	private HomeApplianceDAO homeApplianceDAO;
	private CustomerDAO customerDAO;
	private UsersDAO usersDAO;
	private ApplianceItemDAO applianceItemDAO;
	Scanner in = new Scanner(System.in);


	/**
     * Constructor initialises the various DAO objects for managing different database tables.
     */
	public Controller() {
		homeApplianceDAO = new HomeApplianceDAO();
		customerDAO = new CustomerDAO();
		usersDAO = new UsersDAO();
		applianceItemDAO = new ApplianceItemDAO();
	}
	
    /**
     * Displays the main menu system and allows users to navigate through product, customer, and user menus.
     */
	public void menuSystem() {
		String selection;  //Variable to hold the user's menu choice input.
		boolean exit = false;
		 do {   //Do-while loop which repeatedly shows the menu and process user inputs until the user exit's the menu
			 System.out.println("----------------------------");
			 System.out.println("Amrit's Home Appliance Store");
			 System.out.println("Choose from these options");
			 System.out.println("----------------------------");
			 System.out.println("[1] Product Menu");
			 System.out.println("[2] Customer Menu");
			 System.out.println("[3] User Menu");
			 System.out.println("[4] Exit");
			 selection = in.nextLine();
			 
			 
	        switch (selection) {
	            case "1": 
	                productMenu();
	                break;

	            case "2": 
	                customerMenu();
	                break;

	            case "3":
	                userMenu();
	                break;

	            case "4":
	                System.out.println("Exiting the menu...");
	                exit = true;
	                break;

	            default:
	                System.out.println("Invalid selection. Please try again.");
	                break;
	        }
	    } while (!exit);
	}
	
	/**
	 * Displays the Product Menu and allows the user to perform various operations 
	 * related to home appliances, such as listing, searching, adding, updating, and deleting products. 
	 * The menu operates in a loop until the user chooses to return to the main menu.
	 * The menu options include:
	 * Case "1": List/Sort options - Displays various options to list or sort products.
	 * Case "2": Search options - Allows searching for products by specific attributes.
	 * Case "3": Add a new product - Prompts the user to input details for a new product and adds it to the database.
	 * Case "4": Update a product by ID - Lets the user update details of an existing product by selecting its ID.
	 * Case "5": Delete a product by ID - Allows the user to delete a product by entering its ID.
	 * Case "6": Home Appliance Items - Opens the Appliance Items menu for managing individual items.
	 * Case "7": Return to the main menu - Exits the product menu and returns to the main menu.
	 * 
	 */
	private void productMenu() {
		String productSelection;
		do {
			 System.out.println("---------Product Menu----------");
			 System.out.println("[1] List/Sort options");
			 System.out.println("[2] Search options");
			 System.out.println("[3] Add a new product");
			 System.out.println("[4] Update a product by ID");
			 System.out.println("[5] Delete a product by ID");
			 System.out.println("[6] Home Appliance Items");
			 System.out.println("[7] Return to main menu");
			 productSelection = in.nextLine();
			 
			 switch (productSelection) { 
			 	
				 case "1":
					 
					 listOptions();
					 break;
		
				 case "2": 
					 searchOptions();
					 break;
				 
				 case "3": // Add product
					 System.out.println("\nEnter details for the new product:");
					 System.out.print("Please enter SKU: ");
					 String sku = in.nextLine();
					 System.out.print("Please enter description: ");
					 String description = in.nextLine();
					 System.out.print("Please enter category: ");
					 String category = in.nextLine();
					 System.out.print("Please enter price: ");
					 int price = Integer.parseInt(in.nextLine());
					
					 HomeAppliance addAppliance = new HomeAppliance(sku, description, category, price);
					 
					 if (homeApplianceDAO.addProduct(addAppliance)) {
						 System.out.println("Product addedd successfully");
					 } else {
						 System.out.println("Failed to add product");
					 }
					 
					 System.out.println();
					 break;
				 
				 case "4": // Update product
					    System.out.print("\nEnter the ID of the product to update: ");
					    int productId;
					    try {
					        productId = Integer.parseInt(in.nextLine());
					        HomeAppliance update = homeApplianceDAO.findProduct(productId);
					        if (update != null) {
					            System.out.print("Please enter SKU: ");
					            String newSku = in.nextLine();
					            System.out.print("Please enter description: ");
					            String newDescription = in.nextLine();
					            System.out.print("Please enter category: ");
					            String newCategory = in.nextLine();
					            System.out.print("Please enter price: ");
					            int newPrice = Integer.parseInt(in.nextLine());

					            update.setSku(newSku);
					            update.setDescription(newDescription);
					            update.setCategory(newCategory);
					            update.setPrice(newPrice);
					            if (homeApplianceDAO.updateProduct(update)) {
					                System.out.println("Product updated successfully.");
					            } else {
					                System.out.println("Failed to update product.");
					            }
					        } else {
					            System.out.println("Invalid product ID.");
					        }
					    } catch (NumberFormatException e) {
					        System.out.println("Invalid input. Please enter a valid ID.");
					    }
					    System.out.println();
					    break;
				    
				 case "5": // Delete product
				    System.out.print("\nEnter the ID of the product to delete: ");
				    int productIdToDelete = in.nextInt(); 
				    in.nextLine(); 
	
				    HomeAppliance applianceToDelete = homeApplianceDAO.findProduct(productIdToDelete); 
	
				    if (applianceToDelete != null) {
				        if (homeApplianceDAO.deleteProduct(applianceToDelete.getId())) {
				            System.out.println("Product deleted successfully.");
				        } else {
				            System.out.println("Failed to delete product.");
				        }
				    } else {
				        System.out.println("Invalid product ID.");
				    }
				    System.out.println();
				    break;

				 case "6": // ApplianceItem menu
					 applianceItemMenu();
					 break;
				 
			 }
		 } while (!productSelection.equals("7"));
	}
	
	/**
	 * Displays the List Options Menu and allows the user to view and sort products based on various criteria.
	 * The menu options include:
	 * Case "1": List All Products - Displays all products from the database.
	 * Case "2": Filter by Description - Filters and lists products based on their description.
	 * Case "3": Filter by Category - Filters and lists products based on their category.
	 * Case "4": Sort by Price (Ascending) - Displays products sorted by price in ascending order.
	 * Case "5": Sort by Price (Descending) - Displays products sorted by price in descending order.
	 * Case "6": Sort by Warranty Years (Ascending) - Displays appliance items sorted by warranty years in ascending order.
	 * Case "7": Sort by Warranty Years (Descending) - Displays appliance items sorted by warranty years in descending order.
	 * Case "8": Return to Product Menu - Exits the List Options menu and returns to the Product Menu.
	 *
	 */
	private void listOptions() {
		String optionSelection;
		
		do {
	        System.out.println("------List Options------");
	        System.out.println("[1] List All Products");
	        System.out.println("[2] Filter by Description");
	        System.out.println("[3] Filter by Category");
	        System.out.println("[4] Sort by Price (Ascending)");
	        System.out.println("[5] Sort by Price (Descending)");
	        System.out.println("[6] Sort by Warranty Years (Ascending)");
	        System.out.println("[7] Sort by Warranty Years (Descending)");
	        System.out.println("[8] Return to Product Menu");
	        optionSelection = in.nextLine();
	        
	        switch (optionSelection) {
            case "1": // List all products
            	ArrayList<HomeAppliance> appliances = homeApplianceDAO.findAllProducts(); 
				 for (int i = 0; i < appliances.size(); i++){ 
					 HomeAppliance appliance = appliances.get(i); 
					 System.out.println(appliance); 
				 }
				 System.out.println();
				 break; 
            case "2": // Filter by Description
                System.out.println("Enter description:");
                String descValue = in.nextLine();
                appliances = homeApplianceDAO.filterProductsByAttribute("description", descValue);
                for (HomeAppliance appliance : appliances) {
                    System.out.println(appliance);
                }
                System.out.println();
                break;
            case "3": //Filter by Category
                System.out.println("Enter category:");
                String catValue = in.nextLine();
                appliances = homeApplianceDAO.filterProductsByAttribute("category", catValue);
                for (HomeAppliance appliance : appliances) {
                    System.out.println(appliance);
                }
                System.out.println();
                break;
            case "4": //Sort by Price (Ascending)
            	appliances = homeApplianceDAO.productsSortedByPrice(true);
                for (HomeAppliance appliance : appliances) {
                    System.out.println(appliance);
                }
                System.out.println();
                break;
            case "5": //Sort by Price (Descending)
                appliances = homeApplianceDAO.productsSortedByPrice(false);
                for (HomeAppliance appliance : appliances) {
                    System.out.println(appliance);
                }
                System.out.println();
                break;
            case "6": //Sort by Warranty Years (Ascending)
                ArrayList<ApplianceItem> sortedAscItems = applianceItemDAO.itemsSortedByWarrantyYears(true);
                for (ApplianceItem item : sortedAscItems) {
                    System.out.println(item);
                }
                System.out.println();
                break;
            case "7": //Sort by Warranty Years (Descending)
                ArrayList<ApplianceItem> sortedDescItems = applianceItemDAO.itemsSortedByWarrantyYears(false);
                for (ApplianceItem item : sortedDescItems) {
                    System.out.println(item);
                }
                System.out.println();
                break;    
            case "8":
                return;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    } while (!optionSelection.equals("6"));
	}

	/**
	 * Displays the Search Options Menu and allows the user to search for products using various criteria.
	 * The menu options include:
	 * Case "1": Search by ID - Prompts the user to enter a product ID and searches for the product in the database.
	 * Case "2": Search by SKU - Prompts the user to enter an SKU and lists all products matching the SKU.
	 * Case "3": Search by Description - Prompts the user to enter a description and lists all products matching the description.
	 * Case "4": Search by Category - Prompts the user to enter a category and lists all products in that category.
	 * Case "5": Return to Product Menu - Exits the Search Options menu and returns to the Product Menu.
	 * 
	 */
	private void searchOptions() {
		String searchSelection;
	    do {
	        System.out.println("------Search Options------");
	        System.out.println("[1] Search by ID");
	        System.out.println("[2] Search by SKU");
	        System.out.println("[3] Search by Description");
	        System.out.println("[4] Search by Category");
	        System.out.println("[5] Return to Product Menu");
	        searchSelection = in.nextLine();

	        switch (searchSelection) {
		        case "1": //Search by ID
		            System.out.println("\nSearch for a product by ID");
		            int idIn;
		            try {
		                idIn = Integer.parseInt(in.nextLine()); // Read the whole line and parse the integer
		                HomeAppliance appliance = homeApplianceDAO.findProduct(idIn);
		                if (appliance != null) {
		                    System.out.println(appliance);
		                } else {
		                    System.out.println("No product found");
		                }
		            } catch (NumberFormatException e) {
		                System.out.println("Invalid input. Please enter a valid ID.");
		            }
		            break;
	            case "2": //Search by SKU
	            	System.out.println("Enter SKU:");
	                String sku = in.nextLine();
	                ArrayList<HomeAppliance> appliances = homeApplianceDAO.searchProductsByAttribute("sku", sku);
	                if (!appliances.isEmpty()) {
	                    for (HomeAppliance app : appliances) {
	                        System.out.println(app);
	                    }
	                } else {
	                    System.out.println("No products found with SKU: " + sku);
	                }
	                System.out.println();
	                break;
	            case "3": //Search by Description
	            	System.out.println("Enter Description:");
	                String description = in.nextLine();
	                appliances = homeApplianceDAO.searchProductsByAttribute("description", description);
	                if (!appliances.isEmpty()) {
	                    for (HomeAppliance app : appliances) {
	                        System.out.println(app);
	                    }
	                } else {
	                    System.out.println("No products found with Description: " + description);
	                }
	                System.out.println();
	                break;
	            case "4": //Search by Category
	            	System.out.println("Enter Category:");
	                String category = in.nextLine();
	                appliances = homeApplianceDAO.searchProductsByAttribute("category", category);
	                if (!appliances.isEmpty()) {
	                    for (HomeAppliance app : appliances) {
	                        System.out.println(app);
	                    }
	                } else {
	                    System.out.println("No products found with Category: " + category);
	                }
	                System.out.println();
	                break;
	            case "5":
	                return;
	            default:
	                System.out.println("Invalid option. Please try again.");
	                break;
	        }
	    } while (!searchSelection.equals("5"));
	}
	
	/**
	 * Displays the Customer Menu and allows the user to manage customer records.
	 * The menu options include:
	 * Case "1": List all customers - Retrieves and displays a list of all customers from the database.
	 * Case "2": Search for a customer by ID - Prompts the user to enter a customer ID and displays the matching customer details.
	 * Case "3": Add a new customer - Prompts the user to enter customer details and adds the customer to the database.
	 * Case "4": Update a customer by ID - Prompts the user to select a customer by ID and update their details.
	 * Case "5": Delete a customer by ID - Prompts the user to select a customer by ID and deletes the customer from the database.
	 * Case "6": Return to main menu - Exits the Customer Menu and returns to the main menu.
	 * 
	 */
	private void customerMenu() {
		String customerSelection;
		
		do{
			 System.out.println("---------Customer Menu----------");
			 System.out.println("[1] List all customers");
			 System.out.println("[2] Search for customer by ID");
			 System.out.println("[3] Add a new customer");
			 System.out.println("[4] Update a customer by ID");
			 System.out.println("[5] Delete a customer by ID");
			 System.out.println("[6] Return to main menu");
			 
			 customerSelection = in.nextLine();
			 
			 switch (customerSelection) {
				 case "1": //List all customers
					 ArrayList<Customer> customers = customerDAO.findAllCustomers();
					 for (int i = 0; i < customers.size(); i++){
						 Customer customer = customers.get(i);
						 System.out.println(customer);
					 }
					 System.out.println();
					 break;
		
				 case "2": //Search for customer by ID
					 System.out.println("\nSearch for a customer by ID: ");
					 int customerIdIn = in.nextInt();
					 Customer customer = customerDAO.findCustomer(customerIdIn);
					 if (customer != null) {
						 System.out.println(customer);
					 } else {
						 System.out.println("No customer found");
					 }
					 break;
				 
				 case "3": //Add a new customer
					 System.out.println("\nEnter details for the new customer:");
					 System.out.print("Please enter business name: ");
					 String businessName = in.nextLine();
					 
					 System.out.print("Please enter street: ");
					 String street = in.nextLine();
					 System.out.print("Please enter town: ");
					 String town = in.nextLine();
					 System.out.print("Please enter city: ");
					 String city = in.nextLine();
					 System.out.print("Please enter country: ");
					 String country = in.nextLine();
					 System.out.print("Please enter postcode: ");
					 String postcode = in.nextLine();
					 String addressIn = street + ", " + town + ", " + city + ", " + country + ", " + postcode;
					 Address address = Address.fromString(addressIn);
					 
					 System.out.print("Please enter telephoneNumber: ");
					 String telephoneNumber = in.nextLine();
					 System.out.print("Please enter email address: ");
					 String emailAddress = in.nextLine();
					
					 Customer addCust = new Customer(businessName, address, telephoneNumber, emailAddress);
					 
					 if (customerDAO.addCustomer(addCust)) {
						 System.out.println("Customer addedd successfully");
					 } else {
						 System.out.println("Failed to add customer");
					 }
					 
					 System.out.println();
					 break;
				 
				 case "4":  //update customer
					 System.out.print("\nEnter the ID of the customer to update: ");
					    int custId;
					    try {
					        custId = Integer.parseInt(in.nextLine());
					        Customer update = customerDAO.findCustomer(custId);
					        if (update != null) {
					            System.out.print("Please enter business name: ");
					            String newBusinessName = in.nextLine();

					            System.out.print("Please enter street: ");
					            String newStreet = in.nextLine();
					            System.out.print("Please enter town: ");
					            String newTown = in.nextLine();
					            System.out.print("Please enter city: ");
					            String newCity = in.nextLine();
					            System.out.print("Please enter country: ");
					            String newCountry = in.nextLine();
					            System.out.print("Please enter postcode: ");
					            String newPostcode = in.nextLine();

					            String fullAddress = String.format("%s, %s, %s, %s, %s", newStreet, newTown, newCity, newCountry, newPostcode);
					            Address newAddress = Address.fromString(fullAddress);

					            System.out.print("Please enter telephone number: ");
					            String newTelephoneNumber = in.nextLine();
					            System.out.print("Please enter email address: ");
					            String newEmailAddress = in.nextLine();

					            update.setBusinessName(newBusinessName);
					            update.setAddress(newAddress);
					            update.setTelephoneNumber(newTelephoneNumber);
					            update.setEmailAddress(newEmailAddress);
					            if (customerDAO.updateCustomer(update)) {
					                System.out.println("Customer updated successfully.");
					            } else {
					                System.out.println("Failed to update customer.");
					            }
					        } else {
					            System.out.println("No customer found with ID: " + custId);
					        }
					    } catch (NumberFormatException e) {
					        System.out.println("Invalid input. Please enter a valid integer ID.");
					    }
					    System.out.println();
					    break;
				    
				 case "5": //Delete a customer by ID
				    ArrayList<Customer> deleteCustomer = customerDAO.findAllCustomers();
				    System.out.print("\nEnter the ID of the customer to delete: ");
				    int deleteCustIndex = Integer.parseInt(in.nextLine()) - 1;
				    if (deleteCustIndex >= 0 && deleteCustIndex < deleteCustomer.size()) {
				        if (customerDAO.deleteCustomer(deleteCustomer.get(deleteCustIndex).getCustomerID())) {
				            System.out.println("Customer deleted successfully.");
				        } else {
				            System.out.println("Failed to delete customer.");
				        }
				    } else {
				        System.out.println("Invalid customer ID.");
				    }
				    System.out.println();
				    break;
				 
		 }	 	
	 } while (!customerSelection.equals("6"));
	}
	
	/**
	 * Displays the User Menu and allows the user to manage user accounts.
	 * The menu options include:
	 * Case "1": Add user - Prompts the user to enter details for a new user and adds them to the database.
	 * Case "2": List all users - Retrieves and displays a list of all users from the database.
	 * Case "3": Search for user by ID - Prompts the user to enter a user ID and displays the matching user's details.
	 * Case "4": Update a user by ID - Prompts the user to select a user by ID and update their details.
	 * Case "5": Delete a user by ID - Prompts the user to select a user by ID and deletes the user from the database.
	 * Case "6": Return to main menu - Exits the User Menu and returns to the main menu.
	 * 
	 */
	private void userMenu() {
		String userSelection;
		
		do{
			 System.out.println("---------User Menu----------");
			 System.out.println("[1] Add user");
			 System.out.println("[2] List all users");
			 System.out.println("[3] Search for user by ID");
			 System.out.println("[4] Update a user by ID");
			 System.out.println("[5] Delete a user by ID");
			 System.out.println("[6] Return to main menu");
			 
			 userSelection = in.nextLine();
			 
			 switch (userSelection) {
				 case "1": //Add user
					  System.out.println("Enter details for the new user:");

					    System.out.print("Please enter username: ");
					    String username = in.nextLine();
					    if (usersDAO.isUsernameExist(username)) {
					        System.out.println("This username is already taken. Please choose another.");
					        break;
					    }

					    System.out.print("Please enter password: ");
					    String password = in.nextLine();

					    System.out.print("Please enter role (Admin or Customer): ");
					    String role = in.nextLine();

					    System.out.print("Please enter customer ID (leave blank if not applicable): ");
					    String customerIdStr = in.nextLine().trim();
					    Integer customerId = null;
					    if (!customerIdStr.isEmpty()) {
					        try {
					            customerId = Integer.parseInt(customerIdStr);
					        } catch (NumberFormatException e) {
					            System.out.println("Invalid customer ID entered. Please enter a numeric value or leave blank.");
					            break;  
					        }
					    }

					    Users newUser = new Users(username, password, role, customerId);
					    if (usersDAO.addUser(newUser)) {
					        System.out.println("User added successfully.");
					    } else {
					        System.out.println("Failed to add user. Please check your data and try again.");
					    }
					    break; 
			  
			 
			 	case "2": //List all users
					 ArrayList<Users> user = usersDAO.findAllUsers();
					 for (int i = 0; i < user.size(); i++){
						 Users user2 = user.get(i);
						 System.out.println(user2);
					 }
					 System.out.println();
					 break;
		
				 case "3": //Search for user by ID
					 System.out.println("\nSearch for a customer by ID");
					 int userIdInput = in.nextInt();
					 Users users = usersDAO.findUser(userIdInput);
					 if (users != null) {
						 System.out.println(users);
					 } else {
						 System.out.println("No user found");
					 }
					 break;
				 

				 case "4":  //update user
				    ArrayList<Users> allUsers = usersDAO.findAllUsers();
				    System.out.print("\nEnter the ID of the user to update: ");
				    int selectionCustIndex = Integer.parseInt(in.nextLine()) - 1;
				    if (selectionCustIndex >= 0 && selectionCustIndex < allUsers.size()) {
				        Users update = allUsers.get(selectionCustIndex);
				        System.out.print("Please enter username: ");
				        String newUsername = in.nextLine();
				        System.out.print("Please enter password: ");
				        String newPassword = in.nextLine();
				        System.out.print("Please enter role: ");
				        String newRole = in.nextLine();
				        System.out.print("Please enter customer ID (if user is a existing customer otherwise press Enter): ");
				        String customerIdInput = in.nextLine();
				        
				        Integer newCustomerId = customerIdInput.isEmpty() ? null : Integer.parseInt(customerIdInput);
	
				        update.setUsername(newUsername);
				        update.setPassword(newPassword);
				        update.setRole(newRole);
				        update.setCustomerId(newCustomerId);
				        if (usersDAO.updateUser(update)) {
				            System.out.println("User updated successfully.");
				        } else {
				            System.out.println("Failed to update user.");
				        }
				    } else {
				        System.out.println("Invalid user ID.");
				    }
				    System.out.println();
				    break;
				    
				 case "5": // Delete user
					 System.out.print("\nEnter the ID of the user to delete: ");
					    int userIdToDelete = Integer.parseInt(in.nextLine());

					    if (usersDAO.deleteUser(userIdToDelete)) {
					        System.out.println("User deleted successfully.");
					    } else {
					        System.out.println("Failed to delete user. Ensure the ID is correct.");
					    }
					    System.out.println();
					    break;
				 
			 }	 	
		 } while (!userSelection.equals("6"));			    
		 
	}
	
	/**
	 * Displays the Appliance Item Menu and provides options for managing appliance items.
	 * The menu options include:
	 * Case "1": List all appliance items - Retrieves and displays a list of all appliance items from the database.
	 * Case "2": Add a new appliance item - Prompts the user to enter details for a new appliance item and adds it to the database.
	 * Case "3": Update an appliance item by ID - Prompts the user to select an appliance item by ID and update its details.
	 * Case "4": Delete an appliance item by ID - Prompts the user to select an appliance item by ID and deletes it from the database.
	 * Case "5": List appliance item by ID - Prompts the user to enter an appliance item ID and displays the matching appliance item's details.
	 * Case "6": Return to main menu - Exits the Appliance Item Menu and returns to the main menu.
	 * 
	 */
	private void applianceItemMenu() {
	    String selection;
	    do {
	        System.out.println("---------Appliance Item Menu----------");
	        System.out.println("[1] List all appliance items");
	        System.out.println("[2] Add a new appliance item");
	        System.out.println("[3] Update an appliance item by ID");
	        System.out.println("[4] Delete an appliance item by ID");
	        System.out.println("[5] List appliance item by ID");
	        System.out.println("[6] Return to main menu");
	        selection = in.nextLine();

	        switch (selection) {
	            case "1": //List all appliance items
	            	ArrayList<ApplianceItem> applianceItems = applianceItemDAO.findAllApplianceItems();
					 for (int i = 0; i < applianceItems.size(); i++){ 
						 ApplianceItem applianceItem = applianceItems.get(i); 
						 System.out.println(applianceItem); 
					 }
					 System.out.println();
					 break; 
	            case "2": //Add a new appliance item
	            	System.out.println("\nEnter details for the new appliance item:");
					 System.out.print("Please enter home appliance ID: ");
					 int homeApplianceId = Integer.parseInt(in.nextLine());
					 HomeAppliance homeAppliance = homeApplianceDAO.findProduct(homeApplianceId);
					 if (homeAppliance == null) {
						 System.out.println("Invalid home appliance ID. Please try again.");
						 return;
					 }
					 System.out.print("Please enter warranty years: ");
					 int warrantyYears = Integer.parseInt(in.nextLine());
					 System.out.print("Please enter brand: ");
					 String brand = in.nextLine();
					 System.out.print("Please enter model: ");
					 String model = in.nextLine();
					
					 ApplianceItem newItem = new ApplianceItem(homeAppliance, warrantyYears, brand, model);
					 
					 if (applianceItemDAO.addApplianceItem(newItem)) {
						 System.out.println("Product addedd successfully");
					 } else {
						 System.out.println("Failed to add product");
					 }
					 
					 System.out.println();
					 break;
	            case "3": //Update an appliance item by ID
	                System.out.print("\nEnter the ID of the appliance item to update: ");
	                int itemId;
	                try {
	                    itemId = Integer.parseInt(in.nextLine());
	                    ApplianceItem update = applianceItemDAO.findApplianceItem(itemId);
	                    if (update != null) {
	                        System.out.print("Please enter new warranty years: ");
	                        int newWarrantyYears = Integer.parseInt(in.nextLine());
	                        System.out.print("Please enter new brand: ");
	                        String newBrand = in.nextLine();
	                        System.out.print("Please enter new model: ");
	                        String newModel = in.nextLine();

	                        update.setWarrantyYears(newWarrantyYears);
	                        update.setBrand(newBrand);
	                        update.setModel(newModel);
	                        if (applianceItemDAO.updateApplianceItem(update)) {
	                            System.out.println("Appliance item updated successfully.");
	                        } else {
	                            System.out.println("Failed to update appliance item.");
	                        }
	                    } else {
	                        System.out.println("Invalid appliance item ID.");
	                    }
	                } catch (NumberFormatException e) {
	                    System.out.println("Invalid input. Please enter a valid ID.");
	                }
	                System.out.println();
	                break;

	            case "4": //Delete an appliance item by ID
				    System.out.print("\nEnter the ID of the appliance item to delete: ");
				    int itemIdToDelete = in.nextInt(); 
				    in.nextLine();  
	
				    ApplianceItem itemToDelete = applianceItemDAO.findApplianceItem(itemIdToDelete); 
	
				    if (itemToDelete != null) {
				        if (applianceItemDAO.deleteApplianceItem(itemToDelete.getId())) {
				            System.out.println("Appliance item deleted successfully.");
				        } else {
				            System.out.println("Failed to delete appliance item.");
				        }
				    } else {
				        System.out.println("Invalid appliance item ID."); 
				    }
				    System.out.println();
				    break;
	            case "5": //List appliance item by ID
					 System.out.println("\nSearch for a appliance item by ID"); 
					 int itemIdInput = in.nextInt(); 
					 ApplianceItem item = applianceItemDAO.findApplianceItem(itemIdInput); 
					 if (item != null) {  
						 System.out.println(item);
					 } else {  
						 System.out.println("No product found");
					 }
					 break;
	            case "6":
	                return;
	        }
	    } while (!selection.equals("5"));
	}

	/**
	 * The main method serves as the entry point of the application.
	 * 
	 * It initialises the {@code Controller} object and starts the menu system.
	 * 
	 * @param args command-line arguments (not used in this application).
	 */
	public static void main(String[] args) {
		Controller menu = new Controller();
		menu.menuSystem();

	}
}

