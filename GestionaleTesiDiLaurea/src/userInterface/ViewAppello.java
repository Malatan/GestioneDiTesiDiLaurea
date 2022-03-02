package userInterface;

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
import utils.Console;
import utils.Pair;
import utils.Utils;

public class ViewAppello {
	private ControllerAppello controllerAppello;
	private Shell parentShell;
	private Shell appelloShell;
	private int countDocentiRelatori;
	
	
	private Label lblData;
	private Label lblOre;
	private Label lblAula;
	private Label lblLinkTele;
	private Text membri;
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
		if (appello.getData() != null) {
			lblData.setText(appello.getDateString());
		} else {
			lblData.setText(no_value);
		}
		if (appello.getStartTime() != -1) {
			lblOre.setText(appello.getStartTimeString());
		} else {
			lblOre.setText(no_value);
		}
		if (appello.getAula() != null) {
			lblAula.setText(appello.getAula().second);
		} else {
			lblAula.setText(no_value);
		}
		if (appello.getLinkTeleconferenza() != null) {
			lblLinkTele.setText(appello.getLinkTeleconferenza());
		} else {
			lblLinkTele.setText(no_value);
		}
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
		lblDataLabel.setBounds(10, 10, 441, 15);
		lblDataLabel.setText("Data:");

		lblData = new Label(composite, SWT.NONE);
		lblData.setAlignment(SWT.CENTER);
		lblData.setBounds(51, 10, 80, 15);

		Label lblOreLabel = new Label(composite, SWT.NONE);
		lblOreLabel.setBounds(10, 31, 441, 15);
		lblOreLabel.setText("Ore:");

		lblOre = new Label(composite, SWT.NONE);
		lblOre.setAlignment(SWT.CENTER);
		lblOre.setBounds(51, 31, 80, 15);

		Label lblAulaLabel = new Label(composite, SWT.NONE);
		lblAulaLabel.setBounds(10, 52, 441, 15);
		lblAulaLabel.setText("Aula:");

		lblAula = new Label(composite, SWT.NONE);
		lblAula.setAlignment(SWT.CENTER);
		lblAula.setBounds(51, 52, 80, 15);
		
		Label lblLinkTeleLabel = new Label(composite, SWT.NONE);
		lblLinkTeleLabel.setBounds(10, 73, 441, 15);
		lblLinkTeleLabel.setText("Link Teleconferenza:");
		
		lblLinkTele = new Label(composite, SWT.NONE);
		lblLinkTele.setBounds(126, 73, 325, 15);
		
		
		
		Label lblMembriDellaCommissione = new Label(composite, SWT.NONE);
		lblMembriDellaCommissione.setText("Membri della commissione:");
		lblMembriDellaCommissione.setBounds(10, 115, 441, 15);
		
		Label lblPresidenteDellaCommissione = new Label(composite, SWT.NONE);
		lblPresidenteDellaCommissione.setText("Presidente della commissione: " + controllerAppello.getPresidenteCommissioneFromDB(controllerAppello.getAppello().getId()));
		lblPresidenteDellaCommissione.setBounds(10, 94, 441, 15);
		
		
		ArrayList<Pair<Integer,String>> membriDellaCommissione = controllerAppello.getMembriFromCommissioneDB(controllerAppello.getAppello().getId());
		
		membri = new Text(composite, SWT.BORDER);
		membri.setEditable(false);
		membri.setBounds(10, 136, 441, 65);
		
