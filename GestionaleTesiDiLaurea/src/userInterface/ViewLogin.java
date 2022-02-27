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

import businessLogic.ControllerLogin;
import org.eclipse.wb.swt.SWTResourceManager;

public class ViewLogin {
	
	private static Text textFieldMatricola;
	private static Text textFieldPassword;
	private ControllerLogin controllerLogin;
	private Shell loginShell;
	private Label lblMessaggio;
	
	public ViewLogin(ControllerLogin cl) {
		this.controllerLogin = cl;
	}
	
	public void close() {
		loginShell.close();
	}
	
	public void showErrorMessage(String msg) {
		lblMessaggio.setText(msg);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		loginShell = new Shell();
		loginShell.setSize(509, 317);
		loginShell.setText("Gestionale di tesi di laurea");
		
		Button btnLogin = new Button(loginShell, SWT.NONE);
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				controllerLogin.checkLogin(textFieldMatricola, textFieldPassword);
			}
		});
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnLogin.setBounds(158, 194, 184, 40);
		btnLogin.setText("Login");
		
		textFieldMatricola = new Text(loginShell, SWT.BORDER);
		textFieldMatricola.setBounds(141, 90, 227, 21);
		
		textFieldPassword = new Text(loginShell, SWT.BORDER);
		textFieldPassword.setBounds(141, 140, 227, 21);
		
		Label lblMatricola = new Label(loginShell, SWT.NONE);
		lblMatricola.setBounds(72, 93, 55, 15);
		lblMatricola.setText("Matricola");
		
		Label lblPassword = new Label(loginShell, SWT.NONE);
		lblPassword.setBounds(72, 143, 55, 15);
		lblPassword.setText("Password");
		
		lblMessaggio = new Label(loginShell, SWT.NONE);
		lblMessaggio.setAlignment(SWT.CENTER);
		lblMessaggio.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		lblMessaggio.setBounds(72, 240, 347, 15);
		
		Label lblTitolo = new Label(loginShell, SWT.NONE);
		lblTitolo.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.NORMAL));
		lblTitolo.setBounds(72, 26, 347, 40);
		lblTitolo.setText("Gestione Delle Tesi di Laurea");

		loginShell.open();
		loginShell.layout();
		while (!loginShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
