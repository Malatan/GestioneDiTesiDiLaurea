package system;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import databaseAccessObject.Database;
import domainModel.Studente;
import utils.Pair;
import utils.Utils;

public class MessaggioManager {
	private static Shell shell;
	private static MessaggioManager mm;
	private ArrayList<Messaggio> messaggi;
	
	public MessaggioManager() {
		messaggi = new ArrayList<Messaggio>();
	}

	public static MessaggioManager getInstance(Shell s) {
		shell = s;
		if (mm != null) {
			return mm;
		}
		mm = new MessaggioManager();
		return mm;
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
