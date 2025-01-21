package customers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Customer} class.
 * 
 * This test class ensures the proper functioning of the {@link Customer} class's methods, 
 * including getters, setters, and the `toString` method.
 * 
 * Test cases include:
 * - Verifying initial values set by the constructor.
 * - Ensuring setters correctly update fields.
 * - Checking the proper formatting of the `toString` output.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
class CustomerTest {

	private Customer customer;
    private Address address;
    
    /**
     * Sets up a test {@link Customer} instance with a mock {@link Address} before each test.
     */
    @BeforeEach
    public void setUp() {
        // Assuming Address has a suitable constructor
        address = new Address("1234 Main Street", "Salford", "Manchester", "UK", "M1 1AA");
        customer = new Customer("Northern Tech", address, "01617921111", "info@northerntech.com");
    }
    
    /**
     * Tests the getter methods of the {@link Customer} class to ensure 
     * they return the correct initial values set by the constructor.
     */
    @Test
    public void testGetters() {
        // Test initial values set by constructor
        assertEquals("Northern Tech", customer.getBusinessName());
        assertEquals(address, customer.getAddress());
        assertEquals("01617921111", customer.getTelephoneNumber());
        assertEquals("info@northerntech.com", customer.getEmailAddress());
    }
    
    /**
     * Tests the setter methods of the {@link Customer} class to ensure 
     * they correctly update the object's fields.
     */
    @Test
    public void testSetters() {
        // Set new values
        Address newAddress = new Address("5678 Main Close", "Eccles", "Greater Manchester", "United Kingdom" , "M2 2BB");
        customer.setCustomerID(10);
        customer.setBusinessName("New Northern Tech");
        customer.setAddress(newAddress);
        customer.setTelephoneNumber("01617771111");
        customer.setEmailAddress("info@newnortherntech.com");

        // Test updated values
        assertEquals(10, customer.getCustomerID());
        assertEquals("New Northern Tech", customer.getBusinessName());
        assertEquals(newAddress, customer.getAddress());
        assertEquals("01617771111", customer.getTelephoneNumber());
        assertEquals("info@newnortherntech.com", customer.getEmailAddress());
    }
    
    /**
     * Tests the `toString` method of the {@link Customer} class to ensure 
     * it produces the correct string representation of the object.
     */
    @Test
    public void testToString() {
        String expected = "ID: 0, Name: Northern Tech, Address: " + address.toString() + ", Telephone Number: 01617921111, Email Address: info@northerntech.com";
        assertEquals(expected, customer.toString());
    }
}