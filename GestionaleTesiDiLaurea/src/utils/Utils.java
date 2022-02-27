package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class Utils {

	public static void setShellToCenterMonitor(Shell sh, Display d) {
		Monitor primary = d.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = sh.getBounds();
		sh.setLocation((bounds.width - rect.width) / 2, (bounds.height - rect.height) / 2);
	}

	public static void createConfirmDialog(Shell s, String title, String text) {
		MessageBox messageBox = new MessageBox(s, SWT.ICON_INFORMATION | SWT.YES);
		messageBox.setMessage(text);
		messageBox.setText(title);
		messageBox.open();
	}
	
	public static void createErrorDialog(Shell s, String title, String text) {
		MessageBox messageBox = new MessageBox(s, SWT.ICON_ERROR | SWT.YES);
		messageBox.setMessage(text);
		messageBox.setText(title);
		messageBox.open();
	}
	
	public static Boolean createYesNoDialog(Shell s, String title, String text) {
		MessageBox messageBox = new MessageBox(s, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		messageBox.setMessage(text);
		messageBox.setText(title);
		int response = messageBox.open();
        if (response == SWT.YES)
        	return true;
        else
        	return false;
	}
}
