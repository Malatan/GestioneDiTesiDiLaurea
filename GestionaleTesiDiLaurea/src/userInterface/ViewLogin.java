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
import utils.Utils;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class ViewLogin {
	private ControllerLogin controller;
	private Shell shell;

	public ViewLogin(ControllerLogin cl) {
		this.controller = cl;
		this.shell = new Shell();
	}

	public Shell getShell() {
		return shell;
	}

	public void close() {
		shell.close();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(509, 317);
		shell.setText("Gestionale di tesi di laurea");
		Utils.setShellToCenterMonitor(shell, display);
		
		Label lblTitolo = new Label(shell, SWT.NONE);
		lblTitolo.setAlignment(SWT.CENTER);
		lblTitolo.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.NORMAL));
		lblTitolo.setBounds(0, 10, 493, 40);
		lblTitolo.setText("Gestionale Di Tesi Di Laurea");
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 56, 493, 4);
		
		Text textFieldMatricola = new Text(shell, SWT.BORDER);
		textFieldMatricola.setBounds(141, 90, 227, 21);
		
		Text textFieldPassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		textFieldPassword.setBounds(141, 140, 227, 21);
		
		Button btnLogin = new Button(shell, SWT.NONE);
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				controller.checkLogin(textFieldMatricola.getText(), textFieldPassword.getText());
			}
		});
		btnLogin.setBounds(158, 194, 184, 40);
		btnLogin.setText("Login");

		Label lblMatricola = new Label(shell, SWT.NONE);
		lblMatricola.setBounds(72, 93, 55, 15);
		lblMatricola.setText("Matricola");

		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(72, 143, 55, 15);
		lblPassword.setText("Password");

		textFieldMatricola.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent event) {
		    	  if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
		    		  controller.checkLogin(textFieldMatricola.getText(), textFieldPassword.getText());
		    	  }
		      }
		    });
		
		textFieldPassword.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent event) {
		    	  if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
		    		  controller.checkLogin(textFieldMatricola.getText(), textFieldPassword.getText());
		    	  }
		      }
		    });
		
		textFieldMatricola.setText("10010");
		textFieldPassword.setText("123");
		
		
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
