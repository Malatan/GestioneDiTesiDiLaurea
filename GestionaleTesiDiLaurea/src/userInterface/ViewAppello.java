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
import org.eclipse.wb.swt.SWTResourceManager;

import businessLogic.ControllerAppello;
import databaseAccessObject.Database;
import domainModel.AppelloTesi;
import domainModel.Docente;
import domainModel.Studente;
import domainModel.SuggerimentoSostituto;
import utils.Console;
import utils.Pair;
import utils.Utils;

public class ViewAppello {
	private ControllerAppello controller;
	private Shell parentShell;
	private Shell shell;
	private int countDocentiRelatori;

	private Label lblData;
	private Label lblCorso;
	private Label lblOre;
	private Label lblAula;
	private Label lblLinkTele;
	private Text membri;
	private Text candidati;

	public ViewAppello(Shell parent, ControllerAppello cr) {
		this.controller = cr;
		this.parentShell = parent;
		this.countDocentiRelatori = 0;
	}

	public Shell getShell() {
		return shell;
	}

	public void aggiornaPagina() {
		this.countDocentiRelatori = 0;
		AppelloTesi appello = controller.getAppello();
		String no_value = "INDEFINITO";
		// aggiorna info appello
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

		// aggiorna lista commissione
		ArrayList<Docente> commissione = controller.getCommissioneNoRelatoriDB();
		String text = "";
		Docente presidenteC = controller.getPresidenteCommissioneFromDB();
		if (presidenteC != null) {
			text += presidenteC.getNomeCognome() + "-Presidente Commissione" + "\n";
		}
		for (Docente membro : commissione) {
			text += membro.getNomeCognome() + "\n";
		}
		membri.setText(text);

		// aggiorna lista candidati relatori
		ArrayList<Pair<Studente, Docente>> studentiRelatori = controller.getStudentiRelatoriFromAppelloFromDB();
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
		shell = new Shell(parentShell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		shell.setSize(500, 500);
		shell.setText("Appello [" + controller.getAppello().getId() + "]");
		Utils.setShellToCenterMonitor(shell, display);

		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
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
		membri.setBounds(10, 136, 435, 90);

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
		candidati.setBounds(10, 253, 435, 100);

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		switch (controller.getRuoloDidattica()) {
		case 0:
			shell.setText(shell.getText() + " - Studente");
			break;
		case 1:
			shell.setText(shell.getText() + " - Responsabile");
			createResponsabileComposite();
			break;
		case 2:
			shell.setText(shell.getText() + " - Presidente Scuola");
			createPresidenteScuolaComposite();
			break;
		case 3:
			shell.setText(shell.getText() + " - Presidente Corso");
			createPresidenteCorsoComposite();
			break;
		case 4:
			shell.setText(shell.getText() + " - Docente");
			createDocenteComposite();
			break;
		}

		aggiornaPagina();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void createPresidenteScuolaComposite() {
		Composite compositePresidenteScuola = new Composite(shell, SWT.BORDER);
		compositePresidenteScuola.setBounds(10, 370, 465, 80);
		
		Label lblStatusAppello = new Label(compositePresidenteScuola, SWT.NONE);
		lblStatusAppello.setBounds(10, 45, 465, 15);
		lblStatusAppello.setText("Status Appello: " + controller.getAppello().getStatusString());
		int appello_status = controller.getAppello().getStatus();
		if (appello_status == 2 || appello_status == 1 || appello_status == 0) {
			Button approvaAppello = new Button(compositePresidenteScuola, SWT.NONE);	
			approvaAppello.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					if (Utils.createYesNoDialog(shell, "Conferma", "Confermi di approvare l'appello?")) {
						controller.approvaAppello();
						lblStatusAppello.setText("Status Appello: " + controller.getStatusAppello());
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
					if (Utils.createYesNoDialog(shell, "Conferma", "Confermi di richiedere la correzione?")) {
						controller.richiediCorrezione();
						lblStatusAppello.setText("Status Appello: " + controller.getStatusAppello());
						aggiornaPagina();
					}
				}
			});
			richiediCorrezione.setBounds(136, 10, 120, 25);
			richiediCorrezione.setText("Richiedi Correzione");
		}
	}

