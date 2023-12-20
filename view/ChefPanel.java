package view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import controller.MenuItemController;
import controller.OrderController;
import controller.OrderItemController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.User;

public class ChefPanel extends Stage {

	private BorderPane borderPane = new BorderPane();
	private VBox root1 = new VBox(20);
	private VBox root2 = new VBox(20);
	private VBox root3 = new VBox(20);
	private Scene scene;

	public ChefPanel(User user) {

		super(StageStyle.DECORATED);
		this.setTitle("Chef Dashboard");

		scene = new Scene(borderPane, 1400, 800);
		this.setScene(scene);

		viewOrdered(user);

		borderPane.setLeft(root1);
		root1.setAlignment(Pos.CENTER);
		root1.setPadding(new Insets(20));
		root1.setPrefHeight(getMaxHeight());
		root1.setStyle("-fx-background-color: lightgray;");

		borderPane.setCenter(root2);
		root2.setAlignment(Pos.CENTER);
		root2.setPadding(new Insets(20));
		
		borderPane.setRight(root3);
		root3.setAlignment(Pos.CENTER);
		root3.setPadding(new Insets(20));

	}

	private TableView<OrderItem> createOrderDetails() {
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

		table.getColumns().addAll(List.of(itemName, itemQuantity, itemTotalPrice));

		return table;
	}

	TableView<OrderItem> orderDetails = createOrderDetails();

	private void viewOrdered(User user) {
		// TODO Auto-generated method stub
		root1.getChildren().clear();
		root2.getChildren().clear();
		TableView<Order> pendingOrders = createOrderedTable(user);

		root1.getChildren().add(pendingOrders);

		if (pendingOrders.getSelectionModel().isEmpty()) {
			Label selectOrderLabel = new Label("Select an order from the table to see order details.");
			root2.getChildren().add(selectOrderLabel);
		} else {
			selectedOrder = pendingOrders.getSelectionModel().getSelectedItem();
			showOrderDetails(selectedOrder, pendingOrders, user);
		}
	}

	private TableView<OrderItem> createOrdersByOrderIdTable(Order OrderId) {
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

		table.getColumns().addAll(List.of(itemName, itemQuantity, itemTotalPrice));

		return table;

	}

	Label totalUpdateLabel;

	private Order selectedOrder;

