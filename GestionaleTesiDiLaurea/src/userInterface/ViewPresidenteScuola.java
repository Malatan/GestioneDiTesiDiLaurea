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
import businessLogic.ControllerPresidenteScuola;
import domainModel.AppelloTesi;
import utils.Utils;

public class ViewPresidenteScuola {
	private ControllerPresidenteScuola controllerPresidenteScuola;
	private Shell presidenteScuolaShell;
	private Composite compositeMenu;
	private Label lbl1;
	private Label lbl2;
	private Label lbl3;
	private ScrolledComposite scrolledCompositeListaAppelli;

	public ViewPresidenteScuola(ControllerPresidenteScuola cps) {
		this.controllerPresidenteScuola = cps;
	}

	public Shell getShell() {
		return presidenteScuolaShell;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {

		Display display = Display.getDefault();
		presidenteScuolaShell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		presidenteScuolaShell.setSize(500, 508);
		presidenteScuolaShell.setText("Presidente della Scuola");
		Utils.setShellToCenterMonitor(presidenteScuolaShell, display);

		Composite composite = new Composite(presidenteScuolaShell, SWT.BORDER);
		composite.setBounds(20, 22, 441, 80);

		Label lblMatricola = new Label(composite, SWT.NONE);
		lblMatricola.setBounds(10, 10, 340, 15);
		lblMatricola.setText("Matricola: " + controllerPresidenteScuola.getPresidenteScuola().getMatricola());

		Label lblNome = new Label(composite, SWT.NONE);
		lblNome.setBounds(10, 31, 340, 15);
		lblNome.setText("Nome: " + controllerPresidenteScuola.getPresidenteScuola().getNome());

		Label lblCognome = new Label(composite, SWT.NONE);
		lblCognome.setBounds(10, 52, 340, 15);
		lblCognome.setText("Cognome: " + controllerPresidenteScuola.getPresidenteScuola().getCognome());

		compositeMenu = new Composite(presidenteScuolaShell, SWT.BORDER);
		compositeMenu.setBounds(22, 126, 439, 333);
		
		lbl1 = new Label(presidenteScuolaShell, SWT.NONE);
		lbl1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lbl1.setAlignment(SWT.CENTER);
		lbl1.setVisible(false);
		
		lbl2 = new Label(presidenteScuolaShell, SWT.NONE);
		lbl2.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lbl2.setAlignment(SWT.CENTER);
		lbl2.setVisible(false);
		
		lbl3 = new Label(presidenteScuolaShell, SWT.NONE);
		lbl3.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lbl3.setAlignment(SWT.CENTER);
		lbl3.setVisible(false);
		
		Button btnVisualizzaAppelli = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaAppelli.setLocation(10, 10);
		btnVisualizzaAppelli.setSize(415, 38);
		btnVisualizzaAppelli.setText("Visualizza gli appelli di tesi");
		btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				lbl1.setVisible(true);
				lbl2.setVisible(true);
				lbl3.setVisible(true);
				visualizzaListaAppelli();
			}
		});

		Button btnLogOut = new Button(compositeMenu, SWT.NONE);
		btnLogOut.setText("Log out");
		btnLogOut.setBounds(10, 288, 415, 31);
		
		Button btnVisualizzaVerbaliTesi = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaVerbaliTesi.setText("Visualizza i verbali delle sessioni");
		btnVisualizzaVerbaliTesi.setBounds(10, 54, 415, 38);
		btnVisualizzaVerbaliTesi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				visualizzaListaVerbali();
			}
		});
		
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				presidenteScuolaShell.close();
				controllerPresidenteScuola = null;
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});

		Button btnIndietro = new Button(presidenteScuolaShell, SWT.NONE);
		btnIndietro.setLocation(385, 125);
		btnIndietro.setSize(75, 25);
		btnIndietro.setText("Indietro");
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(true);
				scrolledCompositeListaAppelli.dispose();
				lbl1.setVisible(false);
				lbl2.setVisible(false);
				lbl3.setVisible(false);
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
	
	public void visualizzaListaVerbali() {
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		lbl3.setVisible(true);
		lbl1.setBounds(40, 126, 50, 15);
		lbl1.setText("ID");
		lbl2.setBounds(100, 126, 100, 15);
		lbl2.setText("Data");
		lbl3.setBounds(215, 126, 100, 15);
		lbl3.setText("Approvato");
		scrolledCompositeListaAppelli = new ScrolledComposite(presidenteScuolaShell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeListaAppelli.setBounds(20, 152, 439, 297);
		scrolledCompositeListaAppelli.setExpandVertical(true);
		Composite compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
		compositeListaAppelli.setBounds(scrolledCompositeListaAppelli.getBounds());
		ArrayList<AppelloTesi> appelli = controllerPresidenteScuola.getAppelliCompletatiFromDB();
		int offset_y = 10;
		for (AppelloTesi a : appelli) {
			Label lblId = new Label(compositeListaAppelli, SWT.NONE);
			lblId.setAlignment(SWT.CENTER);
			lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblId.setBounds(10, offset_y + 3, 60, 25);
			lblId.setText(a.getId() + "");

			Label lblData = new Label(compositeListaAppelli, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(90, offset_y + 3, 80, 25);
			lblData.setText(a.getDateString());
			
			Label lblStatus = new Label(compositeListaAppelli, SWT.NONE);
			lblStatus.setAlignment(SWT.CENTER);
			lblStatus.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			lblStatus.setBounds(175, offset_y + 3, 145, 25);
			lblStatus.setText(a.getStatusString());
			
			Button btnDettaglio = new Button(compositeListaAppelli, SWT.NONE);
			btnDettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					AppelloTesi appello = controllerPresidenteScuola.getAppelloFromDB(a.getId());
					ControllerAppello ca = new ControllerAppello(appello, presidenteScuolaShell,
							controllerPresidenteScuola.getPresidenteScuola().getMatricola(),
							Utils.getRuolo(controllerPresidenteScuola.getPresidenteScuola()));
					ca.run();
				}
			});
			btnDettaglio.setBounds(345, offset_y, 75, 25);
			btnDettaglio.setText("Dettagli");

			Label lblSeperator = new Label(compositeListaAppelli, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 420, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
		scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void visualizzaListaAppelli() {
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		lbl3.setVisible(true);
		lbl1.setBounds(40, 126, 50, 15);
		lbl1.setText("ID");
		lbl2.setBounds(100, 126, 100, 15);
		lbl2.setText("Data");
		lbl3.setBounds(215, 126, 100, 15);
		lbl3.setText("Status");
		scrolledCompositeListaAppelli = new ScrolledComposite(presidenteScuolaShell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeListaAppelli.setBounds(20, 152, 439, 297);
		scrolledCompositeListaAppelli.setExpandVertical(true);
		Composite compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
		compositeListaAppelli.setBounds(scrolledCompositeListaAppelli.getBounds());
		ArrayList<AppelloTesi> appelli = controllerPresidenteScuola.getAppelliFromDB();
		int offset_y = 10;
		for (AppelloTesi a : appelli) {
			Label lblId = new Label(compositeListaAppelli, SWT.NONE);
			lblId.setAlignment(SWT.CENTER);
			lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblId.setBounds(10, offset_y + 3, 60, 25);
			lblId.setText(a.getId() + "");

			Label lblData = new Label(compositeListaAppelli, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(90, offset_y + 3, 80, 25);
			lblData.setText(a.getDateString());
			
			Label lblStatus = new Label(compositeListaAppelli, SWT.NONE);
			lblStatus.setAlignment(SWT.CENTER);
			lblStatus.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			lblStatus.setBounds(175, offset_y + 3, 145, 25);
			lblStatus.setText(a.getStatusString());
			
			Button btnDettaglio = new Button(compositeListaAppelli, SWT.NONE);
			btnDettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					AppelloTesi appello = controllerPresidenteScuola.getAppelloFromDB(a.getId());
					ControllerAppello ca = new ControllerAppello(appello, presidenteScuolaShell,
							controllerPresidenteScuola.getPresidenteScuola().getMatricola(),
							Utils.getRuolo(controllerPresidenteScuola.getPresidenteScuola()));
					ca.run();
				}
			});
			btnDettaglio.setBounds(325, offset_y, 75, 25);
			btnDettaglio.setText("Dettagli");

			Label lblSeperator = new Label(compositeListaAppelli, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 420, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
		scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}