		for(Pair<Integer,String> membro : membriDellaCommissione) {
			membri.setText(membri.getText()+membro.second);
			membri.setText(membri.getText()+", ");
			
			
		}
		if(!membri.getText().isEmpty()) {
			membri.setText(membri.getText().substring(0, membri.getText().lastIndexOf(",")));
		}
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		switch(controllerAppello.getRuolo()) {
			case 0:
				appelloShell.setText(appelloShell.getText()+" - Studente");
				break;
			case 1:
				appelloShell.setText(appelloShell.getText()+" - Responsabile");
				createResponsabileComposite();
				break;
			case 2:
				appelloShell.setText(appelloShell.getText()+" - Presidente Scuola");
				break;
			case 3:
				appelloShell.setText(appelloShell.getText()+" - Presidente Corso");
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
		child.setText("Data dell'appello");
		Utils.setShellToCenterParent(child, appelloShell);
		String original = controllerAppello.getAppello().getDateString();
		
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
				} else if (!s.equals(original)){
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
		btnPrenotaAula.setBounds(10, 10, 120, 25);
		btnPrenotaAula.setText("Prenota Aula");
		
		Button btnLinkTele = new Button(compositeResponsabile, SWT.NONE);
		btnLinkTele.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				aggiungeLinkTeleDialog();
			}
		});
		btnLinkTele.setBounds(136, 10, 120, 25);
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
				} else if (!s.equals(original)){
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
		
		Combo comboAule = new Combo(child, SWT.READ_ONLY);
		comboAule.setBounds(30, 20, 80, 23);
		for(int i = 0 ; i < aule.size() ; i++) {
			comboAule.add(aule.get(i).second);
		}
		
		DateTime dateTimeAula = new DateTime(child, SWT.BORDER);
		dateTimeAula.setBounds(125, 20, 80, 23);

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

		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (comboAule.getSelectionIndex() != -1) {
					int id_aula = 0;
					String nome_aula = comboAule.getText();
					for(Pair<Integer, String> p : aule) {
						if (p.second.equals(nome_aula)) {
							id_aula = p.first;
							break;
						}
					}
					String data = dateTimeAula.getDay() + "/" + (dateTimeAula.getMonth() + 1) + "/" + dateTimeAula.getYear();
					if (controllerAppello.prenotaAula(id_aula, data)) {
						child.close();
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
		child.setSize(597, 311);
		child.setText("Identificazione membri di commissione");
		Utils.setShellToCenterParent(child, appelloShell);
		ArrayList<Pair<Integer,String>> studenti = controllerAppello.getStudentiFromDB();
		ArrayList<Pair<Integer,String>> relatori = controllerAppello.getRelatoriFromDB();
		ArrayList<Pair<Integer,String>> docenti = controllerAppello.getDocentiFromDB();
		
		Label lblCorsoLabel = new Label(child, SWT.NONE);
		lblCorsoLabel.setBounds(30, 28, 45, 15);
		lblCorsoLabel.setText("Studenti:");
		
		Combo comboStudenti = new Combo(child, SWT.READ_ONLY);
		comboStudenti.setBounds(92, 25, 160, 23);
		for(int i = 0 ; i < studenti.size() ; i++) {
			comboStudenti.add(studenti.get(i).second);
		}
		
		Label lblRelatoreLabel_1 = new Label(child, SWT.NONE);
		lblRelatoreLabel_1.setText("Relatori:");
		lblRelatoreLabel_1.setBounds(30, 67, 55, 15);
		
		Combo comboRelatori = new Combo(child, SWT.READ_ONLY);
		comboRelatori.setBounds(92, 64, 160, 23);
		
		for(int i = 0 ; i < relatori.size() ; i++) {
			comboRelatori.add(relatori.get(i).second);
		}
		
		Label lblDocenti = new Label(child, SWT.NONE);
		lblDocenti.setBounds(30, 108, 55, 15);
		lblDocenti.setText("Docenti:");
		
		Combo comboDocenti = new Combo(child, SWT.READ_ONLY);
		comboDocenti.setBounds(92, 105, 160, 23);
		for(int i = 0 ; i < docenti.size() ; i++) {
			comboDocenti.add(docenti.get(i).second);
		}
		
		List list = new List(child, SWT.BORDER);
		list.setBounds(289, 24, 282, 196);
		
		Button btnNewButton = new Button(child, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			
			
			private void setStudente() {
				if(!comboStudenti.getText().equals("")) {
			        for(String element : list.getItems()) {
			        	if(element.equals(comboStudenti.getText() + "(Studente)")) {
			        		comboStudenti.deselectAll();
			        		return;
			        	}
			        }
			    
					list.add(comboStudenti.getText() + "(Studente)");
					
					comboStudenti.deselectAll();
				}
			}
			

			
			private void setDocenteRelatore(Combo combo, String name) {
				if(!combo.getText().equals("")) {
			        for(String element : list.getItems()) {
			        	if(element.equals(combo.getText() + name)) {
			        		combo.deselectAll();
			        		return;
			        	}
			        }
			    
		        
					list.add(combo.getText() + name);
					countDocentiRelatori++;
					Console.print("Count docenti e relatori: " + countDocentiRelatori, "GUI");
					combo.deselectAll();
		        }
			}
			

			
			@Override
			public void mouseDown(MouseEvent e) {
				
				setStudente();
				setDocenteRelatore(comboDocenti,"(Docente)");
				setDocenteRelatore(comboRelatori,"(Relatore)");
				

				
			}


		});
		btnNewButton.setBounds(59, 164, 146, 25);
		btnNewButton.setText("Aggiungi");
		
		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (countDocentiRelatori >= 5) {
					ArrayList<Integer> matricoleStudenti = new ArrayList<Integer>();
					ArrayList<Integer> matricoleDocentiRelatori = new ArrayList<Integer>();
					
					for(String text : list.getItems()) {
						for(Pair<Integer, String> s : studenti) {
							if(text.equals(s.second+"(Studente)")) {
								Console.print("Aggiunto come studente nel membro di commissione " + s.second+"(Studente)", "APP");
								matricoleStudenti.add(Integer.valueOf(s.first));
								break;
							}
						}
						for(Pair<Integer, String> d : docenti) {
							if(text.equals(d.second + "(Docente)")) {
								Console.print("Aggiunto come docente nel membro di commissione " + d.second+"(Docente)", "APP");
								matricoleDocentiRelatori.add(Integer.valueOf(d.first));
								break;
							}
						}
						
						for(Pair<Integer, String> r : relatori) {
							if(text.equals(r.second+"(Relatore)")) {
								Console.print("Aggiunto come relatore nel membro di commissione " + r.second+"(Relatore)", "APP");
								matricoleDocentiRelatori.add(Integer.valueOf(r.first));
								break;
							}
						}
					}
					

					if (controllerAppello.aggiungiStudentiDocentiToCommissione(controllerAppello.getAppello().getId(), matricoleStudenti, matricoleDocentiRelatori))
						aggiornaPagina();
					child.close();
				} else {
					Utils.createWarningDialog(child, "Messaggio", "Devi inserire almeno 5 membri delle commissione");
				}
			}
		});
		btnYes.setBounds(30, 234, 75, 25);
		btnYes.setText("Conferma");
	
 		Button btnNo = new Button(child, SWT.NONE);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				child.close();
			}
		});
		btnNo.setBounds(177, 234, 75, 25);
		btnNo.setText("Indietro");
		
		Button btnRimuovi = new Button(child, SWT.NONE);
		btnRimuovi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				countDocentiRelatori -= list.getSelectionCount();
				list.remove(list.getSelectionIndices());
				
				Console.print("Count docenti e relatori: " + countDocentiRelatori, "GUI");
			}
		});
		btnRimuovi.setBounds(496, 234, 75, 25);
		btnRimuovi.setText("Rimuovi");
		

		child.open();
	}
	

	public void scegliPresidenteCommissioneDialog() {
		Shell child = new Shell(appelloShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(311, 150);
		child.setText("Identificazione Presidente di Commissione di tesi");
		Utils.setShellToCenterParent(child, appelloShell);

		ArrayList<Pair<Integer,String>> membriDellaCommissione = controllerAppello.getMembriFromCommissioneDB(controllerAppello.getAppello().getId());
		
		
		Label lblRelatoreLabel_1 = new Label(child, SWT.NONE);
		lblRelatoreLabel_1.setText("Membri:");
		lblRelatoreLabel_1.setBounds(30, 26, 55, 15);
		
		Combo comboMembri = new Combo(child, SWT.READ_ONLY);
		comboMembri.setBounds(92, 23, 160, 23);
		
		for(int i = 0 ; i < membriDellaCommissione.size() ; i++) {
			comboMembri.add(membriDellaCommissione.get(i).second);
		}
		

		
		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (comboMembri.getSelectionIndex() != -1) {

					int matricola = 0;
					for(Pair<Integer, String> membro : membriDellaCommissione) {
						if(comboMembri.getText().equals(membro.second)) {
							matricola = membro.first;
							break;
						}
					}

					if (controllerAppello.aggiungiPresidenteCorsoToCommissione(controllerAppello.getAppello().getId(), matricola))
						aggiornaPagina();
					child.close();
				} 
				else {
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
