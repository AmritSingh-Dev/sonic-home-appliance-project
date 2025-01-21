package homeappliance;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link HomeAppliance} class.
 * 
 * This test class ensures the proper functioning of the {@link HomeAppliance} class's methods,
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

class HomeApplianceTest {

	private HomeAppliance appliance;

    /**
     * Sets up a sample {@link HomeAppliance} object for testing.
     * This method is executed before each test to provide a consistent test fixture.
     */
    @BeforeEach
    void setUp() {
        appliance = new HomeAppliance("SKU123", "Dishwasher", "Kitchen", 500);
    }

    /**
     * Tests the getter methods of the {@link HomeAppliance} class.
     * Ensures that values initialised in the constructor are retrieved correctly.
     */
    @Test
    void testGetters() {
    	assertEquals(0, appliance.getId());
        assertEquals("SKU123", appliance.getSku(), "SKU should match constructor value");
        assertEquals("Dishwasher", appliance.getDescription(), "Description should match constructor value");
        assertEquals("Kitchen", appliance.getCategory(), "Category should match constructor value");
        assertEquals(500, appliance.getPrice(), "Price should match constructor value");
    }

    /**
     * Tests the setter methods of the {@link HomeAppliance} class.
     * Ensures that values can be updated correctly through setters.
     */
    @Test
    void testSetters() {
        appliance.setId(101);
        assertEquals(101, appliance.getId(), "ID should match the set value");

        appliance.setSku("SKU456");
        assertEquals("SKU456", appliance.getSku(), "SKU should be updated");

        appliance.setDescription("Refrigerator");
        assertEquals("Refrigerator", appliance.getDescription(), "Description should be updated");

        appliance.setCategory("Appliances");
        assertEquals("Appliances", appliance.getCategory(), "Category should be updated");

        appliance.setPrice(750);
        assertEquals(750, appliance.getPrice(), "Price should be updated");
    }

    /**
     * Tests the {@code toString} method of the {@link HomeAppliance} class.
     * Verifies that the method returns the correct string representation of the object.
     */
    @Test
    void testToString() {
        String expectedOutput = "ID: 0, SKU: SKU123, Description: Dishwasher, Category: Kitchen, Price: 500";
        assertEquals(expectedOutput, appliance.toString(), "toString should return the correct format");
    }
}