	public void createPresidenteCorsoComposite() {
		Composite compositeResponsabile = new Composite(shell, SWT.BORDER);
		compositeResponsabile.setBounds(10, 370, 465, 80);
		
		Label lblStatusAppello = new Label(compositeResponsabile, SWT.NONE);
		lblStatusAppello.setBounds(10, 45, 465, 15);
		lblStatusAppello.setText("Status Appello: " + controller.getAppello().getStatusString());
		int appello_status = controller.getAppello().getStatus();
		
		Button btnData = new Button(compositeResponsabile, SWT.NONE);
		btnData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				aggiungereDataDialog();
			}
		});
		btnData.setBounds(10, 10, 120, 25);
		btnData.setText("Inserisci Data");
		
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
		
		if (appello_status == 1) {
			btnNominaPresidente.setEnabled(false);
			btnIdentificaMembri.setEnabled(false);
			btnData.setEnabled(false);
		}
	}

	public void createDocenteComposite() {
		Composite compositeDocente = new Composite(shell, SWT.BORDER);
		compositeDocente.setBounds(10, 370, 465, 80);

		Button btnDeterminazione = new Button(compositeDocente, SWT.NONE);
		Label lblStatusAppello = new Label(compositeDocente, SWT.NONE);
		lblStatusAppello.setBounds(10, 45, 465, 15);
		lblStatusAppello.setText("Status Appello: " + controller.getAppello().getStatusString());
		btnDeterminazione.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

			}
		});
		btnDeterminazione.setBounds(10, 10, 120, 25);
		btnDeterminazione.setText("Determinazione");
		if (controller.getAppello().getStatus() != 3) {
			btnDeterminazione.setEnabled(false);
		}
		// membro commissione
		if (controller.getRuoloAppello() == 1) {
			Button btnSostituto = new Button(compositeDocente, SWT.NONE);
			btnSostituto.setFont(SWTResourceManager.getFont("Segoe UI", 7, SWT.NORMAL));
			btnSostituto.setBounds(136, 10, 120, 25);
			btnSostituto.setText("Suggerimento sostituto");
			btnSostituto.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					richiestaSostitutoDialog();
				}
			});
		}
		// presidente commissione
		if (controller.getRuoloAppello() == 3) {
			Button btnProposteSostituto = new Button(compositeDocente, SWT.NONE);
			btnProposteSostituto.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			btnProposteSostituto.setBounds(136, 10, 120, 25);
			btnProposteSostituto.setText("Proposte sostituto");
			btnProposteSostituto.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					proposteSostitutoDialog();
				}
			});

			Button btnFineAppello = new Button(compositeDocente, SWT.NONE);
			btnFineAppello.setBounds(262, 10, 139, 25);
			btnFineAppello.setText("Fine discussione");
			if (controller.getAppello().getStatus() == 1) {
				btnFineAppello.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						fineDiscussioneDialog();
					}
				});
			} else {
				btnFineAppello.setVisible(false);
				btnProposteSostituto.setVisible(false);
			}
		}

	}

	public void fineDiscussioneDialog() {
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setText("Fine discussione");
		child.setSize(430, 500);
		Utils.setShellToCenterParent(child, shell);
		ArrayList<Docente> commissione = controller.getCommissioneFromDB();
		ArrayList<Studente> candidati = controller.getStudentiFromAppelloDB();
		Docente presidenteC = controller.getPresidenteCommissioneFromDB();
		for (int i = 0; i < commissione.size(); i++) {
			if (commissione.get(i).getMatricolaInt() == presidenteC.getMatricolaInt()) {
				commissione.remove(i);
				break;
			}
		}

		Label lblLabel1 = new Label(child, SWT.NONE);
		lblLabel1.setAlignment(SWT.CENTER);
		lblLabel1.setBounds(10, 10, 390, 15);
		lblLabel1.setText("Assegnazione voti dei candidati");

		Label lblCandidati = new Label(child, SWT.NONE);
		lblCandidati.setAlignment(SWT.CENTER);
		lblCandidati.setBounds(10, 31, 150, 15);
		lblCandidati.setText("Candidati");

		Label lblEsiti = new Label(child, SWT.NONE);
		lblEsiti.setAlignment(SWT.CENTER);
		lblEsiti.setBounds(250, 31, 150, 15);
		lblEsiti.setText("Esiti");

		Label lblSeparetor = new Label(child, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparetor.setBounds(10, 198, 390, 2);

		Label lblLabel2 = new Label(child, SWT.NONE);
		lblLabel2.setAlignment(SWT.CENTER);
		lblLabel2.setBounds(10, 206, 390, 15);
		lblLabel2.setText("Membri della commissione presenti");

		Label lblPrevista = new Label(child, SWT.NONE);
		lblPrevista.setAlignment(SWT.CENTER);
		lblPrevista.setBounds(10, 227, 150, 15);
		lblPrevista.setText("Commissione prevista");

		Label lblPresenti = new Label(child, SWT.NONE);
		lblPresenti.setAlignment(SWT.CENTER);
		lblPresenti.setBounds(250, 227, 150, 15);
		lblPresenti.setText("Membri presenti");
		
		Text textVoto = new Text(child, SWT.BORDER);
		textVoto.setBounds(166, 109, 78, 21);
		textVoto.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		
		List listCandidati = new List(child, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		listCandidati.setBounds(10, 52, 150, 140);
		for (Studente s : candidati) {
			listCandidati.add(s.getNomeCognome());
		}
		
		List listEsiti = new List(child, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listEsiti.setBounds(250, 52, 150, 140);

		List listPrevista = new List(child, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listPrevista.setBounds(10, 248, 150, 160);
		for (Docente d : commissione) {
			listPrevista.add(d.getNomeCognome());
		}

		List listPresenti = new List(child, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listPresenti.setBounds(250, 248, 150, 160);
		
		Button btnAggiunge = new Button(child, SWT.NONE);
		btnAggiunge.setBounds(166, 136, 78, 25);
		btnAggiunge.setText("Aggiunge");
		btnAggiunge.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (listCandidati.getSelectionCount() != 0) {
					if (textVoto.getText().length() != 0) {
						String esito = listCandidati.getItem(listCandidati.getSelectionIndex()) + "-" + textVoto.getText();
						listEsiti.add(esito);
						listCandidati.remove(listCandidati.getSelectionIndex());
					} else {
						Utils.createWarningDialog(child, "Warning", "Inserisci il voto del candidato.");
					}
				} else {
					Utils.createWarningDialog(child, "Warning", "Seleziona il candidato da assegnare il voto.");
				}
			}
		});
		
		Button btnRimuove = new Button(child, SWT.NONE);
		btnRimuove.setText("Rimuove");
		btnRimuove.setBounds(166, 167, 78, 25);
		btnRimuove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (listEsiti.getSelectionCount() != 0) {
					for (int index : listEsiti.getSelectionIndices()) {
						String candidato = listEsiti.getItem(index).split("-")[0];
						listCandidati.add(candidato);
					}
					listEsiti.remove(listEsiti.getSelectionIndices());
				} else {
					Utils.createWarningDialog(child, "Warning", "Seleziona il candidato da rimuovere.");
				}
			}
		});
		
		Button btnAddPresente = new Button(child, SWT.NONE);
		btnAddPresente.setBounds(166, 352, 78, 25);
		btnAddPresente.setText("Presente-->");
		btnAddPresente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				for (int index : listPrevista.getSelectionIndices()) {
					listPresenti.add(listPrevista.getItem(index));
				}
				listPrevista.remove(listPrevista.getSelectionIndices());
			}
		});

		Button btnRemovePresente = new Button(child, SWT.NONE);
		btnRemovePresente.setBounds(166, 383, 78, 25);
		btnRemovePresente.setText("<--Rimuove");
		btnRemovePresente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				for (int index : listPresenti.getSelectionIndices()) {
					listPrevista.add(listPresenti.getItem(index));
				}
				listPresenti.remove(listPresenti.getSelectionIndices());
			}
		});

		Button btnConferma = new Button(child, SWT.NONE);
		btnConferma.setBounds(85, 420, 100, 25);
		btnConferma.setText("Conferma");
		btnConferma.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (listCandidati.getItems().length == 0) {
					if (Utils.createYesNoDialog(child, "Conferma", "Conferma di chiudere la discussione?")) {
						ArrayList<Pair<Studente, Integer>> esiti = new ArrayList<Pair<Studente, Integer>>();
						for (int i = 0 ; i < candidati.size() ; i++) {
							for (String esito : listEsiti.getItems()) {
								String nomeEsito = esito.split("-")[0];
								int votoEsito = Integer.parseInt(esito.split("-")[1]);
								if (candidati.get(i).getNomeCognome().equals(nomeEsito)) {
									esiti.add(Pair.of(candidati.get(i), votoEsito));
									break;
								}
							}
						}
						for (String d_assente : listPrevista.getItems()) {
							for (int i = 0; i < commissione.size(); i++) {
								if (commissione.get(i).getNomeCognome().equals(d_assente)) {
									commissione.remove(i);
									break;
								}
							}
						}
						if (controller.fineDiscussione(esiti, commissione)) {
							controller.generaVerbale(commissione, esiti, presidenteC);
							Utils.createConfirmDialog(child, "Messaggio", "Valutazione inserita con successo.");
							controller.getAppello().setStatus(3);
							aggiornaPagina();
							child.close();
						}
					}
				} else {
					Utils.createWarningDialog(child, "Messaggio", "Ci sono candidati da assegnare il voto.");
				}
			}
		});

		Button btnIndietro = new Button(child, SWT.NONE);
		btnIndietro.setBounds(225, 420, 100, 25);
		btnIndietro.setText("Indietro");
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				child.close();
			}
		});

		child.open();
	}

	public void proposteSostitutoDialog() {
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setText("Proposte sostituto");
		child.setSize(450, 270);
		Utils.setShellToCenterParent(child, shell);
		ArrayList<SuggerimentoSostituto> proposte = controller.getProposteByAppelloFromDB();

		Label lblProposteLabel = new Label(child, SWT.NONE);
		lblProposteLabel.setBounds(20, 13, 55, 15);
		lblProposteLabel.setText("Proposte:");

		Label lblPropostaDettaglio = new Label(child, SWT.NONE);
		lblPropostaDettaglio.setBounds(20, 39, 104, 15);
		lblPropostaDettaglio.setText("Dettaglio Proposta");

		Label lblSostitutoLabel = new Label(child, SWT.NONE);
		lblSostitutoLabel.setBounds(20, 60, 55, 15);
		lblSostitutoLabel.setText("Sostituto:");

		Label lblSostituto = new Label(child, SWT.NONE);
		lblSostituto.setBounds(81, 60, 150, 15);

		Text textNota = new Text(child, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		textNota.setBounds(20, 102, 391, 90);

		Label lblNotaLabel = new Label(child, SWT.NONE);
		lblNotaLabel.setBounds(20, 81, 55, 15);
		lblNotaLabel.setText("Nota:");

		Combo comboProposte = new Combo(child, SWT.READ_ONLY);
		comboProposte.setBounds(81, 10, 330, 23);

		Button btnApprova = new Button(child, SWT.NONE);
		btnApprova.setBounds(20, 198, 75, 25);
		btnApprova.setText("Approva");

		Button btnRifiuta = new Button(child, SWT.NONE);
		btnRifiuta.setBounds(109, 198, 75, 25);
		btnRifiuta.setText("Rifiuta");

		Button btnIndietro = new Button(child, SWT.NONE);
		btnIndietro.setBounds(336, 198, 75, 25);
		btnIndietro.setText("Indietro");
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				child.close();
			}
		});

		btnApprova.setEnabled(false);
		btnRifiuta.setEnabled(false);
		if (!proposte.isEmpty()) {
			btnRifiuta.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					if (Utils.createYesNoDialog(child, "Conferma", "Conferma di rifiutare la prospota?")) {
						for (int i = 0; i < proposte.size(); i++) {
							if (comboProposte.getText().equals(proposte.get(i).getNomeCognomeRichiedente())) {
								controller.setSuggerimentoStatus(proposte.get(i), 2);
								Utils.createConfirmDialog(child, "Messaggio", "La proposta e' stata rifiutata.");
								child.close();
								break;
							}
						}
					}
				}
			});
			btnApprova.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					if (Utils.createYesNoDialog(child, "Conferma", "Conferma di approvare la prospota?")) {
						for (int i = 0; i < proposte.size(); i++) {
							if (comboProposte.getText().equals(proposte.get(i).getNomeCognomeRichiedente())) {
								controller.setSuggerimentoStatus(proposte.get(i), 1);
								Utils.createConfirmDialog(child, "Messaggio", "La proposta e' stata approvata.");
								aggiornaPagina();
								child.close();
								break;
							}
						}
					}
				}
			});
			comboProposte.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (comboProposte.getSelectionIndex() != -1) {
						btnApprova.setEnabled(true);
						btnRifiuta.setEnabled(true);
						for (int i = 0; i < proposte.size(); i++) {
							if (comboProposte.getText().equals(proposte.get(i).getNomeCognomeRichiedente())) {
								textNota.setText(proposte.get(i).getNota());
								lblSostituto.setText(proposte.get(i).getNomeCognomeSostituto());
								break;
							}
						}
					} else {
						btnApprova.setEnabled(false);
						btnRifiuta.setEnabled(false);
					}
				}
			});
			for (int i = 0; i < proposte.size(); i++) {
				comboProposte.add(proposte.get(i).getNomeCognomeRichiedente());
			}
		}

		child.open();
	}

	public void aggiungereDataDialog() {
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Inserisci data dell'appello");
		Utils.setShellToCenterParent(child, shell);
		String original = controller.getAppello().getDateString();
		AppelloTesi a = controller.getAppello();

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
					controller.addDateToAppello(s);
					controller.updateAppelloFromDB();
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
		Composite compositeResponsabile = new Composite(shell, SWT.BORDER);
		compositeResponsabile.setBounds(10, 370, 465, 80);

		Button btnPrenotaAula = new Button(compositeResponsabile, SWT.NONE);
		btnPrenotaAula.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (controller.getAppello().getData() != null) {
					dataAulaDialog();
				} else {
					Utils.createWarningDialog(shell, "Messaggio", "La data dell'appello non e' definita");
				}
			}
		});

		Label lblStatusAppello = new Label(compositeResponsabile, SWT.NONE);
		lblStatusAppello.setBounds(10, 45, 465, 15);
		lblStatusAppello.setText("Status Appello: " + controller.getStatusAppello());

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
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Link Teleconferenza");
		Utils.setShellToCenterParent(child, shell);
		String original = controller.getLinkTeleFromDB();

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
					controller.addLinkTele(s);
					controller.updateAppelloFromDB();
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
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Prenotazione Aula");
		Utils.setShellToCenterParent(child, shell);
		ArrayList<Pair<Integer, String>> aule = controller.getAuleFromDB();
		Pair<Integer, String> original_aula = controller.getAppello().getAula();
		String original_orario = controller.getAppello().getStartTime();
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
						if (controller.prenotaAula(id_aula) && controller.setOrario(orario)) {
							controller.updateAppelloFromDB();
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
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(600, 330);
		child.setText("Membri appello");
		Utils.setShellToCenterParent(child, shell);
		ArrayList<Pair<Studente, Docente>> studentiRelatori = controller.getStudentiRelatoriFromDB();
		ArrayList<Pair<Docente, String>> docentiDip = controller.getDocentiDipFromDB();

		ArrayList<Studente> studentiMembri = controller.getStudentiFromAppelloDB();
		ArrayList<Docente> relatoriMembri = controller.getRelatoriFromDB();
		ArrayList<Docente> commissioniMembri = controller.getCommissioneNoRelatoriDB();
		Docente presidenteC = controller.getPresidenteCommissioneFromDB();

		Label lblCorsoLabel = new Label(child, SWT.NONE);
		lblCorsoLabel.setBounds(30, 20, 100, 15);
		lblCorsoLabel.setText("Studenti-relatori:");

		Combo comboStudentiRelatori = new Combo(child, SWT.READ_ONLY);
		comboStudentiRelatori.setBounds(166, 17, 385, 23);
		for (int i = 0; i < studentiRelatori.size(); i++) {
			if (studentiRelatori.get(i).first.getStatusTesi() != 2) {
				comboStudentiRelatori.add(studentiRelatori.get(i).first.getNomeCognome() + "-"
						+ studentiRelatori.get(i).second.getNomeCognome());
			}
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
					if (controller.updateMembriAppello(controller.getAppello().getId(), matricoleStudenti,
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
										countDocentiRelatori--;
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
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(311, 150);
		child.setText("Identificazione Presidente di Commissione di tesi");
		Utils.setShellToCenterParent(child, shell);
		ArrayList<Docente> commissioni = controller.getCommissioneNoRelatoriDB();

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

					if (controller.updatePresidenteCommissione(controller.getAppello().getId(), matricola))
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

	public void richiestaSostitutoDialog() {
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(450, 260);
		child.setText("Suggerimento sostituto");
		Utils.setShellToCenterParent(child, shell);
		ArrayList<Docente> docenti = controller.getDocentiPerSostitutzione();
		SuggerimentoSostituto suggerimento = controller.getSuggerimentoByAppelloDocente();

		Label lblDocenteLabel = new Label(child, SWT.NONE);
		lblDocenteLabel.setBounds(22, 24, 55, 15);
		lblDocenteLabel.setText("Sostituto:");

		Combo comboDocenti = new Combo(child, SWT.READ_ONLY);
		comboDocenti.setBounds(83, 21, 328, 23);
		for (Docente d : docenti) {
			comboDocenti.add(d.getNomeCognome() + "-" + d.getDipartimento().second);
		}

		Text textNota = new Text(child, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		textNota.setBounds(22, 80, 389, 86);

		Label lblNotaLabel = new Label(child, SWT.NONE);
		lblNotaLabel.setBounds(22, 59, 55, 15);
		lblNotaLabel.setText("Nota:");

		Button btnConferma = new Button(child, SWT.NONE);
		btnConferma.setBounds(22, 183, 120, 25);
		btnConferma.setText("Invia");
		btnConferma.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (suggerimento != null) {
					String original_sostituto = suggerimento.getNomeCognomeSostituto();
					String original_nota = suggerimento.getNota();
					String combo_docente = comboDocenti.getText().split("-")[0];
					if (!combo_docente.equals(original_sostituto) || !textNota.getText().equals(original_nota)) {
						if (comboDocenti.getSelectionIndex() != -1) {
							int new_docente = suggerimento.getIdSosituto();
							String new_nota = original_nota;
							if (!combo_docente.equals(original_sostituto)) {
								String docente = comboDocenti.getText().split("-")[0];
								for (Docente d : docenti) {
									if (docente.equals(d.getNomeCognome())) {
										new_docente = d.getMatricolaInt();
										break;
									}
								}
							}
							if (!textNota.getText().equals(original_nota)) {
								new_nota = textNota.getText().isBlank() ? "" : textNota.getText();
							}
							suggerimento.setIdSosituto(new_docente);
							suggerimento.setNota(new_nota);
							if (controller.updateSuggerimento(suggerimento)) {
								Utils.createConfirmDialog(child, "Messaggio", "Il suggerimento e' stato aggiornato.");
							}
							child.close();
						} else {
							Utils.createWarningDialog(child, "Messaggio", "Nomina il sostituto.");
						}
					} else {
						child.close();
					}
				} else {
					if (comboDocenti.getSelectionIndex() != -1) {
						String nota = textNota.getText().isBlank() ? "" : textNota.getText();
						String docente = comboDocenti.getText().split("-")[0];
						for (Docente d : docenti) {
							if (docente.equals(d.getNomeCognome())) {
								if (controller.addSuggerimento(d.getMatricolaInt(), nota))
									Utils.createConfirmDialog(child, "Messaggio",
											"Il suggerimento e' stato proposto al presidente di commissione!");
								child.close();
								break;
							}
						}
					} else {
						Utils.createWarningDialog(child, "Messaggio", "Nomina il sostituto.");
					}
				}
			}
		});

		Button btnRevoca = new Button(child, SWT.NONE);
		btnRevoca.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		btnRevoca.setBounds(148, 183, 120, 25);
		btnRevoca.setText("Revoca");
		btnRevoca.setEnabled(false);
		btnRevoca.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (Utils.createYesNoDialog(child, "Conferma", "Conferma di ritirare la proposta")) {
					if (controller.revocaSuggerimento(suggerimento.getId())) {
						Utils.createConfirmDialog(child, "Messaggio", "La proposta e' stata revocata");
						child.close();
					}
				}
			}
		});

		Button btnIndietro = new Button(child, SWT.NONE);
		btnIndietro.setBounds(331, 183, 80, 25);
		btnIndietro.setText("Indietro");
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				child.close();
			}
		});

		if (suggerimento != null) {
			for (String text : comboDocenti.getItems()) {
				String[] parse = text.split("-");
				if (parse[0].equals(suggerimento.getNomeCognomeSostituto())) {
					comboDocenti.setText(text);
					break;
				}
			}
			textNota.setText(suggerimento.getNota());
			btnConferma.setText("Modifica");
			btnRevoca.setEnabled(true);
		}

		child.open();
	}
}
