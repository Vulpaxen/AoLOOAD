package controller;

import java.util.ArrayList;
import model.User;

public class UserController {
	
	private static ArrayList<User> userList = new ArrayList<>();
	private static String[] validUserRoles = { "Admin", "Chef", "Waiter", "Cashier", "Customer" };
	
    public static String createUser(String userRole, String userName, String userEmail, String userPassword,String confirmPassword) {
        if (userName == null || userName.isEmpty()) {
            return "Error: Name cannot be empty.";
        }
        
        if (userEmail.isEmpty() || !isEmailUnique(userEmail)) {
            return "Error: Email must be unique and not empty.";
        }
        
        if(userPassword.length() < 6 || userPassword.isEmpty()) {
            return "Error: Password must be 6 characters long";
        }
        
        if(userPassword.equals(confirmPassword)) {
        	return "Error: Confirm Password must be the same as Password";
        }
        
    	User.createUser(userRole, userName, userEmail, userPassword);
    	return "User created successfully!";
    }
    
    private static boolean isEmailUnique(String userEmail) {
        for (User user : userList) {
            if (user.getUserEmail().equals(userEmail)) {
                return false;
            }
        }
        return true;
    }
    
    public static void deleteUser(int userId) {
        User.deleteUser(userId);
    }
    
    public static String updateUser(int userId, String userRole, String userName, String userEmail, String userPassword) {
		for (String validRole : validUserRoles) {
			if (userRole.equals(validRole)) {
				User.updateUser(userId, userRole, userName, userEmail, userPassword);
				return "User successfully updated!";
			}
		}
		return "Role must be valid!";
    }
    
    
    public static ArrayList<User> getAllUsers() {
    	return User.getAllUsers();
    }
    
	public static User getUserById(int userId){
		return User.getUserById(userId);
	}

	public static String authenticateUser(String userEmail, String userPassword) {
		return User.authenticateUser(userEmail, userPassword);
	}
	
}
