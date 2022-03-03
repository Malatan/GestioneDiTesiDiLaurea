package businessLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import org.eclipse.swt.widgets.Shell;

import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.Docente;
import domainModel.Studente;
import userInterface.ViewAppello;
import utils.Console;
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
	
	public boolean aggiungiStudentiDocentiToCommissione(int id_appello, ArrayList<Integer> studenti, ArrayList<Integer> docentiRelatori) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().aggiungiStudentiDocenti(id_appello, studenti, docentiRelatori);
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "I membri della commissione di tesi sono stati identificati correttamente!");
			return true;
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean aggiungiPresidenteCorsoToCommissione(int id_appello, int matricola) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().aggiungiPresidenteCorso(id_appello, matricola);
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Presidente di commissione di tesi aggiunto correttamente!");
			return true;
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
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
	
	public String getStatusAppello() {
		String s = "";
		if(Database.getInstance().isConnected()) {
			s = Database.getInstance().getStatusApprovazioneAppello(appello.getId());
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		if (s != null)
			return s;
		else
			return "";
	}
	
	public ArrayList<Pair<Integer,String>> getStudentiFromDB(){
		ArrayList<Pair<Integer,String>> studenti = new ArrayList<Pair<Integer,String>>();
		if (Database.getInstance().isConnected()) {
			studenti = Database.getInstance().getMyStudenti(matricola);
		} else {
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return studenti;
	}
	
	public Pair<Integer,String> getPresidenteCommissioneFromDB(){
		Pair<Integer,String> presidente = null;
		if (Database.getInstance().isConnected()) {
			presidente = Database.getInstance().getPresidenteCommissione(getAppello().getId());
		} else {
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return presidente;
	}
	
	public ArrayList<Pair<Integer,String>> getMembriFromCommissioneDB(){
		ArrayList<Pair<Integer,String>> membri = new ArrayList<Pair<Integer,String>>();
		if (Database.getInstance().isConnected()) {
			membri = Database.getInstance().getMembriCommissioneById(getAppello().getId());
		} else {
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return membri;
	}
	
	public ArrayList<Pair<Integer,String>> getStudentiFromAppelloDB(){
		ArrayList<Pair<Integer,String>> studentiMembri = new ArrayList<Pair<Integer,String>>();
		if (Database.getInstance().isConnected()) {
			studentiMembri = Database.getInstance().getStudentiFromAppello(getAppello().getId());
		} else {
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return studentiMembri;
	}
	
	public ArrayList<Pair<Integer,String>> getRelatoriFromDB(){
		ArrayList<Pair<Integer,String>> relatori = new ArrayList<Pair<Integer,String>>();
		if (Database.getInstance().isConnected()) {
			relatori = Database.getInstance().getRelatori();
		} else {
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return relatori;
	}
	
	public ArrayList<Pair<Integer,String>> getDocentiFromDB(){
		ArrayList<Pair<Integer,String>> docenti = new ArrayList<Pair<Integer,String>>();
		if (Database.getInstance().isConnected()) {
			docenti = Database.getInstance().getDocenti();
		} else {
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return docenti;
	}
	
	public boolean addDateToAppello(String date) {
		if(Database.getInstance().isConnected()) {
			
			try {
				java.util.Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				
				Console.print(sqlDate.toString(),"GUI");
				
				Database.getInstance().addDateToAppello(sqlDate.toString(), appello.getId());
				Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "La data dell'appello e' stata inserita correttamente!");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			

			return true;
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
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
	
	public boolean approvaAppello() {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().setAppelloApprovazione(appello.getId(),1);
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "L'appello e' stato approvato correttamente!");
			return true;
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean richiediCorrezione() {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().setAppelloApprovazione(appello.getId(),2);
			Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "E' stato richiesto una correzione");
			return true;
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean prenotaAula(int id_aula) {
		if(Database.getInstance().isConnected()) {
			if (Database.getInstance().prenotaAula(id_aula, appello.getId(), matricola)){
				Utils.createConfirmDialog(viewAppello.getShell(), "Messaggio", "L'aula prenotata con successo e orario settato correttamente.");
				return true;
			} else {
				Utils.createWarningDialog(viewAppello.getShell(), "Messaggio", "L'aula occupata");
			}
		}else {
			Utils.createErrorDialog(viewAppello.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean setOrario(String time) {
		if(Database.getInstance().isConnected()) {
			if (Database.getInstance().setOrario(appello.getId(),time)){

				return true;
			} else {
				Utils.createWarningDialog(viewAppello.getShell(), "Messaggio", "Orario gia' inserito");
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
