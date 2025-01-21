package customers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link CustomerDAO} class.
 * 
 * This test class verifies the behaviour of methods in the {@link CustomerDAO} class using mocked
 * database interactions. It uses Mockito to simulate {@link Connection}, {@link PreparedStatement},
 * and {@link ResultSet} objects.
 * 
 * The tests cover:
 * - Retrieval of all customers
 * - Retrieval of a specific customer by ID
 * - Deleting a customer
 * - Updating customer information
 * - Adding a new customer
 * - Retrieving a customer ID based on business name and email address
 * 
 * Test data is mocked to simulate real-world scenarios, including valid, invalid, and edge cases.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 * 
 */

class CustomerDAOTest {

    private CustomerDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    
    /**
     * Sets up mock objects and initialises the {@link CustomerDAO} instance before each test.
     * This method ensures that database interactions are simulated using mocked objects.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        dao = new CustomerDAO() {
            @Override
            protected Connection connect() {
                return mockConnection;
            }
        };

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate success
    }
    
    /**
     * Tests the retrieval of all customers when valid data is returned from the database.
     */
    @Test
    public void testFindAllCustomersValid() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false); // Two customers found, then end
        when(mockResultSet.getInt("customerId")).thenReturn(1, 2);
        when(mockResultSet.getString("businessName")).thenReturn("Business1", "Business2");
        when(mockResultSet.getString("address")).thenReturn(
            "1234 Main Street, Salford, Manchester, UK, M1 1AA", 
            "5678 Main Close, Eccles, Greater Manchester, UK, M2 2BB"
        );
        when(mockResultSet.getString("telephoneNumber")).thenReturn("01617921111", "01617771111");
        when(mockResultSet.getString("emailAddress")).thenReturn("info@business1.com", "info@business2.com");

        ArrayList<Customer> customers = dao.findAllCustomers();

        assertNotNull(customers, "Customer list should not be null");
        assertEquals(2, customers.size(), "Should return two customers");
        assertEquals("Business1", customers.get(0).getBusinessName());
        assertEquals("Business2", customers.get(1).getBusinessName());
    }

    /**
     * Tests the retrieval of all customers when no customers exist in the database.
     */
    @Test
    public void testFindAllCustomersInvalid() throws SQLException {
        when(mockResultSet.next()).thenReturn(false); // No customers found

        ArrayList<Customer> customers = dao.findAllCustomers();

        assertNotNull(customers, "Customer list should not be null");
        assertTrue(customers.isEmpty(), "Customer list should be empty when no customers are found");
    }
    
    /**
     * Tests the retrieval of a single customer by ID when the customer exists.
     */
    @Test
    public void testFindCustomerValid() throws SQLException {
        when(mockResultSet.next()).thenReturn(true); // Customer found
        when(mockResultSet.getInt("customerId")).thenReturn(1);
        when(mockResultSet.getString("businessName")).thenReturn("Business1");
        when(mockResultSet.getString("address")).thenReturn("1234 Main Street, Salford, Manchester, UK, M1 1AA");
        when(mockResultSet.getString("telephoneNumber")).thenReturn("01617921111");
        when(mockResultSet.getString("emailAddress")).thenReturn("info@business1.com");

        Customer customer = dao.findCustomer(1);

        assertNotNull(customer, "Customer should not be null");
        assertEquals(1, customer.getCustomerID());
        assertEquals("Business1", customer.getBusinessName());
    }
    
    /**
     * Tests the retrieval of a single customer by ID when the customer does not exist.
     */
    @Test
    public void testFindCustomerInvalid() throws SQLException {
        when(mockResultSet.next()).thenReturn(false); // No customer found

        Customer customer = dao.findCustomer(999);

        assertNull(customer, "Customer should be null when not found");
    }
    
    /**
     * Tests deleting a customer when the operation is successful.
     */
    @Test
    public void testDeleteCustomerValid() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate success

        boolean result = dao.deleteCustomer(1);

        assertTrue(result, "Delete should return true when a row is affected");
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Tests deleting a customer when the customer does not exist.
     */
    @Test
    public void testDeleteCustomerInvalid() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Simulate failure

        boolean result = dao.deleteCustomer(999);

        assertFalse(result, "Delete should return false when no rows are affected");
        verify(mockPreparedStatement).setInt(1, 999);
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Tests updating a customer's information when the operation is successful.
     */
    @Test
    public void testUpdateCustomerValid() throws SQLException {
        Address updatedAddress = new Address("456 New Road", "Eccles", "Greater Manchester", "UK", "M4 4BB");
        Customer updatedCustomer = new Customer("Updated Business", updatedAddress, "01612345678", "updated@example.com");
        updatedCustomer.setCustomerID(1); // Assume the customer with ID 1 exists

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate 1 row affected

        boolean result = dao.updateCustomer(updatedCustomer);

        assertTrue(result, "Update should return true when a row is affected");
        verify(mockPreparedStatement).setString(1, updatedCustomer.getBusinessName());
        verify(mockPreparedStatement).setString(2, updatedCustomer.getAddress().toString());
        verify(mockPreparedStatement).setString(3, updatedCustomer.getTelephoneNumber());
        verify(mockPreparedStatement).setString(4, updatedCustomer.getEmailAddress());
        verify(mockPreparedStatement).setInt(5, updatedCustomer.getCustomerID());
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Tests updating a customer's information when the customer does not exist.
     */
    @Test
    public void testUpdateCustomerInvalid() throws SQLException {
        Address updatedAddress = new Address("789 Nonexistent Street", "Nowhere", "No County", "UK", "M9 9ZZ");
        Customer nonexistentCustomer = new Customer("Nonexistent Business", updatedAddress, "00000000000", "nonexistent@example.com");
        nonexistentCustomer.setCustomerID(999); // Assume customer ID 999 does not exist

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Simulate 0 rows affected

        boolean result = dao.updateCustomer(nonexistentCustomer);

        assertFalse(result, "Update should return false when no rows are affected");
        verify(mockPreparedStatement).setString(1, nonexistentCustomer.getBusinessName());
        verify(mockPreparedStatement).setString(2, nonexistentCustomer.getAddress().toString());
        verify(mockPreparedStatement).setString(3, nonexistentCustomer.getTelephoneNumber());
        verify(mockPreparedStatement).setString(4, nonexistentCustomer.getEmailAddress());
        verify(mockPreparedStatement).setInt(5, nonexistentCustomer.getCustomerID());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    /**
     * Tests adding a new customer when the operation is successful.
     */
    @Test
    public void testAddCustomerValid() throws SQLException {
        Address address = new Address("1234 Main Street", "Salford", "Manchester", "UK", "M1 1AA");
        Customer customer = new Customer("Business1", address, "01617921111", "info@business1.com");

        boolean result = dao.addCustomer(customer);

        assertTrue(result, "Add should return true when a row is affected");
        verify(mockPreparedStatement).setString(1, customer.getBusinessName());
        verify(mockPreparedStatement).setString(2, customer.getAddress().toString());
        verify(mockPreparedStatement).setString(3, customer.getTelephoneNumber());
        verify(mockPreparedStatement).setString(4, customer.getEmailAddress());
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Tests adding a new customer when the operation fails.
     */
    @Test
    public void testAddCustomerInvalid() throws SQLException {
        Address address = new Address("1234 Main Street", "Salford", "Manchester", "UK", "M1 1AA");
        Customer customer = new Customer("Business1", address, "01617921111", "info@business1.com");
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Simulate failure

        boolean result = dao.addCustomer(customer);

        assertFalse(result, "Add should return false when no rows are affected");
    }
    
    /**
     * Tests retrieving a customer ID by business name and email when the customer exists.
     */
    @Test
    public void testGetCustomerIdValid() throws SQLException {
        // Arrange
        String businessName = "Test Business";
        String emailAddress = "test@business.com";
        int expectedCustomerId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate customer found
        when(mockResultSet.getInt("customerId")).thenReturn(expectedCustomerId);

        int customerId = dao.getCustomerId(businessName, emailAddress);

        assertEquals(expectedCustomerId, customerId, "Customer ID should match the expected value");
        verify(mockPreparedStatement).setString(1, businessName);
        verify(mockPreparedStatement).setString(2, emailAddress);
        verify(mockPreparedStatement).executeQuery();
    }
    
    /**
     * Tests retrieving a customer ID by business name and email when the customer does not exist.
     */
    @Test
    public void testGetCustomerIdInvalid() throws SQLException {
        String businessName = "Nonexistent Business";
        String emailAddress = "nonexistent@business.com";

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate customer not found

        int customerId = dao.getCustomerId(businessName, emailAddress);

        assertEquals(-1, customerId, "Customer ID should be -1 when no customer is found");
        verify(mockPreparedStatement).setString(1, businessName);
        verify(mockPreparedStatement).setString(2, emailAddress);
        verify(mockPreparedStatement).executeQuery();
    }
    
}
