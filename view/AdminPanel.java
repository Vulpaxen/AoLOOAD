package view;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminPanel extends Stage {

    public AdminPanel() {
        start();
    }

    private void start() {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu userMenu = new Menu("User Management");
        Menu menuMenu = new Menu("Menu Management");

        MenuItem userMenuItem = new MenuItem("Open User Management");
        userMenuItem.setOnAction(e -> openUserManagement());

        MenuItem menuMenuItem = new MenuItem("Open Menu Management");
        menuMenuItem.setOnAction(e -> openMenuManagement());

        userMenu.getItems().add(userMenuItem);
        menuMenu.getItems().add(menuMenuItem);

        menuBar.getMenus().addAll(userMenu, menuMenu);

        root.setTop(menuBar);

        Scene scene = new Scene(root, 800, 600);
        setScene(scene);
        setTitle("Admin Panel");
    }

    private void openUserManagement() {
        UserManagementView userManagementView = new UserManagementView();
        BorderPane root = new BorderPane();
        root.setCenter(userManagementView.getRoot());
        Stage userManagementStage = new Stage();
        userManagementStage.setScene(new Scene(root, 600, 400));
        userManagementStage.setTitle("User Management");
        userManagementStage.show();
    }

    private void openMenuManagement() {
        MenuManagementView menuManagementView = new MenuManagementView();
        BorderPane root = new BorderPane();
        root.setCenter(menuManagementView.getRoot());
        Stage menuManagementStage = new Stage();
        menuManagementStage.setScene(new Scene(root, 600, 400));
        menuManagementStage.setTitle("Menu Management");
        menuManagementStage.show();
    }

    public void showAdminPanel() {
        showAndWait();
    }
}