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

public class ViewPresidenteCorso {
	private ControllerPresidenteCorso controller;
	private Shell shell;
	private Composite compositeMenu;
	private Label lblId;
	private Label lblData;
	private Label lblStatus;
	private ScrolledComposite scrolledCompositeListaAppelli;

	public ViewPresidenteCorso(ControllerPresidenteCorso cpc) {
		this.controller = cpc;
	}

	public Shell getShell() {
		return shell;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(480, 500);
		shell.setText("Presidente del Corso di " + controller.getPresidenteCorso().getCorso().second);
		Utils.setShellToCenterMonitor(shell, display);

		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setBounds(21, 22, 420, 80);

		Label lblMatricola = new Label(composite, SWT.NONE);
		lblMatricola.setBounds(10, 10, 320, 15);
		lblMatricola.setText("Matricola: " + controller.getPresidenteCorso().getMatricola());

		Label lblNome = new Label(composite, SWT.NONE);
		lblNome.setBounds(10, 31, 320, 15);
		lblNome.setText("Nome: " + controller.getPresidenteCorso().getNome());

		Label lblCognome = new Label(composite, SWT.NONE);
		lblCognome.setBounds(10, 52, 320, 15);
		lblCognome.setText("Cognome: " + controller.getPresidenteCorso().getCognome());

		compositeMenu = new Composite(shell, SWT.BORDER);
		compositeMenu.setBounds(20, 125, 421, 326);
		
		lblId = new Label(shell, SWT.NONE);
		lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblId.setAlignment(SWT.CENTER);
		lblId.setBounds(40, 126, 50, 15);
		lblId.setText("ID");
		lblId.setVisible(false);
		
		lblData = new Label(shell, SWT.NONE);
		lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblData.setAlignment(SWT.CENTER);
		lblData.setBounds(100, 126, 100, 15);
		lblData.setText("Data");
		lblData.setVisible(false);
		
		lblStatus = new Label(shell, SWT.NONE);
		lblStatus.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblStatus.setAlignment(SWT.CENTER);
		lblStatus.setBounds(215, 126, 100, 15);
		lblStatus.setText("Status");
		lblStatus.setVisible(false);
		
		Button btnVisualizzaAppelli = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaAppelli.setLocation(10, 10);
		btnVisualizzaAppelli.setSize(397, 44);
		btnVisualizzaAppelli.setText("Visualizza gli appelli di tesi\r\n\r\n");
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

		Button btnLogout = new Button(compositeMenu, SWT.NONE);
		btnLogout.setText("Log out");
		btnLogout.setBounds(10, 276, 397, 31);

		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.close();
				controller = null;
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});

		Button btnIndietro = new Button(shell, SWT.NONE);
		btnIndietro.setLocation(365, 125);
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

		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	public void visualizzaListaAppelli() {
		scrolledCompositeListaAppelli = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeListaAppelli.setBounds(20, 152, 421, 297);
		scrolledCompositeListaAppelli.setExpandVertical(true);
		Composite compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
		compositeListaAppelli.setBounds(scrolledCompositeListaAppelli.getBounds());
		ArrayList<AppelloTesi> appelli = controller.getAppelliFromDB();
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
					AppelloTesi appello = controller.getAppelloFromDB(a.getId());
					ControllerAppello ca = new ControllerAppello(appello, shell, Utils.getRuolo(controller.getPresidenteCorso()), 
											5, controller.getPresidenteCorso());
					ca.run();
				}
			});
			btnDettaglio.setBounds(335, offset_y, 75, 25);
			btnDettaglio.setText("Dettagli");
			Label lblSeperator = new Label(compositeListaAppelli, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 400, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
		scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}
