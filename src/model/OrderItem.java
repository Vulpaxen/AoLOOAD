package model;

public class OrderItem {
	private int orderId;
	private String menuItem;
	private int quantity;
	
	public OrderItem(int orderId, String menuItem, int quantity) {
		this.orderId = orderId;
		this.menuItem = menuItem;
		this.quantity = quantity;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
