package businessLogic;

import domainModel.PresidenteCorso;
import userInterface.ViewPresidenteCorso;

public class ControllerPresidenteCorso {
	
	private ViewPresidenteCorso viewPresidenteCorso;
	private PresidenteCorso presidenteCorso;
	
	public ControllerPresidenteCorso(String matricola, String nome, String cognome) {
		presidenteCorso = new PresidenteCorso(nome,cognome,matricola);
		viewPresidenteCorso = new ViewPresidenteCorso(this);
	}
	
	public PresidenteCorso getPresidenteCorso() {
		return presidenteCorso;
	}

	public void run() {
		viewPresidenteCorso.createAndRun();
		
	}

}
