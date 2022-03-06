package businessLogic;

import userInterface.ViewLogin;
import utils.Console;
import utils.Pair;
import utils.Utils;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Text;
import databaseAccessObject.Database;

public class ControllerLogin {
	private ViewLogin viewLogin;
	
	public ControllerLogin() {
		viewLogin = new ViewLogin(this);
	}
	
	public void checkLogin(Text matricola, Text password) {
		if(matricola.getText().equals("") || password.getText().equals("") || matricola.getText().equals("100")) {
			Utils.createErrorDialog(viewLogin.getShell(), "Messaggio", "Matricola o Password non puo' essere vuota");
		} else {
			if(Database.getInstance().isConnected()) {
				String[] info = Database.getInstance().verificaCredenziali(matricola.getText(), password.getText());
				if(info != null) {
					Console.print("Utente matricola: " + matricola.getText() + " password: " + password.getText() + " loggato", "app");
					viewLogin.close();
					switch(Integer.parseInt(info[2])) {
						case 0:					
							ControllerStudente controllerStudente = new ControllerStudente(info[0],info[1],info[3]);
							controllerStudente.run();		
							break;
						case 1:
							ControllerResponsabile controllerResponsabile = new ControllerResponsabile(info[0],info[1],info[3]);
							controllerResponsabile.run();
							break;
						case 2:
							ControllerPresidenteScuola controllerPresidenteScuola = new ControllerPresidenteScuola(info[0],info[1],info[3]);
							controllerPresidenteScuola.run();
							break;
						case 3:
							ControllerPresidenteCorso controllerPresidenteCorso = new ControllerPresidenteCorso(info[0],info[1],info[3],
									getCorsoByPresidente(info[0]));
							controllerPresidenteCorso.run();
							break;
						case 4:		
							ControllerDocente controllerDocente = new ControllerDocente(info[0],info[1],info[3],
									getDipByDocente(info[0]));
							controllerDocente.run();
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
	
	public Pair<Integer, String> getCorsoByPresidente(String matricola) {
		Pair<Integer, String> corso = null;
		if (Database.getInstance().isConnected()) {
			corso = Database.getInstance().getCorsoByPresidente(Integer.parseInt(matricola));
		} else {
			Utils.createConfirmDialog(viewLogin.getShell(), "Messaggio", "Connessione al database persa");
		}
		return corso;
	}
	
	public Pair<Integer, String> getDipByDocente(String matricola) {
		Pair<Integer, String> corso = null;
		if (Database.getInstance().isConnected()) {
			corso = Database.getInstance().getDipByDocente(matricola);
		} else {
			Utils.createConfirmDialog(viewLogin.getShell(), "Messaggio", "Connessione al database persa");
		}
		return corso;
	}
	
	public void run() {
		viewLogin.createAndRun();
	}
	
}
