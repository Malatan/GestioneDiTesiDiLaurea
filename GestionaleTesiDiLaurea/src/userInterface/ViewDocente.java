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
import utils.Utils;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class ViewDocente {
	private ControllerDocente controllerDocente;
	private Shell docenteShell;
	
	private ScrolledComposite scrolledCompositeDomandeTesi;
	private Label lblMatricolaLabel;
	private Label lblNomeCognomeLabel;
	private Label lblCorsoLabel;
	private Label lblRepositoryLabel;
	private Label lblDataLabel;
	
	public ViewDocente(ControllerDocente cd) {
		this.controllerDocente = cd;
	}
	
	public Shell getShell() {
		return this.docenteShell;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		docenteShell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		docenteShell.setSize(770, 500);
		docenteShell.setText("Gestionale di tesi di laurea - Docente");
		Utils.setShellToCenterMonitor(docenteShell, display);
		
		Composite compositeUserInfo = new Composite(docenteShell, SWT.BORDER);
		compositeUserInfo.setBounds(20, 22, 711, 80);

		Label lblMatricola = new Label(compositeUserInfo, SWT.NONE);
		lblMatricola.setBounds(10, 10, 200, 15);
		lblMatricola.setText("Matricola: " + controllerDocente.getDocente().getMatricola());

		Label lblNome = new Label(compositeUserInfo, SWT.NONE);
		lblNome.setBounds(10, 31, 200, 15);
		lblNome.setText("Nome: " + controllerDocente.getDocente().getNome());

		Label lblCognome = new Label(compositeUserInfo, SWT.NONE);
		lblCognome.setBounds(10, 52, 200, 15);
		lblCognome.setText("Cognome: " + controllerDocente.getDocente().getCognome());
			
		Composite compositeMenu = new Composite(docenteShell, SWT.BORDER);
		compositeMenu.setBounds(20, 120, 711, 320);
		
		Button btnDomandeTesi = new Button(compositeMenu, SWT.NONE);
		btnDomandeTesi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				scrolledCompositeDomandeTesi = new ScrolledComposite(docenteShell, SWT.BORDER | SWT.V_SCROLL);
				scrolledCompositeDomandeTesi.setBounds(20, 145, 711, 300);
				scrolledCompositeDomandeTesi.setExpandVertical(true);
				Composite compositeDomandeTesi = new Composite(scrolledCompositeDomandeTesi, SWT.NONE);
				compositeDomandeTesi.setBounds(scrolledCompositeDomandeTesi.getBounds());
				
				lblMatricolaLabel = new Label(docenteShell, SWT.NONE);
				lblMatricolaLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
				lblMatricolaLabel.setAlignment(SWT.CENTER);
				lblMatricolaLabel.setBounds(35, 125, 55, 15);
				lblMatricolaLabel.setText("Matricola");
				
				lblNomeCognomeLabel = new Label(docenteShell, SWT.NONE);
				lblNomeCognomeLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
				lblNomeCognomeLabel.setAlignment(SWT.CENTER);
				lblNomeCognomeLabel.setBounds(120, 125, 97, 15);
				lblNomeCognomeLabel.setText("Nome Cognome");
				
				lblCorsoLabel = new Label(docenteShell, SWT.NONE);
				lblCorsoLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
				lblCorsoLabel.setAlignment(SWT.CENTER);
				lblCorsoLabel.setBounds(280, 125, 55, 15);
				lblCorsoLabel.setText("Corso");
				
				lblDataLabel = new Label(docenteShell, SWT.NONE);
				lblDataLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
				lblDataLabel.setAlignment(SWT.CENTER);
				lblDataLabel.setBounds(380, 125, 55, 15);
				lblDataLabel.setText("Data");
				
				lblRepositoryLabel = new Label(docenteShell, SWT.NONE);
				lblRepositoryLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
				lblRepositoryLabel.setAlignment(SWT.CENTER);
				lblRepositoryLabel.setBounds(470, 125, 90, 15);
				lblRepositoryLabel.setText("Repository Tesi");
				visualizzaDomandeTesi(compositeDomandeTesi);
				scrolledCompositeDomandeTesi.setContent(compositeDomandeTesi);
				scrolledCompositeDomandeTesi.setMinSize(compositeDomandeTesi.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		btnDomandeTesi.setBounds(262, 55, 180, 30);
		btnDomandeTesi.setText("Domande Tesi");
		
		Button btnLogOut = new Button(compositeMenu, SWT.NONE);
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				docenteShell.close();
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});
		btnLogOut.setBounds(262, 276, 180, 30);
		btnLogOut.setText("Log Out");
		
		Button btnIndietro = new Button(docenteShell, SWT.NONE);
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(true);
				scrolledCompositeDomandeTesi.dispose();
				lblMatricolaLabel.dispose();
				lblNomeCognomeLabel.dispose();
				lblCorsoLabel.dispose();
				lblRepositoryLabel.dispose();
			}
		});
		btnIndietro.setBounds(656, 121, 75, 25);
		btnIndietro.setText("Indietro");
		
		docenteShell.open();
		docenteShell.layout();

		while (!docenteShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void visualizzaDomandeTesi(Composite c) {
		ArrayList<DomandaTesi> domande = controllerDocente.getDomandeTesiFromDB();
		int offset_y = 10;
		for(DomandaTesi d : domande) {
			Label lblMatricola = new Label(c, SWT.NONE);
			lblMatricola.setAlignment(SWT.CENTER);
			lblMatricola.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblMatricola.setBounds(10, offset_y+3, 60, 25);
			lblMatricola.setText(d.getMatricolaStudente()+"");
			
			Label lblNomeCognome = new Label(c, SWT.NONE);
			lblNomeCognome.setAlignment(SWT.CENTER);
			lblNomeCognome.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblNomeCognome.setBounds(75, offset_y+3, 150, 25);
			lblNomeCognome.setText(d.getNomeCognomeStudente());
			
			Label lblCorso = new Label(c, SWT.NONE);
			lblCorso.setAlignment(SWT.CENTER);
			lblCorso.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblCorso.setBounds(230, offset_y+3, 110, 25);
			lblCorso.setText(d.getNomeCorso());
			
			Label lblData = new Label(c, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(345, offset_y+3, 80, 25);
			lblData.setText(d.getData());
			
			Text textRepository = new Text(c, SWT.NONE | SWT.READ_ONLY);
			textRepository.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			textRepository.setBounds(430, offset_y+3, 130, 25);
			textRepository.setText("www.gooooooooooooooooogle.it");
			
			Button btnApprova = new Button(c, SWT.NONE);
			if (d.isApprovato()) {
				btnApprova.setText("Domanda approvata");
				btnApprova.setEnabled(false);
			}else {
				btnApprova.setText("Approva Domanda");
				btnApprova.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						
					}
				});
			}
			btnApprova.setBounds(570, offset_y, 130, 25);
			
			Label lblSeperator = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 685, 2);
			
			offset_y = offset_y + 35;
		}
	}
}
