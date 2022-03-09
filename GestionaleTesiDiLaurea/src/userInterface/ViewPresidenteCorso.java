package userInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import businessLogic.ControllerAppello;
import businessLogic.ControllerLogin;
import businessLogic.ControllerPresidenteCorso;
import domainModel.AppelloTesi;
import system.Messaggio;
import system.MessaggioManager;
import utils.Utils;

public class ViewPresidenteCorso {
	private ControllerPresidenteCorso controller;
	private Shell shell;
	private Composite compositeMenu;
	private Label lblId;
	private Label lblData;
	private Label lblStatus;
	private ScrolledComposite scrolledCompositeListaAppelli;

	public ViewPresidenteCorso(ControllerPresidenteCorso cpc) {
		this.controller = cpc;
	}

	public Shell getShell() {
		return shell;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(480, 500);
		shell.setText("Presidente del Corso di " + controller.getPresidenteCorso().getCorso().second);
		Utils.setShellToCenterMonitor(shell, display);

		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setBounds(21, 22, 420, 80);

		Label lblMatricola = new Label(composite, SWT.NONE);
		lblMatricola.setBounds(10, 10, 320, 15);
		lblMatricola.setText("Matricola: " + controller.getPresidenteCorso().getMatricola());

		Label lblNome = new Label(composite, SWT.NONE);
		lblNome.setBounds(10, 31, 320, 15);
		lblNome.setText("Nome: " + controller.getPresidenteCorso().getNome());

		Label lblCognome = new Label(composite, SWT.NONE);
		lblCognome.setBounds(10, 52, 320, 15);
		lblCognome.setText("Cognome: " + controller.getPresidenteCorso().getCognome());

		compositeMenu = new Composite(shell, SWT.BORDER);
		compositeMenu.setBounds(20, 125, 421, 326);
		
		lblId = new Label(shell, SWT.NONE);
		lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblId.setAlignment(SWT.CENTER);
		lblId.setBounds(40, 126, 50, 15);
		lblId.setText("ID");
		lblId.setVisible(false);
		
		lblData = new Label(shell, SWT.NONE);
		lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblData.setAlignment(SWT.CENTER);
		lblData.setBounds(100, 126, 100, 15);
		lblData.setText("Data");
		lblData.setVisible(false);
		
		lblStatus = new Label(shell, SWT.NONE);
		lblStatus.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblStatus.setAlignment(SWT.CENTER);
		lblStatus.setBounds(215, 126, 100, 15);
		lblStatus.setText("Status");
		lblStatus.setVisible(false);
		
		Button btnVisualizzaAppelli = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaAppelli.setLocation(10, 10);
		btnVisualizzaAppelli.setSize(397, 44);
		btnVisualizzaAppelli.setText("Visualizza gli appelli di tesi\r\n\r\n");
		btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				lblId.setVisible(true);
				lblData.setVisible(true);
				lblStatus.setVisible(true);
				visualizzaListaAppelli();
			}
		});

		Button btnLogout = new Button(compositeMenu, SWT.NONE);
		btnLogout.setText("Log out");
		btnLogout.setBounds(10, 276, 397, 31);
		
		Button btnMessaggi = new Button(compositeMenu, SWT.NONE);
		btnMessaggi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				visualizzaMessaggi();
			}
		});
		btnMessaggi.setBounds(10, 60, 397, 44);
		btnMessaggi.setText("Messaggi");

		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.close();
				controller = null;
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});

		Button btnIndietro = new Button(shell, SWT.NONE);
		btnIndietro.setLocation(365, 125);
		btnIndietro.setSize(75, 25);
		btnIndietro.setText("Indietro");

		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(true);
				scrolledCompositeListaAppelli.dispose();
				lblId.setVisible(false);
				lblData.setVisible(false);
				lblStatus.setVisible(false);
			}
		});

		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	public void visualizzaMessaggi() {
		Shell child = new Shell(shell, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.APPLICATION_MODAL);
		child.setText("Messaggi");
		child.setSize(500, 450);
		Utils.setShellToCenterParent(child, shell);
		int[] ordine = new int[1];
		ordine[0] = 0;
		ArrayList<Messaggio> messaggi = MessaggioManager.getInstance(child).getMyMessaggi(controller.getPresidenteCorso());

		Label lblTitolo = new Label(child, SWT.NONE);
		lblTitolo.setAlignment(SWT.CENTER);
		lblTitolo.setBounds(10, 10, 465, 15);
		lblTitolo.setText("Lista messaggi");

		Text textMessaggio = new Text(child, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		textMessaggio.setBounds(10, 31, 465, 340);
		textMessaggio.setVisible(false);

		List listMessaggi = new List(child, SWT.BORDER | SWT.V_SCROLL);
		Object layout = listMessaggi.getLayoutData();
		listMessaggi.setBounds(10, 31, 465, 340);
		for (int i = 0; i < messaggi.size(); i++) {
			listMessaggi.add(messaggi.get(i).display());
			listMessaggi.setData(i + "", messaggi.get(i).getId());

		}

		Button btnOrdinaLetto = new Button(child, SWT.NONE);
		btnOrdinaLetto.setBounds(10, 377, 150, 25);
		btnOrdinaLetto.setText("Ordina per letto/nuovo");
		btnOrdinaLetto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (!messaggi.isEmpty() && ordine[0] != 1) {
					Utils.changeIntValue(ordine, 1);
					Collections.sort(messaggi, new Comparator<Messaggio>() {
						@Override
						public int compare(Messaggio m1, Messaggio m2) {
							return m1.isLettoString().compareTo(m2.isLettoString());
						}
					});
					listMessaggi.removeAll();
					listMessaggi.setLayoutData(layout);
					listMessaggi.setBounds(10, 31, 465, 340);
					for (int i = 0; i < messaggi.size(); i++) {
						listMessaggi.add(messaggi.get(i).display());
						listMessaggi.setData(i + "", messaggi.get(i).getId());

					}
				}
			}
		});

		Button btnOrdinaData = new Button(child, SWT.NONE);
		btnOrdinaData.setBounds(166, 377, 150, 25);
		btnOrdinaData.setText("Ordina per data");
		btnOrdinaData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (!messaggi.isEmpty() && ordine[0] != 2) {
					Utils.changeIntValue(ordine, 2);
					Collections.sort(messaggi, new Comparator<Messaggio>() {
						@Override
						public int compare(Messaggio m1, Messaggio m2) {
							try {
								Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(m1.getDataEmissione());
								Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(m2.getDataEmissione()); 
								return date1.compareTo(date2);
							} catch (ParseException e) {
								e.printStackTrace();
							} 
							return 0;
						}
					});
					listMessaggi.removeAll();
					listMessaggi.setLayoutData(layout);
					listMessaggi.setBounds(10, 31, 465, 340);
					for (int i = 0; i < messaggi.size(); i++) {
						listMessaggi.add(messaggi.get(i).display());
						listMessaggi.setData(i + "", messaggi.get(i).getId());

					}
				}
			}
		});

		Button btnIndietro = new Button(child, SWT.NONE);
		btnIndietro.setBounds(400, 377, 75, 25);
		btnIndietro.setText("Indietro");
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (textMessaggio.isVisible()) {
					lblTitolo.setText("Lista messaggi");
					textMessaggio.setVisible(false);
					listMessaggi.setVisible(true);
					btnOrdinaLetto.setVisible(true);
					btnOrdinaData.setVisible(true);
				} else {
					child.close();
				}
			}
		});

		listMessaggi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				textMessaggio.setVisible(true);
				listMessaggi.setVisible(false);
				for (int i = 0; i < messaggi.size(); i++) {
					int id = (int) listMessaggi.getData(listMessaggi.getSelectionIndex() + "");
					if (messaggi.get(i).getId() == id) {
						btnOrdinaLetto.setVisible(false);
						btnOrdinaData.setVisible(false);
						textMessaggio.setText(messaggi.get(i).getContenuto());
						lblTitolo.setText(messaggi.get(i).getId() + "-" + messaggi.get(i).getTitolo() 
								+ "-" + messaggi.get(i).getDataEmissione());
						if (!messaggi.get(i).isLetto()) {
							messaggi.get(i).setLetto(true);
							listMessaggi.setItem(listMessaggi.getSelectionIndex(), messaggi.get(i).display());
							MessaggioManager.getInstance(child).updateLetto(id);
						}
						break;
					}
				}
			}
		});
		child.open();
	}
	
	public void visualizzaListaAppelli() {
		scrolledCompositeListaAppelli = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeListaAppelli.setBounds(20, 152, 421, 297);
		scrolledCompositeListaAppelli.setExpandVertical(true);
		Composite compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
		compositeListaAppelli.setBounds(scrolledCompositeListaAppelli.getBounds());
		ArrayList<AppelloTesi> appelli = controller.getAppelliFromDB();
		int offset_y = 10;
		for (AppelloTesi a : appelli) {
			Label lblId = new Label(compositeListaAppelli, SWT.NONE);
			lblId.setAlignment(SWT.CENTER);
			lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblId.setBounds(10, offset_y + 3, 60, 25);
			lblId.setText(a.getId() + "");

			Label lblData = new Label(compositeListaAppelli, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(90, offset_y + 3, 80, 25);
			lblData.setText(a.getDateString());
			
			Label lblStatus = new Label(compositeListaAppelli, SWT.NONE);
			lblStatus.setAlignment(SWT.CENTER);
			lblStatus.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			lblStatus.setBounds(175, offset_y + 3, 145, 25);
			lblStatus.setText(a.getStatusString());
			
			Button btnDettaglio = new Button(compositeListaAppelli, SWT.NONE);
			btnDettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					AppelloTesi appello = controller.getAppelloFromDB(a.getId());
					ControllerAppello ca = new ControllerAppello(appello, shell, Utils.getRuolo(controller.getPresidenteCorso()), 
											5, controller.getPresidenteCorso());
					ca.run();
				}
			});
			btnDettaglio.setBounds(335, offset_y, 75, 25);
			btnDettaglio.setText("Dettagli");
			Label lblSeperator = new Label(compositeListaAppelli, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 400, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
		scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}
