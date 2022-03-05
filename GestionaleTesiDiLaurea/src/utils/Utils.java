package utils;

import java.time.LocalDate;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import domainModel.PresidenteCorso;
import domainModel.PresidenteScuola;
import domainModel.Responsabile;
import domainModel.Studente;

public class Utils {
	public static void setShellToCenterParent(Shell sh, Shell parentsh) {
		Rectangle bounds = parentsh.getBounds();
		Rectangle rect = sh.getBounds();
		sh.setLocation(bounds.x + (bounds.width - rect.width) / 2, bounds.y + (bounds.height - rect.height) / 2);
	}
	
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
	
	public static void createWarningDialog(Shell s, String title, String text) {
		MessageBox messageBox = new MessageBox(s, SWT.ICON_WARNING | SWT.YES);
		messageBox.setMessage(text);
		messageBox.setText(title);
		messageBox.open();
	}
	
	public static void createCancelDialog(Shell s, String title, String text) {
		MessageBox messageBox = new MessageBox(s, SWT.ICON_CANCEL | SWT.YES);
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
	
	public static String getTodayDate() {
		LocalDate today = LocalDate.now();
		String data = today.getYear() + "-" + today.getMonthValue() + "-" + today.getDayOfMonth();
		return data;
	}
	
	public static String hhmmssTimeFormat(int h, int m, int s) {
		String output = "";
		if(h<10) {
			output += "0" + h;
		} else {
			output += h;
		}
		output += ":";
		if(m<10) {
			output += "0" + m;
		} else {
			output += m;
		}
		output += ":";
		if(s<10) {
			output += "0" + s;
		} else {
			output += s;
		}
		return output;
	}
	
	public static String yymmddTimeFormat(int y, int m, int d) {
		String output = y + "-";
		if(m<10) {
			output += "0" + m;
		} else {
			output += m;
		}
		output += "-";
		if(d<10) {
			output += "0" + d;
		} else {
			output += d;
		}
		return output;
	}
	
	public static int getRuolo(Object o) {
		if (o instanceof Studente) {
			return 0;
		} else if (o instanceof Responsabile) {
			return 1;
		} else if (o instanceof PresidenteScuola) {
			return 2;
		} else if (o instanceof PresidenteCorso) {
			return 3;
		}
		return -1;
	}
	
	public static String dateSubtractDays(String data, int minus_day) {
		//yy-mm-dd
		String[] parseData = data.split("-");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, Integer.parseInt(parseData[1]));
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parseData[2]));
		c.set(Calendar.YEAR, Integer.parseInt(parseData[0]));
		
		c.add(Calendar.DAY_OF_MONTH, -minus_day);
		return yymmddTimeFormat(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String getAppelloRuoloByInt(int ruolo) {
		switch(ruolo) {
			case 0:
				return "Studente";
			case 1:
				return "Membro Commissione";
			case 2:
				return "Relatore";
			case 3:
				return "Presidente Commissione";
			default:
				return "error";
		}
	}
}
