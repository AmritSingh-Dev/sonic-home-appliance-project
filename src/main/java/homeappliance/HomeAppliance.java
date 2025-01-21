package homeappliance;

/**
 * A home appliance constructor class with ID, SKU, description, category, and price attributes.
 * This class also provides getters and setters for each field and a custom toString method.
 *
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */
public class HomeAppliance {

    /**
     * Unique ID for the home appliance.
     */
    private int id;

    /**
     * Stock Keeping Unit identifier.
     */
    private String sku;

    /**
     * Description of the home appliance.
     */
    private String description;

    /**
     * Category of the home appliance.
     */
    private String category;

    /**
     * Price of the home appliance in integer format.
     */
    private int price;

    /**
     * Constructs a new HomeAppliance with the specified details.
     * The ID constructor has been left out due to it being auto incrementing within the database.
     *
     * @param sku         the SKU of the appliance
     * @param description the description of the appliance
     * @param category    the category of the appliance
     * @param price       the price of the appliance
     */
    public HomeAppliance(String sku, String description, String category, int price) {
        this.sku = sku;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    /**
     * Returns the ID of the home appliance.
     *
     * @return the ID of the appliance
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the SKU of the home appliance.
     *
     * @return the SKU of the appliance
     */
    public String getSku() {
        return sku;
    }

    /**
     * Returns the description of the home appliance.
     *
     * @return the description of the appliance
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the category of the home appliance.
     *
     * @return the category of the appliance
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the price of the home appliance.
     *
     * @return the price of the appliance
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the ID of the home appliance.
     *
     * @param id the new ID of the appliance
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the SKU of the home appliance.
     *
     * @param sku the new SKU of the appliance
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * Sets the description of the home appliance.
     *
     * @param description the new description of the appliance
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the category of the home appliance.
     *
     * @param category the new category of the appliance
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the price of the home appliance.
     *
     * @param price the new price of the appliance
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the home appliance, including its ID, SKU, description, category, and price.
     *
     * @return a string representation of the appliance
     */
    @Override
    public String toString() {
        return "ID: " + id + ", SKU: " + sku + ", Description: " + description + ", Category: " + category + ", Price: " + price;
    }
}
