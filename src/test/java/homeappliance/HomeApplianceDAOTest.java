package homeappliance;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link HomeApplianceDAO} class.
 * 
 * This test class validates the functionality of the data access object (DAO)
 * for home appliances. It uses Mockito for mocking database interactions.
 * 
 * Test scenarios include:
 * - Retrieving all products
 * - Searching products by attributes and keywords
 * - Adding, updating, and deleting products
 * - Filtering and sorting products
 * - Retrieving distinct categories
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

class HomeApplianceDAOTest {

    private HomeApplianceDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    /**
     * Sets up the DAO and mock objects before each test.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        dao = new HomeApplianceDAO() {
            @Override
            protected Connection connect() {
                return mockConnection;
            }
        };

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); 
    }

    /**
     * Tests retrieving all products when records are found in the database.
     */
    @Test
    public void testFindAllProductsValid() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("sku")).thenReturn("SKU1", "SKU2");
        when(mockResultSet.getString("description")).thenReturn("Dishwasher", "Refrigerator");
        when(mockResultSet.getString("category")).thenReturn("Kitchen", "Kitchen");
        when(mockResultSet.getInt("price")).thenReturn(500, 1500);

        ArrayList<HomeAppliance> products = dao.findAllProducts();

        assertNotNull(products, "Product list should not be null");
        assertEquals(2, products.size(), "Should have found two products");
        assertEquals("SKU1", products.get(0).getSku(), "First product SKU should match");
        assertEquals("Dishwasher", products.get(0).getDescription(), "First product description should match");
        assertEquals("Kitchen", products.get(0).getCategory(), "First product category should match");
        assertEquals(500, products.get(0).getPrice(), "First product price should match");

        assertEquals("SKU2", products.get(1).getSku(), "Second product SKU should match");
        assertEquals("Refrigerator", products.get(1).getDescription(), "Second product description should match");
        assertEquals("Kitchen", products.get(1).getCategory(), "Second product category should match");
        assertEquals(1500, products.get(1).getPrice(), "Second product price should match");
    }
    
    /**
     * Tests retrieving all products when no records are found.
     */
    @Test
    public void testFindAllProductsInvalid() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
        ArrayList<HomeAppliance> products = dao.findAllProducts();

        assertNotNull(products, "Product list should not be null");
        assertTrue(products.isEmpty(), "Product list should be empty when no products are found");
    }
    
    /**
     * Tests retrieving a specific product by ID when the product exists.
     */
    @Test
    public void testFindProductValid() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("sku")).thenReturn("SKU100");
        when(mockResultSet.getString("description")).thenReturn("A good product");
        when(mockResultSet.getString("category")).thenReturn("Electronics");
        when(mockResultSet.getInt("price")).thenReturn(300);

        HomeAppliance foundProduct = dao.findProduct(1);

        assertNotNull(foundProduct, "Product should not be null");
        assertEquals(1, foundProduct.getId());
        assertEquals("SKU100", foundProduct.getSku());
        assertEquals("A good product", foundProduct.getDescription());
        assertEquals("Electronics", foundProduct.getCategory());
        assertEquals(300, foundProduct.getPrice());
    }
    
    /**
     * Tests retrieving a specific product by ID when the product does not exist.
     */
    @Test
    public void testFindProductInvalid() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        HomeAppliance foundProduct = dao.findProduct(999);

        assertNull(foundProduct, "Product should be null when not found");
    }

    /**
     * Tests deleting a product by ID when the product exists.
     */
    @Test
    public void testDeleteProductValid() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = dao.deleteProduct(1);

        assertTrue(result, "Delete should return true when a product is successfully deleted");
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).executeUpdate();
    }
    
    /**
     * Tests deleting a product by ID when the product does not exist.
     */
    @Test
    public void testDeleteProductInvalid() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        boolean result = dao.deleteProduct(999);

        assertFalse(result, "Delete should return false when no rows are affected");
        verify(mockPreparedStatement).setInt(1, 999);
        verify(mockPreparedStatement).executeUpdate();
    }
    
    /**
     * Tests updating a product when it exists.
     */
    @Test
    public void testUpdateProductValid() throws SQLException {
        HomeAppliance updatedAppliance = new HomeAppliance("SKU200", "Updated Product", "Electronics", 400);
        updatedAppliance.setId(1); 
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); 

        boolean result = dao.updateProduct(updatedAppliance);

        assertTrue(result, "Update should return true when a row is affected");
        verify(mockPreparedStatement).setString(1, "SKU200");
        verify(mockPreparedStatement).setString(2, "Updated Product");
        verify(mockPreparedStatement).setString(3, "Electronics");
        verify(mockPreparedStatement).setInt(4, 400);
        verify(mockPreparedStatement).setInt(5, 1);
        verify(mockPreparedStatement).executeUpdate();
    }
    
    /**
     * Tests updating a product when it does not exist.
     */
    @Test
    public void testUpdateProductInvalid() throws SQLException {
        HomeAppliance updatedAppliance = new HomeAppliance("SKU200", "Updated Product", "Electronics", 400);
        updatedAppliance.setId(999);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        boolean result = dao.updateProduct(updatedAppliance);

        assertFalse(result, "Update should return false when no rows are affected");
        verify(mockPreparedStatement).setString(1, updatedAppliance.getSku());
        verify(mockPreparedStatement).setString(2, updatedAppliance.getDescription());
        verify(mockPreparedStatement).setString(3, updatedAppliance.getCategory());
        verify(mockPreparedStatement).setInt(4, updatedAppliance.getPrice());
        verify(mockPreparedStatement).setInt(5, 999);
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Tests adding a new product to the database.
     */
    @Test
    public void testAddProductValid() throws SQLException {
        HomeAppliance newAppliance = new HomeAppliance("SKU3", "Microwave", "Kitchen", 200);
        
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
        
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        ResultSet mockGeneratedKeys = mock(ResultSet.class);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
        when(mockGeneratedKeys.next()).thenReturn(true);
        when(mockGeneratedKeys.getInt(1)).thenReturn(1); 
        
        boolean result = dao.addProduct(newAppliance);
        
        assertTrue(result);
        verify(mockPreparedStatement).setString(1, newAppliance.getSku());
        verify(mockPreparedStatement).setString(2, newAppliance.getDescription());
        verify(mockPreparedStatement).setString(3, newAppliance.getCategory());
        verify(mockPreparedStatement).setInt(4, newAppliance.getPrice());
        verify(mockPreparedStatement).executeUpdate();
        verify(mockGeneratedKeys).getInt(1);
    }
    
    /**
     * Tests adding a product when the insert fails.
     */
    @Test
    public void testAddProductInvalid() throws SQLException {
        HomeAppliance newAppliance = new HomeAppliance("SKU3", "Microwave", "Kitchen", 200);
        
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        
        boolean result = dao.addProduct(newAppliance);
        
        assertFalse(result, "Result should be false when no rows are affected");
        verify(mockPreparedStatement).setString(1, newAppliance.getSku());
        verify(mockPreparedStatement).setString(2, newAppliance.getDescription());
        verify(mockPreparedStatement).setString(3, newAppliance.getCategory());
        verify(mockPreparedStatement).setInt(4, newAppliance.getPrice());
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Tests searching for products by a valid attribute and value.
     * Verifies that matching products are returned as expected.
     *
     */
    @Test
    public void testSearchProductsByAttributeValid() throws SQLException {
        String attribute = "category";
        String searchValue = "Kitchen";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("sku")).thenReturn("SKU1", "SKU2");
        when(mockResultSet.getString("description")).thenReturn("Blender", "Toaster");
        when(mockResultSet.getString("category")).thenReturn("Kitchen", "Kitchen");
        when(mockResultSet.getInt("price")).thenReturn(150, 100);

        ArrayList<HomeAppliance> appliances = dao.searchProductsByAttribute(attribute, searchValue);

        assertNotNull(appliances, "Returned list should not be null");
        assertEquals(2, appliances.size(), "Should return two appliances");
        assertEquals("Blender", appliances.get(0).getDescription());
        assertEquals("Toaster", appliances.get(1).getDescription());
    }

    /**
     * Tests searching for products by an attribute and value with no matches.
     * Verifies that an empty list is returned.
     *
     */
    @Test
    public void testSearchProductsByAttributeInvalid() throws SQLException {
        String attribute = "category";
        String searchValue = "Outdoor";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        ArrayList<HomeAppliance> appliances = dao.searchProductsByAttribute(attribute, searchValue);

        assertTrue(appliances.isEmpty(), "Returned list should be empty when no products are found");
    }

    /**
     * Tests sorting products by price in ascending order.
     * Verifies that products are returned in the correct order based on price.
     *
     */
    @Test
    public void testProductsSortedByPriceAscending() throws SQLException {
        when(mockConnection.prepareStatement(contains("ASC"))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("sku")).thenReturn("SKU1", "SKU2");
        when(mockResultSet.getString("description")).thenReturn("Toaster", "Microwave");
        when(mockResultSet.getString("category")).thenReturn("Kitchen", "Kitchen");
        when(mockResultSet.getInt("price")).thenReturn(100, 200);

        ArrayList<HomeAppliance> appliances = dao.productsSortedByPrice(true);

        assertNotNull(appliances, "Product list should not be null");
        assertEquals(2, appliances.size(), "Should return two appliances");
        assertEquals(100, appliances.get(0).getPrice(), "First product should have the lower price");
        assertEquals(200, appliances.get(1).getPrice(), "Second product should have the higher price");
    }

    /**
     * Tests sorting products by price in descending order.
     * Verifies that products are returned in the correct order based on price.
     *
     */
    @Test
    public void testProductsSortedByPriceDescending() throws SQLException {
        when(mockConnection.prepareStatement(contains("DESC"))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("sku")).thenReturn("SKU1", "SKU2");
        when(mockResultSet.getString("description")).thenReturn("Microwave", "Toaster");
        when(mockResultSet.getString("category")).thenReturn("Kitchen", "Kitchen");
        when(mockResultSet.getInt("price")).thenReturn(200, 100);

        ArrayList<HomeAppliance> appliances = dao.productsSortedByPrice(false);

        assertNotNull(appliances, "Product list should not be null");
        assertEquals(2, appliances.size(), "Should return two appliances");
        assertEquals(200, appliances.get(0).getPrice(), "First product should have the higher price");
        assertEquals(100, appliances.get(1).getPrice(), "Second product should have the lower price");
    }

    /**
     * Tests filtering products by a valid attribute value.
     * Verifies that the method returns the correct list of matching products.
     *
     */
    @Test
    public void testFilterProductsByAttributeValid() throws SQLException {
        String attribute = "category";
        String value = "Electronics";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(10, 20);
        when(mockResultSet.getString("sku")).thenReturn("SKU10", "SKU20");
        when(mockResultSet.getString("description")).thenReturn("Camera", "Laptop");
        when(mockResultSet.getString("category")).thenReturn("Electronics", "Electronics");
        when(mockResultSet.getInt("price")).thenReturn(800, 1500);

        ArrayList<HomeAppliance> appliances = dao.filterProductsByAttribute(attribute, value);

        assertNotNull(appliances, "Product list should not be null");
        assertEquals(2, appliances.size(), "Should return two appliances");
        assertEquals("Camera", appliances.get(0).getDescription());
        assertEquals("Laptop", appliances.get(1).getDescription());
    }

    /**
     * Tests filtering products by an invalid attribute value.
     * Verifies that the method returns an empty list when no matches are found.
     *
     */
    @Test
    public void testFilterProductsByAttributeInvalid() throws SQLException {
        String attribute = "category";
        String value = "Automotive";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        ArrayList<HomeAppliance> appliances = dao.filterProductsByAttribute(attribute, value);

        assertTrue(appliances.isEmpty(), "Returned list should be empty when no products are found");
    }

    /**
     * Tests filtering products by a valid category.
     * Verifies that the method returns the correct list of matching products.
     *
     */
    @Test
    public void testFilterProductsByCategoryValid() throws SQLException {
        String category = "Kitchen";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("sku")).thenReturn("SKU1", "SKU2");
        when(mockResultSet.getString("description")).thenReturn("Toaster", "Blender");
        when(mockResultSet.getString("category")).thenReturn("Kitchen", "Kitchen");
        when(mockResultSet.getInt("price")).thenReturn(100, 150);

        ArrayList<HomeAppliance> appliances = dao.filterProductsByCategory(category);

        assertNotNull(appliances, "Product list should not be null");
        assertEquals(2, appliances.size(), "Should return two appliances");
        assertEquals("Toaster", appliances.get(0).getDescription());
        assertEquals("Blender", appliances.get(1).getDescription());
    }

    /**
     * Tests filtering products by an invalid category.
     * Verifies that the method returns an empty list when no matches are found.
     *
     */
    @Test
    public void testFilterProductsByCategoryInvalid() throws SQLException {
        String category = "Outdoor";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        ArrayList<HomeAppliance> appliances = dao.filterProductsByCategory(category);

        assertTrue(appliances.isEmpty(), "Returned list should be empty when no products are found");
    }

    /**
     * Tests retrieving distinct categories.
     */
    @Test
    public void testFindAllCategoriesValid() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); 
        when(mockResultSet.getString("category")).thenReturn("Kitchen", "Electronics");

        List<String> categories = dao.findAllCategories();

        assertNotNull(categories, "Categories list should not be null");
        assertEquals(2, categories.size(), "Should return two distinct categories");
        assertTrue(categories.contains("Kitchen"), "List should contain 'Kitchen'");
        assertTrue(categories.contains("Electronics"), "List should contain 'Electronics'");
    }

    /**
     * Tests retrieving distinct categories when none exist.
     */
    @Test
    public void testFindAllCategoriesInvalid() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 

        List<String> categories = dao.findAllCategories();

        assertTrue(categories.isEmpty(), "Returned list should be empty when no categories are found");
    }
    
    /**
     * Tests searching for products by a keyword when matches are found.
     */
    @Test
    public void testSearchProductsByKeywordValid() throws SQLException {
        String keyword = "test";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("sku")).thenReturn("TEST1", "CODE2");
        when(mockResultSet.getString("description")).thenReturn("Test product", "Another test item");
        when(mockResultSet.getString("category")).thenReturn("Electronics", "Hardware");
        when(mockResultSet.getInt("price")).thenReturn(150, 300);

        ArrayList<HomeAppliance> appliances = dao.searchProductsByKeyword(keyword);

        assertNotNull(appliances, "Product list should not be null");
        assertEquals(2, appliances.size(), "Should return two matching products");
        assertTrue(appliances.get(0).getDescription().contains("Test"), "First product should match the keyword");
        assertTrue(appliances.get(1).getDescription().contains("test"), "Second product should match the keyword");
    }
    
    /**
     * Tests searching for products by a keyword when no matches are found.
     */
    @Test
    public void testSearchProductsByKeywordInvalid() throws SQLException {
        String keyword = "nonexistent";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        ArrayList<HomeAppliance> appliances = dao.searchProductsByKeyword(keyword);

        assertTrue(appliances.isEmpty(), "Returned list should be empty when no products are found");
    }

}
