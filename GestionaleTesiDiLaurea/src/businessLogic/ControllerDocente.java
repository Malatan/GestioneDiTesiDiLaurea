package businessLogic;

import domainModel.MembroCommissione;
import domainModel.PresidenteCommissioneTesi;
import domainModel.Relatore;
import domainModel.Responsabile;
import userInterface.ViewDocente;

public class ControllerDocente {

	private Relatore relatore;
	private PresidenteCommissioneTesi responsabile;
	private MembroCommissione membroCommissione;
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
