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
			return Database.getInstance().getDomandeTesi();
		}else {
			Utils.createConfirmDialog(viewDocente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
}
