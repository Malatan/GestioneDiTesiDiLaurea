package system;

public class Messagio {
	private int idMessage;
	private int idSorgente;
	private int idDestinatario;
	private String dataEmissione;
	private String contenuto;
	private boolean letto;
	
	public Messagio(int idMessage, int idSorgente, int idDestinatario, String dataEmissione, String contenuto,
			boolean letto) {
		super();
		this.idMessage = idMessage;
		this.idSorgente = idSorgente;
		this.idDestinatario = idDestinatario;
		this.dataEmissione = dataEmissione;
		this.contenuto = contenuto;
		this.letto = letto;
	}

	public int getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
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

	public boolean isLetto() {
		return letto;
	}

	public void setLetto(boolean letto) {
		this.letto = letto;
	}
	
	
	
}
