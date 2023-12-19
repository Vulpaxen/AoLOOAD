package controller;

import java.sql.Date;
import java.util.ArrayList;

import model.Order;
import model.Receipt;

public class ReceiptController {
	
	public static void createReceipt(Order order, String receiptPaymentType, Date receiptPaymentDate,int receiptPaymentAmount) {
		Receipt.createReceipt(order, receiptPaymentType, receiptPaymentDate,receiptPaymentAmount);
	}
	
	public static void deleteReceipt(Order orderId) {
        Receipt.deleteReceipt(orderId);
    }
	
	public static Receipt getReceiptByid(int receiptId) {
		return Receipt.getReceiptByid(receiptId);
	}
	
	public static ArrayList<Receipt> getAllReceipts() {
        return Receipt.getAllReceipts();
    }
}
