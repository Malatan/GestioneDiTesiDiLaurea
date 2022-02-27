package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console{
	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	public static void print(String msg, String type) {
		Date date = new Date();
		String toprint = "";
		switch(type.toUpperCase()) {
			case "SQL": 
				toprint = "[" + sdf.format(date) + "]" + "[Sql]" + msg;
				break;
			case "APP": 
				toprint = "[" + sdf.format(date) + "]" + "[Application]" + msg;
				break;
			case "GUI": 
				toprint = "[" + sdf.format(date) + "]" + "[Gui]" + msg;
				break;
			case "DB": 
				toprint = "[" + sdf.format(date) + "]" + "[Database]" + msg;
				break;
			case "DEBUG": 
				toprint = "[" + sdf.format(date) + "]" + "[Debug]" + msg;
				break;
			case "EXCEPTION": 
				toprint = "[" + sdf.format(date) + "]" + "[Exception]" + msg;
				break;
			case "ERROR": 
				toprint = "[" + sdf.format(date) + "]" + "[ERROR]" + msg;
				break;
			default:
				toprint = "[" + sdf.format(date) + "]" + "[Default]" + msg;
				break;
		}
		System.out.println(toprint);
	}
}
