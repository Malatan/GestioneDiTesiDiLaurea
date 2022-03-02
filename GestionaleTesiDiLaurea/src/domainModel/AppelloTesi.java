package domainModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import utils.Console;
import utils.Pair;

public class AppelloTesi {
	
	private int id;
	private String date;
	private Pair<Integer, String> aula;
	private int startTime;
	private String linkTeleconferenza;
	private String nota;
	
	public AppelloTesi(int idAppello, String data, int startTime, Pair<Integer, String> aula, String linkTeleconferenza, String nota) {
		this.id = idAppello;
		this.date = data;
		this.startTime = startTime;
		this.aula = aula;
		this.linkTeleconferenza = linkTeleconferenza;
		this.nota = nota;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateString() {
		
		Console.print("(getDateString) " + date, date);
		if (date == null)
			return "INDEFINITO";
		else {
		    
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return new String(LocalDate.parse(date, formatter).format(formatter2));
		}
	}
	
	public String getData() {
		return date;
	}

	public void setData(String data) {
		this.date = date;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String informazione) {
		this.nota = informazione;
	}

	public Pair<Integer, String> getAula() {
		return aula;
	}

	public void setAula(Pair<Integer, String> aula) {
		this.aula = aula;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
	public String getStartTimeString() {
		return new String(startTime+":00");
	}

	public String getLinkTeleconferenza() {
		return linkTeleconferenza;
	}

	public void setLinkTeleconferenza(String linkTeleconferenza) {
		this.linkTeleconferenza = linkTeleconferenza;
	}
}
