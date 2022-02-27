package userInterface;

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
import businessLogic.ControllerResponsabile;
import domainModel.AppelloTesi;
import domainModel.Aula;
import utils.Console;
import utils.Utils;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ViewResponsabile {
	private ControllerResponsabile controllerResponsabile;
	private Shell responsabileShell;

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
		responsabileShell = new Shell(display, SWT.CLOSE | SWT.TITLE);
		responsabileShell.setSize(800,600);
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

		Button btnVisualizzaAppelli = new Button(responsabileShell, SWT.NONE);
		btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				controllerResponsabile.showListaAppelli();
			}
		});
		btnVisualizzaAppelli.setBounds(225, 236, 264, 80);
		btnVisualizzaAppelli.setText("VISUALIZZA APPELLI");
		
		DateTime dateTimeAppello = new DateTime(responsabileShell, SWT.BORDER);
		dateTimeAppello.setDate(2021, 2, 26);
		dateTimeAppello.setBounds(132, 119, 80, 24);
		
		Button btnCreaAppello = new Button(responsabileShell, SWT.NONE);
		btnCreaAppello.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String data = dateTimeAppello.getYear() + "-" +
							  dateTimeAppello.getMonth() + "-" +
							  dateTimeAppello.getDay();
				controllerResponsabile.creaAppello(data);
			}
		});
		btnCreaAppello.setBounds(226, 119, 75, 25);
		btnCreaAppello.setText("Crea");
		
		Label lblCreaAppello = new Label(responsabileShell, SWT.CENTER);
		lblCreaAppello.setBounds(20, 119, 106, 24);
		lblCreaAppello.setText("Creazione appello:");

		responsabileShell.open();
		responsabileShell.layout();

		while (!responsabileShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

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
				controllerResponsabile.showListaAppelli();
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
				controllerResponsabile.showListaAppelli();
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
}
