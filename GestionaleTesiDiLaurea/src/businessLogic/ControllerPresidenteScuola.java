package businessLogic;

import domainModel.PresidenteScuola;
import userInterface.ViewPresidenteScuola;

public class ControllerPresidenteScuola {
	private PresidenteScuola presidenteScuola;
	private ViewPresidenteScuola viewPresidenteScuola;
	
	public ControllerPresidenteScuola(String matricola, String nome, String cognome) {
		viewPresidenteScuola = new ViewPresidenteScuola(this);
	}
	
	public void ShowPresidenteScuolaWidget() {
		viewPresidenteScuola.ShowPresidenteScuolaWidget();
	}


}
