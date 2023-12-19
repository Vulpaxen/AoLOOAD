package view;

import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.User;

import java.util.List;
import java.util.Optional;

public class UserManagementView {
    private TableView<User> userTableView;
    private ObservableList<User> userData;

    public Node getRoot() {
        BorderPane rootPane = new BorderPane();

        userTableView = new TableView<>();
        userData = FXCollections.observableArrayList();

        TableColumn<User, String> userIdColumn = createColumn("User ID", "userId");
        TableColumn<User, String> usernameColumn = createColumn("Username", "userName");
        TableColumn<User, String> roleColumn = createColumn("Role", "userRole");

        userTableView.getColumns().addAll(List.of(userIdColumn, usernameColumn, roleColumn));

        Button viewButton = new Button("View All Users");
        Button removeButton = new Button("Remove User");
        Button changeRoleButton = new Button("Change Role");

        viewButton.setOnAction(e -> viewAllUsers());
        removeButton.setOnAction(e -> removeUser());
        changeRoleButton.setOnAction(e -> changeUserRole());

        VBox buttonVBox = new VBox(10);
        buttonVBox.setPadding(new Insets(10, 10, 10, 10));
        buttonVBox.getChildren().addAll(viewButton, removeButton, changeRoleButton);

        rootPane.setCenter(userTableView);
        rootPane.setRight(buttonVBox);

        return rootPane;
    }

    private TableColumn<User, String> createColumn(String columnName, String propertyName) {
        TableColumn<User, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(cell -> new TextFieldTableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });
        return column;
    }

    public void viewAllUsers() {
        userData.clear();
        List<User> users = UserController.getAllUsers();

        for (User user : users) {
         if (user != null) {
                user.setUserRole("customer");
            }
        }

        userData.addAll(users);
        userTableView.setItems(userData);
    }

    public void removeUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            int userId = selectedUser.getUserId();
            UserController.deleteUser(userId);
            viewAllUsers();
        } else {
            showAlert("Please select a user to remove.");
        }
    }

    public void changeUserRole() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            TextInputDialog dialog = new TextInputDialog(selectedUser.getUserRole());
            dialog.setTitle("Change User Role");
            dialog.setHeaderText("Enter the new role for the user:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newRole -> {
                if (!newRole.equals(selectedUser.getUserRole())) {
                    UserController.updateUser(
                            selectedUser.getUserId(),
                            newRole,
                            selectedUser.getUserName(),
                            selectedUser.getUserEmail(),
                            selectedUser.getUserPassword()
                    );

                    showAlert("User role changed to " + newRole + ".");
                    viewAllUsers();
                } else {
                    showAlert("New role is the same as the current role.");
                }
            });
        } else {
            showAlert("Please select a user to change role.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}