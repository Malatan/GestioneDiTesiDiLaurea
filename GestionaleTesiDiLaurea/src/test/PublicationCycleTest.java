package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import businessLogic.ControllerAppello;
import businessLogic.ControllerDocente;
import businessLogic.ControllerLogin;
import businessLogic.ControllerPresidenteCorso;
import businessLogic.ControllerPresidenteScuola;
import businessLogic.ControllerResponsabile;
import businessLogic.ControllerStudente;
import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.DomandaTesi;
import utils.Pair;
import utils.Utils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublicationCycleTest {
	

    @Test
    @DisplayName("Test pubblica appello")
    @Order(1)    
    public void pubblicaAppelloTest() {
    	ControllerResponsabile controller = new ControllerResponsabile("10002","Gianni","Manfrin");
    	assertTrue(controller.creaAppello(20002), () -> "Ok");
    }
    
    @Test
    @DisplayName("Test iscrizione appello")
    @Order(2)    
    public void iscrizioneStudenteTest() {
    	ControllerStudente controller = new ControllerStudente("10000","Tizio","Caio");
    	assertTrue(controller.iscrizione(Pair.of(20002, "Informatica"),Pair.of(10017, "Alvise Pagnotto")), () -> "Ok");
    	assertTrue(controller.addRepo("test"), () -> "Ok");
    }
    
    @Test
    @DisplayName("Test approvazione appello")
    @Order(3)    
    public void approvazioneAppelloTest() {
    	ControllerDocente controller = new ControllerDocente("10017","Alvise","Pagnotto",Pair.of(40002, "Dipartimento di Ingegneria dell'Informazione"));
    	ArrayList<DomandaTesi> domande = controller.getDomandeTesiFromDB();
    	DomandaTesi d = domande.get(domande.size()-1);
    	assertTrue(controller.approvaDomandaTesi(d,true), () -> "Ok");
    }
    
    
    @Test
    @DisplayName("Test identificazione membri, assegnazione presidente e set orario")
    @Order(4)    
    public void identificazioneMembriTest() {
    	ControllerPresidenteCorso controller = new ControllerPresidenteCorso("10006","Delfino","Pirozzi",Pair.of(20002, "Informatica"));

    	ArrayList<AppelloTesi> appelli = controller.getAppelliFromDB();
    	AppelloTesi appello = appelli.get(appelli.size()-1);
    	
		ControllerAppello ca = new ControllerAppello(appello, null, Utils.getRuolo(controller.getPresidenteCorso()), 
				6, controller.getPresidenteCorso());
    	
    	assertTrue(ca.updateMembriAppello(appello.getId(), new ArrayList<Integer>(Arrays.asList(10000)),
    			new ArrayList<Integer>(Arrays.asList(10017)), new ArrayList<Integer>(Arrays.asList(10018,10019,10020,10021))) , () -> "Ok");
    	
    	assertTrue(ca.updatePresidenteCommissione(appello.getId(), 10017),() -> "Ok");
    	assertTrue(ca.addDateToAppello("10/03/2022"),() -> "Ok");
    }
    
    
    @Test
    @DisplayName("Test settaggio orario, aula appello e link teleconferenza")
    @Order(5)    
    public void setOrarioAppelloTest() {
    	ControllerResponsabile controller = new ControllerResponsabile("10002","Gianni","Manfrin");
    	

    	ArrayList<AppelloTesi> appelli = controller.getAppelliFromDB();
    	AppelloTesi appello = appelli.get(appelli.size()-1);
    	
		ControllerAppello ca = new ControllerAppello(appello, null, Utils.getRuolo(controller.getResponsabile()), 
				4, controller.getResponsabile());
    	
    	assertTrue(ca.prenotaAula(50001) , () -> "Ok");
    	assertTrue(ca.setOrario("13:00") , () -> "Ok");
    	assertTrue(ca.addLinkTele("test.com") , () -> "Ok");
    }
    
    @Test
    @DisplayName("Test approvazione dell'appello")
    @Order(6)    
    public void approvaAppelloTest() {
    	ControllerPresidenteScuola controller = new ControllerPresidenteScuola("10003","Raniero","Calabrese");

    	ArrayList<AppelloTesi> appelli = controller.getAppelliFromDB();
    	AppelloTesi appello = appelli.get(appelli.size()-1);
    	
		ControllerAppello ca = new ControllerAppello(appello, null, Utils.getRuolo(controller.getPresidenteScuola()), 
				5, controller.getPresidenteScuola());
    	
    	assertTrue(ca.approvaAppello() , () -> "Ok");
    	
    }
    
    
}
