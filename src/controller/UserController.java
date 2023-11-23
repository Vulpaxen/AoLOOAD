package controller;

import java.util.ArrayList;

import model.User;

public class UserController {
	private ArrayList<User> users;
	User user;
	
	public void createUser(String userName, String userEmail, String userPassword) {
        if (userName == null || userName.isEmpty()) {
            System.out.println("Error: Username cannot be empty.");
            return;
        }

        if (userEmail == null || userEmail.isEmpty()) {
            System.out.println("Error: Email cannot be empty.");
            return;
        }

        if (!isEmailUnique(userEmail)) {
            System.out.println("Error: Email must be unique.");
            return;
        }

        if (userPassword == null || userPassword.isEmpty()) {
            System.out.println("Error: Password cannot be empty.");
            return;
        }

        if (userPassword.length() < 6) {
            System.out.println("Error: Password must be at least 6 characters long.");
            return;
        }
        
        //nnti pas ad viewnya
        /*
        if (confirmPassword == null || confirmPassword.isEmpty() || !userPassword.equals(confirmPassword)) {
            System.out.println("Error: Confirm Password must be the same as Password.");
            return;
        }
        */
		
        User newUser = new User(userName, userEmail, userPassword);
        users.add(newUser);
	}
	
    private boolean isEmailUnique(String userEmail) {
        for (User user : users) {
            if (user.userEmail.equals(userEmail)) {
                return false;
            }
        }
        return true;
    }
	
    public void updateUser(String userId, String userRole, String userName, String userEmail, String userPassword) {
        for (User user : users) {
            if (user.userId == userId) {
                user.userRole = userRole;
                user.userName = userName;
                user.userEmail = userEmail;
                user.userPassword = userPassword;
                return;
            }
            else {
            	System.out.println("User not found");
            }
        }
    }
    
    public void deleteUser(String userId) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.userId == userId) {
                users.remove(i);
                System.out.println("User deleted: " + user);
                return;
            }
        }
        System.out.println("User not found with ID: " + userId);
    }
    
    public User authenticateUser(String userEmail, String userPassword) {
        for (User user : users) {
            if (user.userEmail.equals(userEmail) && user.userPassword.equals(userPassword)) {
                System.out.println("User authenticated: " + user);
                return user;
            }
        }
        System.out.println("Authentication failed for email: " + userEmail);
        return null;
    }
	
	public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>(users);

        for (User user : allUsers) {
            System.out.println("User: " + user.userName + ", Email: " + user.userEmail);
        }

        return allUsers;
	}
	
    public User getUserById(String userId) {
        for (User user : users) {
            if (user.userId.equals(userId)) {
                System.out.println("User found by ID: " + user);
                return user;
            }
        }
        System.out.println("User not found with ID: " + userId);
        return null;
    }
	
}
