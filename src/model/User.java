package model;

public class User {
	public String userId;
	public String userRole;
	public String userName;
	public String userEmail;
	public String userPassword;
	
	public User(String userName, String userEmail, String userPassword) {
		this.userRole = "Customer";
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}
}
