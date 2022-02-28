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
	
	public ControllerAppello(AppelloTesi appello, Shell parent, String matricola) {
		this.appello = appello;
		this.matricola = matricola;
		viewAppello = new ViewAppello(parent, this);
	}
	
	public AppelloTesi getAppello() {
		return this.appello;
	}
	
	public void run() {
		viewAppello.createAndRun();
	}
	
	public ArrayList<Pair<Integer, String>> getAuleFromDB(){
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getAule();
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public boolean prenotaAula(int id_aula, String data) {
		if(Database.getInstance().isConnected()) {
			if (Database.getInstance().prenotaAula(data, id_aula, matricola)){
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
}
