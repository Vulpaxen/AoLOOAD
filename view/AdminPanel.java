package view;

import controller.MenuItemController;
import controller.UserController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.MenuItem;
import model.User;

public class AdminPanel extends Stage {

	private BorderPane borderPane = new BorderPane();
	private VBox root1 = new VBox(20);
	private VBox root2 = new VBox(20);
	private VBox root3 = new VBox(20);
	private Scene scene;
	private MenuBar menuBar = new MenuBar();

	public AdminPanel(User user) {

		super(StageStyle.DECORATED);
		this.setTitle("Admin Dashboard");

		scene = new Scene(borderPane, 900, 800);
		this.setScene(scene);

		Menu menuMenu = new Menu("Menu Management");
		javafx.scene.control.MenuItem menuManagementItem = new javafx.scene.control.MenuItem("Go To Menu Management");
		menuMenu.getItems().addAll(menuManagementItem);

		Menu userMenu = new Menu("User Management");
		javafx.scene.control.MenuItem userManagementItem = new javafx.scene.control.MenuItem("Go to User Management");
		userMenu.getItems().addAll(userManagementItem);

		menuBar.getMenus().addAll(menuMenu);
		menuBar.getMenus().addAll(userMenu);

		borderPane.setTop(menuBar);

		menuManagement(user);

		// Menu untuk Management MenuItem
		menuManagementItem.setOnAction(e -> {

			menuManagement(user);

		});

		// Menu untuk management User
		userManagementItem.setOnAction(e -> {

			userManagement(user);
		});

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

		// jika klik clos (x), maka akan terlogout dan teralihkan ke Authentication Page
		// (Login Register)
		this.setOnCloseRequest(event -> {
			if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
				Authentication authenticationPanel = new Authentication();
				authenticationPanel.show();
			}
		});

	}

	// Untuk View User Management
	private void userManagement(User user) {
		// TODO Auto-generated method stub
		root1.getChildren().clear();
		root2.getChildren().clear();
		root3.getChildren().clear();

		TableView<User> tableUser = createUserTable();
		tableUser.setStyle("-fx-background-color: lightblue;");
		root1.getChildren().addAll(tableUser);

		GridPane form = createUserForm(tableUser);
		root2.getChildren().add(form);
	}

	// variabel nyimpen select dan form
	private TextField formUserId = new TextField();
	private TextField formUserName = new TextField();
	private TextField formUserRole = new TextField();
	private TextField formUserEmail = new TextField();
	private TextField formUserPasssword = new TextField();

	// Mengambil User untuk Table
	private TableView<User> createUserTable() {
		TableView<User> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<User, String> userId = new TableColumn<>("ID");
		userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
		userId.setPrefWidth(50);

		TableColumn<User, String> userName = new TableColumn<>("Name");
		userName.setCellValueFactory(new PropertyValueFactory<>("userName"));

		TableColumn<User, String> userEmail = new TableColumn<>("Email");
		userEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));

		TableColumn<User, String> userRole = new TableColumn<>("Role");
		userRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));

		table.getColumns().add(userId);
		table.getColumns().add(userName);
		table.getColumns().add(userEmail);
		table.getColumns().add(userRole);

		table.setPrefHeight(1200);

		table.setMinHeight(700);
		table.setMinWidth(450);

		table.setItems(FXCollections.observableArrayList(UserController.getAllUsers()));

		// biar bisa select data-data
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				formUserId.setText(newSelection.getUserId() + "");
				formUserName.setText(newSelection.getUserName());
				formUserRole.setText(newSelection.getUserRole());
				formUserEmail.setText(newSelection.getUserEmail());
				formUserPasssword.setText(newSelection.getUserPassword());
			}
		});

		return table;
	}

	// Untuk mengubah Role User, atau Remove User
	private GridPane createUserForm(TableView<User> tableUser) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button updateUserButton = new Button("Update User");

		Button removeUserButton = new Button("Remove User");

		form.add(new Label("Id:"), 0, 0);
		form.add(formUserId, 1, 0);
		formUserId.setDisable(true);

		form.add(new Label("Name"), 0, 1);
		form.add(formUserName, 1, 1);
		formUserName.setDisable(true);

		form.add(new Label("Email"), 0, 2);
		form.add(formUserEmail, 1, 2);
		formUserEmail.setDisable(true);

		form.add(new Label("Password"), 0, 3);
		form.add(formUserPasssword, 1, 3);
		formUserPasssword.setDisable(true);

		form.add(new Label("Role"), 0, 4);
		form.add(formUserRole, 1, 4);

		form.add(updateUserButton, 0, 5);
		form.add(removeUserButton, 0, 8);

		// Update role user
		updateUserButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				User selectedUser = tableUser.getSelectionModel().getSelectedItem();

				if (selectedUser != null) {
					String newRole = formUserRole.getText();

					if (newRole.equals(selectedUser.getUserRole())) {
						showAlert("Update User", "Update Role The Same With The Current Role");

					} else if (newRole.equals("Chef") || newRole.equals("Admin") || newRole.equals("Customer")
							|| newRole.equals("Waiter") || newRole.equals("Cashier")) {
						selectedUser.setUserRole(newRole);

						UserController.updateUser(selectedUser.getUserId(), newRole, selectedUser.getUserName(),
								selectedUser.getUserEmail(), selectedUser.getUserPassword());

						tableUser.setItems(FXCollections.observableArrayList(UserController.getAllUsers())); // Update
																												// table
																												// data
						tableUser.refresh();
						showAlert("Update Order", "Selected User's Role Has Been Changed");
					} else {
						showAlert("Update Order",
								"Please input Valid Role 'Customer', 'Admin', 'Chef', 'Waiter', 'Cashier'");
					}
				} else {
					showAlert("Update User", "Please Select An User From Table");
				}
				tableUser.getSelectionModel().clearSelection();

				formUserId.clear();
				formUserEmail.clear();
				formUserName.clear();
				formUserPasssword.clear();
				formUserRole.clear();

			}
		});

		// remove user yang dipilih
		removeUserButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				User selectedUser = tableUser.getSelectionModel().getSelectedItem();

				if (selectedUser != null) {
					UserController.deleteUser(selectedUser.getUserId());
					tableUser.setItems(FXCollections.observableArrayList(UserController.getAllUsers()));
					tableUser.refresh();

					showAlert("Remove User", "Remove User Succes, All Data related to User Has Been Deleted");
				} else {
					showAlert("Remove User", "Please Select An User From Table");
				}

				tableUser.getSelectionModel().clearSelection();

				formUserId.clear();
				formUserEmail.clear();
				formUserName.clear();
				formUserPasssword.clear();
				formUserRole.clear();

			}
		});

		return form;
	}

	// ============================================
	// ===========================================
	// Panel MenuItem menuManagement
	private void menuManagement(User user) {
		root1.getChildren().clear();
		root2.getChildren().clear();
		root3.getChildren().clear();

		TableView<MenuItem> tableMenuItem = createMenuItemTable();
		tableMenuItem.setStyle("-fx-background-color: lightblue;");
		root1.getChildren().addAll(tableMenuItem);

		GridPane form = createMenuItemForm(tableMenuItem);
		root2.getChildren().add(form);

	}

	// variabel nyimpen select dan form
	private TextField ItemId = new TextField();
	private TextField ItemName = new TextField();
	private TextField ItemDesc = new TextField();
	private TextField ItemPrice = new TextField();

	// Mengambil data MenuItem
	private TableView<MenuItem> createMenuItemTable() {
		TableView<MenuItem> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<MenuItem, String> menuItemName = new TableColumn<>("Name");
		menuItemName.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));

		TableColumn<MenuItem, String> menuItemDesc = new TableColumn<>("Desc");
		menuItemDesc.setCellValueFactory(new PropertyValueFactory<>("menuItemDescription"));

		TableColumn<MenuItem, Double> menuItemPrice = new TableColumn<>("Price");
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
			}
		});

		return table;
	}

	// Untuk management Menuitem
	// Add MenuItem, Update Menu Item, Remove MenuItem
	private GridPane createMenuItemForm(TableView<MenuItem> tableMenuItem) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button addItemButton = new Button("Add Menu Item");
		Button updateItemButton = new Button("Update Menu Item");
		Button removeItemButton = new Button("Remove Menu Item");

		// form.add(new Label("Id:"), 0, 0);
		// form.add(ItemId, 1, 0);
		// ItemId.setDisable(true);

		form.add(new Label("Name:"), 0, 1);
		form.add(ItemName, 1, 1);

		form.add(new Label("Desc:"), 0, 2);
		form.add(ItemDesc, 1, 2);

		form.add(new Label("Price:"), 0, 3);
		form.add(ItemPrice, 1, 3);

		form.add(addItemButton, 0, 4);
		form.add(updateItemButton, 0, 6);
		form.add(removeItemButton, 0, 8);

		// Untuk add Item
		addItemButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String newName = ItemName.getText();
				double newPrice = Double.parseDouble(ItemPrice.getText());
				String newDesc = ItemDesc.getText();

				boolean checkUnique = true;
				for (MenuItem menuItem : MenuItemController.getAllMenuItems()) {
					if (menuItem.getMenuItemName().equals(newName)) {
						checkUnique = false;
						break;
					}

				}

				if (newName.length() == 0) {
					showAlert("Update Menu Item", "Menu Item Name Cannot Be Empty ");

				} else if (!checkUnique) {
					showAlert("Update Menu Item", "Menu Item Name Must Be Unique");
				} else {
					if (newDesc.length() <= 10) {
						showAlert("Update Menu Item", "Menu Item Desc Must Be More Than 10 chars ");
					} else if (newPrice <= 2.5) {
						showAlert("Update Menu Item", "Menu Item Price Must Greater Than >= 2.5 ");
					} else {

						MenuItemController.createMenuItem(newName, newDesc, newPrice);

						tableMenuItem.setItems(FXCollections.observableArrayList(MenuItemController.getAllMenuItems()));
						tableMenuItem.refresh();
						tableMenuItem.getSelectionModel().clearSelection();
						showAlert("Update Menu Item", "Succes Add Menu Item");
					}

				}

				formUserId.clear();
				formUserEmail.clear();
				formUserName.clear();
				formUserPasssword.clear();
				formUserRole.clear();

			}
		});

		// Untuk update item
		updateItemButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MenuItem selectedMenuItem = tableMenuItem.getSelectionModel().getSelectedItem();

				if (selectedMenuItem != null) {
					String newName = ItemName.getText();
					double newPrice = Double.parseDouble(ItemPrice.getText());
					String newDesc = ItemDesc.getText();

					boolean checkUnique = true;
					for (MenuItem menuItem : MenuItemController.getAllMenuItems()) {
						if (menuItem.getMenuItemName().equals(newName)) {
							if (menuItem.getMenuItemName().equals(selectedMenuItem.getMenuItemName())) {
								continue;
							}

							checkUnique = false;
							break;
						}

					}

					if (newName.length() == 0) {
						showAlert("Update Menu Item", "Menu Item Name Cannot Be Empty ");

					} else if (!checkUnique) {
						showAlert("Update Menu Item", "Menu Item Name  ");
					} else {
						if (newDesc.length() <= 10) {
							showAlert("Update Menu Item", "Menu Item Desc Must Be More Than 10 chars ");
						} else if (newPrice <= 2.5) {
							showAlert("Update Menu Item", "Menu Item Price Must Greater Than >= 2.5 ");
						} else {
							selectedMenuItem.setMenuItemName(newName);
							selectedMenuItem.setMenuItemDescription(newDesc);
							selectedMenuItem.setMenuItemPrice(newPrice);

							MenuItemController.updateMenuItem(selectedMenuItem.getMenuItemId(),
									selectedMenuItem.getMenuItemName(), selectedMenuItem.getMenuItemDescription(),
									selectedMenuItem.getMenuItemPrice());

							tableMenuItem
									.setItems(FXCollections.observableArrayList(MenuItemController.getAllMenuItems()));
							tableMenuItem.refresh();
							tableMenuItem.getSelectionModel().clearSelection();
							showAlert("Update Menu Item", "Succes To Update Menu Item");
						}

					}

					// }
				} else {
					showAlert("Update User", "Please Select An User From Table");
				}

				formUserId.clear();
				formUserEmail.clear();
				formUserName.clear();
				formUserPasssword.clear();
				formUserRole.clear();

			}
		});

		// Untuk remove item
		removeItemButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MenuItem selectedItem = tableMenuItem.getSelectionModel().getSelectedItem();

				if (selectedItem != null) {
					MenuItemController.deleteMenuItem(selectedItem.getMenuItemId());

					formUserId.clear();
					formUserEmail.clear();
					formUserName.clear();
					formUserPasssword.clear();
					formUserRole.clear();

					tableMenuItem.getSelectionModel().clearSelection();

					tableMenuItem.setItems(FXCollections.observableArrayList(MenuItemController.getAllMenuItems()));
					tableMenuItem.refresh();

					showAlert("Remove User", "Remove User Succes, All Data related to User Has Been Deleted");
				} else {
					showAlert("Remove User", "Please Select An User From Table");
				}

			}
		});
		return form;
	}

	// ALERT
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
