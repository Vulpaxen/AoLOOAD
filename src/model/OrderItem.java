package model;

public class OrderItem {
	public String orderId;
	public String menuItem;
	public int quantity;
	
	public OrderItem(String orderId, String menuItem, int quantity) {
		this.orderId = orderId;
		this.menuItem = menuItem;
		this.quantity = quantity;
	}
}
