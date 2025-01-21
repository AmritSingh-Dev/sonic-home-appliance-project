package customers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Address} class.
 * 
 * This class tests the functionality of the {@link Address} class, including its getters, setters, 
 * string representation, and parsing capabilities.
 * 
 * The tests cover:
 * - Getter methods for retrieving address fields.
 * - Setter methods for updating address fields.
 * - String representation via the `toString` method.
 * - Parsing an address from a string with `fromString`.
 * - Failure handling when `fromString` is called with an invalid input.
 * 
 * Mock data used in the tests is based on a sample business address.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
class AddressTest {

	private Address address;
	
    /**
     * Sets up a sample {@link Address} object before each test.
     * Uses a mock address: "10 Sonic Street, Eccles, Manchester, UK, M1 1AA".
     */
    @BeforeEach
    public void setUp() {
        // Using a mock address from Manchester, UK
        address = new Address("10 Sonic Street", "Eccles", "Manchester", "UK", "M1 1AA");
    }
    
    /**
     * Tests the getter methods for all fields in the {@link Address} class.
     * Ensures the returned values match the values set in the setup.
     */
    @Test
    public void testGetters() {
        assertEquals("10 Sonic Street", address.getAddressLine0());
        assertEquals("Eccles", address.getAddressLine1());
        assertEquals("Manchester", address.getAddressLine2());
        assertEquals("UK", address.getCountry());
        assertEquals("M1 1AA", address.getPostCode());
    }
    
    /**
     * Tests the setter methods for all fields in the {@link Address} class.
     * Updates the address fields and verifies the changes are reflected correctly.
     */
    @Test
    public void testSetters() {
        address.setAddresLine0("20 Sonic Street");
        address.setAddressLine1("Salford");
        address.setAddressLine2("Greater Manchester");
        address.setCountry("United Kingdom");
        address.setpostCode("M2 2BB");

        assertEquals("20 Sonic Street", address.getAddressLine0());
        assertEquals("Salford", address.getAddressLine1());
        assertEquals("Greater Manchester", address.getAddressLine2());
        assertEquals("United Kingdom", address.getCountry());
        assertEquals("M2 2BB", address.getPostCode());
    }
    
    /**
     * Tests the {@link Address#toString()} method.
     * Ensures the string representation of the address matches the expected format.
     */
    @Test
    public void testToString() {
        String expected = "10 Sonic Street, Eccles, Manchester, UK, M1 1AA";
        assertEquals(expected, address.toString());
    }
    
    /**
     * Tests the {@link Address#fromString(String)} method.
     * Ensures a valid input string is correctly parsed into an {@link Address} object.
     */
    @Test
    public void testFromString() {
        String input = "10 Sonic Street, Eccles, Manchester, UK, M1 1AA";
        Address result = Address.fromString(input);

        assertEquals("10 Sonic Street", result.getAddressLine0().trim());
        assertEquals("Eccles", result.getAddressLine1().trim());
        assertEquals("Manchester", result.getAddressLine2().trim());
        assertEquals("UK", result.getCountry().trim());
        assertEquals("M1 1AA", result.getPostCode().trim());
    }
    
    /**
     * Tests the failure case for the {@link Address#fromString(String)} method.
     * Ensures an {@link IllegalArgumentException} is thrown for invalid input formats.
     */
    @Test
    public void testFromStringFailure() {
        String input = "Incorrect format";
        assertThrows(IllegalArgumentException.class, () -> Address.fromString(input));
    }
}