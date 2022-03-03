package userInterface;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import businessLogic.ControllerLogin;
import utils.Console;
import utils.Utils;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class ViewLogin {
	private ControllerLogin controllerLogin;
	private Shell loginShell;
	
	public ViewLogin(ControllerLogin cl) {
		this.controllerLogin = cl;
	}
	
	public Shell getShell() {
		return loginShell;
	}
	
	public void close() {
		loginShell.close();
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		loginShell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);

		loginShell.setSize(509, 317);
		loginShell.setText("Gestionale di tesi di laurea");
		Utils.setShellToCenterMonitor(loginShell, display);
		
		Text textFieldMatricola = new Text(loginShell, SWT.BORDER);
		textFieldMatricola.setBounds(141, 90, 227, 21);
		
		Text textFieldPassword = new Text(loginShell, SWT.BORDER | SWT.PASSWORD);
		textFieldPassword.setBounds(141, 140, 227, 21);
		
		Button btnLogin = new Button(loginShell, SWT.NONE);
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				controllerLogin.checkLogin(textFieldMatricola, textFieldPassword);
			}
		});
		btnLogin.setBounds(158, 194, 184, 40);
		btnLogin.setText("Login");
		
		Label lblMatricola = new Label(loginShell, SWT.NONE);
		lblMatricola.setBounds(72, 93, 55, 15);
		lblMatricola.setText("Matricola");
		
		Label lblPassword = new Label(loginShell, SWT.NONE);
		lblPassword.setBounds(72, 143, 55, 15);
		lblPassword.setText("Password");
		
		Label lblTitolo = new Label(loginShell, SWT.NONE);
		lblTitolo.setAlignment(SWT.CENTER);
		lblTitolo.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.NORMAL));
		lblTitolo.setBounds(0, 10, 493, 40);
		lblTitolo.setText("Gestionale Di Tesi Di Laurea");
		
		textFieldMatricola.setText("10002");
		textFieldPassword.setText("123");
		
		Label label = new Label(loginShell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 56, 493, 4);
		
		//PRESS ENTER TO LOGIN
		textFieldMatricola.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				Console.print(String.valueOf(e.keyCode), "DEBUG");
				if(e.keyCode==SWT.CR || e.keyCode==SWT.KEYPAD_CR) {
					controllerLogin.checkLogin(textFieldMatricola, textFieldPassword);
				}
			}
		});
		
		textFieldPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				Console.print(String.valueOf(e.keyCode), "DEBUG");
				if(e.keyCode==SWT.CR || e.keyCode==SWT.KEYPAD_CR) {
					controllerLogin.checkLogin(textFieldMatricola, textFieldPassword);
				}
			}
		});
		
		loginShell.open();
		loginShell.layout();
		while (!loginShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
