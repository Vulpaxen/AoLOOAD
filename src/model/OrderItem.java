package model;

public class OrderItem {
	private int orderId;
	private int menuItemId;
	private int quantity;
	
	public OrderItem(int orderId, int menuItemId, int quantity) {
		this.orderId = orderId;
		this.menuItemId = menuItemId;
		this.quantity = quantity;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItem(int menuItemId) {
		this.menuItemId = menuItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
