package controller;

import java.util.ArrayList;
import model.OrderItem;

public class OrderItemController {
	
    public static void createOrderItem(int orderId, int menuItemId, int quantity) {
    	OrderItem.createOrderItem(orderId, menuItemId, quantity);
    }
    
    public static void updateOrderItem(int orderId, int menuItemId, int quantity) {
    	OrderItem.updateOrderItem(orderId, menuItemId, quantity);
    }
    
    public static void deleteOrderItem(int orderId, int menuItemId) {
    	OrderItem.deleteOrderItem(orderId, menuItemId);
    }
    
    public static ArrayList<OrderItem> getAllOrderItemsByOrderId(int orderId) {
        return OrderItem.getAllOrderItemsByOrderId(orderId);
    }
}
