package domainModel;

import utils.Pair;

public class AppelloTesi {
	
	private int id;
	private String data;
	private Pair<Integer, String> aula;
	private int startTime;
	private String linkTeleconferenza;
	private String nota;
	
	public AppelloTesi(int idAppello, String data, int startTime, Pair<Integer, String> aula, String linkTeleconferenza, String nota) {
		this.id = idAppello;
		this.data = data;
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

	public String getDataString() {
		if (data == null)
			return "INDEFINITO";
		else
			return new String(data);
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
