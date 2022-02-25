package businessLogic;

import domainModel.PresidenteCorso;
import userInterface.ViewPresidenteCorso;

public class ControllerPresidenteCorso {
	
	private ViewPresidenteCorso viewPresidenteCorso;
	private PresidenteCorso presidenteCorso;
	
	public ControllerPresidenteCorso(String matricola, String nome, String cognome) {
		viewPresidenteCorso = new ViewPresidenteCorso(this);
	}

	public void ShowPresidenteCorsoWidget() {
		viewPresidenteCorso.ShowPresidenteCorsoWidget();
		
	}

}
