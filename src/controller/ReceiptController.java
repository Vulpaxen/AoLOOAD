package controller;

import model.OrderItem;

public class ReceiptController {
	//createOrderItem(orderId, menuItem, quantity)
	
	public void createReceipt(int orderId, String menuItem, int quantity) {
		
	}
	public void createOrderItem(int orderId, String menuItem, int quantity) {
        OrderItem newOrderItem = new OrderItem(orderId, menuItem, quantity);
        orderItems.add(newOrderItem);
        System.out.println("OrderItem created: " + newOrderItem);
    }
}
