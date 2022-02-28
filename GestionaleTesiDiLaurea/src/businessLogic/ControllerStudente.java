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
	
	public String getStatusTesi() {
		String s = "";
		if(Database.getInstance().isConnected()) {
			int caso = Database.getInstance().getStatusTesi(studente.getMatricolaInt());
			switch(caso) {
				case 0:
					s = "Non hai ancora prensetato nessuna domanda";
					break;
				case 1:
					s = "Hai presentato la domanda per la tesi. "
							+ "Attenta la conferma da parte del tuo relatore e l'assegnazione all'appello.";
					break;
				case 2:
					break;
				default:
					s = "Errore";
			}		
		}else {
			Utils.createConfirmDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
		return s;
	}
	
	public void iscrizione(int id_corso) {
		LocalDate today = LocalDate.now();
		String data = today.getYear() + "-" + (today.getMonthValue() + 1) + "-" + today.getDayOfMonth();
		if(Database.getInstance().isConnected()) {
			Database.getInstance().iscrizioneTesi(studente, data, id_corso);
			Utils.createConfirmDialog(viewStudente.getShell(), "Messaggio", "Iscrizione e' avvenuta con successo!");
		}else {
			Utils.createErrorDialog(viewStudente.getShell(), "Messaggio", "Connessione al database persa");
		}
	}
	
	
}
