package businessLogic;

import domainModel.Studente;
import userInterface.ViewStudente;

public class ControllerStudente {

	private Studente studente;
	private ViewStudente viewStudente;
	
	public ControllerStudente() {
		viewStudente = new ViewStudente(this);
	}
	
	public void ShowStudenteWidget() {
		viewStudente.ShowStudenteWidget();
	}
	
	
}
