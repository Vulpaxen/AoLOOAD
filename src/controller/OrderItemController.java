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
    	/*
 CREATE TABLE `orderitem` (
  `orderItemId` int(11) NOT NULL,
  `orderId` int(11) NOT NULL,
  `menuItemId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
)
    	 */
    	// OrderItem newOrderItem = new OrderItem(orderId, menuItemId, quantity);
     //    orderItems.add(newOrderItem);
     //    System.out.println("OrderItem created: " + newOrderItem);
        
        String query = "INSERT INTO orderitem (menuItemId, quantity) VALUES (?, ?)";
    	try (Connection connection = Connect.getInstance().getConnection();
    	  PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1, menuItemId);
    		ps.setInt(2, quantity);
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
    }
    
    public void updateOrderItem(int orderId, int menuItemId, int quantity) {
    	// for (OrderItem orderItem : orderItems) {
     //        if (orderItem.getOrderId() == orderId && orderItem.getMenuItemId() == (menuItemId)) {
     //            orderItem.setQuantity(quantity);
     //            System.out.println("Order Item updated: " + orderItem);
     //            return;
     //        }
     //    }
     //    System.out.println("Item not found");
    	
    	String query = "UPDATE orderitem SET menuItemId = ?, quantity = ? WHERE orderItemId = ?";
    	try (Connection connection = Connect.getInstance().getConnection();
    		PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1, menuItemId);
    		ps.setInt(2, quantity);
    		ps.setInt(3, orderId);
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
    }
    
    public void deleteOrderItem(int orderId, int menuItemId) {
    	// for (int i = 0; i < orderItems.size(); i++) {
     //        OrderItem item = orderItems.get(i);
     //        if (item.getOrderId() == (orderId)) {
     //        	orderItems.remove(i);
     //            System.out.println("Order Item deleted: " + item);
     //            return;
     //        }
     //    }
     //    System.out.println("Order not found with ID: " + orderId);
	    
    	String query = "DELETE FROM orderitem WHERE orderItemId = ?";
        try (Connection connection = Connect.getInstance().getConnection();
        	PreparedStatement ps = connection.prepareStatement(query)) {
        	ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<OrderItem> getAllOrderItemsByOrderId(int orderId) {
        ArrayList<OrderItem> orderItemsList = new ArrayList<>();
        
//        for (OrderItem orderItem : orderItems) {
//            if (orderItem.getOrderId() == (orderId)) {
//                orderItemsList.add(orderItem);
//            }
//        }
//        
//        for (OrderItem orderItem : orderItemsList) {
//            System.out.println("OrderItem: " + orderItem.getMenuItemId() + ", Quantity: " + orderItem.getQuantity());
//        }
        
        String query = "SELECT * FROM orderitem";
        try (Connection connection = Connect.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
				int id = resultSet.getInt("orderItemId");
				int menuItemId = resultSet.getInt("menuItemId");
				
				MenuItemController menuItemController = new MenuItemController();
				MenuItem menuItem = menuItemController.getMenuItemById(menuItemId);
				
				int quantity = resultSet.getInt("quantity");
				
                OrderItem orderItem = new OrderItem(id,menuItem,quantity);
                orderItemsList.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItemsList;
    }
}
