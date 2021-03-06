package businessLogic;

import userInterface.ViewLogin;
import utils.Console;
import utils.Pair;
import utils.Utils;
import databaseAccessObject.Database;

public class ControllerLogin {
	private ViewLogin view;
	
	public ControllerLogin() {
		view = new ViewLogin(this);
	}
	
	public ViewLogin getView() {
		return view;
	}
	
	public void disposeView() {
		view = null;
	}
	
	public boolean checkLogin(String matricola, String password) {
		if(matricola.equals("") || password.equals("") || matricola.equals("100")) {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Matricola o Password non puo' essere vuota");
			return false;
		} else {
			if(Database.getInstance().isConnected()) {
				String[] info = Database.getInstance().verificaCredenziali(matricola, password);
				if (info != null && view == null) {
					return true;
				}
				if(info != null && view != null) {
					Console.print("Utente matricola: " + matricola + " password: " + password + " loggato", "app");
					view.close();
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
					return true;
				}else {
					if (view != null) {
						Utils.createErrorDialog(view.getShell(), "Messaggio", "Matricola o Password errata");
					}
					return false;
				}	
			}
			else {
				Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database fallita");
				return false;
			}
		}
	}
	
	public Pair<Integer, String> getCorsoByPresidente(String matricola) {
		Pair<Integer, String> corso = null;
		if (Database.getInstance().isConnected()) {
			corso = Database.getInstance().getCorsoByPresidente(Integer.parseInt(matricola));
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return corso;
	}
	
	public Pair<Integer, String> getDipByDocente(String matricola) {
		Pair<Integer, String> corso = null;
		if (Database.getInstance().isConnected()) {
			corso = Database.getInstance().getDipByDocente(matricola);
			Console.print(corso.first + " " +corso.second, "DEBUG");
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return corso;
	}
	
	public void run() {
		view.createAndRun();
	}
	
}
