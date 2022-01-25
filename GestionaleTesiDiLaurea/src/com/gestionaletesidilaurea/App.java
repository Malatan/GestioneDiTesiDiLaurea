package com.gestionaletesidilaurea;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

public class App {
	private static Text text;
	private static Text text_1;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shlGestionaleDiTesi = new Shell();
		shlGestionaleDiTesi.setSize(509, 317);
		shlGestionaleDiTesi.setText("Gestionale di tesi di laurea");
		
		Button btnNewButton = new Button(shlGestionaleDiTesi, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setBounds(158, 163, 184, 40);
		btnNewButton.setText("Login");
		
		text = new Text(shlGestionaleDiTesi, SWT.BORDER);
		text.setBounds(141, 68, 227, 21);
		
		text_1 = new Text(shlGestionaleDiTesi, SWT.BORDER);
		text_1.setBounds(141, 111, 227, 21);
		
		Label lblMatricola = new Label(shlGestionaleDiTesi, SWT.NONE);
		lblMatricola.setBounds(72, 68, 55, 15);
		lblMatricola.setText("Matricola");
		
		Label lblPassword = new Label(shlGestionaleDiTesi, SWT.NONE);
		lblPassword.setBounds(72, 111, 55, 15);
		lblPassword.setText("Password");

		shlGestionaleDiTesi.open();
		shlGestionaleDiTesi.layout();
		while (!shlGestionaleDiTesi.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
