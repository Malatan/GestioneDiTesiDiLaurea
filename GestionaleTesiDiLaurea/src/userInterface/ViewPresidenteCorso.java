package userInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import businessLogic.ControllerLogin;
import businessLogic.ControllerPresidenteCorso;

public class ViewPresidenteCorso {
	private ControllerPresidenteCorso controllerPresidenteCorso;
	private Shell presidenteCorsoShell;
	
	public ViewPresidenteCorso(ControllerPresidenteCorso cpc) {
		this.controllerPresidenteCorso = cpc;
	}

	 /**
	  * @wbp.parser.entryPoint
	  */
	 public void createAndRun() {
      
			Display display = Display.getDefault();
			presidenteCorsoShell = new Shell();
			presidenteCorsoShell.setSize(377, 325);
			presidenteCorsoShell.setText("Gestionale di tesi di laurea");
			
			Composite composite = new Composite(presidenteCorsoShell, SWT.BORDER);
			composite.setBounds(10, 22, 341, 80);
			
			Label lblMatricola = new Label(composite, SWT.NONE);
			lblMatricola.setBounds(10, 10, 55, 15);
			lblMatricola.setText("Matricola:        ");
			
			Label lblNome = new Label(composite, SWT.NONE);
			lblNome.setBounds(10, 31, 55, 15);
			lblNome.setText("Nome:");
			
			Label lblCognome = new Label(composite, SWT.NONE);
			lblCognome.setBounds(10, 52, 55, 15);
			lblCognome.setText("Cognome:");
			
			Composite composite_1 = new Composite(presidenteCorsoShell, SWT.BORDER);
			composite_1.setBounds(10, 130, 341, 146);
			
			
			Button btnCreaAppello = new Button(composite_1, SWT.NONE);
			btnCreaAppello.setLocation(10, 10);
			btnCreaAppello.setSize(317, 25);
			btnCreaAppello.setText("Visualizza Gli Appelli\r\n");
			
			Button btnLogout = new Button(composite_1, SWT.NONE);
			btnLogout.setText("Logout");
			btnLogout.setBounds(10, 107, 317, 25);
			
			presidenteCorsoShell.open();
			presidenteCorsoShell.layout();
			
			while (!presidenteCorsoShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			
      
    }
}
