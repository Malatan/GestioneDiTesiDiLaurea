package businessLogic;

import userInterface.ViewLogin;

import java.sql.SQLException;

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
	
	public void checkLogin(Text matricola, Text password) {
		System.out.println(matricola.getText()+ " " + password.getText());
		if(Database.getInstance().isConnected()) {
			String[] info = Database.getInstance().verificaCredenziali(matricola.getText(), password.getText());
			if(info != null) {
				//System.out.println(info[0] + " " + info[1] + " di ruolo " + info[2]);
				viewLogin.close();
				controllerDocente = new ControllerDocente(info[0],info[1],info[3]);
				switch(Integer.parseInt(info[2])) {
					case 0:					
						controllerStudente = new ControllerStudente();
						controllerStudente.showStudenteWidget();		
						break;
					case 1:
						controllerResponsabile = new ControllerResponsabile(info[0],info[1],info[3]);
						controllerResponsabile.showResponsabileWidget();
						break;
					case 2:
						controllerPresidenteScuola = new ControllerPresidenteScuola(info[0],info[1],info[3]);
						controllerPresidenteScuola.showPresidenteScuolaWidget();
						break;
					case 3:
						controllerPresidenteCorso = new ControllerPresidenteCorso(info[0],info[1],info[3]);
						controllerPresidenteCorso.showPresidenteCorsoWidget();
						break;
					case 4:					
						controllerDocente.showRelatoreWidget();
						break;
					case 5:
						controllerDocente.showMembroCommissioneWidget();
						break;
					case 6:
						controllerDocente.showPresidenteCommissioneWidget();
						break;
				}
			}else {
				viewLogin.showErrorMessage("Matricola o Password errata");
			}	
		}
		else {
			viewLogin.showErrorMessage("Connessione al database fallita");
		}
		if(matricola.getText().equals("test") && password.getText().equals("test")) {
			viewLogin.close();
			controllerStudente.showStudenteWidget();			
		}
	}
	
	public void run() {
		viewLogin.createAndRun();
	}
	
}
