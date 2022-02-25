package userInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import businessLogic.ControllerLogin;

public class ViewResponsabile {
	
	private ControllerLogin controllerLogin;
	private Shell responsabileShell;
	
	public ViewResponsabile(ControllerLogin cl) {
		this.controllerLogin = cl;
	}

	 /**
	  * @wbp.parser.entryPoint
	  */
	 public void ShowResponsabileWidget() {
      
			Display display = Display.getDefault();
			responsabileShell = new Shell();
			responsabileShell.setSize(760, 523);
			responsabileShell.setText("Gestionale di tesi di laurea");
			
			Composite composite = new Composite(responsabileShell, SWT.NONE);
			composite.setBounds(20, 22, 281, 80);
			
			Label lblMatricola = new Label(composite, SWT.NONE);
			lblMatricola.setBounds(10, 10, 55, 15);
			lblMatricola.setText("Matricola:        ");
			
			Label lblNome = new Label(composite, SWT.NONE);
			lblNome.setBounds(10, 31, 55, 15);
			lblNome.setText("Nome:");
			
			Label lblCognome = new Label(composite, SWT.NONE);
			lblCognome.setBounds(10, 52, 55, 15);
			lblCognome.setText("Cognome:");
			
			
			Button btnCreaAppello = new Button(responsabileShell, SWT.NONE);
			btnCreaAppello.setBounds(276, 182, 158, 69);
			btnCreaAppello.setText("CREA APPELLO");
			
			Button btnVisualizzaAppelli = new Button(responsabileShell, SWT.NONE);
			btnVisualizzaAppelli.setBounds(276, 300, 158, 80);
			btnVisualizzaAppelli.setText("VISUALIZZA APPELLI");
			
			responsabileShell.open();
			responsabileShell.layout();
			
			while (!responsabileShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			
      
    }
}
