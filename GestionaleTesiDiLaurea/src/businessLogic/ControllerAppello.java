package businessLogic;

import org.eclipse.swt.widgets.Shell;

import domainModel.AppelloTesi;
import userInterface.ViewAppello;

public class ControllerAppello {
	public AppelloTesi appello;
	private ViewAppello viewAppello;

	public ControllerAppello(AppelloTesi appello, Shell parent) {
		this.appello = appello;
		viewAppello = new ViewAppello(parent, this);
	}
	
	public AppelloTesi getAppello() {
		return this.appello;
	}
	
	public void run() {
		viewAppello.createAndRun();
	}
}
