package homeappliance;

/**
 * A ApplianceItem constructor class.
 * Each appliance item is associated with a specific home appliance 
 * and includes additional details such as warranty years, brand, and model.
 * 
 * This class provides getter and setter methods to access and modify 
 * the fields, as well as a custom `toString` method for a readable string.
 * 
 * 
 * @author Amrit Singh
 * @version 5/1/2025
 */

public class ApplianceItem {
	
    /**
     * Unique ID for the appliance item.
     */
	private int id;
	
    /**
     * The home appliance associated with this appliance item.
     */
	private HomeAppliance homeAppliance;
	
    /**
     * Number of warranty years for the appliance item.
     */
	private int warrantyYears;
	
    /**
     * Brand name of the appliance item.
     */
	private String brand;
	
    /**
     * Model of the appliance item.
     */
	private String model;
	
	/**
     * Constructs a new ApplianceItem with the specified details.
     * 
     * @param homeAppliance the associated home appliance
     * @param warrantyYears the number of warranty years
     * @param brand the brand of the appliance item
     * @param model the model of the appliance item
     */
	public ApplianceItem(HomeAppliance homeAppliance, int warrantyYears, String brand, String model) {
		this.homeAppliance = homeAppliance;
		this.warrantyYears = warrantyYears;
		this.brand = brand;
		this.model = model;
	}
	
	/**
     * Returns the unique ID of the appliance item.
     * 
     * @return the unique ID
     */
	public int getId() {
		return id;
	}
	
	/**
     * Returns the associated home appliance.
     * 
     * @return the associated HomeAppliance object
     */
	public HomeAppliance getHomeAppliance() {
		return homeAppliance;
	}
	
    /**
     * Returns the number of warranty years for the appliance item.
     * 
     * @return the number of warranty years
     */
	public int getWarrantyYears() {
		return warrantyYears;
	}
	
    /**
     * Returns the brand of the appliance item.
     * 
     * @return the brand name
     */
	public String getBrand() {
		return brand;
	}
	
    /**
     * Returns the model of the appliance item.
     * 
     * @return the model name
     */
	public String getModel() {
		return model;
	}
	
    /**
     * Sets the unique ID of the appliance item.
     * 
     * @param id the new ID to set
     */
	public void setId(int id) {
		this.id = id;
	}
	
    /**
     * Sets the associated home appliance.
     * 
     * @param homeAppliance the HomeAppliance object to associate with this item
     */
	public void setHomeAppliance(HomeAppliance homeAppliance) {
		this.homeAppliance = homeAppliance;
	}
	
    /**
     * Sets the number of warranty years for the appliance item.
     * 
     * @param warrantyYears the number of warranty years to set
     */
	public void setWarrantyYears(int warrantyYears) {
		this.warrantyYears = warrantyYears;
	}
	
    /**
     * Sets the brand of the appliance item.
     * 
     * @param brand the brand name to set
     */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
    /**
     * Sets the model of the appliance item.
     * 
     * @param model the model name to set
     */
	public void setModel(String model) {
		this.model = model;
	}
	
    /**
     * Provides a string representation of the ApplianceItem object.
     * The string includes the ID, associated home appliance, warranty years, brand, and model.
     * 
     * @return a formatted string representation of the appliance item
     */
	@Override
	public String toString() {
		return "ID: " + id + ", Home Appliance: " + homeAppliance + ", Warranty: " + warrantyYears + ", Brand: " + brand + ", Model: " + model;
	}

}
