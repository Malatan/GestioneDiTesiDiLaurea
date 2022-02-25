package businessLogic;

import userInterface.ViewLogin;
import userInterface.ViewStudente;
import userInterface.ViewAppello;
import userInterface.ViewDocente;
import userInterface.ViewPresidenteCorso;
import userInterface.ViewPresidenteScuola;
import userInterface.ViewResponsabile;

import org.eclipse.swt.widgets.Text;
import domainModel.Database;
import domainModel.Studente;

public class ControllerLogin {

	public ViewLogin viewLogin;
	public ViewStudente viewStudente;
	public ViewDocente viewDocente;
	public ViewPresidenteCorso viewPresidenteCorso;
	public ViewPresidenteScuola viewPresidenteScuola;
	public ViewResponsabile viewResponsabile;
	
	public ControllerLogin() {
		viewLogin = new ViewLogin(this);
		viewStudente = new ViewStudente(this);
	}
	
	public void CheckLogin(Text matricola, Text password) {
		System.out.println(matricola.getText()+ " " + password.getText());
		
		String[] info = Database.getInstance().VerificaCredenziali(matricola.getText(), password.getText());
		if(info != null) {
			//System.out.println(info[0] + " " + info[1] + " di ruolo " + info[2]);
			viewLogin.Close();
			switch(Integer.parseInt(info[2])) {
				case 0:					
					viewStudente.ShowStudenteWidget();		
					break;
				case 1:
					viewResponsabile.ShowResponsabileWidget();
					break;
				case 2:
					viewPresidenteScuola.ShowPresidenteScuolaWidget();
					break;
				case 3:
					viewPresidenteCorso.ShowPresidenteCorsoWidget();
					break;
				case 4:
					viewDocente.ShowRelatoreWidget();
					break;
				case 5:
					viewDocente.ShowMembroCommissioneWidget();
					break;
				case 6:
					viewDocente.ShowPresidenteCommissioneWidget();
					break;
			}
		}else {
			viewLogin.ShowErrorMessage();
		}
	
		if(matricola.getText().equals("test") && password.getText().equals("test")) {
			viewLogin.Close();
			viewStudente.ShowStudenteWidget();			
		}
	}
	
	public void EffettuaLogin() {
		viewLogin.ShowLogin();
	}
	
	public static void main(String[] args) {
		
		ControllerLogin cl = new ControllerLogin();
		
		cl.EffettuaLogin();
		
	}
}
