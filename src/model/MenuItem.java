package model;

public class MenuItem {
	public String menuItemId;
	public String menuItemName;
	public String menuItemDescription;
	public int menuItemPrice;
	
	public MenuItem(String menuItemName, String menuItemDescription, int menuItemPrice) {
		this.menuItemName = menuItemName;
		this.menuItemDescription = menuItemDescription;
		this.menuItemPrice = menuItemPrice;
	}
}
