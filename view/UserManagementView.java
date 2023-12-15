package view;

import controller.UserController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.util.List;
import java.util.Optional;

public class UserManagementView extends Application {
    private UserController userController;

    private TableView<User> userTableView;
    private ObservableList<User> userData;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        userController = new UserController();

        primaryStage.setTitle("User Management View");

        userTableView = new TableView<>();
        userData = FXCollections.observableArrayList();

        TableColumn<User, String> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("userRole"));

        userTableView.getColumns().addAll(userIdColumn, usernameColumn, roleColumn);

        Button viewButton = new Button("View All Users");
        Button removeButton = new Button("Remove User");
        Button changeRoleButton = new Button("Change Role");

        viewButton.setOnAction(e -> viewAllUsers());
        removeButton.setOnAction(e -> removeUser());
        changeRoleButton.setOnAction(e -> changeUserRole());

        VBox buttonVBox = new VBox(10);
        buttonVBox.setPadding(new Insets(10, 10, 10, 10));
        buttonVBox.getChildren().addAll(viewButton, removeButton, changeRoleButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(userTableView);
        borderPane.setRight(buttonVBox);

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void viewAllUsers() {
        userData.clear();
        List<User> users = userController.getAllUsers();
        userData.addAll(users);
        userTableView.setItems(userData);
    }

    private void removeUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            int userId = selectedUser.getUserId();
            userController.deleteUser(Integer.toString(userId));
            viewAllUsers();
        } else {
            showAlert("Please select a user to remove.");
        }
    }

    private void changeUserRole() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            TextInputDialog dialog = new TextInputDialog(selectedUser.getUserRole());
            dialog.setTitle("Change User Role");
            dialog.setHeaderText("Enter the new role for the user:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newRole -> {
                userController.updateUser(
                        String.valueOf(selectedUser.getUserId()),
                        newRole,
                        selectedUser.getUserName(),
                        selectedUser.getUserEmail(),
                        selectedUser.getUserPassword()
                );

                showAlert("User role changed to " + newRole + ".");
                viewAllUsers();
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
