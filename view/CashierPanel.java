package view;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import controller.OrderController;
import controller.OrderItemController;
import controller.ReceiptController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Order;
import model.OrderItem;
import model.Receipt;

public class CashierPanel extends Stage{
	
	private BorderPane borderPane = new BorderPane();
	private VBox root1 = new VBox(20);
	private VBox root2 = new VBox(20);
	private MenuBar menuBar = new MenuBar();
	private Scene scene;
	Label totalUpdateLabel;
	
	public CashierPanel() {
		super(StageStyle.DECORATED);
		this.setTitle("Cashier Dashboard");
		
		scene = new Scene(borderPane, 1000, 800);
		this.setScene(scene);
		
		viewOrderCashier();
		Menu orderMenu = new Menu("View Order");
		MenuItem orderMenuItem = new MenuItem("View Order");
		Menu receiptMenu = new Menu("View Receipt");
		MenuItem receiptMenuItem = new MenuItem("View Receipt");
		
		orderMenu.getItems().add(orderMenuItem);
		receiptMenu.getItems().add(receiptMenuItem);
		
		menuBar.getMenus().addAll(orderMenu, receiptMenu);
		
		orderMenuItem.setOnAction(e -> {
			viewOrderCashier();
		});
		
		receiptMenuItem.setOnAction(e -> {
			
		});
		
		borderPane.setTop(menuBar);
		borderPane.setLeft(root1);
		root1.setAlignment(Pos.CENTER);
		root1.setPadding(new Insets(20));
		root1.setPrefHeight(getMaxHeight());
		root1.setStyle("-fx-background-color: lightgray;");

		borderPane.setCenter(root2);
		root2.setAlignment(Pos.CENTER);
		root2.setPadding(new Insets(20));
	}
	
	TableView<Order> servedOrders = createServedTable();
	
	private void viewOrderCashier() {
		root1.getChildren().clear();
		root2.getChildren().clear();
		
		root1.getChildren().add(servedOrders);
		if (servedOrders.getSelectionModel().isEmpty()) {
			Label selectOrderLabel = new Label("Select an order from the table to see order details.");
			root2.getChildren().add(selectOrderLabel);
		} else {
			selectedOrder = servedOrders.getSelectionModel().getSelectedItem();
			showOrderDetails(selectedOrder);
		}
	}

	private Order selectedOrder;
	private TableView<Order> createServedTable() {
		// TODO Auto-generated method stunt
		TableView<Order> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<Order, Integer> orderId = new TableColumn<>("Order ID");
		orderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));

		TableColumn<Order, String> orderStatus = new TableColumn<>("Status");
		orderStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

		TableColumn<Order, Date> orderDate = new TableColumn<>("Date");
		orderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

		TableColumn<Order, Double> orderTotal = new TableColumn<>("Total Order Price");
		orderTotal.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

		table.getColumns().add(orderId);
		table.getColumns().add(orderStatus);
		table.getColumns().add(orderDate);
		table.getColumns().add(orderTotal);

		table.setPrefHeight(1200);

		table.setMinHeight(700);
		table.setMinWidth(400);

		table.setItems(FXCollections.observableArrayList(OrderController.getAllServedOrders()));

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedOrder = newSelection;
				showOrderDetails(selectedOrder);
			}
		});

		return table;
	}

	private void showOrderDetails(Order order) {
		TableView<OrderItem> orderItemTable = null;
		root2.getChildren().clear();
		if (order != null) {
			orderItemTable = createOrdersByOrderIdTable(order);
			root2.getChildren().add(orderItemTable);

			double totalOrderPrice = order.getOrderTotal();
			totalUpdateLabel = new Label("Total Price: " + totalOrderPrice);
			root2.getChildren().add(totalUpdateLabel);

			root2.getChildren().add(createProcessOrderForm(orderItemTable));
		}
	}

	private TableView<OrderItem> createOrdersByOrderIdTable(Order order) {
		TableView<OrderItem> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<OrderItem, String> itemName = new TableColumn<>("Item Name");
		itemName.setCellValueFactory(orderItem -> new SimpleStringProperty(orderItem.getValue().getMenuItemName()));
		itemName.setPrefWidth(150);

		TableColumn<OrderItem, Integer> itemQuantity = new TableColumn<>("Quantity");
		itemQuantity.setCellValueFactory(
				orderItem -> new SimpleIntegerProperty(orderItem.getValue().getQuantity()).asObject());
		itemQuantity.setPrefWidth(50);

		TableColumn<OrderItem, Double> itemTotalPrice = new TableColumn<>("Total Price");
		itemTotalPrice.setCellValueFactory(orderItem -> new SimpleDoubleProperty(
				orderItem.getValue().getQuantity() * orderItem.getValue().getMenuItem().getMenuItemPrice()).asObject());
		itemTotalPrice.setPrefWidth(100);

		ArrayList<OrderItem> orderItem = OrderItemController.getAllOrderItemsByOrderId(selectedOrder.getOrderId());

		table.setItems(FXCollections.observableArrayList(orderItem));

		table.getColumns().addAll(itemName, itemQuantity, itemTotalPrice);

		return table;
	}

	private GridPane createProcessOrderForm(TableView<OrderItem> orderItemTable) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button prepareOrderButton = new Button("Process Order");
		TextField paymentType = new TextField();
		TextField paymentAmount = new TextField();
		
		paymentType.setPromptText("Cash / Debit / Credit");
		paymentAmount.setPromptText("Payment Amount");
		
		form.add(new Label("Payment Type:"), 0, 0);
		form.add(paymentType, 1, 0);
		form.add(new Label("Payment amount:"), 0, 1);
		form.add(paymentAmount, 1, 1);
		form.add(prepareOrderButton, 0, 2);
		
		prepareOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				OrderItem selectedOrderItem = orderItemTable.getSelectionModel().getSelectedItem();
				if (selectedOrder != null) {
					//Check paymenttype
					try {
						String payment = paymentType.getText();
						int amount = Integer.parseInt(paymentAmount.getText());
						
						int totalPrice = 0;
						for(OrderItem oi:selectedOrder.getOrderItem()) {
							totalPrice += (oi.getMenuItem().getMenuItemPrice() * oi.getQuantity());
							
						}
						ArrayList<OrderItem> orderItems = OrderItemController
								.getAllOrderItemsByOrderId(selectedOrder.getOrderId());
						if(amount < totalPrice) {
							showAlert("insufficient payment","Please input more than payment");
						}else if(payment.equals("Cash") || payment.equals("Debit") || payment.equals("Credit")) {
							
							OrderController.updateOrder(selectedOrder.getOrderId(), orderItems, "Paid");
							refreshOrderedTable();
							showAlert("Order Paid", "Selected order has been paid.");
							
							//TODO: masukin ke receipt
							
							Date date = new Date(System.currentTimeMillis());
							ReceiptController.createReceipt(selectedOrder, payment, date, amount);
						}else {
							showAlert("Payment Type Invalid", "Please select either Cash/Credit/Debit");
						}
						
						
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
				} else {
					showAlert("No Order Selected", "Please select a pending order to prepare.");
				}
				viewOrderCashier();
			}
			
			
		});
		return form;
	}
	
	private void refreshOrderedTable() {
		servedOrders.setItems(FXCollections.observableArrayList(OrderController.getAllPendingOrders()));
	}
	
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
