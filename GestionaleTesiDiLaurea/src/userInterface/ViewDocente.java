package userInterface;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import businessLogic.ControllerAppello;
import businessLogic.ControllerDocente;
import businessLogic.ControllerLogin;
import domainModel.AppelloTesi;
import domainModel.DomandaTesi;
import utils.Pair;
import utils.Utils;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class ViewDocente {
	private ControllerDocente controller;
	private Shell shell;

	private ScrolledComposite scrolledCompositeDomandeTesi;
	// pagina domande tesi
	private Label lblMatricolaLabel;
	private Label lblNomeCognomeLabel;
	private Label lblCorsoLabel;
	private Label lblRepositoryLabel;
	private Label lblDataLabel;

	// pagina appelli
	private Label lblAppelloId;
	private Label lblAppelloData;
	private Label lblAppelloRuolo;
	private Label lblAppelloDeterminazione;
	
	public ViewDocente(ControllerDocente cd) {
		this.controller = cd;
	}

	public Shell getShell() {
		return this.shell;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(770, 500);
		shell.setText("Docente - " + controller.getDocente().getDipartimento().second);
		Utils.setShellToCenterMonitor(shell, display);

		Composite compositeUserInfo = new Composite(shell, SWT.BORDER);
		compositeUserInfo.setBounds(20, 22, 711, 80);

		Label lblMatricola = new Label(compositeUserInfo, SWT.NONE);
		lblMatricola.setBounds(10, 10, 200, 15);
		lblMatricola.setText("Matricola: " + controller.getDocente().getMatricola());

		Label lblNome = new Label(compositeUserInfo, SWT.NONE);
		lblNome.setBounds(10, 31, 200, 15);
		lblNome.setText("Nome: " + controller.getDocente().getNome());

		Label lblCognome = new Label(compositeUserInfo, SWT.NONE);
		lblCognome.setBounds(10, 52, 200, 15);
		lblCognome.setText("Cognome: " + controller.getDocente().getCognome());

		Composite compositeMenu = new Composite(shell, SWT.BORDER);
		compositeMenu.setBounds(20, 119, 711, 321);

		lblMatricolaLabel = new Label(shell, SWT.NONE);
		lblMatricolaLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblMatricolaLabel.setAlignment(SWT.CENTER);
		lblMatricolaLabel.setBounds(35, 125, 55, 15);
		lblMatricolaLabel.setText("Matricola");
		lblMatricolaLabel.setVisible(false);

		lblNomeCognomeLabel = new Label(shell, SWT.NONE);
		lblNomeCognomeLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblNomeCognomeLabel.setAlignment(SWT.CENTER);
		lblNomeCognomeLabel.setBounds(120, 125, 97, 15);
		lblNomeCognomeLabel.setText("Nome Cognome");
		lblNomeCognomeLabel.setVisible(false);

		lblCorsoLabel = new Label(shell, SWT.NONE);
		lblCorsoLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblCorsoLabel.setAlignment(SWT.CENTER);
		lblCorsoLabel.setBounds(280, 125, 55, 15);
		lblCorsoLabel.setText("Corso");
		lblCorsoLabel.setVisible(false);

		lblDataLabel = new Label(shell, SWT.NONE);
		lblDataLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblDataLabel.setAlignment(SWT.CENTER);
		lblDataLabel.setBounds(380, 125, 55, 15);
		lblDataLabel.setText("Data");
		lblDataLabel.setVisible(false);

		lblRepositoryLabel = new Label(shell, SWT.NONE);
		lblRepositoryLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblRepositoryLabel.setAlignment(SWT.CENTER);
		lblRepositoryLabel.setBounds(470, 125, 90, 15);
		lblRepositoryLabel.setText("Repository Tesi");
		lblRepositoryLabel.setVisible(false);

		Button btnDomandeTesi = new Button(compositeMenu, SWT.NONE);
		btnDomandeTesi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				lblRepositoryLabel.setVisible(true);
				lblDataLabel.setVisible(true);
				lblNomeCognomeLabel.setVisible(true);
				lblCorsoLabel.setVisible(true);
				lblMatricolaLabel.setVisible(true);
				visualizzaDomandeTesi();
			}
		});
		btnDomandeTesi.setBounds(10, 10, 687, 40);
		btnDomandeTesi.setText("Visualizza le domande Tesi");

		Button btnLogOut = new Button(compositeMenu, SWT.NONE);
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.close();
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});
		btnLogOut.setBounds(10, 276, 687, 30);
		btnLogOut.setText("Log Out");

		lblAppelloId = new Label(shell, SWT.NONE);
		lblAppelloId.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblAppelloId.setAlignment(SWT.CENTER);
		lblAppelloId.setBounds(35, 125, 55, 15);
		lblAppelloId.setText("ID");
		lblAppelloId.setVisible(false);

		lblAppelloData = new Label(shell, SWT.NONE);
		lblAppelloData.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblAppelloData.setAlignment(SWT.CENTER);
		lblAppelloData.setBounds(95, 125, 97, 15);
		lblAppelloData.setText("Data");
		lblAppelloData.setVisible(false);

		lblAppelloRuolo = new Label(shell, SWT.NONE);
		lblAppelloRuolo.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblAppelloRuolo.setAlignment(SWT.CENTER);
		lblAppelloRuolo.setBounds(273, 125, 55, 15);
		lblAppelloRuolo.setText("Ruolo");
		lblAppelloRuolo.setVisible(false);
		
		lblAppelloDeterminazione = new Label(shell, SWT.NONE);
		lblAppelloDeterminazione.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblAppelloDeterminazione.setAlignment(SWT.CENTER);
		lblAppelloDeterminazione.setBounds(442, 125, 100, 15);
		lblAppelloDeterminazione.setText("Determinazione");
		lblAppelloDeterminazione.setVisible(false);
		
		Button btnListaAppelli = new Button(compositeMenu, SWT.NONE);
		btnListaAppelli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				lblAppelloId.setVisible(true);
				lblAppelloData.setVisible(true);
				lblAppelloRuolo.setVisible(true);
				lblAppelloDeterminazione.setVisible(true);
				visualizzaAppelli();
			}
		});
		btnListaAppelli.setBounds(10, 56, 687, 40);
		btnListaAppelli.setText("Visualizza gli appelli appartenenti");

		Button btnIndietro = new Button(shell, SWT.NONE);
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(true);

				lblRepositoryLabel.setVisible(false);
				lblDataLabel.setVisible(false);
				lblNomeCognomeLabel.setVisible(false);
				lblCorsoLabel.setVisible(false);
				lblMatricolaLabel.setVisible(false);

				lblAppelloId.setVisible(false);
				lblAppelloData.setVisible(false);
				lblAppelloRuolo.setVisible(false);
				lblAppelloDeterminazione.setVisible(false);
				if (scrolledCompositeDomandeTesi != null)
					scrolledCompositeDomandeTesi.dispose();
			}
		});
		btnIndietro.setBounds(656, 118, 75, 25);
		btnIndietro.setText("Indietro");

		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void visualizzaDomandeTesi() {
		scrolledCompositeDomandeTesi = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeDomandeTesi.setBounds(20, 145, 711, 300);
		scrolledCompositeDomandeTesi.setExpandVertical(true);
		Composite compositeDomandeTesi = new Composite(scrolledCompositeDomandeTesi, SWT.NONE);
		compositeDomandeTesi.setBounds(scrolledCompositeDomandeTesi.getBounds());

		ArrayList<DomandaTesi> domande = controller.getDomandeTesiFromDB();
		int offset_y = 10;
		for (DomandaTesi d : domande) {
			Label lblMatricola = new Label(compositeDomandeTesi, SWT.NONE);
			lblMatricola.setAlignment(SWT.CENTER);
			lblMatricola.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblMatricola.setBounds(10, offset_y + 3, 60, 25);
			lblMatricola.setText(d.getMatricolaStudente() + "");

			Label lblNomeCognome = new Label(compositeDomandeTesi, SWT.NONE);
			lblNomeCognome.setAlignment(SWT.CENTER);
			lblNomeCognome.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblNomeCognome.setBounds(75, offset_y + 3, 150, 25);
			lblNomeCognome.setText(d.getNomeCognomeStudente());

			Label lblCorso = new Label(compositeDomandeTesi, SWT.NONE);
			lblCorso.setAlignment(SWT.CENTER);
			lblCorso.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblCorso.setBounds(230, offset_y + 3, 110, 25);
			lblCorso.setText(d.getNomeCorso());

			Label lblData = new Label(compositeDomandeTesi, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(345, offset_y + 3, 80, 25);
			lblData.setText(d.getData());

			Text textRepository = new Text(compositeDomandeTesi, SWT.NONE | SWT.READ_ONLY);
			textRepository.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			textRepository.setBounds(430, offset_y + 3, 130, 25);
			textRepository.setText(d.getRepository());

			Button btnApprova = new Button(compositeDomandeTesi, SWT.NONE);
			if (d.isApprovato()) {
				btnApprova.setText("Domanda approvata");
				btnApprova.setEnabled(false);
			} else {
				btnApprova.setText("Approva Domanda");
				btnApprova.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						if (textRepository.getText().equals("")) {
							Utils.createWarningDialog(shell, "Attenzione",
									"Impossibile approvare la tesi, lo studente non ha ancora inserito un repository tesi");
						} else {
							if (controller.approvaDomandaTesi(d)) {
								btnApprova.setText("Domanda approvata");
								btnApprova.setEnabled(false);
								d.setApprovato(true);
							}
						}
					}
				});
			}
			btnApprova.setBounds(570, offset_y, 130, 25);

			Label lblSeperator = new Label(compositeDomandeTesi, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 685, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeDomandeTesi.setContent(compositeDomandeTesi);
		scrolledCompositeDomandeTesi.setMinSize(compositeDomandeTesi.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public void visualizzaAppelli() {
		scrolledCompositeDomandeTesi = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeDomandeTesi.setBounds(20, 145, 711, 300);
		scrolledCompositeDomandeTesi.setExpandVertical(true);
		Composite compositeDomandeTesi = new Composite(scrolledCompositeDomandeTesi, SWT.NONE);
		compositeDomandeTesi.setBounds(scrolledCompositeDomandeTesi.getBounds());

		// appello-ruolo
		ArrayList<Pair<AppelloTesi, Integer>> appelli = controller.getAppelliFromDB();
		int offset_y = 10;
		for (Pair<AppelloTesi, Integer> p : appelli) {
			Label lblAppelloId = new Label(compositeDomandeTesi, SWT.NONE);
			lblAppelloId.setAlignment(SWT.CENTER);
			lblAppelloId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblAppelloId.setBounds(10, offset_y + 3, 60, 25);
			lblAppelloId.setText(p.first.getId()+"");

			Label lblAppelloData = new Label(compositeDomandeTesi, SWT.NONE);
			lblAppelloData.setAlignment(SWT.CENTER);
			lblAppelloData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblAppelloData.setBounds(75, offset_y + 3, 100, 25);
			lblAppelloData.setText(p.first.getDateString());

			Label lblAppelloRuolo = new Label(compositeDomandeTesi, SWT.NONE);
			lblAppelloRuolo.setAlignment(SWT.CENTER);
			lblAppelloRuolo.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblAppelloRuolo.setBounds(180, offset_y + 3, 200, 25);
			lblAppelloRuolo.setText(Utils.getAppelloRuoloByInt(p.second));
			
			Label lblAppelloDeterminazione = new Label(compositeDomandeTesi, SWT.NONE);
			lblAppelloDeterminazione.setAlignment(SWT.CENTER);
			lblAppelloDeterminazione.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblAppelloDeterminazione.setBounds(400, offset_y + 3, 140, 25);
			lblAppelloDeterminazione.setText("Da inserire");
			
			Button btnDettaglio = new Button(compositeDomandeTesi, SWT.NONE);
			btnDettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					ControllerAppello ca = new ControllerAppello(p.first, shell,
							controller.getDocente().getMatricola(),
							Utils.getRuolo(controller.getDocente()));
					ca.run();
				}
			});
			btnDettaglio.setBounds(570, offset_y, 130, 25);
			btnDettaglio.setText("Dettagli");
			
			Label lblSeperator = new Label(compositeDomandeTesi, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 685, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeDomandeTesi.setContent(compositeDomandeTesi);
		scrolledCompositeDomandeTesi.setMinSize(compositeDomandeTesi.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}
