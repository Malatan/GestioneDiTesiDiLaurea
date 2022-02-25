package userInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import businessLogic.ControllerLogin;
import businessLogic.ControllerResponsabile;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import domainModel.AppelloTesi;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class ViewResponsabile {
	
	private ControllerResponsabile controllerResponsabile;
	private Shell responsabileShell;
	private Text text;
	private Label lblData;
	private Label messaggio;
	
	public ViewResponsabile(ControllerResponsabile cr) {
		this.controllerResponsabile = cr;
	}
	
	public void ShowMessage(String message, int type) {
		if(type == 1) {
			messaggio.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		}else {
			messaggio.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		messaggio.setText(message);
	}

	
	 public void ShowResponsabileWidget() {
      
			Display display = Display.getDefault();
			responsabileShell = new Shell();
			responsabileShell.setSize(754, 491);
			responsabileShell.setText("Gestionale di tesi di laurea");
			
			Composite composite = new Composite(responsabileShell, SWT.NONE);
			composite.setBounds(20, 22, 281, 80);
			
			Label lblMatricola = new Label(composite, SWT.NONE);
			lblMatricola.setBounds(10, 10, 261, 15);
			lblMatricola.setText("Matricola: "+controllerResponsabile.responsabile.getMatricola());
			
			Label lblNome = new Label(composite, SWT.NONE);
			lblNome.setBounds(10, 31, 261, 15);
			lblNome.setText("Nome: " +controllerResponsabile.responsabile.getNome());
			
			Label lblCognome = new Label(composite, SWT.NONE);
			lblCognome.setBounds(10, 52, 261, 15);
			lblCognome.setText("Cognome: " + controllerResponsabile.responsabile.getCognome());
			
			
			Button btnCreaAppello = new Button(responsabileShell, SWT.NONE);
			btnCreaAppello.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					controllerResponsabile.CreaAppello(text.getText());
				}
			});
			btnCreaAppello.setBounds(225, 174, 264, 41);
			btnCreaAppello.setText("CREA APPELLO");
			
			Button btnVisualizzaAppelli = new Button(responsabileShell, SWT.NONE);
			btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					controllerResponsabile.ShowListaAppelli();
				}
			});
			btnVisualizzaAppelli.setBounds(225, 236, 264, 80);
			btnVisualizzaAppelli.setText("VISUALIZZA APPELLI");
			
			text = new Text(responsabileShell, SWT.BORDER);
			text.setToolTipText("Inserisci una data");
			text.setBounds(225, 136, 264, 21);
			
			lblData = new Label(responsabileShell, SWT.NONE);
			lblData.setAlignment(SWT.RIGHT);
			lblData.setBounds(31, 139, 185, 15);
			lblData.setText("Data (gg/mm/yyyy hh:mm):");
			
			messaggio = new Label(responsabileShell, SWT.NONE);
			messaggio.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			messaggio.setAlignment(SWT.CENTER);
			messaggio.setBounds(225, 373, 264, 15);
			
			responsabileShell.open();
			responsabileShell.layout();
			
			while (!responsabileShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			
      
    }
	 
	 /**
	  * @wbp.parser.entryPoint
	  */ 
	public void ShowListaAppello(AppelloTesi[] appelli) {
		Display display = Display.getDefault();
		responsabileShell = new Shell();
		responsabileShell.setSize(448, 523);
		responsabileShell.setText("Lista Appelli");		

		int num = 0;
		System.out.println(appelli[0].getData());
		for(AppelloTesi at :appelli) {
			if(at != null) {
			Button btnCreaAppello = new Button(responsabileShell, SWT.NONE);
			btnCreaAppello.setBounds(155, 24 + 45 * num, 116, 25);
			btnCreaAppello.setText("ISCRIVITI");
			
			Label lblAppello = new Label(responsabileShell, SWT.NONE);
			lblAppello.setAlignment(SWT.CENTER);
			lblAppello.setBounds(10, 29 + 45 * num, 116, 15);
			lblAppello.setText(at.getData());
			
			Button btnProgramma = new Button(responsabileShell, SWT.NONE);
			btnProgramma.setText("PROGRAMMA");
			btnProgramma.setBounds(287, 24 + 45 * num, 116, 25);
			
			num++;
			}
		}

		
		responsabileShell.open();
		responsabileShell.layout();
		

	}
}
