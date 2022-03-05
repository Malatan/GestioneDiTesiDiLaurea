package businessLogic;

import java.util.ArrayList;

import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.PresidenteCorso;
import userInterface.ViewPresidenteCorso;
import utils.Pair;
import utils.Utils;

public class ControllerPresidenteCorso {
	
	private ViewPresidenteCorso view;
	private PresidenteCorso presidenteCorso;
	
	public ControllerPresidenteCorso(String matricola, String nome, String cognome, Pair<Integer, String> corso) {
		presidenteCorso = new PresidenteCorso(nome,cognome,matricola, corso);
		view = new ViewPresidenteCorso(this);
	}
	
	public PresidenteCorso getPresidenteCorso() {
		return presidenteCorso;
	}

	public ArrayList<AppelloTesi> getAppelliFromDB() {
		ArrayList<AppelloTesi> appelli = null;
		if (Database.getInstance().isConnected()) {
			appelli = Database.getInstance().getAppelliByCorso(presidenteCorso.getCorso().first);
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appelli;
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
	
	public void run() {
		view.createAndRun();
		
	}

}
