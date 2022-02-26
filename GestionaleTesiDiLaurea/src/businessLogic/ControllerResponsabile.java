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
	
	public void ShowResponsabileWidget() {
		viewResponsabile.ShowResponsabileWidget();
	}
	
	public void ShowListaAppelli() {
		AppelloTesi[] appelli = Database.getInstance().GetAppelli();
		
		viewResponsabile.ShowListaAppello(appelli);
	}
	
	public void ShowListaAule(int idAppello, String currentAula) {
		Aula[] aule = Database.getInstance().GetAule();
		
		viewResponsabile.ShowListaAule(aule,idAppello,currentAula);
	}

	public boolean CreaAppello(String date) {
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
        if(Database.getInstance().AggiungiAppello(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(cal.getTime()))) {
        	viewResponsabile.ShowMessage("Messaggio: Inserimento appello con successo!",1);
        }else {
        	viewResponsabile.ShowMessage("Errore: La data dell'appello esista di gi�",0);
        }
        
        

		return true;
	}
	
	public boolean PrenotaAula(int idAula, int idAppello, String currentAula) {
		if(Database.getInstance().PrenotaAula(idAula, idAppello,currentAula)) {
			
			return true;
		}
		return false;
	}
	
	public void ShowProgrammaInformazioniAppelloWidget(AppelloTesi currentAppelloTesi) {
		String informazioni = Database.getInstance().GetInformazioniAppello(currentAppelloTesi.getId());
		viewResponsabile.ShowProgrammaInformazioniAppelloWidget(currentAppelloTesi,informazioni);
	}
	
	public Boolean ProgrammaInformazioniPerAppello(int idAppello, String informazioni) {
		if(Database.getInstance().ProgrammaInformazioniPerAppello(idAppello, informazioni)) {
			return true;
		}
		return false;
	}
	
}
