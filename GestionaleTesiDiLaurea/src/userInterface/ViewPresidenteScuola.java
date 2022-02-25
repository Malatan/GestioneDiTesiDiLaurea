package userInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import businessLogic.ControllerLogin;
import businessLogic.ControllerPresidenteScuola;

public class ViewPresidenteScuola {
	private ControllerPresidenteScuola controllerPresidenteScuola;
	private Shell presidenteScuolaShell;
	
	public ViewPresidenteScuola(ControllerPresidenteScuola cps) {
		this.controllerPresidenteScuola = cps;
	}

	 /**
	  * @wbp.parser.entryPoint
	  */
	 public void ShowPresidenteScuolaWidget() {
      
			Display display = Display.getDefault();
			presidenteScuolaShell = new Shell();
			presidenteScuolaShell.setSize(760, 523);
			presidenteScuolaShell.setText("Gestionale di tesi di laurea");
			
			Composite composite = new Composite(presidenteScuolaShell, SWT.NONE);
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
			
			
			Button btnCreaAppello = new Button(presidenteScuolaShell, SWT.NONE);
			btnCreaAppello.setBounds(276, 182, 158, 69);
			btnCreaAppello.setText("PROPOSTE");
			
			presidenteScuolaShell.open();
			presidenteScuolaShell.layout();
			
			while (!presidenteScuolaShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			
      
    }
}
