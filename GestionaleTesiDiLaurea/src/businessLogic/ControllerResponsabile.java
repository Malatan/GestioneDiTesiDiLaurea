package businessLogic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;  

import domainModel.Responsabile;
import userInterface.ViewResponsabile;
import domainModel.AppelloTesi;
import domainModel.Database;

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
        	viewResponsabile.ShowMessage("Errore: La data dell'appello esista di già",0);
        }
        
        

		return true;
	}
	
	
	
	
}
