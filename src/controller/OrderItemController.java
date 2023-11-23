package controller;

import java.util.ArrayList;

import model.OrderItem;

public class OrderItemController {
	private ArrayList<OrderItem> orderItems;
	
    public void createOrderItem(String orderId, String menuItem, int quantity) {
        OrderItem newOrderItem = new OrderItem(orderId, menuItem, quantity);
        orderItems.add(newOrderItem);
        System.out.println("OrderItem created: " + newOrderItem);
    }
    
    public void updateOrderItem(String orderId, String menuItem, int quantity) {
        for (OrderItem orderItem : orderItems) {
            if (orderItem.orderId.equals(orderId) && orderItem.menuItem.equals(menuItem)) {
                orderItem.quantity = quantity;
                System.out.println("Order Item updated: " + orderItem);
                return;
            }
        }
        System.out.println("Item not found");
    }
    
    public void deleteOrderItem(String orderId, String menuItem) {
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            if (item.orderId.equals(orderId)) {
            	orderItems.remove(i);
                System.out.println("Order Item deleted: " + item);
                return;
            }
        }
        System.out.println("Order not found with ID: " + orderId);
    }
    
    public ArrayList<OrderItem> getAllOrderItemsByOrderId(String orderId) {
        ArrayList<OrderItem> orderItemsByOrderId = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.orderId.equals(orderId)) {
                orderItemsByOrderId.add(orderItem);
            }
        }
        
        for (OrderItem orderItem : orderItemsByOrderId) {
            System.out.println("OrderItem: " + orderItem.menuItem + ", Quantity: " + orderItem.quantity);
        }
        return orderItemsByOrderId;
    }
    
}
