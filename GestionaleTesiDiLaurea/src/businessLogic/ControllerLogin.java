package businessLogic;

import userInterface.ViewLogin;
import utils.Console;
import utils.Utils;

import org.eclipse.swt.widgets.Text;
import databaseAccessObject.Database;

public class ControllerLogin {
	private ViewLogin viewLogin;
	private ControllerDocente controllerDocente;
	private ControllerPresidenteCorso controllerPresidenteCorso;
	private ControllerPresidenteScuola controllerPresidenteScuola;
	
	public ControllerLogin() {
		viewLogin = new ViewLogin(this);
	}
	
	public void checkLogin(Text matricola, Text password) {
		matricola.setText("1");
		password.setText("test");
		if(matricola.getText().equals("") || password.getText().equals("")) {
			Utils.createErrorDialog(viewLogin.getShell(), "Messaggio", "Matricola o Password non puo' essere vuota");
		} else {
			if(Database.getInstance().isConnected()) {
				String[] info = Database.getInstance().verificaCredenziali(matricola.getText(), password.getText());
				if(info != null) {
					Console.print("Utente matricola: " + matricola.getText() + " password: " + password.getText() + " loggato", "app");
					viewLogin.close();
					controllerDocente = new ControllerDocente(info[0],info[1],info[3]);
					switch(Integer.parseInt(info[2])) {
						case 0:					
							ControllerStudente controllerStudente = new ControllerStudente();
							controllerStudente.run();		
							break;
						case 1:
							ControllerResponsabile controllerResponsabile = new ControllerResponsabile(info[0],info[1],info[3]);
							controllerResponsabile.run();
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
					Utils.createErrorDialog(viewLogin.getShell(), "Messaggio", "Matricola o Password errata");
				}	
			}
			else {
				Utils.createErrorDialog(viewLogin.getShell(), "Messaggio", "Connessione al database fallita");
			}
		}
	}
	
	public void run() {
		viewLogin.createAndRun();
	}
	
}
