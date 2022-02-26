package businessLogic;

import userInterface.ViewLogin;
import org.eclipse.swt.widgets.Text;
import domainModel.*;
import businessLogic.*;
import databaseAccessObject.Database;


public class ControllerLogin {

	private ViewLogin viewLogin;
	private ControllerStudente controllerStudente;
	private ControllerDocente controllerDocente;
	private ControllerPresidenteCorso controllerPresidenteCorso;
	private ControllerPresidenteScuola controllerPresidenteScuola;
	private ControllerResponsabile controllerResponsabile;
	
	public ControllerLogin() {
		viewLogin = new ViewLogin(this);
	}
	
	public void CheckLogin(Text matricola, Text password) {
		System.out.println(matricola.getText()+ " " + password.getText());
		
		String[] info = Database.getInstance().VerificaCredenziali(matricola.getText(), password.getText());
		if(info != null) {
			//System.out.println(info[0] + " " + info[1] + " di ruolo " + info[2]);
			viewLogin.Close();
			controllerDocente = new ControllerDocente(info[0],info[1],info[3]);
			switch(Integer.parseInt(info[2])) {
				case 0:					
					controllerStudente = new ControllerStudente();
					controllerStudente.ShowStudenteWidget();		
					break;
				case 1:
					controllerResponsabile = new ControllerResponsabile(info[0],info[1],info[3]);
					controllerResponsabile.ShowResponsabileWidget();
					break;
				case 2:
					controllerPresidenteScuola = new ControllerPresidenteScuola(info[0],info[1],info[3]);
					controllerPresidenteScuola.ShowPresidenteScuolaWidget();
					break;
				case 3:
					controllerPresidenteCorso = new ControllerPresidenteCorso(info[0],info[1],info[3]);
					controllerPresidenteCorso.ShowPresidenteCorsoWidget();
					break;
				case 4:					
					controllerDocente.ShowRelatoreWidget();
					break;
				case 5:
					controllerDocente.ShowMembroCommissioneWidget();
					break;
				case 6:
					controllerDocente.ShowPresidenteCommissioneWidget();
					break;
			}
		}else {
			viewLogin.ShowErrorMessage();
		}
	
		if(matricola.getText().equals("test") && password.getText().equals("test")) {
			viewLogin.Close();
			controllerStudente.ShowStudenteWidget();			
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
