package main;

import controller.UserController;

public class Main {
	UserController userController = new UserController();
	
	public Main() {
		userController.createUser("haha", "haha", "haha", "inipw");
		
		userController.getAllUsers();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