	private TableView<Order> createOrderedTable(User user) {
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

		table.setItems(FXCollections.observableArrayList(OrderController.getAllPendingOrders()));

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedOrder = newSelection;
				showOrderDetails(selectedOrder, table, user);
			}
		});

		return table;
	}

	private TextField updateItemQuantity = new TextField();

	private GridPane createUpdateOrderForm(Order selectedOrder, TableView<OrderItem> orderItemTable,
			TableView<MenuItem> tableMenuItem, TableView<Order> tableOrdered, User user) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button prepareOrderButton = new Button("Prepare Order");
		Button updateOrderButton = new Button("Update Order");
		Button removeOrderButton = new Button("Remove Order");

		form.add(new Label("Quantity:"), 0, 0);
		form.add(updateItemQuantity, 1, 0);

		form.add(prepareOrderButton, 0, 1);
		form.add(updateOrderButton, 0, 2);
		form.add(removeOrderButton, 0, 3);

		orderItemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				updateItemQuantity.setText(String.valueOf(newSelection.getQuantity()));
			}
		});
		prepareOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (selectedOrder != null) {
					ArrayList<OrderItem> orderItems = OrderItemController
							.getAllOrderItemsByOrderId(selectedOrder.getOrderId());
					OrderController.updateOrder(selectedOrder.getOrderId(), orderItems, "Prepared");
					tableOrdered.setItems(FXCollections
							.observableArrayList(OrderController.getOrderByCustomerId(user.getUserId())));

					showAlert("Order Prepared", "Selected order has been prepared.");
				} else {
					showAlert("No Order Selected", "Please select a pending order to prepare.");
				}
				viewOrdered(user);
			}
		});
		updateOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				OrderItem selectedOrderItem = orderItemTable.getSelectionModel().getSelectedItem();
				if (selectedOrderItem != null) {
					int newQuantity = Integer.parseInt(updateItemQuantity.getText());
					System.out.println(selectedOrderItem.getOrderId());
					System.out.println(selectedOrderItem.getMenuItemId());

					if (newQuantity < 0) {
						OrderItem.deleteOrderItem(selectedOrderItem.getOrderId(), selectedOrderItem.getMenuItemId());

						orderItemTable.getItems().remove(selectedOrderItem);

						totalUpdateLabel
								.setText("Total Price: " + Order.getTotalByOrderId(selectedOrderItem.getOrderId()));

						tableOrdered.setItems(FXCollections
								.observableArrayList(OrderController.getOrderByCustomerId(user.getUserId())));

					} else {
						selectedOrderItem.setQuantity(newQuantity);
						OrderItem.updateOrderItem(selectedOrderItem.getOrderId(), selectedOrderItem.getMenuItem(),
								newQuantity);

						orderItemTable.refresh();

						totalUpdateLabel
								.setText("Total Price: " + Order.getTotalByOrderId(selectedOrderItem.getOrderId()));

						tableOrdered.setItems(FXCollections
								.observableArrayList(OrderController.getOrderByCustomerId(user.getUserId())));

					}

					updateItemQuantity.clear();
				}
			}
		});
		removeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TableView<Order> pendingOrders = createOrderedTable(user);
				Order selectedOrder = pendingOrders.getSelectionModel().getSelectedItem();
				if (selectedOrder != null) {
					int selectedOrderId = selectedOrder.getOrderId();

					OrderController.deleteOrder(selectedOrderId);

					tableOrdered.setItems(FXCollections
							.observableArrayList(OrderController.getOrderByCustomerId(user.getUserId())));

				} else {
					showAlert("No Order Item Selected", "Please select an order item to remove.");
				}
			}
		});

		return form;
	}
	
	private TextField ItemId = new TextField();
	private TextField ItemName = new TextField();
	private TextField ItemDesc = new TextField();
	private TextField ItemPrice = new TextField();
	private TextField ItemQuantity = new TextField();
	
	private TableView<MenuItem> createMenuItemTable() {
		TableView<MenuItem> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<MenuItem, String> menuItemName = new TableColumn<>("Name");
		menuItemName.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));

		TableColumn<MenuItem, String> menuItemDesc = new TableColumn<>("Desc");
		menuItemDesc.setCellValueFactory(new PropertyValueFactory<>("menuItemDescription"));

		TableColumn<MenuItem, String> menuItemPrice = new TableColumn<>("Price");
		menuItemPrice.setCellValueFactory(new PropertyValueFactory<>("menuItemPrice"));

		table.getColumns().add(menuItemName);
		table.getColumns().add(menuItemDesc);
		table.getColumns().add(menuItemPrice);

		menuItemName.setPrefWidth(100);
		menuItemName.setPrefWidth(100);
		menuItemName.setPrefWidth(100);

		table.setPrefHeight(1200);

		table.setMinHeight(700);
		table.setMinWidth(400);

		table.setItems(FXCollections.observableArrayList(MenuItemController.getAllMenuItems()));

		// biar bisa select data-data
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				ItemId.setText(newSelection.getMenuItemId() + "");
				ItemName.setText(newSelection.getMenuItemName());
				ItemDesc.setText(newSelection.getMenuItemDescription());
				ItemPrice.setText(String.valueOf(newSelection.getMenuItemPrice()));
				ItemQuantity.setText("1");
			}
		});

		return table;
	}
	
	Label orderDetailLabel;
	Label chooseItemLabel;
	
	private void showOrderDetails(Order selectedOrder, TableView<Order> tableOrdered, User user) {
		TableView<OrderItem> orderItemTable = null;
		orderDetailLabel = new Label("Order Details");
		chooseItemLabel = new Label("Choose Menu Item To Add Item");

		root2.getChildren().clear(); //
		root3.getChildren().clear(); //
		root2.getChildren().add(orderDetailLabel);

		if (selectedOrder != null) {

			orderItemTable = createOrdersByOrderIdTable(selectedOrder);
			TableView<MenuItem> tableMenuItem = createMenuItemTable();
			if (selectedOrder.getOrderStatus().equals("Pending")) {

				root2.getChildren().add(orderItemTable);

				double totalOrderPrice = selectedOrder.getOrderTotal();
				totalUpdateLabel = new Label("Total Price: " + totalOrderPrice);
				root2.getChildren().add(totalUpdateLabel);

				root2.getChildren()
						.add(createUpdateOrderForm(selectedOrder, orderItemTable, tableMenuItem, tableOrdered, user));

				tableMenuItem.setStyle("-fx-background-color: lightblue;");
				root3.getChildren().add(chooseItemLabel);
				root3.getChildren().add(tableMenuItem);

			} else if (selectedOrder.getOrderStatus().equals("Served")
					|| selectedOrder.getOrderStatus().equals("Prepared")
					|| selectedOrder.getOrderStatus().equals("Paid")) {
				root2.getChildren().add(createOrdersByOrderIdTable(selectedOrder));

				Label statusLabel;
				double totalOrderPrice = selectedOrder.getOrderTotal();
				totalUpdateLabel = new Label("Total Price: " + totalOrderPrice);

				root2.getChildren().add(totalUpdateLabel);
				if (selectedOrder.getOrderStatus().equals("Served")) {
					statusLabel = new Label("Order Served, Can Only See Order Item Details");
					root2.getChildren().add(statusLabel);
				} else if (selectedOrder.getOrderStatus().equals("Prepared")) {
					statusLabel = new Label("Order Prepared, Can Only See Order Item Details");
					root2.getChildren().add(statusLabel);
				} else if (selectedOrder.getOrderStatus().equals("Paid")) {
					statusLabel = new Label("Order Already Paid, Can Only See Order Item Details");
					root2.getChildren().add(statusLabel);
				}

			}

		}

	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
