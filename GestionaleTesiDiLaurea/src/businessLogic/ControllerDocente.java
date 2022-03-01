package businessLogic;

import java.util.ArrayList;

import databaseAccessObject.Database;
import domainModel.Docente;
import domainModel.DomandaTesi;
import domainModel.Responsabile;
import userInterface.ViewDocente;
import utils.Utils;

public class ControllerDocente {
	
	private Docente docente;
	private ViewDocente viewDocente;
	
	public ControllerDocente(String matricola, String nome, String cognome) {
		this.docente = new Docente(matricola, nome, cognome);
		viewDocente = new ViewDocente(this);
	}
	
	public Docente getDocente() {
		return docente;
	}

	public ViewDocente getViewDocente() {
		return viewDocente;
	}

	public void run() {
		viewDocente.createAndRun();
	}
	
	public ArrayList<DomandaTesi> getDomandeTesiFromDB(){
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getDomandeTesi(Integer.parseInt(docente.getMatricola()));
		}else {
			Utils.createErrorDialog(viewDocente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public boolean approvaDomandaTesi(DomandaTesi domanda) {
		if(Database.getInstance().isConnected()) {
			if(Utils.createYesNoDialog(viewDocente.getShell(), "Conferma", "Vuole confermare l'approvazione della domanda di tesi dello studente?")) {
				Database.getInstance().approvaDomandaTesi(domanda);
				Utils.createConfirmDialog(viewDocente.getShell(), "Messaggio", "Domanda di tesi approvata");
				return true;
			}

			return false;
		}else {
			Utils.createErrorDialog(viewDocente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
}
