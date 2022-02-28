package userInterface;

import java.time.LocalDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import businessLogic.ControllerAppello;
import businessLogic.ControllerResponsabile;
import domainModel.AppelloTesi;
import domainModel.Aula;
import utils.Utils;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.ScrolledComposite;

public class ViewResponsabile {
	private ControllerResponsabile controllerResponsabile;
	private Shell responsabileShell;
	private Composite compositeListaAppelli;
	
	public ViewResponsabile(ControllerResponsabile cr) {
		this.controllerResponsabile = cr;
	}

	public Shell getShell() {
		return responsabileShell;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		responsabileShell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		responsabileShell.setSize(600, 600);
		responsabileShell.setText("Gestionale di tesi di laurea - Responsabile");
		Utils.setShellToCenterMonitor(responsabileShell, display);

		Composite compositeUserInfo = new Composite(responsabileShell, SWT.BORDER);
		compositeUserInfo.setBounds(20, 22, 281, 80);

		Label lblMatricola = new Label(compositeUserInfo, SWT.NONE);
		lblMatricola.setBounds(10, 10, 261, 15);
		lblMatricola.setText("Matricola: " + controllerResponsabile.responsabile.getMatricola());

		Label lblNome = new Label(compositeUserInfo, SWT.NONE);
		lblNome.setBounds(10, 31, 261, 15);
		lblNome.setText("Nome: " + controllerResponsabile.responsabile.getNome());

		Label lblCognome = new Label(compositeUserInfo, SWT.NONE);
		lblCognome.setBounds(10, 52, 261, 15);
		lblCognome.setText("Cognome: " + controllerResponsabile.responsabile.getCognome());

		Composite compositeMenu = new Composite(responsabileShell, SWT.BORDER);
		compositeMenu.setBounds(20, 127, 541, 410);
		
		Button btnCreaAppello = new Button(compositeMenu, SWT.NONE);
		btnCreaAppello.setLocation(172, 83);
		btnCreaAppello.setSize(200, 50);
		btnCreaAppello.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				creazioneAppelloDialog();
			}
		});
		btnCreaAppello.setText("Crea Appello");
		
		ScrolledComposite scrolledCompositeListaAppelli = new ScrolledComposite(responsabileShell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeListaAppelli.setBounds(20, 152, 540, 385);
		scrolledCompositeListaAppelli.setExpandVertical(true);
		
		compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
		compositeListaAppelli.setBounds(20, 152, 540, 385);
		
		Label lblId = new Label(responsabileShell, SWT.NONE);
		lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblId.setAlignment(SWT.CENTER);
		lblId.setBounds(40, 126, 50, 15);
		lblId.setText("ID");
		
		Label lblData = new Label(responsabileShell, SWT.NONE);
		lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblData.setAlignment(SWT.CENTER);
		lblData.setBounds(100, 126, 100, 15);
		lblData.setText("Data");
		
		Label lblIscrizione = new Label(responsabileShell, SWT.NONE);
		lblIscrizione.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblIscrizione.setAlignment(SWT.CENTER);
		lblIscrizione.setBounds(230, 126, 130, 15);
		lblIscrizione.setText("Iscrizione studente");
		
		Button btnVisualizzaAppelli = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaAppelli.setLocation(172, 230);
		btnVisualizzaAppelli.setSize(200, 50);
		btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				compositeListaAppelli.setVisible(true);
				visualizzaListaAppelli(compositeListaAppelli);
				scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
				scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		btnVisualizzaAppelli.setText("VISUALIZZA APPELLI");
		
		Button btnIndietro = new Button(responsabileShell, SWT.NONE);
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(true);
				compositeListaAppelli.setVisible(false);
				compositeListaAppelli.dispose();
				compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
				compositeListaAppelli.setBounds(20, 152, 540, 385);
			}
		});
		btnIndietro.setLocation(486, 127);
		btnIndietro.setSize(75, 25);
		btnIndietro.setText("Indietro");
		
		responsabileShell.open();
		responsabileShell.layout();

		while (!responsabileShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}
	
	public void creazioneAppelloDialog() {
		Shell child = new Shell(responsabileShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Creazione appello");
		Utils.setShellToCenterParent(child, responsabileShell);
		DateTime dateTimeAppello = new DateTime(child, SWT.BORDER);
		dateTimeAppello.setBounds(66, 22, 96, 24);
		LocalDate today = LocalDate.now();
		dateTimeAppello.setTime(today.getYear(), today.getMonthValue(), today.getDayOfMonth());

		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String data = dateTimeAppello.getYear() + "-" + (dateTimeAppello.getMonth() + 1)+ "-"
						+ dateTimeAppello.getDay();
				if (controllerResponsabile.creaAppello(data))
					child.close();
			}
		});
		btnYes.setBounds(30, 66, 75, 25);
		btnYes.setText("Crea");

		Button btnNo = new Button(child, SWT.NONE);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				child.close();
			}
		});
		btnNo.setBounds(125, 66, 75, 25);
		btnNo.setText("Indietro");
		child.open();
	}
	
	public void visualizzaListaAppelli(Composite c) {
		AppelloTesi[] appelli = controllerResponsabile.getAppelliFromDB();
		int offset_y = 10;
		boolean skip = true;
		for(AppelloTesi a : appelli) {
			Label lblId = new Label(c, SWT.NONE);
			lblId.setAlignment(SWT.CENTER);
			lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblId.setBounds(10, offset_y, 60, 25);
			lblId.setText(a.getId()+"");
			
			Label lblData = new Label(c, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(90, offset_y, 80, 25);
			lblData.setText(a.getData());
			
			Label lblIscrizione = new Label(c, SWT.NONE);
			lblIscrizione.setAlignment(SWT.CENTER);
			lblIscrizione.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblIscrizione.setBounds(230, offset_y, 80, 25);
			lblIscrizione.setText(a.isIscrizione() ? "Si" : "No");
			
			Button btnDettaglio = new Button(c, SWT.NONE);
			btnDettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					ControllerAppello ca = new ControllerAppello(a, responsabileShell);
					ca.run();
				}
			});
			btnDettaglio.setBounds(400, offset_y, 75, 25);
			btnDettaglio.setText("Dettaglio");
			
			if(!skip) {
				Label lblSeperator = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
				lblSeperator.setBounds(10, offset_y - 5, 500, 2);
			}
			
			skip = false;
			offset_y = offset_y + 35;
		}
	}
	
	/*
	public void ShowListaAppello(AppelloTesi[] appelli) {
		Display display = Display.getDefault();
		Shell listaAppelloShell = new Shell();
		listaAppelloShell.setSize(672, 523);
		listaAppelloShell.setText("Lista Appelli");

		int num = 0;
		System.out.println(appelli[0].getData());
		for (AppelloTesi at : appelli) {
			if (at != null) {

				Label idAppello = new Label(listaAppelloShell, SWT.NONE);
				idAppello.setBounds(21, 15 + 52 * num, 56, 15);
				idAppello.setText("ID: " + at.getId());

				Label lblAppello = new Label(listaAppelloShell, SWT.NONE);
				lblAppello.setAlignment(SWT.CENTER);
				lblAppello.setBounds(83, 15 + 52 * num, 108, 15);
				lblAppello.setText(at.getData());

				Label aulaLabel = new Label(listaAppelloShell, SWT.NONE);
				aulaLabel.setBounds(197, 15 + 52 * num, 108, 15);
				aulaLabel.setText("Aula: " + at.getNumAula());

				Button btnCreaAppello = new Button(listaAppelloShell, SWT.NONE);
				btnCreaAppello.setBounds(346, 10 + 47 * num, 132, 25);

				if (!at.getNumAula().equals("")) {
					btnCreaAppello.setText("CAMBIA AULA");
				} else {
					btnCreaAppello.setText("PRENOTA AULA");
				}

				btnCreaAppello.addMouseListener(new MouseAdapter() {
					private final Integer idAppello = Integer.valueOf(at.getId());

					@Override
					public void mouseDown(MouseEvent e) {
						listaAppelloShell.close();
						controllerResponsabile.showListaAule(idAppello, at.getNumAula());
					}
				});

				Button btnProgramma = new Button(listaAppelloShell, SWT.NONE);
				btnProgramma.setText("PROGRAMMA INFO");
				btnProgramma.setBounds(505, 10 + 47 * num, 122, 25);
				btnProgramma.addMouseListener(new MouseAdapter() {
					private final AppelloTesi currentAppelloTesi = at;

					@Override
					public void mouseDown(MouseEvent e) {
						listaAppelloShell.close();
						controllerResponsabile.showProgrammaInformazioniAppelloWidget(currentAppelloTesi);
					}
				});

				num++;
			}
		}

		listaAppelloShell.open();
		listaAppelloShell.layout();

	}

	public final int maxAulePerRiga = 3;
	private Text text_1;

	public void ShowListaAule(Aula[] aule, int callerIdAppello, String currentAula) {
		Display display = Display.getDefault();
		Shell auleShell = new Shell();
		auleShell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				
			}
		});
		auleShell.setSize(370, 236);
		auleShell.setText("Lista Aule - Aula prenotata: " + currentAula);

		int index = 0;
		int currentRow = 0;
		for (Aula ae : aule) {
			if (ae != null) {
				if (index == maxAulePerRiga) {
					index = 0;
					currentRow += 1;
				}

				Button button = new Button(auleShell, SWT.NONE);
				button.setBounds(10 + (index * 115), 10 + (currentRow * 68), 99, 25);
				button.setText(ae.getNumAula());

				if (ae.getLibera() == 1) {
					button.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
				} else {
					button.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					button.setEnabled(false);
				}

				button.addMouseListener(new MouseAdapter() {
					private final Integer idAula = Integer.valueOf(ae.getId());
					private final String numAula = new String(ae.getNumAula());

					@Override
					public void mouseDown(MouseEvent e) {
						int style = SWT.ICON_INFORMATION | SWT.OK | SWT.CANCEL;

						MessageBox messageBox = new MessageBox(auleShell, style);
						messageBox.setMessage("Vuoi prenotare l'aula " + numAula + "?");
						int rc = messageBox.open();

						switch (rc) {
						case SWT.OK:
						case SWT.YES:
							if (controllerResponsabile.prenotaAula(idAula, callerIdAppello, currentAula)) {

								MessageBox messageBox2 = new MessageBox(auleShell, SWT.ICON_INFORMATION);
								messageBox2.setMessage("Prenotazione dell'aula " + numAula + " avvenuta con successo!");
								messageBox2.open();
								auleShell.close();
							}
							break;

						}

					}
				});
				index++;
			}
		}
		auleShell.open();
		auleShell.layout();
	}

	public void ShowProgrammaInformazioniAppelloWidget(AppelloTesi currentAppelloTesi, String informazioni) {
		Display display = Display.getDefault();
		Shell programmaShell = new Shell();
		programmaShell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				
			}
		});
		programmaShell.setSize(738, 504);
		programmaShell.setText("Programma informazioni ID Appello: " + currentAppelloTesi.getId() + " data:"
				+ currentAppelloTesi.getData());

		text_1 = new Text(programmaShell, SWT.BORDER);
		text_1.setBounds(10, 10, 702, 370);
		if (informazioni != null) {
			text_1.setText(informazioni);
		}
		Button btnNewButton = new Button(programmaShell, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				if (controllerResponsabile.programmaInformazioniPerAppello(currentAppelloTesi.getId(),
						text_1.getText())) {
					MessageBox messageBox2 = new MessageBox(programmaShell, SWT.ICON_INFORMATION);
					messageBox2.setMessage("Invio di informazioni appello avvenuta con successo!");
					messageBox2.open();
					programmaShell.close();
				} else {

				}
			}
		});
		btnNewButton.setBounds(10, 395, 702, 58);
		btnNewButton.setText("INVIA INFORMAZIONI");

		programmaShell.open();
		programmaShell.layout();
	}
	*/
}
