package userInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import businessLogic.ControllerAppello;
import domainModel.AppelloTesi;
import domainModel.Docente;
import domainModel.Studente;
import utils.Console;
import utils.Pair;
import utils.Utils;

public class ViewAppello {
	private ControllerAppello controllerAppello;
	private Shell parentShell;
	private Shell appelloShell;
	private int countDocentiRelatori;

	private Label lblData;
	private Label lblCorso;
	private Label lblOre;
	private Label lblAula;
	private Label lblLinkTele;
	private Text membri;
	private Text candidati;

	public ViewAppello(Shell parent, ControllerAppello cr) {
		this.controllerAppello = cr;
		this.parentShell = parent;
		this.countDocentiRelatori = 0;
	}

	public Shell getShell() {
		return appelloShell;
	}

	public void aggiornaPagina() {
		this.countDocentiRelatori = 0;
		AppelloTesi appello = controllerAppello.getAppello();
		String no_value = "INDEFINITO";
		//aggiorna info appello
		if (appello.getData() != null) {
			lblData.setText(appello.getDateString());
		} else {
			lblData.setText(no_value);
		}
		if (!appello.getStartTime().isEmpty()) {
			lblOre.setText(appello.getStartTimeString());
		} else {
			lblOre.setText(no_value);
		}
		if (appello.getAula() != null && appello.getAula().second != null) {
			lblAula.setText(appello.getAula().second);
		} else {
			lblAula.setText(no_value);
		}
		if (appello.getLinkTeleconferenza() != null) {
			lblLinkTele.setText(appello.getLinkTeleconferenza());
		} else {
			lblLinkTele.setText(no_value);
		}
		lblCorso.setText(appello.getCorso().second);
		
		//aggiorna lista commissione
		ArrayList<Docente> commissioni = controllerAppello.getCommissioniDB();
		String text = "";
		Docente presidenteC = controllerAppello.getPresidenteCommissioneFromDB();
		if (presidenteC != null) {
			text += presidenteC.getNomeCognome() + "-Presidente Commissione" + "\n";
		} 
		for (Docente membro : commissioni) {
			text += membro.getNomeCognome() + "\n";
		}
		membri.setText(text);
		
		//aggiorna lista candidati relatori
		ArrayList<Pair<Studente, Docente>> studentiRelatori = controllerAppello.getStudentiRelatoriFromAppelloFromDB();
		text = "";
		for (Pair<Studente, Docente> p : studentiRelatori) {
			text += p.first.getNomeCognome() + "-" + p.second.getNomeCognome() + "\n";
		}
		candidati.setText(text);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		appelloShell = new Shell(parentShell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		appelloShell.setSize(500, 500);
		appelloShell.setText("Appello [" + controllerAppello.getAppello().getId() + "]");
		Utils.setShellToCenterMonitor(appelloShell, display);

		ScrolledComposite scrolledComposite = new ScrolledComposite(appelloShell, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 10, 465, 350);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);

		Label lblDataLabel = new Label(composite, SWT.NONE);
		lblDataLabel.setBounds(10, 10, 35, 15);
		lblDataLabel.setText("Data:");

		lblData = new Label(composite, SWT.NONE);
		lblData.setAlignment(SWT.CENTER);
		lblData.setBounds(51, 10, 80, 15);

		Label lblOreLabel = new Label(composite, SWT.NONE);
		lblOreLabel.setBounds(10, 52, 35, 15);
		lblOreLabel.setText("Ore:");

		lblOre = new Label(composite, SWT.NONE);
		lblOre.setAlignment(SWT.CENTER);
		lblOre.setBounds(51, 52, 80, 15);

		Label lblAulaLabel = new Label(composite, SWT.NONE);
		lblAulaLabel.setBounds(10, 73, 27, 15);
		lblAulaLabel.setText("Aula:");

		lblAula = new Label(composite, SWT.NONE);
		lblAula.setAlignment(SWT.CENTER);
		lblAula.setBounds(51, 73, 80, 15);

		Label lblLinkTeleLabel = new Label(composite, SWT.NONE);
		lblLinkTeleLabel.setBounds(10, 94, 110, 15);
		lblLinkTeleLabel.setText("Link Teleconferenza:");

		lblLinkTele = new Label(composite, SWT.NONE);
		lblLinkTele.setBounds(126, 94, 325, 15);

		Label lblMembriDellaCommissione = new Label(composite, SWT.NONE);
		lblMembriDellaCommissione.setText("Membri della commissione:");
		lblMembriDellaCommissione.setBounds(10, 115, 441, 15);

		membri = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		membri.setEditable(false);
		membri.setBounds(10, 136, 441, 90);

		Label lblCorsoLabel = new Label(composite, SWT.NONE);
		lblCorsoLabel.setBounds(10, 31, 35, 15);
		lblCorsoLabel.setText("Corso:");

		lblCorso = new Label(composite, SWT.NONE);
		lblCorso.setBounds(51, 31, 150, 15);
		
		Label lblCanditati = new Label(composite, SWT.NONE);
		lblCanditati.setBounds(10, 232, 150, 15);
		lblCanditati.setText("Candidati e relatori:");
		
		candidati = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		candidati.setEditable(false);
		candidati.setBounds(10, 253, 441, 100);

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		switch (controllerAppello.getRuolo()) {
		case 0:
			appelloShell.setText(appelloShell.getText() + " - Studente");
			break;
		case 1:
			appelloShell.setText(appelloShell.getText() + " - Responsabile");
			createResponsabileComposite();
			break;
		case 2:
			appelloShell.setText(appelloShell.getText() + " - Presidente Scuola");
			createPresidenteScuolaComposite();
			break;
		case 3:
			appelloShell.setText(appelloShell.getText() + " - Presidente Corso");
			createPresidenteCorsoComposite();
			break;
		}

		aggiornaPagina();
		appelloShell.open();
		appelloShell.layout();
		while (!appelloShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void createPresidenteScuolaComposite() {
		Composite compositePresidenteScuola = new Composite(appelloShell, SWT.BORDER);
		compositePresidenteScuola.setBounds(10, 370, 465, 80);

		Button approvaAppello = new Button(compositePresidenteScuola, SWT.NONE);
		Label lblStatusAppello = new Label(compositePresidenteScuola, SWT.NONE);
		lblStatusAppello.setBounds(10, 45, 465, 15);
		lblStatusAppello.setText("Status Appello: " + controllerAppello.getStatusAppello());
		approvaAppello.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (Utils.createYesNoDialog(appelloShell, "Conferma", "Confermi di approvare l'appello?")) {
					controllerAppello.approvaAppello();
					lblStatusAppello.setText("Status Appello: " + controllerAppello.getStatusAppello());
					aggiornaPagina();
				}
			}
		});
		approvaAppello.setBounds(10, 10, 120, 25);
		approvaAppello.setText("Approva Appello");

