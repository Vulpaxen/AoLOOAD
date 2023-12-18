package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
	private int userId;
	private String userRole;
	private String userName;
	private String userEmail;
	private String userPassword;
	
	public User(int userId, String userRole, String userName, String userEmail, String userPassword) {
		this.userId = userId;
		this.userRole = "Customer";
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
    public static void createUser(String userRole, String userName, String userEmail, String userPassword) {
    	String query = "INSERT INTO users (userRole, userName, userEmail, userPassword) VALUES (?, ?, ?, ?)";
    	try (Connection connection = Connect.getInstance().getConnection();
    	  PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setString(1, "Customer");
    		ps.setString(2, userName);
    		ps.setString(3, userEmail);
    		ps.setString(4, userPassword);
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
    }
	
    public static void deleteUser(String userId) {
        String query = "DELETE FROM users WHERE userId = ?";
        try (Connection connection = Connect.getInstance().getConnection();
        	PreparedStatement ps = connection.prepareStatement(query)) {
        	ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
    public static void updateUser(String userId, String userRole, String userName, String userEmail, String userPassword) {
    	String query = "UPDATE users SET userRole = ?, userName = ?, userEmail = ?, userPassword = ? WHERE userId = ?";
    	try (Connection connection = Connect.getInstance().getConnection();
    		PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setString(1, userRole);
    		ps.setString(2, userName);
    		ps.setString(3, userEmail);
    		ps.setString(4, userPassword);
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
    }
	
    public static ArrayList<User> getAllUsers() {
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

    public static User getUserById(int userId) {
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
    
    public static String authenticateUser(String userEmail, String userPassword) {
        String query = "SELECT * FROM users WHERE userEmail = ? AND userPassword = ?";
        try (Connection connection = Connect.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, userEmail);
            ps.setString(2, userPassword);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("userRole");
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
