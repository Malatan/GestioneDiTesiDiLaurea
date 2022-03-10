package businessLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.Docente;
import domainModel.Studente;
import system.MessaggioManager;
import userInterface.ViewStudente;
import utils.Console;
import utils.Pair;
import utils.Utils;

public class ControllerStudente {

	private Studente studente;
	private ViewStudente view;
	
	public ControllerStudente(String matricola, String nome, String cognome) {
		this.studente = new Studente(nome, cognome, matricola);
		view = new ViewStudente(this);
		
	}
	
	public Studente getStudente() {
		return this.studente;
	}
	
	public void run() {
		view.createAndRun();
	}
	
	public ArrayList<Pair<Integer, String>> getCorsiFromDB() {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getCorsi();
		}else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public ArrayList<Pair<Integer, String>> getDocentiFromDB() {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getDocenti();
		}else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public String getStatusTesi() {
		Pair<Integer, String> status = Pair.of(-1, "");
		if(Database.getInstance().isConnected()) {
			status = Database.getInstance().getStatusTesiAndString(studente);
			studente.setStatusTesi(status.first);	
		}else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return status.second;
	}
	
	public boolean iscrizione(Pair<Integer, String> corso, Pair<Integer, String> relatore) {
		Console.print(corso.first+" " +corso.second + " " + relatore.first + " " + relatore.second,"DEBUG");
		LocalDate today = LocalDate.now();
		String data = today.getYear() + "-" + today.getMonthValue() + "-" + today.getDayOfMonth();
		if(Database.getInstance().isConnected()) {
			Database.getInstance().iscrizioneTesi(studente, data, corso.first, relatore.first);
			MessaggioManager.getInstance(view.getShell()).notificaNuovaDomanda(studente, corso, relatore);
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Iscrizione e' avvenuta con successo!");
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean ritiraDomanda() {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().ritiraDomanda(studente.getMatricola());
			MessaggioManager.getInstance(view.getShell()).notificaRelatoreRitiraDomanda(studente);
			if (studente.getStatusTesi() == 2) {
				Docente presidente = Database.getInstance().getPresidenteCommissione(studente.getIdAppello());
				ArrayList<Docente> membri = Database.getInstance().getCommissioneNoRelatoreByAppello(studente.getIdAppello());
				MessaggioManager.getInstance(view.getShell()).notificaCommissioneRitiraDomanda(studente, presidente, membri);
			}
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "La domanda per la tesi e' stata ritirata.");
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean addRepo(String file) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().addRepo(file, studente.getMatricola());
			MessaggioManager.getInstance(view.getShell()).notificaUpdateRepo(studente, file);
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Il repository e' stato aggiornato");
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	//appello - esito
	public Pair<AppelloTesi, Integer> getEsitoTesi() {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getEsitoTesi(studente.getMatricolaInt());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public String getRepositoryFromDB() {
		String s = "";
		if(Database.getInstance().isConnected()) {
			s = Database.getInstance().getRepository(studente.getMatricola());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		if (s != null)
			return s;
		else
			return "";
	}
	
}
