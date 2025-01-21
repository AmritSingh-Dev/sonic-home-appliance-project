package customers;

/**
 * A customer class with a unique ID, business name, address, telephone number, and email address attributes.
 * Provides methods to get and set the customer's details.
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class Customer {
    
    /**
     * Unique ID for the customer.
     */
    private int customerID;

    /**
     * Name of the business associated with the customer ID.
     */
    private String businessName;

    /**
     * Address of the business.
     */
    private Address address;

    /**
     * Telephone number of the business.
     */
    private String telephoneNumber;

    /**
     * Email address of the business.
     */
    private String emailAddress;
    
    /**
     * Constructs a new Customer/business with the specified details.
     *
     * @param businessName the name of the business
     * @param address the address of the business
     * @param telephoneNumber the telephone number of the business
     * @param emailAddress the email address of the business
     */
    public Customer(String businessName, Address address, String telephoneNumber, String emailAddress) {
        this.businessName = businessName;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.emailAddress = emailAddress;
    }
    
    /**
     * A Getter function which returns the customer ID.
     *
     * @return the ID of the customer
     */
    public int getCustomerID(){
        return customerID;
    }

    /**
     * A Getter function which returns the business name.
     *
     * @return the business name
     */
    public String getBusinessName(){
        return businessName;
    }

    /**
     * A Getter function which returns the address of the business.
     *
     * @return the address
     */
    public Address getAddress(){
        return address;
    }

    /**
     * A Getter function which returns the telephone number of the business.
     *
     * @return the telephone number
     */
    public String getTelephoneNumber(){
        return telephoneNumber;
    }

    /**
     * A Getter function which returns the email address of the business.
     *
     * @return the email address
     */
    public String getEmailAddress(){
        return emailAddress;
    }
    
    /**
     * A Setter function which sets the customer ID.
     *
     * @param customerID - the new ID for the customer
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * A Setter function which sets the business name.
     *
     * @param businessName the new business name
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * A Setter function which sets the address.
     *
     * @param address the new address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * A Setter function which sets the telephone number.
     *
     * @param telephoneNumber the new telephone number
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * A Setter function which sets the email address.
     *
     * @param emailAddress the new email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Provides a custom string representation of the Customer object, 
     * which includes the customer ID, business name, address, telephone number, 
     * and email address. This overrides the default string which returns the memory location
     *
     * @return a formatted string detailing all pertinent customer information.
     */
    @Override
    public String toString() {
        return "ID: " + customerID + ", Name: " + businessName + ", Address: " + address + ", Telephone Number: " + telephoneNumber + ", Email Address: " + emailAddress;
    }


}
