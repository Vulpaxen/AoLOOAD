package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Connect;
import model.Order;
import model.User;

public class UserController {
    public void createUser(String userRole, String userName, String userEmail, String userPassword) {
    	User.createUser( userRole,  userName,  userEmail,  userPassword);
    }
    
    public void deleteUser(String userId) {
        User.deleteUser(userId);
    }
    
    public static void updateUser(String userId, String userRole, String userName, String userEmail, String userPassword) {
    	User.updateUser(userId, userRole, userName, userEmail, userPassword);
    }
    
    
    public static ArrayList<User> getAllUsers() {
    	return User.getAllUsers();
    }
    
	public static User getUserById(int userId){
		return User.getUserById(userId);
	}
	
}
