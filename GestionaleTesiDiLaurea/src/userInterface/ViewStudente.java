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
	private int idCorso;
	
	private Label lblStatusTesi;
	private Button btnIscrizione;
	private Button btnRitira;
	private Button btnCaricaFile;
	private Button btnAppello;
	public ViewStudente(ControllerStudente cs) {
		this.controllerStudente = cs;
	}
	
	public Shell getShell() {
		return this.studenteShell;
	}
	
	public void aggiornaPagina() {
		lblStatusTesi.setText(controllerStudente.getStatusTesi());
		Studente studente = controllerStudente.getStudente();
		switch(studente.getStatusTesi()) {
			case 0:
				btnIscrizione.setEnabled(true);
				btnRitira.setEnabled(false);
				btnCaricaFile.setEnabled(false);
				btnAppello.setEnabled(false);
				break;
			case 1:
				btnIscrizione.setEnabled(false);
				btnRitira.setEnabled(true);
				btnCaricaFile.setEnabled(true);
			case 2:	
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Studente studente = controllerStudente.getStudente();
		Display display = Display.getDefault();
		studenteShell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		studenteShell.setSize(400, 450);
		studenteShell.setText("Gestionale di tesi di laurea - Studente");
		Utils.setShellToCenterMonitor(studenteShell, display);
		
		Composite compositeUserInfo = new Composite(studenteShell, SWT.BORDER);
		compositeUserInfo.setBounds(20, 22, 340, 80);

		Label lblMatricola = new Label(compositeUserInfo, SWT.NONE);
		lblMatricola.setBounds(10, 10, 200, 15);
		lblMatricola.setText("Matricola: " + studente.getMatricola());

		Label lblNome = new Label(compositeUserInfo, SWT.NONE);
		lblNome.setBounds(10, 31, 200, 15);
		lblNome.setText("Nome: " + studente.getNome());

		Label lblCognome = new Label(compositeUserInfo, SWT.NONE);
		lblCognome.setBounds(10, 52, 200, 15);
		lblCognome.setText("Cognome: " + studente.getCognome());
		
		lblStatusTesi = new Label(studenteShell, SWT.BORDER | SWT.WRAP);
		lblStatusTesi.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblStatusTesi.setBounds(20, 121, 340, 100);
		
		btnIscrizione = new Button(studenteShell, SWT.NONE);
		btnIscrizione.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				sceglieCorsoDialog();
			}
		});
		btnIscrizione.setBounds(90, 240, 200, 25);
		btnIscrizione.setText("Iscrizione Tesi");
		
		btnRitira = new Button(studenteShell, SWT.NONE);
		btnRitira.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				controllerStudente.ritiraDomanda();
				aggiornaPagina();
			}
		});
		btnRitira.setBounds(90, 270, 200, 25);
		btnRitira.setText("Ritira domanda");
		
		btnCaricaFile = new Button(studenteShell, SWT.NONE);
		btnCaricaFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				caricaFileDialog();
			}
		});
		btnCaricaFile.setBounds(90, 301, 200, 25);
		btnCaricaFile.setText("Carica file tesi");
		
		btnAppello = new Button(studenteShell, SWT.NONE);
		btnAppello.setBounds(90, 332, 200, 25);
		btnAppello.setText("Visualizza Appello");
		
		Button btnLogOut = new Button(studenteShell, SWT.NONE);
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				studenteShell.close();
				controllerStudente = null;
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});
		btnLogOut.setBounds(90, 363, 200, 25);
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
	
	public void caricaFileDialog() {
		Shell child = new Shell(studenteShell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(250, 150);
		child.setText("Carica file tesi");
		Utils.setShellToCenterParent(child, studenteShell);
		String original = controllerStudente.getFileCaricatoFromDB();
		
		Text file = new Text(child, SWT.BORDER);
		file.setBounds(30, 20, 171, 23);
		file.setText(original);
		
		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String s = file.getText();
				if (s.equals("")) {
					Utils.createWarningDialog(child, "Messaggio", "Non puo' essere vuoto");
				} else if (!s.equals(original)){
					controllerStudente.caricaFile(s);
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
		child.setSize(250, 150);
		child.setText("Creazione appello");
		Utils.setShellToCenterParent(child, studenteShell);
		ArrayList<Pair<Integer, String>> corsi = controllerStudente.getCorsiFromDB();
		
		Combo comboCorsi = new Combo(child, SWT.READ_ONLY);
		comboCorsi.setBounds(30, 20, 171, 23);
		for(int i = 0 ; i < corsi.size() ; i++) {
			comboCorsi.add(corsi.get(i).second);
		}
		
		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (comboCorsi.getSelectionIndex() != -1) {
					for(Pair<Integer, String> p : corsi) {
						if(comboCorsi.getText().equals(p.second)) {
							idCorso = p.first;
							break;
						}
					}
					if (controllerStudente.iscrizione(idCorso))
						aggiornaPagina();
					child.close();
				} else {
					Utils.createWarningDialog(child, "Messaggio", "Seleziona un corso!");
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
}
