package businessLogic;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import userInterface.ViewAppello;
import utils.Pair;
import utils.Utils;

public class ControllerAppello {
	private AppelloTesi appello;
	private ViewAppello viewAppello;
	private String matricola;
	private int ruolo;
	
	public ControllerAppello(AppelloTesi appello, Shell parent, String matricola, int ruolo) {
		this.appello = appello;
		this.matricola = matricola;
		this.ruolo = ruolo;
		viewAppello = new ViewAppello(parent, this);
	}
	
	public AppelloTesi getAppello() {
		return this.appello;
	}
	
	public void run() {
		viewAppello.createAndRun();
	}
	
	public void updateAppelloFromDB() {
		if (Database.getInstance().isConnected()) {
			appello = Database.getInstance().getAppello(appello.getId());
		} else {
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
	}
	
	public ArrayList<Pair<Integer, String>> getAuleFromDB(){
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getAule();
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public String getLinkTeleFromDB() {
		String s = "";
		if(Database.getInstance().isConnected()) {
			s = Database.getInstance().getLinkTele(appello.getId());
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		if (s != null)
			return s;
		else
			return "";
	}
	
	public boolean addLinkTele(String link) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().addLinkTele(link, appello.getId());
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Il link di teleconferenza e' stato aggiornato");
			return true;
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean prenotaAula(int id_aula, String data) {
		if(Database.getInstance().isConnected()) {
			if (Database.getInstance().prenotaAula(data, id_aula, appello.getId(), matricola)){
				Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "L'aula prenotata con successo");
				return true;
			} else {
				Utils.createWarningDialog(viewAppello.getShell(), "Messaggio", "L'aula occupata");
			}
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}

	public int getRuolo() {
		return ruolo;
	}

	public void setRuolo(int ruolo) {
		this.ruolo = ruolo;
	}
}
