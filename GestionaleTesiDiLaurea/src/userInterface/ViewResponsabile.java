package userInterface;

import java.time.LocalDate;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
import businessLogic.ControllerLogin;
import businessLogic.ControllerResponsabile;
import domainModel.AppelloTesi;
import domainModel.Aula;
import utils.Pair;
import utils.Utils;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.ScrolledComposite;

public class ViewResponsabile {
	private ControllerResponsabile controllerResponsabile;
	private Shell responsabileShell;
	
	private ScrolledComposite scrolledCompositeListaAppelli;
	private Label lblId;
	private Label lblData;
	private Label lblCorso;
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
		responsabileShell.setSize(500, 500);
		responsabileShell.setText("Responsabile");
		Utils.setShellToCenterMonitor(responsabileShell, display);

		Composite compositeUserInfo = new Composite(responsabileShell, SWT.BORDER);
		compositeUserInfo.setBounds(20, 22, 441, 80);

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
		compositeMenu.setBounds(20, 127, 441, 322);


		Button btnCreaAppello = new Button(compositeMenu, SWT.NONE);
		btnCreaAppello.setBounds(120, 79, 200, 30);
		btnCreaAppello.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				creazioneAppelloDialog();
			}
		});
		btnCreaAppello.setText("Pubblica appello");
		
		Button btnVisualizzaAppelli = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaAppelli.setBounds(120, 115, 200, 30);
		btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				scrolledCompositeListaAppelli = new ScrolledComposite(responsabileShell, SWT.BORDER | SWT.V_SCROLL);
				scrolledCompositeListaAppelli.setBounds(20, 152, 441, 297);
				scrolledCompositeListaAppelli.setExpandVertical(true);
				Composite compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
				compositeListaAppelli.setBounds(scrolledCompositeListaAppelli.getBounds());
				
				lblId = new Label(responsabileShell, SWT.NONE);
				lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
				lblId.setAlignment(SWT.CENTER);
				lblId.setBounds(40, 126, 50, 15);
				lblId.setText("ID");
				
				lblData = new Label(responsabileShell, SWT.NONE);
				lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
				lblData.setAlignment(SWT.CENTER);
				lblData.setBounds(100, 126, 100, 15);
				lblData.setText("Data");
				
				lblCorso = new Label(responsabileShell, SWT.NONE);
				lblCorso.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
				lblCorso.setAlignment(SWT.CENTER);
				lblCorso.setBounds(225, 126, 100, 15);
				lblCorso.setText("Corso");
				visualizzaListaAppelli(compositeListaAppelli);
				scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
				scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		btnVisualizzaAppelli.setText("Lista appelli");
		
		Button btnLogOut = new Button(compositeMenu, SWT.NONE);
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				responsabileShell.close();
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});
		btnLogOut.setBounds(120, 278, 200, 30);
		btnLogOut.setText("Log out");
		
		Button btnIndietro = new Button(responsabileShell, SWT.NONE);
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(true);
				scrolledCompositeListaAppelli.dispose();
				lblId.dispose();
				lblData.dispose();
			}
		});
		btnIndietro.setBounds(401, 127, 60, 25);
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
		child.setSize(300, 150);
		child.setText("Domanda Tesi");
		Utils.setShellToCenterParent(child, responsabileShell);
		ArrayList<Pair<Integer, String>> corsi = controllerResponsabile.getCorsiFromDB();
		
		Label lblCorsoLabel = new Label(child, SWT.NONE);
		lblCorsoLabel.setBounds(30, 28, 45, 15);
		lblCorsoLabel.setText("Corso:");
		
		Combo comboCorsi = new Combo(child, SWT.READ_ONLY);
		comboCorsi.setBounds(92, 25, 160, 23);
		for(int i = 0 ; i < corsi.size() ; i++) {
			comboCorsi.add(corsi.get(i).second);
		}
		
		Button btnYes = new Button(child, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (comboCorsi.getSelectionIndex() != -1) {
					int id_corso = 0;
					for(Pair<Integer, String> p : corsi) {
						if(comboCorsi.getText().equals(p.second)) {
							id_corso = p.first;
							break;
						}
					}
					controllerResponsabile.creaAppello(id_corso);
					child.close();
				} else {
					Utils.createWarningDialog(child, "Messaggio", "Completa i campi vuoti!");
				}
			}
		});
		btnYes.setBounds(30, 65, 75, 25);
		btnYes.setText("Conferma");
	
 		Button btnNo = new Button(child, SWT.NONE);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				child.close();
			}
		});
		btnNo.setBounds(176, 65, 75, 25);
		btnNo.setText("Indietro");
		child.open();
	}
	
	public void visualizzaListaAppelli(Composite c) {
		ArrayList<AppelloTesi> appelli = controllerResponsabile.getAppelliFromDB();
		int offset_y = 10;
		for(AppelloTesi a : appelli) {
			Label lblId = new Label(c, SWT.NONE);
			lblId.setAlignment(SWT.CENTER);
			lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblId.setBounds(10, offset_y+3, 60, 25);
			lblId.setText(a.getId()+"");
			
			Label lblData = new Label(c, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(90, offset_y+3, 80, 25);
			lblData.setText(a.getDateString());
			
			Label lblCorso = new Label(c, SWT.NONE);
			lblCorso.setAlignment(SWT.CENTER);
			lblCorso.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblCorso.setBounds(213, offset_y+3, 80, 25);
			lblCorso.setText(a.getCorso().second);
			
			Button btnDettaglio = new Button(c, SWT.NONE);
			btnDettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					AppelloTesi appello = controllerResponsabile.getAppelloFromDB(a.getId());
					ControllerAppello ca = new ControllerAppello(appello, responsabileShell, 
							controllerResponsabile.getResponsabile().getMatricola(),
							Utils.getRuolo(controllerResponsabile.getResponsabile()));
					ca.run();
				}
			});
			btnDettaglio.setBounds(350, offset_y, 75, 25);
			btnDettaglio.setText("Dettaglio");
			
			Label lblSeperator = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 415, 2);
			
			offset_y = offset_y + 35;
		}
	}
	
}
