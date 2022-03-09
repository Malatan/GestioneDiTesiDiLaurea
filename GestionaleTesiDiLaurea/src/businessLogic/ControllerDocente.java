package businessLogic;

import java.util.ArrayList;

import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.Docente;
import domainModel.DomandaTesi;
import domainModel.Responsabile;
import system.Messaggio;
import system.MessaggioManager;
import userInterface.ViewDocente;
import utils.Pair;
import utils.Utils;

public class ControllerDocente {
	
	private Docente docente;
	private ViewDocente view;
	
	public ControllerDocente(String matricola, String nome, String cognome, Pair<Integer, String> dipartimento) {
		this.docente = new Docente(matricola, nome, cognome, dipartimento);
		view = new ViewDocente(this);
	}
	
	public Docente getDocente() {
		return docente;
	}

	public ViewDocente getViewDocente() {
		return view;
	}

	public void run() {
		view.createAndRun();
	}
	
	public ArrayList<Messaggio> getMessaggi(){
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getMessaggi(docente.getMatricolaInt());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public ArrayList<DomandaTesi> getDomandeTesiFromDB(){
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getDomandeTesi(Integer.parseInt(docente.getMatricola()));
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public ArrayList<Pair<AppelloTesi, Integer>> getAppelliFromDB(){
		ArrayList<Pair<AppelloTesi, Integer>> appelli = new ArrayList<Pair<AppelloTesi, Integer>>();
		if(Database.getInstance().isConnected()) {
			appelli =  Database.getInstance().getAppelliAndRuoloByDocente(docente.getMatricola());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
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
	
	public boolean getPrensenzaDeterminazione(int id_appello) {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getPresenzaDeterminazione(docente.getMatricola(), id_appello);
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean approvaDomandaTesi(DomandaTesi domanda) {
		if(Database.getInstance().isConnected()) {
			if(Utils.createYesNoDialog(view.getShell(), "Conferma", "Vuole confermare l'approvazione della domanda di tesi dello studente?")) {
				Database.getInstance().approvaDomandaTesi(domanda);
				MessaggioManager.getInstance(view.getShell()).notificaApprovaDomanda(domanda, docente);
				Utils.createConfirmDialog(view.getShell(), "Messaggio", "Domanda di tesi approvata");
				return true;
			}
			return false;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
}
