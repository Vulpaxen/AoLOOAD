package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Receipt {
	private int receiptId;
	private String receiptOrder;
	private int receiptPaymentAmount;
	private Date receiptPaymentDate;
	private String receiptPaymentType;
	
	public Receipt(int receiptId, String receiptOrder, int receiptPaymentAmount, Date receiptPaymentDate,
			String receiptPaymentType) {
		super();
		this.receiptId = receiptId;
		this.receiptOrder = receiptOrder;
		this.receiptPaymentAmount = receiptPaymentAmount;
		this.receiptPaymentDate = receiptPaymentDate;
		this.receiptPaymentType = receiptPaymentType;
	}
	
	public void deleteReceipt(Order orderId) {
        String query = "DELETE FROM receipts WHERE orderId = ?";
        try (Connection connection = Connect.getInstance().getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
         ps.setInt(1, orderId.getOrderId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static Receipt getReceiptByid(int receiptId) {
		Receipt receipt = null;
		
		try(Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM receipts WHERE receiptId = ?;");
			ps.setInt(1,  receiptId);
			ResultSet resultSet = ps.executeQuery();
			
			if(resultSet.next()){
				int id = resultSet.getInt("receiptId");
				String order = resultSet.getString("receiptOrder");
				int paymentAmount = resultSet.getInt("receiptPaymentAmount");
				Date paymentDate = resultSet.getDate("receiptPaymentDate");
				String paymentType = resultSet.getString("receiptPaymentType");
				receipt = new Receipt(id, order, paymentAmount, paymentDate, paymentType);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return receipt;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

	public String getReceiptOrder() {
		return receiptOrder;
	}

	public void setReceiptOrder(String receiptOrder) {
		this.receiptOrder = receiptOrder;
	}

	public int getReceiptPaymentAmount() {
		return receiptPaymentAmount;
	}

	public void setReceiptPaymentAmount(int receiptPaymentAmount) {
		this.receiptPaymentAmount = receiptPaymentAmount;
	}

	public Date getReceiptPaymentDate() {
		return receiptPaymentDate;
	}

	public void setReceiptPaymentDate(Date receiptPaymentDate) {
		this.receiptPaymentDate = receiptPaymentDate;
	}

	public String getReceiptPaymentType() {
		return receiptPaymentType;
	}

	public void setReceiptPaymentType(String receiptPaymentType) {
		this.receiptPaymentType = receiptPaymentType;
	}
}
