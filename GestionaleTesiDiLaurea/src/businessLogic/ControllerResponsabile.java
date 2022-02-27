package businessLogic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import databaseAccessObject.Database;
import domainModel.Responsabile;
import userInterface.ViewResponsabile;
import domainModel.AppelloTesi;
import domainModel.Aula;


public class ControllerResponsabile {
	
	public Responsabile responsabile;
	private ViewResponsabile viewResponsabile;


	public ControllerResponsabile(String matricola, String nome, String cognome) {
		responsabile = new Responsabile(matricola, nome, cognome);
		viewResponsabile = new ViewResponsabile(this);
	}
	
	public void showResponsabileWidget() {
		viewResponsabile.ShowResponsabileWidget();
	}
	
	public void showListaAppelli() {
		AppelloTesi[] appelli = Database.getInstance().getAppelli();
		
		viewResponsabile.ShowListaAppello(appelli);
	}
	
	public void showListaAule(int idAppello, String currentAula) {
		Aula[] aule = Database.getInstance().getAule();
		
		viewResponsabile.ShowListaAule(aule,idAppello,currentAula);
	}

	public boolean creaAppello(String date) {
		System.out.println(date);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
		Date dataAppello = null;
    	try {
			dataAppello = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        	
        if(dataAppello == null) {
        	return false;
        }
        

        
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataAppello);
        
        System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(cal.getTime()));
        if(Database.getInstance().aggiungiAppello(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(cal.getTime()))) {
        	viewResponsabile.showMessage("Messaggio: Inserimento appello con successo!",1);
        }else {
        	viewResponsabile.showMessage("Errore: La data dell'appello esista di gi�",0);
        }
        
        

		return true;
	}
	
	public boolean prenotaAula(int idAula, int idAppello, String currentAula) {
		if(Database.getInstance().prenotaAula(idAula, idAppello,currentAula)) {
			
			return true;
		}
		return false;
	}
	
	public void showProgrammaInformazioniAppelloWidget(AppelloTesi currentAppelloTesi) {
		String informazioni = Database.getInstance().getInformazioniAppello(currentAppelloTesi.getId());
		viewResponsabile.ShowProgrammaInformazioniAppelloWidget(currentAppelloTesi,informazioni);
	}
	
	public Boolean programmaInformazioniPerAppello(int idAppello, String informazioni) {
		if(Database.getInstance().programmaInformazioniPerAppello(idAppello, informazioni)) {
			return true;
		}
		return false;
	}
	
}
