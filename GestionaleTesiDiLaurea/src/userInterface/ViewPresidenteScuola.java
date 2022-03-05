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
	private Label lblId;
	private Label lblData;
	private Label lblStatus;
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
		
		lblId = new Label(presidenteScuolaShell, SWT.NONE);
		lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblId.setAlignment(SWT.CENTER);
		lblId.setBounds(40, 126, 50, 15);
		lblId.setText("ID");
		lblId.setVisible(false);
		
		lblData = new Label(presidenteScuolaShell, SWT.NONE);
		lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblData.setAlignment(SWT.CENTER);
		lblData.setBounds(100, 126, 100, 15);
		lblData.setText("Data");
		lblId.setVisible(false);
		
		lblStatus = new Label(presidenteScuolaShell, SWT.NONE);
		lblStatus.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblStatus.setAlignment(SWT.CENTER);
		lblStatus.setBounds(215, 126, 100, 15);
		lblStatus.setText("Status");
		lblStatus.setVisible(false);
		
		Button btnVisualizzaAppelli = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaAppelli.setLocation(10, 10);
		btnVisualizzaAppelli.setSize(415, 38);
		btnVisualizzaAppelli.setText("Visualizza gli appelli di tesi");
		btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				lblId.setVisible(true);
				lblData.setVisible(true);
				lblStatus.setVisible(true);
				visualizzaListaAppelli();
			}
		});

		Button btnLogOut = new Button(compositeMenu, SWT.NONE);
		btnLogOut.setText("Log out");
		btnLogOut.setBounds(10, 288, 415, 31);

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
				lblId.setVisible(false);
				lblData.setVisible(false);
				lblStatus.setVisible(false);
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

	public void visualizzaListaAppelli() {
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
			btnDettaglio.setBounds(345, offset_y, 75, 25);
			btnDettaglio.setText("Dettagli");

			Label lblSeperator = new Label(compositeListaAppelli, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 420, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
		scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}
