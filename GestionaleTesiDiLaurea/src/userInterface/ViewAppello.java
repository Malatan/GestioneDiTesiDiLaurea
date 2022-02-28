package userInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import businessLogic.ControllerAppello;
import utils.Utils;

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

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createAndRun() {
		Display display = Display.getDefault();
		appelloShell = new Shell(parentShell, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		appelloShell.setSize(500, 500);
		appelloShell.setText("Appello [" +controllerAppello.getAppello().getId() + "][" 
							+ controllerAppello.getAppello().getData() + "]");
		Utils.setShellToCenterMonitor(appelloShell, display);
		
		
		
		appelloShell.open();
		appelloShell.layout();
		while (!appelloShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
