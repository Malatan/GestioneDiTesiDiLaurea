package userInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import businessLogic.ControllerLogin;

public class ViewDocente {
	private ControllerLogin controllerLogin;
	private Shell relatoreShell;
	private Shell membroCommissioneShell;
	private Shell presidenteCommissioneShell;
	
	public ViewDocente(ControllerLogin cl) {
		this.controllerLogin = cl;
	}

	 /**
	  * @wbp.parser.entryPoint
	  */
	 public void ShowRelatoreWidget() {
      
			Display display = Display.getDefault();
			relatoreShell = new Shell();
			relatoreShell.setSize(760, 523);
			relatoreShell.setText("Gestionale di tesi di laurea");
			
			Composite composite = new Composite(relatoreShell, SWT.NONE);
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
			
			
			Button btnCreaAppello = new Button(relatoreShell, SWT.NONE);
			btnCreaAppello.setBounds(276, 182, 158, 69);
			btnCreaAppello.setText("TESI");
			
			relatoreShell.open();
			relatoreShell.layout();
			
			while (!relatoreShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			
      
    }
	 
	 public void ShowMembroCommissioneWidget() {
	      
			Display display = Display.getDefault();
			membroCommissioneShell = new Shell();
			membroCommissioneShell.setSize(760, 523);
			membroCommissioneShell.setText("Gestionale di tesi di laurea");
			
			Composite composite = new Composite(membroCommissioneShell, SWT.NONE);
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
			
			
			Button btnCreaAppello = new Button(membroCommissioneShell, SWT.NONE);
			btnCreaAppello.setBounds(276, 182, 158, 69);
			btnCreaAppello.setText("VOTO");
			
			membroCommissioneShell.open();
			membroCommissioneShell.layout();
			
			while (!membroCommissioneShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			
   
	 }
	 
	 public void ShowPresidenteCommissioneWidget() {
	      
			Display display = Display.getDefault();
			presidenteCommissioneShell = new Shell();
			presidenteCommissioneShell.setSize(760, 523);
			presidenteCommissioneShell.setText("Gestionale di tesi di laurea");
			
			Composite composite = new Composite(presidenteCommissioneShell, SWT.NONE);
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
			
			
			Button btnCreaAppello = new Button(presidenteCommissioneShell, SWT.NONE);
			btnCreaAppello.setBounds(276, 182, 158, 69);
			btnCreaAppello.setText("APPELLO");
			
			presidenteCommissioneShell.open();
			presidenteCommissioneShell.layout();
			
			while (!presidenteCommissioneShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			

	 }
}
