package businessLogic;

import userInterface.ViewLogin;



public class ControllerLogin {

	public ViewLogin viewLogin;
	
	public ControllerLogin() {
		viewLogin = new ViewLogin();
	}
	
	public void effettuaLogin() {
		viewLogin.ShowLogin();
	}
	
	public static void main(String[] args) {
		
		ControllerLogin cl = new ControllerLogin();
		
		cl.effettuaLogin();
		
	}
}
