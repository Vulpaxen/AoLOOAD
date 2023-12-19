package view;

import controller.MenuItemController;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.MenuItem;
import model.User;

import java.util.ArrayList;

public class MenuManagementView {
    private TableView<MenuItem> table = new TableView<>();
    private TextField idInput = new TextField();
    private TextField nameInput = new TextField();
    private TextField descriptionInput = new TextField();
    private TextField priceInput = new TextField();
    private Button addButton = new Button("Add");
    private Button updateButton = new Button("Update");
    private Button deleteButton = new Button("Delete");
    private User currentUser;

    public VBox getRoot() {
        if (currentUser != null) {
            VBox root = new VBox();
            root.getChildren().addAll(createMenuItemTable(), createMenuItemForm());
            return root;
        } else {
            return new VBox();
        }
    }

    public void loadData() {
        ArrayList<MenuItem> data = MenuItemController.getAllMenuItems();
        table.getItems().setAll(data);
    }

    private TableView<MenuItem> createMenuItemTable() {
        TableView<MenuItem> table = new TableView<>();
        TableColumn<MenuItem, Integer> idColumn = new TableColumn<>("Menu Item Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemId"));

        TableColumn<MenuItem, String> nameColumn = new TableColumn<>("Menu Item Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));

        TableColumn<MenuItem, String> descriptionColumn = new TableColumn<>("Menu Item Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemDescription"));

        TableColumn<MenuItem, Double> priceColumn = new TableColumn<>("Menu Item Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemPrice"));

        table.getColumns().addAll(idColumn, nameColumn, descriptionColumn, priceColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idInput.setText(String.valueOf(newSelection.getMenuItemId()));
                nameInput.setText(newSelection.getMenuItemName());
                descriptionInput.setText(newSelection.getMenuItemDescription());
                priceInput.setText(String.valueOf(newSelection.getMenuItemPrice()));
            }
        });

        return table;
    }

    private VBox createMenuItemForm() {
        VBox form = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20);
        gridPane.setHgap(10);

        gridPane.add(new Label("Menu Item Id:"), 0, 0);
        idInput.setDisable(true);
        gridPane.add(idInput, 1, 0);
        gridPane.add(new Label("Menu Item Name:"), 0, 1);
        gridPane.add(nameInput, 1, 1);
        gridPane.add(new Label("Menu Item Description:"), 0, 2);
        gridPane.add(descriptionInput, 1, 2);
        gridPane.add(new Label("Menu Item Price:"), 0, 3);
        gridPane.add(priceInput, 1, 3);

        form.getChildren().add(gridPane);

        addButton.setOnAction(event -> {
            if (currentUser != null) {
                try {
                    String name = nameInput.getText();
                    String description = descriptionInput.getText();
                    double price = Double.parseDouble(priceInput.getText());

                    MenuItemController.createMenuItem(name, description, price);
                    loadData();
                } catch (NumberFormatException e) {
                    showAlert("Input Error", "Price must be a valid number.");
                }
            } else {
                showAlert("Unauthorized Access", "You do not have permission to add menu items.");
            }
        });

        updateButton.setOnAction(event -> {
            if (currentUser != null) {
                try {
                    int id = Integer.parseInt(idInput.getText());
                    String name = nameInput.getText();
                    String description = descriptionInput.getText();
                    double price = Double.parseDouble(priceInput.getText());

                    MenuItemController.updateMenuItem(id, name, description, price);
                    loadData();
                } catch (NumberFormatException e) {
                    showAlert("Input Error", "Price must be a valid number.");
                }
            } else {
                showAlert("Unauthorized Access", "You do not have permission to update menu items.");
            }
        });

        deleteButton.setOnAction(event -> {
            if (currentUser != null) {
                MenuItem selectedMenuItem = table.getSelectionModel().getSelectedItem();
                if (selectedMenuItem != null) {
                    MenuItemController.deleteMenuItem(selectedMenuItem.getMenuItemId());
                    loadData();
                }
            } else {
                showAlert("Unauthorized Access", "You do not have permission to delete menu items.");
            }
        });

        form.getChildren().addAll(addButton, updateButton, deleteButton);

        return form;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}