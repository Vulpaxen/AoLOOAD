package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.Authentication;
import view.CashierPanel;
import view.ChefPanel;
import view.CustomerPanel;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Authentication authentication = new Authentication();
        authentication.show();
    }
}
