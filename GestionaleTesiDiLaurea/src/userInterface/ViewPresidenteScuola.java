package userInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
	 public void createAndRun() {
      
			Display display = Display.getDefault();
			presidenteScuolaShell = new Shell();
			presidenteScuolaShell.setSize(400, 427);
			presidenteScuolaShell.setText("Gestionale di tesi di laurea - Presidente della Scuola\r\n");
			
			Composite composite = new Composite(presidenteScuolaShell, SWT.BORDER);
			composite.setBounds(10, 22, 364, 80);
			
			Label lblMatricola = new Label(composite, SWT.NONE);
			lblMatricola.setBounds(10, 10, 55, 15);
			lblMatricola.setText("Matricola: ");
			
			Label lblNome = new Label(composite, SWT.NONE);
			lblNome.setBounds(10, 31, 55, 15);
			lblNome.setText("Nome:");
			
			Label lblCognome = new Label(composite, SWT.NONE);
			lblCognome.setBounds(10, 52, 55, 15);
			lblCognome.setText("Cognome:");
			
			Composite composite_1 = new Composite(presidenteScuolaShell, SWT.BORDER);
			composite_1.setBounds(10, 155, 364, 206);
			
			
			Button btnCreaAppello = new Button(composite_1, SWT.NONE);
			btnCreaAppello.setLocation(10, 10);
			btnCreaAppello.setSize(340, 38);
			btnCreaAppello.setText("Visualizza gli appelli");
			
			Button btnLogOut = new Button(composite_1, SWT.NONE);
			btnLogOut.setText("Log out");
			btnLogOut.setBounds(10, 167, 340, 25);
			
			btnLogOut.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					presidenteScuolaShell.close();
					controllerPresidenteScuola = null;
					ControllerLogin cl = new ControllerLogin();
					cl.run();
				}
			});
			
			presidenteScuolaShell.open();
			presidenteScuolaShell.layout();
			
			while (!presidenteScuolaShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			
      
    }
}
