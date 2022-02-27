package businessLogic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

	public void showListaAppelli() {
		AppelloTesi[] appelli = Database.getInstance().getAppelli();

		viewResponsabile.ShowListaAppello(appelli);
	}

	public void showListaAule(int idAppello, String currentAula) {
		Aula[] aule = Database.getInstance().getAule();

		viewResponsabile.ShowListaAule(aule, idAppello, currentAula);
	}

	public void creaAppello(String data) {
		String msg = "Vuoi creare l'appello nella data di " + data + " ?";
		if(Utils.createYesNoDialog(viewResponsabile.getShell(), "Messaggio", msg)){
			if (Database.getInstance().isConnected()) {
				if (Database.getInstance().aggiungiAppello(data)) {
					msg = "L'appello del "+ data + " inserito con successo.";
					Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", msg);
				} else {
					Utils.createErrorDialog(viewResponsabile.getShell(), "Messaggio", "Inserimento fallito");
				}
			} else {
				Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", "Connessione al database persa");
			}
		}
	}

	public boolean prenotaAula(int idAula, int idAppello, String currentAula) {
		if (Database.getInstance().prenotaAula(idAula, idAppello, currentAula)) {

			return true;
		}
		return false;
	}

	public void showProgrammaInformazioniAppelloWidget(AppelloTesi currentAppelloTesi) {
		String informazioni = Database.getInstance().getInformazioniAppello(currentAppelloTesi.getId());
		viewResponsabile.ShowProgrammaInformazioniAppelloWidget(currentAppelloTesi, informazioni);
	}

	public Boolean programmaInformazioniPerAppello(int idAppello, String informazioni) {
		if (Database.getInstance().programmaInformazioniPerAppello(idAppello, informazioni)) {
			return true;
		}
		return false;
	}

}
