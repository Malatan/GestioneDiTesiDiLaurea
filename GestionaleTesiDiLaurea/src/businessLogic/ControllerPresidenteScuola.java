package businessLogic;

import java.util.ArrayList;

import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.PresidenteScuola;
import userInterface.ViewPresidenteScuola;
import utils.Utils;

public class ControllerPresidenteScuola {
	private PresidenteScuola presidenteScuola;
	private ViewPresidenteScuola viewPresidenteScuola;
	
	public ControllerPresidenteScuola(String matricola, String nome, String cognome) {
		presidenteScuola = new PresidenteScuola(nome,cognome,matricola);
		viewPresidenteScuola = new ViewPresidenteScuola(this);
	}
	
	public PresidenteScuola getPresidenteScuola() {
		return presidenteScuola;
	}
	
	public void run() {
		viewPresidenteScuola.createAndRun();
	}

	public ArrayList<AppelloTesi> getAppelliFromDB() {
		ArrayList<AppelloTesi> appelli = null;
		if (Database.getInstance().isConnected()) {
			appelli = Database.getInstance().getAppelli();
		} else {
			Utils.createConfirmDialog(viewPresidenteScuola.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appelli;
	}
	
	public ArrayList<AppelloTesi> getAppelliCompletatiFromDB() {
		ArrayList<AppelloTesi> appelli = null;
		if (Database.getInstance().isConnected()) {
			appelli = Database.getInstance().getAppelliCompletati();
		} else {
			Utils.createConfirmDialog(viewPresidenteScuola.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appelli;
	}
	
	public AppelloTesi getAppelloFromDB(int id_appello) {
		AppelloTesi appello = null;
		if (Database.getInstance().isConnected()) {
			appello = Database.getInstance().getAppello(id_appello);
		} else {
			Utils.createConfirmDialog(viewPresidenteScuola.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appello;
	}

}
