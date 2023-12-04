package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Connect;
import model.User;

public class UserController {
    public void createProduct(User user) {
    	String query = "INSERT INTO users (userRole, userName, userEmail, userPassword) VALUES (?, ?, ?, ?)";
    	try (Connection connection = Connect.getInstance().getConnection();
    	  PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setString(1, user.getUserRole());
    		ps.setString(2, user.getUserName());
    		ps.setString(3, user.getUserEmail());
    		ps.setString(4, user.getUserPassword());
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
    }
    
    public void deleteUser(String userId) {
        String query = "DELETE FROM users WHERE userId = ?";
        try (Connection connection = Connect.getInstance().getConnection();
        	PreparedStatement ps = connection.prepareStatement(query)) {
        	ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateUser(User newUser) {
    	String query = "UPDATE users SET userRole = ?, userName = ?, userEmail = ?, userPassword = ? WHERE userId = ?";
    	try (Connection connection = Connect.getInstance().getConnection();
    		PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setString(1, newUser.getUserRole());
    		ps.setString(2, newUser.getUserName());
    		ps.setString(3, newUser.getUserEmail());
    		ps.setString(4, newUser.getUserPassword());
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
    }
    
    public static User getUserByid(int userId) {
		User user = null;
		
		try(Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE userId = ?;");
			ps.setInt(1,  userId);
			ResultSet resultSet = ps.executeQuery();
			
			if(resultSet.next()){
				int id = resultSet.getInt("userId");
				String role = resultSet.getString("UserRole");
				String name = resultSet.getString("UserName");
				String email = resultSet.getString("UserEmail");
				String password = resultSet.getString("UserPassword");
				user = new User(id, role, name, email, password);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}
    
    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = Connect.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
				int id = resultSet.getInt("userId");
				String role = resultSet.getString("UserRole");
				String name = resultSet.getString("UserName");
				String email = resultSet.getString("UserEmail");
				String password = resultSet.getString("UserPassword");

                User user = new User(id, role, name, email, password);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
	
}
