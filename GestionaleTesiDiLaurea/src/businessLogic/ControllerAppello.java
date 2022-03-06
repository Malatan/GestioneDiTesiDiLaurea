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
import domainModel.SuggerimentoSostituto;
import domainModel.Utente;
import userInterface.ViewAppello;
import utils.Console;
import utils.Pair;
import utils.Utils;

public class ControllerAppello {
	private AppelloTesi appello;
	private ViewAppello view;
	private String matricola;
	private Utente utente;
	private int ruoloDidattica;
	private int ruoloAppello;
	
	public ControllerAppello(AppelloTesi appello, Shell parent, String matricola, int ruoloDidattica) {
		this.appello = appello;
		this.matricola = matricola;
		this.ruoloDidattica = ruoloDidattica;
		view = new ViewAppello(parent, this);
	}
	
	public ControllerAppello(AppelloTesi appello, Shell parent, String matricola, int ruolo, Utente utente) {
		this.appello = appello;
		this.matricola = matricola;
		this.ruoloDidattica = ruolo;
		this.utente = utente;
		view = new ViewAppello(parent, this);
	}
	
	public ControllerAppello(AppelloTesi appello, Shell parent, String matricola, int ruoloDidattica, int ruoloAppello, Utente utente) {
		this.appello = appello;
		this.matricola = matricola;
		this.ruoloDidattica = ruoloDidattica;
		this.utente = utente;
		this.setRuoloAppello(ruoloAppello);
		view = new ViewAppello(parent, this);
	}
	
	public AppelloTesi getAppello() {
		return this.appello;
	}
	
	public Utente getUtente() {
		return this.utente;
	}
	
	public void run() {
		view.createAndRun();
	}
	
