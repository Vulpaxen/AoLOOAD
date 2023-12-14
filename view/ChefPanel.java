package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.OrderItem;
import controller.OrderController;
import controller.OrderItemController;

import java.util.ArrayList;
import java.util.List;

public class ChefPanel extends Stage {
	private VBox root = new VBox(10);
	private Scene scene = new Scene(root, 800, 600);
	private TableView<OrderItem> ordersTableView = new TableView<>();
	private TextField orderIdTextField = new TextField();
	private TextArea orderDetailsTextArea = new TextArea();
	private Button loadOrdersBtn = new Button("Load Orders");
	private Button prepareOrderBtn = new Button("Prepare Order");
	private Button updateOrderBtn = new Button("Update Order");
	private Button removeOrderBtn = new Button("Remove Order");

	public ChefPanel() {
		super(StageStyle.DECORATED);
		this.setScene(scene);
		root.setPadding(new Insets(10));
		root.setAlignment(Pos.TOP_CENTER);

		this.setTitle("Chef Panel");
		showChefPanel();
	}

	private void showChefPanel() {
		Label titleLabel = new Label("Chef Panel");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleLabel.setAlignment(Pos.CENTER);

		HBox orderIdInputBox = new HBox(10);
		orderIdInputBox.setAlignment(Pos.CENTER);
		orderIdInputBox.getChildren().addAll(new Label("Enter Order ID:"), orderIdTextField, loadOrdersBtn);

		ordersTableView.setPrefHeight(300);

		loadOrdersBtn.setOnAction(e -> loadPendingOrders());

		prepareOrderBtn.setMaxWidth(Double.MAX_VALUE);
		prepareOrderBtn.setOnAction(e -> prepareSelectedOrder());

		updateOrderBtn.setMaxWidth(Double.MAX_VALUE);
		updateOrderBtn.setOnAction(e -> updateSelectedOrder());

		removeOrderBtn.setMaxWidth(Double.MAX_VALUE);
		removeOrderBtn.setOnAction(e -> removeSelectedOrder());

		VBox container = new VBox(10);
		container.setPadding(new Insets(20));
		container.setAlignment(Pos.CENTER);
		container.getChildren().addAll(titleLabel, orderIdInputBox, ordersTableView, orderDetailsTextArea,
				prepareOrderBtn, updateOrderBtn, removeOrderBtn);

		root.getChildren().clear();
		root.getChildren().add(container);
	}

	private void loadPendingOrders() {
		try {
			int selectedOrderId = Integer.parseInt(orderIdTextField.getText());
			List<OrderItem> pendingOrderItems = OrderItemController.getAllOrderItemsByOrderId(selectedOrderId);

			ObservableList<OrderItem> observableOrderItems = FXCollections.observableArrayList(pendingOrderItems);
			ordersTableView.setItems(observableOrderItems);
		} catch (NumberFormatException e) {
			showAlert("Invalid Input", "Please enter a valid Order ID.");
		}
	}

	private void setupTableColumns() {
		TableColumn<OrderItem, Integer> orderIdColumn = new TableColumn<>("Order ID");
		orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));

		TableColumn<OrderItem, String> menuItemColumn = new TableColumn<>("Menu Item");
		menuItemColumn.setCellValueFactory(new PropertyValueFactory<>("menuItem"));

		TableColumn<OrderItem, Integer> quantityColumn = new TableColumn<>("Quantity");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		ordersTableView.getColumns().addAll(orderIdColumn, menuItemColumn, quantityColumn);
	}

	private void prepareSelectedOrder() {
		OrderItem selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
		if (selectedOrder != null) {
			ArrayList<OrderItem> orderItems = OrderItemController.getAllOrderItemsByOrderId(selectedOrder.getOrderId());

			OrderController.updateOrder(selectedOrder.getOrderId(), orderItems, "Prepared");

			loadPendingOrders();

			showAlert("Order Prepared", "Selected order has been prepared.");
		} else {
			showAlert("No Order Selected", "Please select a pending order to prepare.");
		}
	}

	private void updateSelectedOrder() {
		OrderItem selectedOrderItem = ordersTableView.getSelectionModel().getSelectedItem();

		if (selectedOrderItem != null) {
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Update Order Quantity");

			VBox popupLayout = new VBox(10);
			popupLayout.setPadding(new Insets(20));

			Label quantityLabel = new Label("Enter New Quantity:");
			TextField quantityTextField = new TextField();
			quantityTextField.setPromptText("New Quantity");

			Button updateButton = new Button("Update");
			updateButton.setOnAction(e -> {
				try {
					int newQuantity = Integer.parseInt(quantityTextField.getText());

					selectedOrderItem.setQuantity(newQuantity);

					showAlert("Quantity Updated", "Order quantity has been updated successfully.");
					popupStage.close();
				} catch (NumberFormatException ex) {
					showAlert("Invalid Input", "Please enter a number.");
				}
			});

			popupLayout.getChildren().addAll(quantityLabel, quantityTextField, updateButton);

			Scene popupScene = new Scene(popupLayout, 300, 150);
			popupStage.setScene(popupScene);

			popupStage.showAndWait();
		} else {
			showAlert("No Order Item Selected", "Please select an order item to update.");
		}
	}

	private void removeSelectedOrder() {
		OrderItem selectedOrderItem = ordersTableView.getSelectionModel().getSelectedItem();
		if (selectedOrderItem != null) {
			int selectedOrderId = selectedOrderItem.getOrderId();

			OrderController.deleteOrder(selectedOrderId);
		} else {
			showAlert("No Order Item Selected", "Please select an order item to remove.");
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
