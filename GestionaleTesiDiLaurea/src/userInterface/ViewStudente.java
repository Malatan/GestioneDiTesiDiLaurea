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

import businessLogic.ControllerLogin;
import businessLogic.ControllerStudente;
import domainModel.Studente;
import utils.Console;
import utils.Pair;
import utils.Utils;
import org.eclipse.wb.swt.SWTResourceManager;

public class ViewStudente {
	private ControllerStudente controllerStudente;
	private Shell studenteShell;
	
	private Text textStatusTesi;
	private Button btnIscrizione;
	private Button btnRitira;
	private Button btnAddRepo;
	private Button btnAppello;
	private Composite compositeUserInfo_1;
	public ViewStudente(ControllerStudente cs) {
		this.controllerStudente = cs;
	}
	
	public Shell getShell() {
		return this.studenteShell;
	}
	
	public void aggiornaPagina() {
		textStatusTesi.setText(controllerStudente.getStatusTesi());
		Studente studente = controllerStudente.getStudente();
		Console.print("Status tesi: " + studente.getStatusTesi(),"DEBUG");
		switch(studente.getStatusTesi()) {
			case 0:
				btnIscrizione.setEnabled(true);
				btnRitira.setEnabled(false);
				btnAddRepo.setEnabled(false);
				btnAppello.setEnabled(false);
				break;
			case 1:
				btnIscrizione.setEnabled(false);
				btnRitira.setEnabled(true);
				btnAddRepo.setEnabled(true);
			case 2:	
		}
	}
	
	public void creazioneRitiraDomandaDialog() {
		if(Utils.createYesNoDialog(studenteShell, "Conferma", "Confermi di volerti ritirare dall'appello?")) {
			controllerStudente.ritiraDomanda();
			aggiornaPagina();
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Studente studente = controllerStudente.getStudente();
		Display display = Display.getDefault();
		studenteShell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		studenteShell.setSize(400, 567);
		studenteShell.setText("Gestionale di tesi di laurea - Studente");
		Utils.setShellToCenterMonitor(studenteShell, display);
		
		Composite compositeUserInfo = new Composite(studenteShell, SWT.BORDER);
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
		
		textStatusTesi = new Text(studenteShell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		textStatusTesi.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		textStatusTesi.setBounds(20, 119, 340, 150);
		
		compositeUserInfo_1 = new Composite(studenteShell, SWT.BORDER);
		compositeUserInfo_1.setBounds(20, 298, 340, 218);
		
		btnIscrizione = new Button(compositeUserInfo_1, SWT.NONE);
		btnIscrizione.setLocation(62, 15);
		btnIscrizione.setSize(200, 25);
		btnIscrizione.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				sceglieCorsoDialog();
			}
		});
		btnIscrizione.setText("Iscrizione Tesi");
		
		btnRitira = new Button(compositeUserInfo_1, SWT.NONE);
		btnRitira.setLocation(63, 55);
		btnRitira.setSize(200, 25);
		btnRitira.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				creazioneRitiraDomandaDialog();
			}
		});
		btnRitira.setText("Ritira domanda");
		
		btnAddRepo = new Button(compositeUserInfo_1, SWT.NONE);
		btnAddRepo.setLocation(63, 95);
		btnAddRepo.setSize(200, 25);
		btnAddRepo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				addRepoDialog();
			}
		});
		btnAddRepo.setText("Repository tesi");
		
		btnAppello = new Button(compositeUserInfo_1, SWT.NONE);
		btnAppello.setLocation(63, 135);
		btnAppello.setSize(200, 25);
		btnAppello.setText("Visualizza Appello");
		
		Button btnLogOut = new Button(compositeUserInfo_1, SWT.NONE);
		btnLogOut.setLocation(63, 175);
		btnLogOut.setSize(200, 25);
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				studenteShell.close();
				controllerStudente = null;
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});
		btnLogOut.setText("Log out");
		
		aggiornaPagina();
		studenteShell.open();
		studenteShell.layout();

		while (!studenteShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void addRepoDialog() {
		Shell child = new Shell(studenteShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Repository tesi");
		Utils.setShellToCenterParent(child, studenteShell);
		String original = controllerStudente.getRepositoryFromDB();
		
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
					controllerStudente.addRepo(s);
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
		Shell child = new Shell(studenteShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(300, 200);
		child.setText("Domanda Tesi");
		Utils.setShellToCenterParent(child, studenteShell);
		ArrayList<Pair<Integer, String>> corsi = controllerStudente.getCorsiFromDB();
		ArrayList<Pair<Integer, String>> docenti = controllerStudente.getDocentiFromDB();
		
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
					int id_corso = 0;
					int matricola_relatore = 0;
					for(Pair<Integer, String> p : corsi) {
						if(comboCorsi.getText().equals(p.second)) {
							id_corso = p.first;
							break;
						}
					}
					for(Pair<Integer, String> d : docenti) {
						if(comboDocenti.getText().equals(d.second)) {
							matricola_relatore = d.first;
							break;
						}
					}
					if (controllerStudente.iscrizione(id_corso, matricola_relatore))
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
