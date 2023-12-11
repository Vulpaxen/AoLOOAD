package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Connect;
import model.MenuItem;
import model.OrderItem;
import model.User;

public class OrderItemController {
	private ArrayList<OrderItem> orderItems;
	
    public void createOrderItem(int orderId, int menuItemId, int quantity) {
    	OrderItem.createOrderItem(orderId, menuItemId, quantity);
    }
    
    public void updateOrderItem(int orderId, int menuItemId, int quantity) {
    	OrderItem.updateOrderItem(orderId, menuItemId, quantity);
    }
    
    public void deleteOrderItem(int orderId, int menuItemId) {
    	OrderItem.deleteOrderItem(orderId, menuItemId);
    }
    
    public ArrayList<OrderItem> getAllOrderItemsByOrderId(int orderId) {
        return OrderItem.getAllOrderItemsByOrderId(orderId);
    }
}
