package system;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.Docente;
import domainModel.DomandaTesi;
import domainModel.Studente;
import domainModel.Utente;
import utils.Pair;
import utils.Utils;

public class MessaggioManager {
	private static Shell shell;
	private static MessaggioManager mm;
	
	public MessaggioManager() {
		
	}

	public static MessaggioManager getInstance(Shell s) {
		shell = s;
		if (mm != null) {
			return mm;
		}
		mm = new MessaggioManager();
		return mm;
	}
	
	public ArrayList<Messaggio> getMyMessaggi(Utente me){
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getMessaggi(me.getMatricolaInt());
		}else {
			Utils.createErrorDialog(shell, "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public boolean notificaNuovaDomanda(Studente studente, Pair<Integer, String> corso, Pair<Integer, String> relatore) {
		String titolo = "Nuova domanda di tesi";
		String contenuto = "Salve " + relatore.second + ",\nlo studente " + studente.getNomeCognome() 
				+ " | Matricola: " + studente.getMatricola() + " ha mandato una domanda di tesi di corso " + corso.second 
				+ " e ha scelto lei come il relatore di tesi.";
		return mandaMessaggio(studente.getMatricolaInt(), relatore.first, titolo, contenuto);
	}
	
	public boolean notificaRitiraDomanda(Studente studente) {
		String titolo = "Ritiro domanda di tesi";
		String contenuto = "Salve " + studente.getRelatore().second + ",\nlo studente " + studente.getNomeCognome() 
				+ " | Matricola: " + studente.getMatricola() + " ha ritirato la domanda di tesi di corso " + studente.getCorso().second 
				+ " e con lei come il relatore di tesi.";
		return mandaMessaggio(studente.getMatricolaInt(), studente.getRelatore().first, titolo, contenuto);
	}
	
	public boolean notificaUpdateRepo(Studente studente, String repository) {
		String titolo = "Aggiornamento Repository Tesi";
		String contenuto = "Salve " + studente.getRelatore().second + ",\nlo studente " + studente.getNomeCognome() 
				+ " | Matricola: " + studente.getMatricola() + " ha aggiornato il link di repository di tesi."
				+ "\nNuovo link repository: " + repository;
		return mandaMessaggio(studente.getMatricolaInt(), studente.getRelatore().first, titolo, contenuto);
	}
	
	public boolean notificaApprovaDomanda(DomandaTesi domanda, Docente docente) {
		String titolo = "Approvazione Domanda Tesi";
		String contenuto = "Salve " + domanda.getNomeCognomeStudente() + ",\nLa sua domanda di tesi di data " + domanda.getData() + " di corso "
				+ domanda.getNomeCorso() + " con il relatore " + docente.getNomeCognome() + " e' stata approvata. "
						+ "Attendere la convocazione per la discussione di tesi.";
		return mandaMessaggio(docente.getMatricolaInt(), domanda.getMatricolaStudente(), titolo, contenuto);
	}
	
	public void notificaConvocazione(AppelloTesi appello, ArrayList<Docente> commissione, ArrayList<Pair<Studente, Docente>> studentiRelatori) {
		String titolo = "Convocazione per la discussione di Tesi";
		String repos = "";
		for (Pair<Studente, Docente> p : studentiRelatori) {
			repos += "\n" + p.first.getNomeCognome() + " - " + p.first.getRepository();
		}
		for (Docente d : commissione) {
			String contenuto = "Salve " + d.getNomeCognome() + ",\n Dettaglio appello di tesi: \nData: " + appello.getData()
			+ "\nore: " + appello.getStartTime() + "\nAula: " + appello.getAula() + "\nLink teleconferenza: " + appello.getLinkTeleconferenza()
			+ "\nlink repository dei candidati: ";
			contenuto += repos;
			mandaMessaggio(100, d.getMatricolaInt(), titolo, contenuto);
		}
		for (Pair<Studente, Docente> p : studentiRelatori) {
			String contenuto = "Salve " + p.second.getNomeCognome() + ",\n Dettaglio appello di tesi: \nData: " + appello.getData()
			+ "\nore: " + appello.getStartTime() + "\nAula: " + appello.getAula() + "\nLink teleconferenza: " + appello.getLinkTeleconferenza()
			+ "\nlink repository dei candidati: ";
			contenuto += repos;
			mandaMessaggio(100, p.second.getMatricolaInt(), titolo, contenuto);
		}
		for (Pair<Studente, Docente> p : studentiRelatori) {
			String contenuto = "Salve " + p.second.getNomeCognome() + ",\n Dettaglio appello di tesi: \nData: " + appello.getData()
			+ "\nore: " + appello.getStartTime() + "\nAula: " + appello.getAula() + "\nLink teleconferenza: " + appello.getLinkTeleconferenza();
			mandaMessaggio(100, p.first.getMatricolaInt(), titolo, contenuto);
		}
	}
	
	public boolean updateLetto(int id) {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().updateMessaggioLetto(id);
		}else {
			Utils.createErrorDialog(shell, "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean mandaMessaggio(int id_sorgente, int id_destinatario, String titolo, String contenuto) {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().mandaMessaggio(id_sorgente, id_destinatario, titolo, contenuto);
		}else {
			Utils.createErrorDialog(shell, "Messaggio", "Connessione al database persa");
		}
		return false;
	}
}
