package domainModel;

public class Determinazione {
	private int id;
	private int idDocente;
	private int idAppello;
	private String data;
	private String nomeCognome;
	private String contenuto;

	public Determinazione(int id, int idDocente, int idAppello, String data, String nomeCognome, String contenuto) {
		super();
		this.id = id;
		this.idDocente = idDocente;
		this.idAppello = idAppello;
		this.data = data;
		this.nomeCognome = nomeCognome;
		this.contenuto = contenuto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdDocente() {
		return idDocente;
	}

	public void setIdDocente(int idDocente) {
		this.idDocente = idDocente;
	}

	public int getIdAppello() {
		return idAppello;
	}

	public void setIdAppello(int idAppello) {
		this.idAppello = idAppello;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNomeCognome() {
		return nomeCognome;
	}

	public void setNomeCognome(String nomeCognome) {
		this.nomeCognome = nomeCognome;
	}

	public String getContenuto() {
		return contenuto;
	}

	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}

}
