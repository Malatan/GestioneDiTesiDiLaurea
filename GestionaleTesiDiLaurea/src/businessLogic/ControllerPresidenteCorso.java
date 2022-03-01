package businessLogic;

import java.util.ArrayList;

import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.PresidenteCorso;
import userInterface.ViewPresidenteCorso;
import utils.Utils;

public class ControllerPresidenteCorso {
	
	private ViewPresidenteCorso viewPresidenteCorso;
	private PresidenteCorso presidenteCorso;
	
	public ControllerPresidenteCorso(String matricola, String nome, String cognome) {
		presidenteCorso = new PresidenteCorso(nome,cognome,matricola);
		viewPresidenteCorso = new ViewPresidenteCorso(this);
	}
	
	public PresidenteCorso getPresidenteCorso() {
		return presidenteCorso;
	}

	public ArrayList<AppelloTesi> getAppelliFromDB() {
		ArrayList<AppelloTesi> appelli = null;
		if (Database.getInstance().isConnected()) {
			appelli = Database.getInstance().getAppelli();
		} else {
			Utils.createConfirmDialog(viewPresidenteCorso.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appelli;
	}
	
	public AppelloTesi getAppelloFromDB(int id_appello) {
		AppelloTesi appello = null;
		if (Database.getInstance().isConnected()) {
			appello = Database.getInstance().getAppello(id_appello);
		} else {
			Utils.createConfirmDialog(viewPresidenteCorso.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appello;
	}
	
	public void run() {
		viewPresidenteCorso.createAndRun();
		
	}

}