		Button richiediCorrezione = new Button(compositePresidenteScuola, SWT.NONE);
		richiediCorrezione.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (Utils.createYesNoDialog(appelloShell, "Conferma", "Confermi di richiedere la correzione?")) {
					controllerAppello.richiediCorrezione();
					lblStatusAppello.setText("Status Appello: " + controllerAppello.getStatusAppello());
					aggiornaPagina();
				}
			}
		});
		richiediCorrezione.setBounds(136, 10, 120, 25);
		richiediCorrezione.setText("Richiedi Correzione");

	}

	public void createPresidenteCorsoComposite() {
		Composite compositeResponsabile = new Composite(appelloShell, SWT.BORDER);
		compositeResponsabile.setBounds(10, 370, 465, 80);

		Button btnData = new Button(compositeResponsabile, SWT.NONE);
		btnData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				aggiungereDataDialog();
			}
		});
		btnData.setBounds(10, 10, 120, 25);
		btnData.setText("Inserisci Data");

		Label lblStatusAppello = new Label(compositeResponsabile, SWT.NONE);
		lblStatusAppello.setBounds(10, 45, 465, 15);
		lblStatusAppello.setText("Status Appello: " + controllerAppello.getStatusAppello());

		Button btnIdentificaMembri = new Button(compositeResponsabile, SWT.NONE);
		btnIdentificaMembri.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				scegliStudentiDocentiDialog();
			}
		});
		btnIdentificaMembri.setBounds(136, 10, 120, 25);
		btnIdentificaMembri.setText("Identifica Membri");

		Button btnNominaPresidente = new Button(compositeResponsabile, SWT.NONE);
		btnNominaPresidente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				scegliPresidenteCommissioneDialog();
			}
		});
		btnNominaPresidente.setBounds(262, 10, 120, 25);
		btnNominaPresidente.setText("Nomina Presidente");
	}

	public void aggiungereDataDialog() {
		Shell child = new Shell(appelloShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Inserisci data dell'appello");
		Utils.setShellToCenterParent(child, appelloShell);
		String original = controllerAppello.getAppello().getDateString();
		AppelloTesi a = controllerAppello.getAppello();

		DateTime dateTimeAula = new DateTime(child, SWT.BORDER);
		dateTimeAula.setBounds(25, 20, 175, 23);
		dateTimeAula.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Calendar cal = Calendar.getInstance();
				int day = dateTimeAula.getDay();
				int month = dateTimeAula.getMonth();
				int year = dateTimeAula.getYear();
				int cday = cal.get(Calendar.DAY_OF_MONTH);
				int cmonth = cal.get(Calendar.MONTH);
				int cyear = cal.get(Calendar.YEAR);

				if (day < cday && month == cmonth && year == cyear) {
					dateTimeAula.setDay(cday);
				} else if (month < cmonth && year == cyear) {
					dateTimeAula.setMonth(cmonth);
				} else if (year < cyear) {
					dateTimeAula.setYear(cyear);
				}
			}
		});
		if (a.getData() != null && !a.getData().equals("")) {
			String[] parts = a.getData().split("-");
			dateTimeAula.setYear(Integer.parseInt(parts[0]));
			dateTimeAula.setMonth(Integer.parseInt(parts[1]) - 1);
			dateTimeAula.setDay(Integer.parseInt(parts[2]));
		}

		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String s = dateTimeAula.getDay() + "/" + (dateTimeAula.getMonth() + 1) + "/" + dateTimeAula.getYear();
				if (s.equals("")) {
					Utils.createWarningDialog(child, "Messaggio", "Non puo' essere vuoto");
				} else if (!s.equals(original)) {
					controllerAppello.addDateToAppello(s);
					controllerAppello.updateAppelloFromDB();
					aggiornaPagina();
					child.close();
				} else {
					child.close();
				}
			}
		});
		btnYes.setBounds(30, 66, 75, 25);
		btnYes.setText("Conferma");

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

	public void createResponsabileComposite() {
		Composite compositeResponsabile = new Composite(appelloShell, SWT.BORDER);
		compositeResponsabile.setBounds(10, 370, 465, 80);

		Button btnPrenotaAula = new Button(compositeResponsabile, SWT.NONE);
		btnPrenotaAula.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (controllerAppello.getAppello().getData() != null) {
					dataAulaDialog();
				} else {
					Utils.createWarningDialog(appelloShell, "Messaggio", "La data dell'appello non e' definita");
				}
			}
		});

		Label lblStatusAppello = new Label(compositeResponsabile, SWT.NONE);
		lblStatusAppello.setBounds(10, 45, 465, 15);
		lblStatusAppello.setText("Status Appello: " + controllerAppello.getStatusAppello());

		btnPrenotaAula.setBounds(10, 10, 180, 25);
		btnPrenotaAula.setText("Prenota Aula e Inserisci Orario");

		Button btnLinkTele = new Button(compositeResponsabile, SWT.NONE);
		btnLinkTele.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				aggiungeLinkTeleDialog();
			}
		});
		btnLinkTele.setBounds(196, 10, 120, 25);
		btnLinkTele.setText("Link teleconferenza");

	}

	public void aggiungeLinkTeleDialog() {
		Shell child = new Shell(appelloShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Link Teleconferenza");
		Utils.setShellToCenterParent(child, appelloShell);
		String original = controllerAppello.getLinkTeleFromDB();

		Text textRepository = new Text(child, SWT.BORDER);
		textRepository.setBounds(30, 20, 171, 23);
		textRepository.setText(original);

		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String s = textRepository.getText();
				if (s.equals("")) {
					Utils.createWarningDialog(child, "Messaggio", "Non puo' essere vuoto");
				} else if (!s.equals(original)) {
					controllerAppello.addLinkTele(s);
					controllerAppello.updateAppelloFromDB();
					aggiornaPagina();
					child.close();
				} else {
					child.close();
				}
			}
		});
		btnYes.setBounds(30, 66, 75, 25);
		btnYes.setText("Conferma");

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

	public void dataAulaDialog() {
		Shell child = new Shell(appelloShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Prenotazione Aula");
		Utils.setShellToCenterParent(child, appelloShell);
		ArrayList<Pair<Integer, String>> aule = controllerAppello.getAuleFromDB();
		Pair<Integer, String> original_aula = controllerAppello.getAppello().getAula();
		String original_orario = controllerAppello.getAppello().getStartTime();
		Combo comboAule = new Combo(child, SWT.READ_ONLY);
		comboAule.setBounds(30, 20, 80, 23);
		for (int i = 0; i < aule.size(); i++) {
			int id = aule.get(i).first;
			comboAule.add(aule.get(i).second);
			if (original_aula.first != null && id == original_aula.first) {
				comboAule.select(i);
			}
		}

		DateTime dateTime = new DateTime(child, SWT.BORDER | SWT.TIME);
		dateTime.setBounds(120, 20, 104, 24);
		if (original_orario != null && !original_orario.equals("")) {
			String[] parts = original_orario.split(":");
			dateTime.setHours(Integer.parseInt(parts[0]));
			dateTime.setMinutes(Integer.parseInt(parts[1]));
			dateTime.setSeconds(Integer.parseInt(parts[2]));
		} else {
			dateTime.setHours(8);
			dateTime.setMinutes(0);
			dateTime.setSeconds(0);
		}

		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (comboAule.getSelectionIndex() != -1) {
					String new_orario = Utils.hhmmssTimeFormat(dateTime.getHours(), dateTime.getMinutes(),
							dateTime.getSeconds());
					if (!comboAule.getText().equals(original_aula.second) || !new_orario.equals(original_orario)) {
						int id_aula = 0;
						String nome_aula = comboAule.getText();
						for (Pair<Integer, String> p : aule) {
							if (p.second.equals(nome_aula)) {
								id_aula = p.first;
								break;
							}
						}

						String orario = dateTime.getHours() + ":" + dateTime.getMinutes();
						if (controllerAppello.prenotaAula(id_aula) && controllerAppello.setOrario(orario)) {
							controllerAppello.updateAppelloFromDB();
							aggiornaPagina();
							child.close();
						}
					}
				} else {
					Utils.createWarningDialog(child, "Messaggio", "Nessuna aula selezionata");
				}
			}
		});
		btnYes.setBounds(30, 66, 75, 25);
		btnYes.setText("Prenota");

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

	public void scegliStudentiDocentiDialog() {
		Shell child = new Shell(appelloShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(600, 330);
		child.setText("Membri appello");
		Utils.setShellToCenterParent(child, appelloShell);
		ArrayList<Pair<Studente, Docente>> studentiRelatori = controllerAppello.getStudentiRelatoriFromDB();
		ArrayList<Pair<Docente, String>> docentiDip = controllerAppello.getDocentiDipFromDB();

		ArrayList<Studente> studentiMembri = controllerAppello.getStudentiFromAppelloDB();
		ArrayList<Docente> relatoriMembri = controllerAppello.getRelatoriFromDB();
		ArrayList<Docente> commissioniMembri = controllerAppello.getCommissioniDB();
		Docente presidenteC = controllerAppello.getPresidenteCommissioneFromDB();

		Label lblCorsoLabel = new Label(child, SWT.NONE);
		lblCorsoLabel.setBounds(30, 20, 100, 15);
		lblCorsoLabel.setText("Studenti-relatori:");

		Combo comboStudentiRelatori = new Combo(child, SWT.READ_ONLY);
		comboStudentiRelatori.setBounds(166, 17, 385, 23);
		for (int i = 0; i < studentiRelatori.size(); i++) {
			comboStudentiRelatori.add(studentiRelatori.get(i).first.getNomeCognome() + "-"
					+ studentiRelatori.get(i).second.getNomeCognome());
		}

		Label lblDocenti = new Label(child, SWT.NONE);
		lblDocenti.setBounds(30, 55, 120, 15);
		lblDocenti.setText("Docenti-dipartimento:");

		Combo comboDocenti = new Combo(child, SWT.READ_ONLY);
		comboDocenti.setBounds(166, 55, 385, 23);
		for (int i = 0; i < docentiDip.size(); i++) {
			comboDocenti.add(docentiDip.get(i).first.getNomeCognome() + "-" + docentiDip.get(i).second);
		}

		List list = new List(child, SWT.BORDER | SWT.V_SCROLL);
		list.setBounds(30, 95, 521, 160);

		for (Studente s : studentiMembri) {
			list.add(s.getNomeCognome() + "-[Studente]");
		}

		for (Docente d : relatoriMembri) {
			list.add(d.getNomeCognome() + "-[Relatore]");
			countDocentiRelatori++;
		}

		for (Docente d : commissioniMembri) {
			list.add(d.getNomeCognome() + "-[Commissione]");
			countDocentiRelatori++;
		}

		if (presidenteC != null) {
			list.add(presidenteC.getNomeCognome() + "-[Commissione]");
			countDocentiRelatori++;
		}

		Button btnAggiungi = new Button(child, SWT.NONE);
		btnAggiungi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// setStudente();
				// setDocenteRelatore(comboDocenti, "(Docente)");
				// setDocenteRelatore(comboRelatori, "(Relatore)");
				boolean add = true;
				if (comboStudentiRelatori.getSelectionIndex() != -1) {
					// studente-relatore
					String[] parseSR = comboStudentiRelatori.getText().split("-");
					for (String element : list.getItems()) {
						if (element.equals(parseSR[0] + "-[Studente]")) {
							comboStudentiRelatori.deselectAll();
							add = false;
						}
					}
					if (add) {
						list.add(parseSR[0] + "-[Studente]");
						list.add(parseSR[1] + "-[Relatore]");
						comboStudentiRelatori.deselectAll();
						countDocentiRelatori++;
					}
				}
				add = true;
				if (comboDocenti.getSelectionIndex() != -1) {
					// docente-dipartimento
					String[] parseSR = comboDocenti.getText().split("-");
					for (String element : list.getItems()) {
						String[] parse = element.split("-");
						if (parse[0].equals(parseSR[0])) {
							comboDocenti.deselectAll();
							add = false;
						}
					}
					if (add) {
						list.add(parseSR[0] + "-[Commissione]");
						comboDocenti.deselectAll();
						countDocentiRelatori++;
					}
				}
			}

		});
		btnAggiungi.setBounds(30, 261, 100, 25);
		btnAggiungi.setText("Aggiungi");

		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (countDocentiRelatori >= 5) {
					ArrayList<Integer> matricoleStudenti = new ArrayList<Integer>();
					ArrayList<Integer> matricoleCommissioni = new ArrayList<Integer>();
					ArrayList<Integer> matricoleRelatori = new ArrayList<Integer>();
					for (String s : list.getItems()) {
						System.out.println(s);
						String[] parseItem = s.split("-");
						if (parseItem[1].equals("[Studente]")) {
							for (int i = 0; i < studentiRelatori.size(); i++) {
								if (studentiRelatori.get(i).first.getNomeCognome().equals(parseItem[0])) {
									matricoleStudenti.add(studentiRelatori.get(i).first.getMatricolaInt());
								}
							}
						} else if (parseItem[1].equals("[Relatore]")) {
							for (int i = 0; i < docentiDip.size(); i++) {
								if (docentiDip.get(i).first.getNomeCognome().equals(parseItem[0])) {
									matricoleRelatori.add(docentiDip.get(i).first.getMatricolaInt());
								}
							}
						} else if (parseItem[1].equals("[Commissione]")) {
							for (int i = 0; i < docentiDip.size(); i++) {
								if (docentiDip.get(i).first.getNomeCognome().equals(parseItem[0])) {
									matricoleCommissioni.add(docentiDip.get(i).first.getMatricolaInt());
								}
							}
						}
					}
					System.out.println(matricoleStudenti.size() +"-"+matricoleRelatori.size()+"-"+matricoleCommissioni.size());
					if (controllerAppello.updateMembriAppello(controllerAppello.getAppello().getId(), matricoleStudenti,
							matricoleRelatori, matricoleCommissioni))
						aggiornaPagina();
					child.close();
				} else {
					Utils.createWarningDialog(child, "Messaggio", "Devi inserire almeno 5 membri delle commissione");
				}
			}
		});
		btnYes.setBounds(242, 261, 100, 25);
		btnYes.setText("Conferma");

		Button btnNo = new Button(child, SWT.NONE);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				child.close();
			}
		});
		btnNo.setBounds(476, 261, 75, 25);
		btnNo.setText("Indietro");

		Button btnRimuovi = new Button(child, SWT.NONE);
		btnRimuovi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (list.getSelectionIndex() != -1) {
					String text = list.getItem(list.getSelectionIndex());
					String[] parseText = text.split("-");
					if (parseText[1].equals("[Studente]")) {
						list.remove(list.getSelectionIndices());
						for (int i = 0; i < studentiRelatori.size(); i++) {
							if (studentiRelatori.get(i).first.getNomeCognome().equals(parseText[0])) {
								for (String s : list.getItems()) {
									String[] split = s.split("-");
									if (split[0].equals(studentiRelatori.get(i).second.getNomeCognome())) {
										list.remove(list.indexOf(s));
										i = studentiRelatori.size();
										break;
									}
								}
							}
						}
					} else if (parseText[1].equals("[Relatore]")) {
						list.remove(list.getSelectionIndices());
						for (int i = 0; i < studentiRelatori.size(); i++) {
							if (studentiRelatori.get(i).second.getNomeCognome().equals(parseText[0])) {
								for (String s : list.getItems()) {
									String[] split = s.split("-");
									if (split[0].equals(studentiRelatori.get(i).first.getNomeCognome())) {
										list.remove(list.indexOf(s));
										countDocentiRelatori--;
										i = studentiRelatori.size();
										break;
									}
								}
							}
						}
					} else {
						countDocentiRelatori -= list.getSelectionCount();
						list.remove(list.getSelectionIndices());
					}
				}
				Console.print("Count docenti e relatori: " + countDocentiRelatori, "GUI");
			}
		});
		btnRimuovi.setBounds(136, 261, 100, 25);
		btnRimuovi.setText("Rimuovi");

		child.open();
	}

	public void scegliPresidenteCommissioneDialog() {
		Shell child = new Shell(appelloShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(311, 150);
		child.setText("Identificazione Presidente di Commissione di tesi");
		Utils.setShellToCenterParent(child, appelloShell);
		ArrayList<Docente> commissioni = controllerAppello.getCommissioniDB();
		
		Label lblRelatoreLabel_1 = new Label(child, SWT.NONE);
		lblRelatoreLabel_1.setText("Membri:");
		lblRelatoreLabel_1.setBounds(30, 26, 55, 15);

		Combo comboMembri = new Combo(child, SWT.READ_ONLY);
		comboMembri.setBounds(92, 23, 160, 23);
		for (int i = 0; i < commissioni.size(); i++) {
			comboMembri.add(commissioni.get(i).getNomeCognome());
		}
		
		
		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (comboMembri.getSelectionIndex() != -1) {
					int matricola = 0;
					for (Docente membro : commissioni) {
						if (comboMembri.getText().equals(membro.getNomeCognome())) {
							matricola = membro.getMatricolaInt();
							break;
						}
					}

					if (controllerAppello.updatePresidenteCommissione(controllerAppello.getAppello().getId(),
							matricola))
						aggiornaPagina();
					child.close();
				} else {
					Utils.createWarningDialog(child, "Messaggio", "Completa i campi vuoti!");
				}
			}
		});
		btnYes.setBounds(30, 69, 75, 25);
		btnYes.setText("Conferma");

		Button btnNo = new Button(child, SWT.NONE);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				child.close();
			}
		});
		btnNo.setBounds(168, 69, 75, 25);
		btnNo.setText("Indietro");

		child.open();
	}
}
