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
import businessLogic.ControllerPresidenteScuola;
import domainModel.AppelloTesi;
import domainModel.Verbale;
import system.Messaggio;
import system.MessaggioManager;
import utils.Utils;

public class ViewPresidenteScuola {
	private ControllerPresidenteScuola controller;
	private Shell shell;
	private Composite compositeMenu;
	private Label lbl1;
	private Label lbl2;
	private Label lbl3;
	private ScrolledComposite scrolledCompositeListaAppelli;

	public ViewPresidenteScuola(ControllerPresidenteScuola cps) {
		this.controller = cps;
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
		shell.setSize(500, 508);
		shell.setText("Presidente della Scuola");
		Utils.setShellToCenterMonitor(shell, display);

		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setBounds(20, 22, 441, 80);

		Label lblMatricola = new Label(composite, SWT.NONE);
		lblMatricola.setBounds(10, 10, 340, 15);
		lblMatricola.setText("Matricola: " + controller.getPresidenteScuola().getMatricola());

		Label lblNome = new Label(composite, SWT.NONE);
		lblNome.setBounds(10, 31, 340, 15);
		lblNome.setText("Nome: " + controller.getPresidenteScuola().getNome());

		Label lblCognome = new Label(composite, SWT.NONE);
		lblCognome.setBounds(10, 52, 340, 15);
		lblCognome.setText("Cognome: " + controller.getPresidenteScuola().getCognome());

		compositeMenu = new Composite(shell, SWT.BORDER);
		compositeMenu.setBounds(22, 126, 439, 333);
		
		lbl1 = new Label(shell, SWT.NONE);
		lbl1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lbl1.setAlignment(SWT.CENTER);
		lbl1.setVisible(false);
		
		lbl2 = new Label(shell, SWT.NONE);
		lbl2.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lbl2.setAlignment(SWT.CENTER);
		lbl2.setVisible(false);
		
		lbl3 = new Label(shell, SWT.NONE);
		lbl3.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lbl3.setAlignment(SWT.CENTER);
		lbl3.setVisible(false);
		
		Button btnVisualizzaAppelli = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaAppelli.setLocation(10, 10);
		btnVisualizzaAppelli.setSize(415, 38);
		btnVisualizzaAppelli.setText("Visualizza gli appelli di tesi");
		btnVisualizzaAppelli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				lbl1.setVisible(true);
				lbl2.setVisible(true);
				lbl3.setVisible(true);
				visualizzaListaAppelli();
			}
		});

		Button btnLogOut = new Button(compositeMenu, SWT.NONE);
		btnLogOut.setText("Log out");
		btnLogOut.setBounds(10, 288, 415, 31);
		
		Button btnVisualizzaVerbaliTesi = new Button(compositeMenu, SWT.NONE);
		btnVisualizzaVerbaliTesi.setText("Visualizza i verbali delle sessioni");
		btnVisualizzaVerbaliTesi.setBounds(10, 54, 415, 38);
		
		Button btnMessaggi = new Button(compositeMenu, SWT.NONE);
		btnMessaggi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				visualizzaMessaggi();
			}
		});
		btnMessaggi.setBounds(10, 98, 415, 38);
		btnMessaggi.setText("Messaggi");
		btnVisualizzaVerbaliTesi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(false);
				visualizzaListaVerbali();
			}
		});
		
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.close();
				controller = null;
				ControllerLogin cl = new ControllerLogin();
				cl.run();
			}
		});

		Button btnIndietro = new Button(shell, SWT.NONE);
		btnIndietro.setLocation(385, 125);
		btnIndietro.setSize(75, 25);
		btnIndietro.setText("Indietro");
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				compositeMenu.setVisible(true);
				scrolledCompositeListaAppelli.dispose();
				lbl1.setVisible(false);
				lbl2.setVisible(false);
				lbl3.setVisible(false);
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
		ArrayList<Messaggio> messaggi = MessaggioManager.getInstance(child).getMyMessaggi(controller.getPresidenteScuola());

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
	
	public void visualizzaListaVerbali() {
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		lbl3.setVisible(true);
		lbl1.setBounds(40, 126, 50, 15);
		lbl1.setText("ID");
		lbl2.setBounds(100, 126, 100, 15);
		lbl2.setText("Data");
		lbl3.setBounds(215, 126, 100, 15);
		lbl3.setText("Approvato");
		scrolledCompositeListaAppelli = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeListaAppelli.setBounds(20, 152, 439, 297);
		scrolledCompositeListaAppelli.setExpandVertical(true);
		Composite compositeListaAppelli = new Composite(scrolledCompositeListaAppelli, SWT.NONE);
		compositeListaAppelli.setBounds(scrolledCompositeListaAppelli.getBounds());
		
		ArrayList<Verbale> verbali = controller.getVerbaliFromDB();
		int offset_y = 10;
		for (Verbale v : verbali) {
			Label lblId = new Label(compositeListaAppelli, SWT.NONE);
			lblId.setAlignment(SWT.CENTER);
			lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblId.setBounds(10, offset_y + 3, 60, 25);
			lblId.setText(v.getId() + "");

			Label lblData = new Label(compositeListaAppelli, SWT.NONE);
			lblData.setAlignment(SWT.CENTER);
			lblData.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			lblData.setBounds(90, offset_y + 3, 80, 25);
			lblData.setText(v.getData());
			
			Label lblStatus = new Label(compositeListaAppelli, SWT.NONE);
			lblStatus.setAlignment(SWT.CENTER);
			lblStatus.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			lblStatus.setBounds(175, offset_y + 3, 145, 25);
			lblStatus.setText(v.isApprovato() ? "Si" : "No");
			
			Button btnDettaglio = new Button(compositeListaAppelli, SWT.NONE);
			btnDettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					verbaleView(v);
				}
			});
			btnDettaglio.setBounds(345, offset_y, 75, 25);
			btnDettaglio.setText("Dettagli");

			Label lblSeperator = new Label(compositeListaAppelli, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 420, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
		scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void visualizzaListaAppelli() {
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		lbl3.setVisible(true);
		lbl1.setBounds(40, 126, 50, 15);
		lbl1.setText("ID");
		lbl2.setBounds(100, 126, 100, 15);
		lbl2.setText("Data");
		lbl3.setBounds(215, 126, 100, 15);
		lbl3.setText("Status");
		scrolledCompositeListaAppelli = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		scrolledCompositeListaAppelli.setBounds(20, 152, 439, 297);
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
					ControllerAppello ca = new ControllerAppello(appello, shell,
							Utils.getRuolo(controller.getPresidenteScuola()), 6, controller.getPresidenteScuola());
					ca.run();
				}
			});
			btnDettaglio.setBounds(325, offset_y, 75, 25);
			btnDettaglio.setText("Dettagli");

			Label lblSeperator = new Label(compositeListaAppelli, SWT.SEPARATOR | SWT.HORIZONTAL);
			lblSeperator.setBounds(10, offset_y - 5, 420, 2);

			offset_y = offset_y + 35;
		}
		scrolledCompositeListaAppelli.setContent(compositeListaAppelli);
		scrolledCompositeListaAppelli.setMinSize(compositeListaAppelli.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public void verbaleView(Verbale v) {
		Shell child = new Shell(shell, SWT.APPLICATION_MODAL | SWT.TITLE);
		child.setSize(450, 500);
		child.setText("Visualizzazione verbale");
		Utils.setShellToCenterParent(child, shell);
		
		Label lblTitolo = new Label(child, SWT.CENTER);
		lblTitolo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblTitolo.setBounds(10, 10, 415, 20);
		lblTitolo.setText("Verbale n." + v.getId());
		
		Text textContenuto = new Text(child, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.CANCEL);
		textContenuto.setBounds(10, 36, 415, 385);
		textContenuto.setText(v.getVerbaleContenuto());
		
		Button btnApprova = new Button(child, SWT.NONE);
		btnApprova.setBounds(10, 427, 75, 25);
		btnApprova.setText("Approva");
		btnApprova.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (Utils.createYesNoDialog(child, "Messaggio", "Conferma di approvare il verbale?")) {
					if (controller.approvaVerbale(v)) {
						Utils.createConfirmDialog(child, "Messaggio", "Il verbale e' stato approvato.");
						child.close();
					}
				}
			}
		});
		
		Button btnIndietro = new Button(child, SWT.NONE);
		btnIndietro.setBounds(350, 427, 75, 25);
		btnIndietro.setText("Indietro");
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				child.close();
			}
		});
		
		if(v.isApprovato()) {
			btnApprova.setEnabled(false);
		}
		child.open();
	}
}
