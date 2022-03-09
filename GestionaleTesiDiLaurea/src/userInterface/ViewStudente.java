package userInterface;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import java.time.LocalDate;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

import businessLogic.ControllerAppello;
import businessLogic.ControllerLogin;
import businessLogic.ControllerStudente;
import domainModel.AppelloTesi;
import domainModel.Studente;
import utils.Console;
import utils.Pair;
import utils.Utils;
import org.eclipse.wb.swt.SWTResourceManager;

public class ViewStudente {
	private ControllerStudente controller;
	private Shell shell;
	
	private Text textStatusTesi;
	private Button btnIscrizione;
	private Button btnRitira;
	private Button btnAddRepo;
	private Composite compositeUserInfo_1;
	private Button btnScaricaDocumento;
	public ViewStudente(ControllerStudente cs) {
		this.controller = cs;
	}
	
	public Shell getShell() {
		return this.shell;
	}
	
	public void aggiornaPagina() {
		Studente studente = controller.getStudente();
		Pair<AppelloTesi, Integer> esito = controller.getEsitoTesi();
		if (esito != null) {
			btnIscrizione.setEnabled(false);
			btnRitira.setEnabled(false);
			btnAddRepo.setEnabled(false);
			btnScaricaDocumento.setVisible(true);
			String status = "Hai partecipato all'appello di tesi: \nCorso: " + esito.first.getCorso().second 
					+ "\nData: " + esito.first.getData() + "\nEsito tesi: " + esito.second;
			textStatusTesi.setText(status);
		} else {
			textStatusTesi.setText(controller.getStatusTesi());
			switch(studente.getStatusTesi()) {
				case 0:
					btnIscrizione.setEnabled(true);
					btnRitira.setEnabled(false);
					btnAddRepo.setEnabled(false);
					break;
				case 1:
					btnIscrizione.setEnabled(false);
					btnRitira.setEnabled(true);
					btnAddRepo.setEnabled(true);
				case 2:	
			}
		}
	}
	
	public void creazioneRitiraDomandaDialog() {
		if(Utils.createYesNoDialog(shell, "Conferma", "Confermi di volerti ritirare dall'appello?")) {
			controller.ritiraDomanda();
			aggiornaPagina();
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Studente studente = controller.getStudente();
		Display display = Display.getDefault();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(400, 567);
		shell.setText("Studente");
		Utils.setShellToCenterMonitor(shell, display);
		
		Composite compositeUserInfo = new Composite(shell, SWT.BORDER);
		compositeUserInfo.setBounds(20, 10, 340, 80);

		Label lblMatricola = new Label(compositeUserInfo, SWT.NONE);
		lblMatricola.setBounds(10, 10, 200, 15);
		lblMatricola.setText("Matricola: " + studente.getMatricola());

		Label lblNome = new Label(compositeUserInfo, SWT.NONE);
		lblNome.setBounds(10, 31, 200, 15);
		lblNome.setText("Nome: " + studente.getNome());

		Label lblCognome = new Label(compositeUserInfo, SWT.NONE);
		lblCognome.setBounds(10, 52, 200, 15);
		lblCognome.setText("Cognome: " + studente.getCognome());
		
		textStatusTesi = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		textStatusTesi.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		textStatusTesi.setBounds(20, 96, 340, 170);
		
		compositeUserInfo_1 = new Composite(shell, SWT.BORDER);
		compositeUserInfo_1.setBounds(20, 272, 340, 244);
		
		btnIscrizione = new Button(compositeUserInfo_1, SWT.NONE);
		btnIscrizione.setLocation(10, 10);
		btnIscrizione.setSize(316, 35);
		btnIscrizione.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				sceglieCorsoDialog();
			}
		});
		btnIscrizione.setText("Iscrizione Tesi");
		
		btnRitira = new Button(compositeUserInfo_1, SWT.NONE);
		btnRitira.setLocation(10, 51);
		btnRitira.setSize(316, 35);
		btnRitira.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				creazioneRitiraDomandaDialog();
			}
		});
		btnRitira.setText("Ritira domanda");
		
		btnAddRepo = new Button(compositeUserInfo_1, SWT.NONE);
		btnAddRepo.setLocation(10, 92);
		btnAddRepo.setSize(316, 35);
		btnAddRepo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				addRepoDialog();
			}
		});
		btnAddRepo.setText("Repository tesi");
		
		Button btnLogOut = new Button(compositeUserInfo_1, SWT.NONE);
		btnLogOut.setLocation(10, 205);
		btnLogOut.setSize(316, 25);
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.close();
				controller = null;
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});
		btnLogOut.setText("Log out");
		
		btnScaricaDocumento = new Button(compositeUserInfo_1, SWT.NONE);
		btnScaricaDocumento.setText("Scarica documento esito tesi");
		btnScaricaDocumento.setBounds(10, 133, 316, 35);
		btnScaricaDocumento.setVisible(false);
		
		aggiornaPagina();
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void addRepoDialog() {
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Repository tesi");
		Utils.setShellToCenterParent(child, shell);
		String original = controller.getRepositoryFromDB();
		
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
					controller.addRepo(s);
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
	
	public void sceglieCorsoDialog() {
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(300, 200);
		child.setText("Domanda Tesi");
		Utils.setShellToCenterParent(child, shell);
		ArrayList<Pair<Integer, String>> corsi = controller.getCorsiFromDB();
		ArrayList<Pair<Integer, String>> docenti = controller.getDocentiFromDB();
		
		Label lblCorsoLabel = new Label(child, SWT.NONE);
		lblCorsoLabel.setBounds(30, 28, 45, 15);
		lblCorsoLabel.setText("Corso:");
		
		Combo comboCorsi = new Combo(child, SWT.READ_ONLY);
		comboCorsi.setBounds(92, 25, 160, 23);
		for(int i = 0 ; i < corsi.size() ; i++) {
			comboCorsi.add(corsi.get(i).second);
		}
		
		Label lblRelatoreLabel = new Label(child, SWT.NONE);
		lblRelatoreLabel.setBounds(30, 68, 55, 15);
		lblRelatoreLabel.setText("Relatore:");
		
		Combo comboDocenti = new Combo(child, SWT.READ_ONLY);
		comboDocenti.setBounds(92, 65, 160, 23);
		for(int i = 0 ; i < docenti.size() ; i++) {
			comboDocenti.add(docenti.get(i).second);
		}
		
		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (comboCorsi.getSelectionIndex() != -1 && comboDocenti.getSelectionIndex() != -1) {
					Pair<Integer, String> corso = null;
					Pair<Integer, String> relatore = null;
					for(Pair<Integer, String> p : corsi) {
						if(comboCorsi.getText().equals(p.second)) {
							corso = p;
							break;
						}
					}
					for(Pair<Integer, String> d : docenti) {
						if(comboDocenti.getText().equals(d.second)) {
							relatore = d;
							break;
						}
					}
					if (controller.iscrizione(corso, relatore))
						aggiornaPagina();
					child.close();
				} else {
					Utils.createWarningDialog(child, "Messaggio", "Completa i campi vuoti!");
				}
			}
		});
		btnYes.setBounds(30, 115, 75, 25);
		btnYes.setText("Conferma");
	
 		Button btnNo = new Button(child, SWT.NONE);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				child.close();
			}
		});
		btnNo.setBounds(176, 115, 75, 25);
		btnNo.setText("Indietro");
		child.open();
	}
}
