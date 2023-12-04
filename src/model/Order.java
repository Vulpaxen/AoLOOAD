package model;

import java.sql.Date;

public class Order {
	private int orderId;
	private User orderUser;
	private OrderItem orderItem;
	private String orderStatus;
	private Date orderDate;
	private int orderTotal;
	
	public Order(int orderId, User orderUser, OrderItem orderItem, String orderStatus, Date orderDate, int orderTotal) {
		super();
		this.orderId = orderId;
		this.orderUser = orderUser;
		this.orderItem = orderItem;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
		this.orderTotal = orderTotal;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public User getOrderUser() {
		return orderUser;
	}

	public void setOrderUser(User orderUser) {
		this.orderUser = orderUser;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(int orderTotal) {
		this.orderTotal = orderTotal;
	}
	
}
