package userInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import businessLogic.ControllerAppello;
import utils.Pair;
import utils.Utils;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DateTime;

public class ViewAppello {
	private ControllerAppello controllerAppello;
	private Shell parentShell;
	private Shell appelloShell;

	public ViewAppello(Shell parent, ControllerAppello cr) {
		this.controllerAppello = cr;
		this.parentShell = parent;
	}

	public Shell getShell() {
		return appelloShell;
	}
	
	public void aggiornaPagina() {
		
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

		Label lblData = new Label(composite, SWT.NONE);
		lblData.setBounds(10, 10, 35, 15);
		lblData.setText("Data:");

		Label lblDataValue = new Label(composite, SWT.NONE);
		lblDataValue.setAlignment(SWT.CENTER);
		lblDataValue.setBounds(51, 10, 80, 15);
		lblDataValue.setText("-");

		Label lblOreLabel = new Label(composite, SWT.NONE);
		lblOreLabel.setBounds(10, 31, 35, 15);
		lblOreLabel.setText("Ore:");

		Label lblOre = new Label(composite, SWT.NONE);
		lblOre.setAlignment(SWT.CENTER);
		lblOre.setBounds(51, 31, 80, 15);
		lblOre.setText("-");

		Label lblAulaLabel = new Label(composite, SWT.NONE);
		lblAulaLabel.setBounds(10, 52, 35, 15);
		lblAulaLabel.setText("Aula:");

		Label lblAula = new Label(composite, SWT.NONE);
		lblAula.setAlignment(SWT.CENTER);
		lblAula.setBounds(51, 52, 80, 15);
		lblAula.setText("-");

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		Composite compositeResponsabile = new Composite(appelloShell, SWT.BORDER);
		compositeResponsabile.setBounds(10, 370, 465, 80);

		Button btnPrenotaAula = new Button(compositeResponsabile, SWT.NONE);
		btnPrenotaAula.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				dataAulaDialog();
			}
		});
		btnPrenotaAula.setBounds(10, 10, 150, 25);
		btnPrenotaAula.setText("Prenota Aula");

		appelloShell.open();
		appelloShell.layout();
		while (!appelloShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
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
					String data = dateTimeAula.getYear() + "-" + (dateTimeAula.getMonth() + 1) + "-" + dateTimeAula.getDay();
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
}
