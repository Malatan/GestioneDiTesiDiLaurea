package system;

public class Messaggio {
	private int id;
	private int idSorgente;
	private int idDestinatario;
	private String dataEmissione;
	private String titolo;
	private String contenuto;
	private boolean letto;
	
	public Messaggio(int idMessage, int idSorgente, int idDestinatario, String dataEmissione, String titolo, String contenuto,
			boolean letto) {
		super();
		this.id = idMessage;
		this.idSorgente = idSorgente;
		this.idDestinatario = idDestinatario;
		this.dataEmissione = dataEmissione;
		this.titolo = titolo;
		this.contenuto = contenuto;
		this.letto = letto;
	}

	public int getId() {
		return id;
	}
	
	public String getIdString() {
		return new String(id+"");
	}
	
	public void setId(int idMessage) {
		this.id = idMessage;
	}

	public int getIdSorgente() {
		return idSorgente;
	}

	public void setIdSorgente(int idSorgente) {
		this.idSorgente = idSorgente;
	}

	public int getIdDestinatario() {
		return idDestinatario;
	}

	public void setIdDestinatario(int idDestinatario) {
		this.idDestinatario = idDestinatario;
	}

	public String getDataEmissione() {
		return dataEmissione;
	}

	public void setDataEmissione(String dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public String getContenuto() {
		return contenuto;
	}

	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}
	
	public String isLettoString() {
		return letto ? "Letto" : "Nuovo";
	}
	
	public boolean isLetto() {
		return letto;
	}

	public void setLetto(boolean letto) {
		this.letto = letto;
	}
	
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String display() {
		String output = "[" + isLettoString() + "] [" + dataEmissione + "] [" + titolo + "] [";
		int label_length = 85;
		if (output.length() + contenuto.length() <= label_length) {
			output += contenuto;
		} else {
			output += contenuto.substring(0, label_length - output.length()) + "...";
		}
		return output;
	}
	
}
