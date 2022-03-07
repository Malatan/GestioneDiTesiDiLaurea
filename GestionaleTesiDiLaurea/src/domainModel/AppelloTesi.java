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
	private Pair<Integer, String> corso;
	private Pair<Integer, String> aula;
	private String startTime;
	private String linkTeleconferenza;
	private String nota;
	int status;
	
	public AppelloTesi(int idAppello, Pair<Integer, String> corso, String data, String startTime, 
			Pair<Integer, String> aula, String linkTeleconferenza, String nota, int status) {
		this.id = idAppello;
		this.corso = corso;
		this.date = data;
		this.startTime = startTime;
		this.aula = aula;
		this.linkTeleconferenza = linkTeleconferenza;
		this.nota = nota;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateString() {
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
		if (aula == null || aula.second == null || aula.first == null)
			return Pair.of(null, "INDEFINITO");
		return aula;
	}

	public void setAula(Pair<Integer, String> aula) {
		this.aula = aula;
	}

	public String getStartTime() {
		return getStartTimeString();
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getStartTimeString() {
		if(startTime != null) {
			return new String(startTime);
		}else {
			return new String("");
		}
	}

	public String getLinkTeleconferenza() {
		return linkTeleconferenza;
	}

	public void setLinkTeleconferenza(String linkTeleconferenza) {
		this.linkTeleconferenza = linkTeleconferenza;
	}

	public Pair<Integer, String> getCorso() {
		return corso;
	}

	public void setCorso(Pair<Integer, String> corso) {
		this.corso = corso;
	}
	
	public String getAulaOreString() {
		if (aula == null || aula.second == null || aula.first == null)
			return "INDEFINITO";
		String s = aula.second + "(" + startTime + ")";
		return s;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int new_status) {
		this.status = new_status;
	}
	
	public static String getStatusString(int status) {
		if (status == 1) {
			return "Approvato";
		} else if (status == 2) {
			return "Correzione dell'appello";
		} else {
			return "In Revisione";
		}
	}
	
	public String getStatusString() {
		if (status == 1) {
			return "Approvato";
		} else if (status == 2) {
			return "Correzione dell'appello";
		} else if (status == 3) {
			return "Completato";
		} else if (status == 4) {
			return "Verbalizzato";
		} else if (status == 0){
			return "In Revisione";
		} else {
			return "Error";
		}
	}
}
