package businessLogic;

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

	public AppelloTesi[] getAppelliFromDB() {
		AppelloTesi[] appelli = null;
		if (Database.getInstance().isConnected()) {
			appelli = Database.getInstance().getAppelli();
		} else {
			Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", "Connessione al database persa");
		}
		return appelli;
	}

	public boolean creaAppello(String data) {
		String msg = "Vuoi creare l'appello nella data di " + data + " ?";
		if(Utils.createYesNoDialog(viewResponsabile.getShell(), "Messaggio", msg)){
			if (Database.getInstance().isConnected()) {
				if (Database.getInstance().aggiungiAppello(data)) {
					msg = "L'appello del "+ data + " inserito con successo.";
					Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", msg);
					Console.print("Creazione appello(" + data + ") con successo", "app");
					return true;
				} else {
					Utils.createErrorDialog(viewResponsabile.getShell(), "Messaggio", "Inserimento fallito");
				}
			} else {
				Utils.createConfirmDialog(viewResponsabile.getShell(), "Messaggio", "Connessione al database persa");
			}
		} else {
			Console.print("Creazione appello(" + data + ") non confermato", "app");
		}
		return false;
	}
	
	/*
	public boolean prenotaAula(int idAula, int idAppello, String currentAula) {
		if (Database.getInstance().prenotaAula(idAula, idAppello, currentAula)) {

			return true;
		}
		return false;
	}
	
	public void showListaAule(int idAppello, String currentAula) {
		Aula[] aule = Database.getInstance().getAule();

		viewResponsabile.ShowListaAule(aule, idAppello, currentAula);
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
	}*/
}
