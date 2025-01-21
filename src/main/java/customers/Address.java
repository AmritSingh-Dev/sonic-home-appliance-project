package customers;

/**
 * Represents an address with multiple address lines, country, and postal code.
 * Provides methods for constructing an address, converting it to and from strings, 
 * and accessing and modifying its properties.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class Address {
	
    /**
     * The first line of the address.
     */
	private String addressLine0;
	
    /**
     * The second line of the address.
     */
	private String addressLine1;
	
    /**
     * The third line of the address.
     */
	private String addressLine2;
	
    /**
     * The country where the address is located.
     */
	private String country;
	
    /**
     * The postal code associated with the address.
     */
	private String postCode;
	
	/**
     * Constructs a new Address object with the specified details.
     * 
     * @param addressLine0 the first line of the address
     * @param addressLine1 the second line of the address
     * @param addressLine2 the third line of the address
     * @param country      the country of the address
     * @param postCode     the postal code of the address
     */
	public Address(String addressLine0, String addressLine1, String addressLine2, String country, String postCode) {
		this.addressLine0 = addressLine0;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.country = country;
		this.postCode = postCode;
	}
	
	/**
     * Creates an Address object from a string representation of the address.
     * The string should contain exactly five comma-separated parts.
     * 
     * @param addressString a comma-separated string containing the address details
     * @return an Address object created from the string
     * @throws IllegalArgumentException if the string does not have exactly five parts
     */
	public static Address fromString(String addressString) {
		String[] parts = addressString.split(",");
		if (parts.length == 5) {
			return new Address(parts[0], parts[1], parts[2], parts[3], parts[4]);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
     * Returns a string representation of the address, formatted as a comma-separated list.
     * 
     * @return a string representation of the address
     */
	@Override
	public String toString() {
		return addressLine0 + ", " + addressLine1 + ", " + addressLine2 + ", " + country + ", " + postCode;
	}
	
	/**
     * Getter method for the first line of the address.
     * 
     * @return the first line of the address
     */
	public String getAddressLine0(){
		return addressLine0;
	}
	
    /**
     * Getter method for the second line of the address.
     * 
     * @return the second line of the address
     */
	public String getAddressLine1(){
		return addressLine1;
	}
	
	/**
     * Getter method for the third line of the address.
     * 
     * @return the third line of the address
     */
	public String getAddressLine2(){
		return addressLine2;
	}
	
    /**
     * Getter method for the country of the address.
     * 
     * @return the country of the address
     */
	public String getCountry(){
		return country;
	}
	
    /**
     * Getter method for the postal code of the address.
     * 
     * @return the postal code of the address
     */
	public String getPostCode(){
		return postCode;
	}
	
	/**
     * Sets the first line of the address.
     * 
     * @param addressLine0 the new first line of the address
     */
	public void setAddresLine0(String addressLine0) {
		this.addressLine0 = addressLine0;
	}
	

    /**
     * Sets the second line of the address.
     * 
     * @param addressLine1 the new second line of the address
     */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	
	 /**
     * Sets the third line of the address.
     * 
     * @param addressLine2 the new third line of the address
     */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	
	/**
     * Sets the country of the address.
     * 
     * @param country the new country of the address
     */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
     * Sets the postal code of the address.
     * 
     * @param postCode the new postal code of the address
     */
	public void setpostCode(String postCode) {
		this.postCode = postCode;
	}
	

}
