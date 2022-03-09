package businessLogic;

import java.util.ArrayList;
import databaseAccessObject.Database;
import domainModel.Responsabile;
import userInterface.ViewResponsabile;
import utils.Console;
import utils.Pair;
import utils.Utils;
import domainModel.AppelloTesi;

public class ControllerResponsabile {
	public Responsabile responsabile;
	private ViewResponsabile view;

	public ControllerResponsabile(String matricola, String nome, String cognome) {
		responsabile = new Responsabile(matricola, nome, cognome);
		view = new ViewResponsabile(this);
	}

	public void run() {
		view.createAndRun();
	}

	public Responsabile getResponsabile() {
		return responsabile;
	}

	public AppelloTesi getAppelloFromDB(int id_appello) {
		AppelloTesi appello = null;
		if (Database.getInstance().isConnected()) {
			appello = Database.getInstance().getAppello(id_appello);
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appello;
	}

	public ArrayList<AppelloTesi> getAppelliFromDB() {
		ArrayList<AppelloTesi> appelli = null;
		if (Database.getInstance().isConnected()) {
			appelli = Database.getInstance().getAppelli();
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appelli;
	}
	
	public ArrayList<Pair<Integer, String>> getCorsiFromDB() {
		ArrayList<Pair<Integer, String>> corsi = null;
		if (Database.getInstance().isConnected()) {
			corsi = Database.getInstance().getCorsi();
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return corsi;
	}
	
	public boolean creaAppello(int id_corso) {
		if (Database.getInstance().isConnected()) {
			if (Database.getInstance().aggiungeAppello(responsabile.getMatricolaInt(), Utils.getTodayDate(), id_corso)) {
				Utils.createConfirmDialog(view.getShell(), "Messaggio", "Nuovo appello pubblicato");
				Console.print("Creazione appello con successo", "app");
				return true;
			} else {
				Utils.createErrorDialog(view.getShell(), "Messaggio", "Inserimento fallito");
			}
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}

}
