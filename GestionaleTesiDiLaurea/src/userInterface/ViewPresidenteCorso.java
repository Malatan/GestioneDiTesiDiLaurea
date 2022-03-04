package userInterface;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import businessLogic.ControllerAppello;
import businessLogic.ControllerLogin;
import businessLogic.ControllerPresidenteCorso;
import domainModel.AppelloTesi;
import utils.Utils;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;

public class ViewPresidenteCorso {
	private ControllerPresidenteCorso controllerPresidenteCorso;
	private Shell presidenteCorsoShell;
	private Composite compositeMenu;
	private Label lblId;
	private Label lblData;
	private ScrolledComposite scrolledCompositeListaAppelli;
	
	public ViewPresidenteCorso(ControllerPresidenteCorso cpc) {
		this.controllerPresidenteCorso = cpc;
	}
	
	public Shell getShell() {
		return presidenteCorsoShell;
	}

	 /**
	  * @wbp.parser.entryPoint
	  */
	 public void createAndRun() {
      
			Display display = Display.getDefault();
			presidenteCorsoShell = new Shell();
			presidenteCorsoShell.setSize(400, 500);
			presidenteCorsoShell.setText("Gestionale di tesi di laurea - Presidente del Corso");
			Utils.setShellToCenterMonitor(presidenteCorsoShell, display);
			
			Composite composite = new Composite(presidenteCorsoShell, SWT.BORDER);
			composite.setBounds(21, 22, 344, 80);
			
			Label lblMatricola = new Label(composite, SWT.NONE);
			lblMatricola.setBounds(10, 10, 320, 15);
			lblMatricola.setText("Matricola: " + controllerPresidenteCorso.getPresidenteCorso().getMatricola());
			
			Label lblNome = new Label(composite, SWT.NONE);
			lblNome.setBounds(10, 31, 320, 15);
			lblNome.setText("Nome: " + controllerPresidenteCorso.getPresidenteCorso().getNome());
			
			Label lblCognome = new Label(composite, SWT.NONE);
			lblCognome.setBounds(10, 52, 320, 15);
			lblCognome.setText("Cognome: " + controllerPresidenteCorso.getPresidenteCorso().getCognome());
			
			compositeMenu = new Composite(presidenteCorsoShell, SWT.BORDER);
			compositeMenu.setBounds(20, 125, 344, 326);
			
			
			Button btnVisualizzaAppelli = new Button(compositeMenu, SWT.NONE);
			btnVisualizzaAppelli.setLocation(10, 10);
			btnVisualizzaAppelli.setSize(320, 44);
			btnVisualizzaAppelli.setText("Visualizza gli appelli di tesi\r\n\r\n");
			btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					compositeMenu.setVisible(false);
					scrolledCompositeListaAppelli = new ScrolledComposite(presidenteCorsoShell, SWT.BORDER | SWT.V_SCROLL);
					scrolledCompositeListaAppelli.setBounds(20, 152, 345, 297);
					scrolledCompositeListaAppelli.setExpandVertical(true);
					Composite compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
					compositeListaAppelli.setBounds(20, 152, 345, 322);
					
					lblId = new Label(presidenteCorsoShell, SWT.NONE);
					lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
					lblId.setAlignment(SWT.CENTER);
					lblId.setBounds(40, 126, 50, 15);
					lblId.setText("ID");
					
					lblData = new Label(presidenteCorsoShell, SWT.NONE);
					lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
					lblData.setAlignment(SWT.CENTER);
					lblData.setBounds(100, 126, 100, 15);
					lblData.setText("Data");
					visualizzaListaAppelli(compositeListaAppelli);
					scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
					scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			});
			
			Button btnLogout = new Button(compositeMenu, SWT.NONE);
			btnLogout.setText("Log out");
			btnLogout.setBounds(10, 276, 320, 31);
			
			btnLogout.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					presidenteCorsoShell.close();
					controllerPresidenteCorso = null;
					ControllerLogin cl = new ControllerLogin();
					cl.run();
				}
			});
			
			Button btnIndietro = new Button(presidenteCorsoShell, SWT.NONE);
			btnIndietro.setLocation(290, 125);
			btnIndietro.setSize(75, 25);
			btnIndietro.setText("Indietro");
			
			btnIndietro.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					compositeMenu.setVisible(true);
					scrolledCompositeListaAppelli.dispose();
					lblId.dispose();
					lblData.dispose();
				}
			});
			
			presidenteCorsoShell.open();
			presidenteCorsoShell.layout();
			
			while (!presidenteCorsoShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			
      
    }
	 
	public void visualizzaListaAppelli(Composite c) {
		ArrayList<AppelloTesi> appelli = controllerPresidenteCorso.getAppelliFromDB();
		int offset_y = 10;
		for(AppelloTesi a : appelli) {
			Label lblId = new Label(c, SWT.NONE);
			lblId.setAlignment(SWT.CENTER);
			lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblId.setBounds(10, offset_y+3, 60, 25);
			lblId.setText(a.getId()+"");
			
			Label lblData = new Label(c, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(90, offset_y+3, 80, 25);
			lblData.setText(a.getDateString());
			
			Button btnDettaglio = new Button(c, SWT.NONE);
			btnDettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					AppelloTesi appello = controllerPresidenteCorso.getAppelloFromDB(a.getId());
					ControllerAppello ca = new ControllerAppello(appello, presidenteCorsoShell, 
							controllerPresidenteCorso.getPresidenteCorso().getMatricola(),
							Utils.getRuolo(controllerPresidenteCorso.getPresidenteCorso()));
					ca.run();
				}
			});
			btnDettaglio.setBounds(230, offset_y, 75, 25);
			btnDettaglio.setText("Dettagli");
			
			Label lblSeperator = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 325, 2);
			
			offset_y = offset_y + 35;
		}
	}
}
