package businessLogic;

import java.util.ArrayList;

import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.PresidenteScuola;
import domainModel.Verbale;
import system.Messaggio;
import userInterface.ViewPresidenteScuola;
import utils.Utils;

public class ControllerPresidenteScuola {
	private PresidenteScuola presidenteScuola;
	private ViewPresidenteScuola view;
	
	public ControllerPresidenteScuola(String matricola, String nome, String cognome) {
		presidenteScuola = new PresidenteScuola(matricola,nome,cognome);
		view = new ViewPresidenteScuola(this);
	}
	
	public PresidenteScuola getPresidenteScuola() {
		return presidenteScuola;
	}
	
	public void run() {
		view.createAndRun();
	}
	
	public ArrayList<Messaggio> getMessaggi(){
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getMessaggi(presidenteScuola.getMatricolaInt());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public boolean approvaVerbale(Verbale verbale) {
		if (Database.getInstance().isConnected()) {
			return Database.getInstance().approvaVerbale(verbale);
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
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
	
	public ArrayList<AppelloTesi> getAppelliCompletatiFromDB() {
		ArrayList<AppelloTesi> appelli = null;
		if (Database.getInstance().isConnected()) {
			appelli = Database.getInstance().getAppelliCompletati();
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appelli;
	}
	
	public ArrayList<Verbale> getVerbaliFromDB() {
		ArrayList<Verbale> verbali = new ArrayList<Verbale>();
		if (Database.getInstance().isConnected()) {
			verbali = Database.getInstance().getVerbali();
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return verbali;
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

}
