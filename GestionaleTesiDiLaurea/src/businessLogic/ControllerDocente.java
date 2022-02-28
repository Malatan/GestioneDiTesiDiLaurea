package businessLogic;

import domainModel.Responsabile;
import userInterface.ViewDocente;

public class ControllerDocente {

	private ViewDocente viewDocente;
	
	public ControllerDocente(String matricola, String nome, String cognome) {
		viewDocente = new ViewDocente(this);
	}
	
	public void showRelatoreWidget() {
		viewDocente.createAndRun();
	}
	
	public void showMembroCommissioneWidget() {
		viewDocente.ShowMembroCommissioneWidget();
	}
	
	public void showPresidenteCommissioneWidget() {
		viewDocente.ShowPresidenteCommissioneWidget();
	}
}
