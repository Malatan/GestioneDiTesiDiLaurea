package domainModel;

public class AppelloTesi {
	
	private int id;
	private String data;
	private String informazione;
	private boolean iscritto;
	
	public AppelloTesi(int idAppello, String d, String informazioni) {
		this.id = idAppello;
		this.data = d;
		this.informazione = informazioni;
		this.iscritto = false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return new String(data);
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getInformazione() {
		return informazione;
	}

	public void setInformazione(String informazione) {
		this.informazione = informazione;
	}

	public boolean isIscrizione() {
		return iscritto;
	}

	public void setIscrizione(boolean iscrizione) {
		this.iscritto = iscrizione;
	}
}
