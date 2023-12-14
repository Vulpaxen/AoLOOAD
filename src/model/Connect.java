package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static Connect instance = null;
    private Connection connection;

    private Connect() {
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/mystic_grills";
            String user = "root";
            String password = "";
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public static Connect getInstance() {
        if (instance == null) {
            synchronized (Connect.class) {
                if (instance == null) {
                    instance = new Connect();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connectToDatabase();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking connection status", e);
        }
        return connection;
    }
}
