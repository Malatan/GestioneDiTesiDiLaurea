package businessLogic;

import java.util.ArrayList;

import databaseAccessObject.Database;
import domainModel.Responsabile;
import userInterface.ViewResponsabile;
import utils.Console;
import utils.Utils;
import domainModel.AppelloTesi;
import domainModel.Aula;

public class ControllerResponsabile {
	public Responsabile responsabile;
	private ViewResponsabile viewResponsabile;

	public ControllerResponsabile(String matricola, String nome, String cognome) {
		responsabile = new Responsabile(matricola, nome, cognome);
		viewResponsabile = new ViewResponsabile(this);
	}

	public void run() {
		viewResponsabile.createAndRun();
	}
	
	public Responsabile getResponsabile() {
		return responsabile;
	}
	
	public AppelloTesi getAppelloFromDB(int id_appello) {
		AppelloTesi appello = null;
		if (Database.getInstance().isConnected()) {
			appello = Database.getInstance().getAppello(id_appello);
		} else {
			Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appello;
	}
	
	public ArrayList<AppelloTesi> getAppelliFromDB() {
		ArrayList<AppelloTesi> appelli = null;
		if (Database.getInstance().isConnected()) {
			appelli = Database.getInstance().getAppelli();
		} else {
			Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appelli;
	}

	public boolean creaAppello() {
		if (Database.getInstance().isConnected()) {
			if (Database.getInstance().aggiungeAppello(responsabile.getMatricolaInt(), Utils.getTodayDate())) {
				Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", "Nuovo appello pubblicato");
				Console.print("Creazione appello con successo", "app");
				return true;
			} else {
				Utils.createErrorDialog(viewResponsabile.getShell(), "Messaggio", "Inserimento fallito");
			}
		} else {
			Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}

	/*
	 * public boolean prenotaAula(int idAula, int idAppello, String currentAula) {
	 * if (Database.getInstance().prenotaAula(idAula, idAppello, currentAula)) {
	 * 
	 * return true; } return false; }
	 * 
	 * public void showListaAule(int idAppello, String currentAula) { Aula[] aule =
	 * Database.getInstance().getAule();
	 * 
	 * viewResponsabile.ShowListaAule(aule, idAppello, currentAula); }
	 * 
	 * public void showProgrammaInformazioniAppelloWidget(AppelloTesi
	 * currentAppelloTesi) { String informazioni =
	 * Database.getInstance().getInformazioniAppello(currentAppelloTesi.getId());
	 * viewResponsabile.ShowProgrammaInformazioniAppelloWidget(currentAppelloTesi,
	 * informazioni); }
	 * 
	 * public Boolean programmaInformazioniPerAppello(int idAppello, String
	 * informazioni) { if
	 * (Database.getInstance().programmaInformazioniPerAppello(idAppello,
	 * informazioni)) { return true; } return false; }
	 */
}
