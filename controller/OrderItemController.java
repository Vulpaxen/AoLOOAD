package controller;

import java.util.ArrayList;

import model.MenuItem;
import model.OrderItem;

public class OrderItemController {
	
    public static void createOrderItem(int orderId, MenuItem menuItem, int quantity) {
    	OrderItem.createOrderItem(orderId, menuItem, quantity);
    }
    
    public static void updateOrderItem(int orderId, MenuItem menuItem, int quantity) {
    	OrderItem.updateOrderItem(orderId, menuItem, quantity);
    }
    
    public static void deleteOrderItem(int orderId, int menuItemId) {
    	OrderItem.deleteOrderItem(orderId, menuItemId);
    }
    
    public static ArrayList<OrderItem> getAllOrderItemsByOrderId(int orderId) {
        return OrderItem.getAllOrderItemsByOrderId(orderId);
    }
}
