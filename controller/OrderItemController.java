package controller;

import java.util.ArrayList;

import model.MenuItem;
import model.OrderItem;

public class OrderItemController {

	public static boolean createOrderItem(int orderId, MenuItem menuItem, int quantity) {
		if (quantity <= 0) {
			return false;
		}
		OrderItem.createOrderItem(orderId, menuItem, quantity);
		return true;
	}

	public static boolean updateOrderItem(int orderId, MenuItem menuItem, int quantity) {
		if (quantity <= 0) {
			return false;
		}
		OrderItem.updateOrderItem(orderId, menuItem, quantity);
		return true;
	}

	public static void deleteOrderItem(int orderId, int menuItemId) {
		OrderItem.deleteOrderItem(orderId, menuItemId);
	}

	public static ArrayList<OrderItem> getAllOrderItemsByOrderId(int orderId) {
		return OrderItem.getAllOrderItemsByOrderId(orderId);
	}
}
