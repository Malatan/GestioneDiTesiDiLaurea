package userInterface;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

import businessLogic.ControllerLogin;
import businessLogic.ControllerStudente;

public class ViewStudente {
	private ControllerStudente controllerStudente;
	private Shell studenteShell;

	public ViewStudente(ControllerStudente cs) {
		this.controllerStudente = cs;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		studenteShell = new Shell();
		studenteShell.setSize(760, 523);
		studenteShell.setText("Gestionale di tesi di laurea - Studente");

		Composite compositeUserInfo = new Composite(studenteShell, SWT.BORDER);
		compositeUserInfo.setBounds(20, 22, 281, 80);

		Label lblMatricola = new Label(compositeUserInfo, SWT.NONE);
		lblMatricola.setBounds(10, 10, 55, 15);
		lblMatricola.setText("Matricola:        ");

		Label lblNome = new Label(compositeUserInfo, SWT.NONE);
		lblNome.setBounds(10, 31, 55, 15);
		lblNome.setText("Nome:");

		Label lblCognome = new Label(compositeUserInfo, SWT.NONE);
		lblCognome.setBounds(10, 52, 55, 15);
		lblCognome.setText("Cognome:");

		Label lblStatusIscrizione = new Label(studenteShell, SWT.BORDER | SWT.WRAP);
		lblStatusIscrizione.setBounds(358, 22, 353, 80);
		lblStatusIscrizione.setText(" Non sei iscritto a nessun appello");

		Button btnIscrizione = new Button(studenteShell, SWT.NONE);
		btnIscrizione.setBounds(276, 182, 158, 69);
		btnIscrizione.setText("ISCRIZIONE");

		Button btnVisualizzaAppello = new Button(studenteShell, SWT.NONE);
		btnVisualizzaAppello.setBounds(276, 300, 158, 80);
		btnVisualizzaAppello.setText("VISUALIZZA APPELLO");

		studenteShell.open();
		studenteShell.layout();

		while (!studenteShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}
}