	public boolean updateMembriAppello(int id_appello, ArrayList<Integer> studenti, ArrayList<Integer> relatori, ArrayList<Integer> commissioni) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().updateMembriAppello(id_appello, studenti, relatori, commissioni);
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "I membri della commissione di tesi sono stati identificati correttamente!");
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean updatePresidenteCommissione(int id_appello, int matricola) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().updatePresidenteCommissione(id_appello, matricola);
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Presidente di commissione di tesi aggiunto correttamente!");
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public void updateAppelloFromDB() {
		if (Database.getInstance().isConnected()) {
			appello = Database.getInstance().getAppello(appello.getId());
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
	}
	
	public ArrayList<Pair<Integer, String>> getAuleFromDB(){
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getAule();
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public ArrayList<Docente> getDocentiPerSostitutzione(){
		ArrayList<Docente> docenti = new ArrayList<Docente>();
		if(Database.getInstance().isConnected()) {
			docenti = Database.getInstance().getDocentiPerSostituzione(appello.getId());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return docenti;
	}
	
	public String getLinkTeleFromDB() {
		String s = "";
		if(Database.getInstance().isConnected()) {
			s = Database.getInstance().getLinkTele(appello.getId());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		if (s != null)
			return s;
		else
			return "";
	}
	
	public String getStatusAppello() {
		String s = "";
		if(Database.getInstance().isConnected()) {
			s = Database.getInstance().getStatusAppello(appello.getId());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		if (s != null)
			return s;
		else
			return "";
	}
	
	public ArrayList<Pair<Studente, Docente>> getStudentiRelatoriFromDB(){
		ArrayList<Studente> studenti = new ArrayList<Studente>();
		ArrayList<Pair<Studente, Docente>> studentiRelatori = new ArrayList<Pair<Studente, Docente>>();
		if (Database.getInstance().isConnected()) {
			studenti = Database.getInstance().getMyStudenti(matricola);
			Database.getInstance().getStudentiStatusTesi(studenti);
			studentiRelatori = Database.getInstance().getRelatoriByStudenti(studenti);
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return studentiRelatori;
	}
	
	public ArrayList<Pair<Studente, Docente>> getStudentiRelatoriFromAppelloFromDB(){
		ArrayList<Pair<Studente, Docente>> studentiRelatori = new ArrayList<Pair<Studente, Docente>>();
		if (Database.getInstance().isConnected()) {
			studentiRelatori = Database.getInstance().getRelatoriStudentiFromAppello(appello.getId());
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return studentiRelatori;
	}
	
	public Docente getPresidenteCommissioneFromDB(){
		Docente presidente = null;
		if (Database.getInstance().isConnected()) {
			presidente = Database.getInstance().getPresidenteCommissione(getAppello().getId());
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return presidente;
	}
	
	public ArrayList<Docente> getCommissioniDB(){
		ArrayList<Docente> membri = new ArrayList<Docente>();
		if (Database.getInstance().isConnected()) {
			membri = Database.getInstance().getCommissioniByAppello(getAppello().getId());
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return membri;
	}
	
	public ArrayList<Studente> getStudentiFromAppelloDB(){
		ArrayList<Studente> studenti= new ArrayList<Studente>();
		if (Database.getInstance().isConnected()) {
			studenti = Database.getInstance().getStudentiFromAppello(getAppello().getId());
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return studenti;
	}
	
	public ArrayList<Docente> getRelatoriFromDB(){
		ArrayList<Docente> relatori = new ArrayList<Docente>();
		if (Database.getInstance().isConnected()) {
			relatori = Database.getInstance().getRelatoriByAppello(appello.getId());
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return relatori;
	}
	
	public ArrayList<Pair<Docente,String>> getDocentiDipFromDB(){
		ArrayList<Pair<Docente,String>> docenti = new ArrayList<Pair<Docente,String>>();
		if (Database.getInstance().isConnected()) {
			docenti = Database.getInstance().getDocentiAndDip();
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
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
				Utils.createConfirmDialog(view.getShell(), "Messaggio", "La data dell'appello e' stata inserita correttamente!");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			

			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean addLinkTele(String link) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().addLinkTele(link, appello.getId());
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Il link di teleconferenza e' stato aggiornato");
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean addSuggerimento(int id_sostituto, String nota) {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().addSuggerimentoSostituto(appello.getId(), utente.getMatricolaInt(), id_sostituto, nota);
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean updateSuggerimento(SuggerimentoSostituto suggerimento) {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().updateSuggerimentoSostituto(suggerimento);
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean setSuggerimentoStatus(SuggerimentoSostituto suggerimento, int status) {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().setSuggerimentoSostitutoStatus(suggerimento, status);
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean revocaSuggerimento(int id_suggerimento) {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().revocaDateToAppello(id_suggerimento);
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public SuggerimentoSostituto getSuggerimentoByAppelloDocente() {
		if(Database.getInstance().isConnected()) {
			return Database.getInstance().getSuggerimentoByAppelloAndDocente(appello.getId(), utente.getMatricolaInt());
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return null;
	}
	
	public ArrayList<SuggerimentoSostituto> getProposteByAppelloFromDB(){
		ArrayList<SuggerimentoSostituto> proposte = new ArrayList<SuggerimentoSostituto>();
		if (Database.getInstance().isConnected()) {
			proposte = Database.getInstance().getProposteByAppelloFromDB(appello.getId());
		} else {
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return proposte;
	}
	
	public boolean approvaAppello() {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().setAppelloApprovazione(appello.getId(),1);
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "L'appello e' stato approvato correttamente!");
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean richiediCorrezione() {
		if(Database.getInstance().isConnected()) {
			Database.getInstance().setAppelloApprovazione(appello.getId(),2);
			Utils.createConfirmDialog(view.getShell(), "Messaggio", "E' stato richiesto una correzione");
			return true;
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean prenotaAula(int id_aula) {
		if(Database.getInstance().isConnected()) {
			if (Database.getInstance().prenotaAula(id_aula, appello.getId(), matricola)){
				Utils.createConfirmDialog(view.getShell(), "Messaggio", "L'aula prenotata con successo e orario settato correttamente.");
				return true;
			} else {
				Utils.createWarningDialog(view.getShell(), "Messaggio", "L'aula occupata");
			}
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}
	
	public boolean setOrario(String time) {
		if(Database.getInstance().isConnected()) {
			if (Database.getInstance().setOrario(appello.getId(),time)){

				return true;
			} else {
				Utils.createWarningDialog(view.getShell(), "Messaggio", "Orario gia' inserito");
			}
		}else {
			Utils.createErrorDialog(view.getShell(), "Messaggio", "Connessione al database persa");
		}
		return false;
	}

	public int getRuoloDidattica() {
		return ruoloDidattica;
	}

	public void setRuoloDidattica(int ruolo) {
		this.ruoloDidattica = ruolo;
	}

	public int getRuoloAppello() {
		return ruoloAppello;
	}

	public void setRuoloAppello(int ruoloAppello) {
		this.ruoloAppello = ruoloAppello;
	}
}
