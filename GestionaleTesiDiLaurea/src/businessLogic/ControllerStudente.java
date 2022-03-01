package businessLogic;

import java.time.LocalDate;
import java.util.ArrayList;

import databaseAccessObject.Database;
import domainModel.Studente;
import userInterface.ViewStudente;
import utils.Pair;
import utils.Utils;

public class ControllerStudente {

	private Studente studente;
	private ViewStudente viewStudente;
	
	public ControllerStudente(String matricola, String nome, String cognome) {
		this.studente = new Studente(nome, cognome, matricola);
		viewStudente = new ViewStudente(this);
		
	}
	
	public Studente getStudente() {
		return this.studente;
	}
	
	public void run() {
		viewStudente.createAndRun();
	}
	
	public ArrayList<Pair<Integer, String>> getCorsiFromDB() {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getCorsi();
		}else {
			Utils.createConfirmDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public ArrayList<Pair<Integer, String>> getDocentiFromDB() {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getDocenti();
		}else {
			Utils.createConfirmDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public String getStatusTesi() {
		Pair<Integer, String> status = Pair.of(-1, "");
		if(Database.getInstance().isConnected()) {
			status = Database.getInstance().getStatusTesi(studente.getMatricolaInt());
			studente.setStatusTesi(status.first);	
		}else {
			Utils.createConfirmDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return status.second;
	}
	
	public boolean iscrizione(int id_corso, int matricola_relatore) {
		LocalDate today = LocalDate.now();
		String data = today.getYear() + "-" + today.getMonthValue() + "-" + today.getDayOfMonth();
		if(Database.getInstance().isConnected()) {
			Database.getInstance().iscrizioneTesi(studente, data, id_corso, matricola_relatore);
			Utils.createConfirmDialog(viewStudente.getShell(), "Messaggio", "Iscrizione e' avvenuta con successo!");
			return true;
		}else {
			Utils.createErrorDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean ritiraDomanda() {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().ritiraDomanda(studente.getMatricola());
			Utils.createConfirmDialog(viewStudente.getShell(), "Messaggio", "La domanda per la tesi e' stata ritirata.");
			return true;
		}else {
			Utils.createErrorDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean addRepo(String file) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().addRepo(file, studente.getMatricola());
			Utils.createConfirmDialog(viewStudente.getShell(), "Messaggio", "Il repository e' stato aggiornato");
			return true;
		}else {
			Utils.createErrorDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public String getRepositoryFromDB() {
		String s = "";
		if(Database.getInstance().isConnected()) {
			s = Database.getInstance().getRepository(studente.getMatricola());
		}else {
			Utils.createErrorDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
		if (s != null)
			return s;
		else
			return "";
	}
}
