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

public class ViewLogin {
	
	private static Text matricola;
	private static Text password;
	private ControllerLogin controllerLogin;
	private Shell loginShell;
	
	public ViewLogin(ControllerLogin cl) {
		this.controllerLogin = cl;
	}
	
	public void Close() {
		loginShell.close();
	}
	
	public void ShowLogin() {
		Display display = Display.getDefault();
		loginShell = new Shell();
		loginShell.setSize(509, 317);
		loginShell.setText("Gestionale di tesi di laurea");
		
		Button btnNewButton = new Button(loginShell, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				controllerLogin.CheckLogin(matricola, password);
			}
		});
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setBounds(158, 163, 184, 40);
		btnNewButton.setText("Login");
		
		matricola = new Text(loginShell, SWT.BORDER);
		matricola.setBounds(141, 68, 227, 21);
		
		password = new Text(loginShell, SWT.BORDER);
		password.setBounds(141, 111, 227, 21);
		
		Label lblMatricola = new Label(loginShell, SWT.NONE);
		lblMatricola.setBounds(72, 68, 55, 15);
		lblMatricola.setText("Matricola");
		
		Label lblPassword = new Label(loginShell, SWT.NONE);
		lblPassword.setBounds(72, 111, 55, 15);
		lblPassword.setText("Password");

		loginShell.open();
		loginShell.layout();
		while (!loginShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
