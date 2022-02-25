package businessLogic;

import userInterface.ViewLogin;
import userInterface.ViewStudente;
import userInterface.ViewAppello;
import userInterface.ViewDocente;
import userInterface.ViewPresidenteCorso;
import userInterface.ViewPresidenteScuola;
import userInterface.ViewResponsabile;

import org.eclipse.swt.widgets.Text;



public class ControllerLogin {

	public ViewLogin viewLogin;
	public ViewStudente viewStudente;
	
	public ControllerLogin() {
		viewLogin = new ViewLogin(this);
		viewStudente = new ViewStudente(this);
	}
	
	public void CheckLogin(Text matricola, Text password) {
		System.out.println(matricola.getText()+ " " + password.getText());
		if(matricola.getText().equals("test") && password.getText().equals("test")) {
			viewLogin.Close();
			viewStudente.ShowStudenteWidget();
			
		}
	}
	
	public void EffettuaLogin() {
		viewLogin.ShowLogin();
	}
	
	public static void main(String[] args) {
		
		ControllerLogin cl = new ControllerLogin();
		
		cl.EffettuaLogin();
		
	}
}
