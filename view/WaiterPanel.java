package view;

import java.sql.Date;
import java.util.ArrayList;

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
import model.Order;
import model.OrderItem;

public class WaiterPanel extends Stage {

	private BorderPane borderPane = new BorderPane();
	private VBox root1 = new VBox(20);
	private VBox root2 = new VBox(20);
	private Scene scene;

	public WaiterPanel() {

		super(StageStyle.DECORATED);
		this.setTitle("Waiter Dashboard");

		scene = new Scene(borderPane, 1000, 800);
		this.setScene(scene);

		viewOrdered();

		borderPane.setLeft(root1);
		root1.setAlignment(Pos.CENTER);
		root1.setPadding(new Insets(20));
		root1.setPrefHeight(getMaxHeight());
		root1.setStyle("-fx-background-color: lightgray;");

		borderPane.setCenter(root2);
		root2.setAlignment(Pos.CENTER);
		root2.setPadding(new Insets(20));

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

		table.getColumns().addAll(itemName, itemQuantity, itemTotalPrice);

		return table;
	}

	TableView<OrderItem> orderDetails = createOrderDetails();

	TableView<Order> preparedOrders = createOrderedTable();

	private void viewOrdered() {
		// TODO Auto-generated method stub
		root1.getChildren().clear();
		root2.getChildren().clear();

		root1.getChildren().add(preparedOrders);

		if (preparedOrders.getSelectionModel().isEmpty()) {
			Label selectOrderLabel = new Label("Select an order from the table to see order details.");
			root2.getChildren().add(selectOrderLabel);
		} else {
			selectedOrder = preparedOrders.getSelectionModel().getSelectedItem();
			showOrderDetails(selectedOrder);
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

		table.getColumns().addAll(itemName, itemQuantity, itemTotalPrice);

		return table;

	}

	Label totalUpdateLabel;

	private void showOrderDetails(Order selectedOrder) {
		TableView<OrderItem> orderItemTable = null;
		root2.getChildren().clear(); //
		if (selectedOrder != null) {
			orderItemTable = createOrdersByOrderIdTable(selectedOrder);
			root2.getChildren().add(orderItemTable);

			double totalOrderPrice = selectedOrder.getOrderTotal();
			totalUpdateLabel = new Label("Total Price: " + totalOrderPrice);
			root2.getChildren().add(totalUpdateLabel);

			root2.getChildren().add(createUpdateOrderForm(orderItemTable));
		}

	}

	private Order selectedOrder;

	private TableView<Order> createOrderedTable() {
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

		table.setItems(FXCollections.observableArrayList(Order.getPreparedOrdersByCustomerId(1)));

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedOrder = newSelection;
				showOrderDetails(selectedOrder);
			}
		});

		return table;
	}

	private void refreshOrderedTable() {
		preparedOrders.setItems(FXCollections.observableArrayList(Order.getPreparedOrdersByCustomerId((1))));
	}

	private TextField updateItemQuantity = new TextField();

	private GridPane createUpdateOrderForm(TableView<OrderItem> orderItemTable) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button serveOrderButton = new Button("Serve Order");
		Button updateOrderButton = new Button("Update Order");
		Button removeOrderButton = new Button("Remove Order");

		form.add(new Label("Quantity:"), 0, 0);
		form.add(updateItemQuantity, 1, 0);

		form.add(serveOrderButton, 0, 1);
		form.add(updateOrderButton, 0, 2);
		form.add(removeOrderButton, 0, 3);

		orderItemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				updateItemQuantity.setText(String.valueOf(newSelection.getQuantity()));
			}
		});
		serveOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				OrderItem selectedOrderItem = orderItemTable.getSelectionModel().getSelectedItem();
				if (selectedOrder != null) {
					ArrayList<OrderItem> orderItems = OrderItemController
							.getAllOrderItemsByOrderId(selectedOrder.getOrderId());
					OrderController.updateOrder(selectedOrder.getOrderId(), orderItems, "Served");
					refreshOrderedTable();
					showAlert("Order Served", "Selected order has been served.");
				} else {
					showAlert("No Order Selected", "Please select a pending order to serve.");
				}
				viewOrdered();
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

						refreshOrderedTable();
					} else {
						selectedOrderItem.setQuantity(newQuantity);
						OrderItem.updateOrderItem(selectedOrderItem.getOrderId(), selectedOrderItem.getMenuItem(),
								newQuantity);

						orderItemTable.refresh();

						totalUpdateLabel
								.setText("Total Price: " + Order.getTotalByOrderId(selectedOrderItem.getOrderId()));

						refreshOrderedTable();
					}

					updateItemQuantity.clear();
				}
			}
		});
		removeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Order selectedOrder = preparedOrders.getSelectionModel().getSelectedItem();
				if (selectedOrder != null) {
					int selectedOrderId = selectedOrder.getOrderId();

					OrderController.deleteOrder(selectedOrderId);

					refreshOrderedTable();
				} else {
					showAlert("No Order Item Selected", "Please select an order item to remove.");
				}
			}
		});

		return form;
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
