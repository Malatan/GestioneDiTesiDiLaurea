package domainModel;

public class DomandaTesi {
	private int matricolaStudente;
	private String nomeCognomeStudente;
	private int matricolaRelatore;
	private String nomeCognomeRelatore;
	private int idCorso;
	private String nomeCorso;
	private String data;
	private String repository;
	private boolean approvato;
	
	public DomandaTesi(int matricolaStudente, String nomeCognomeStudente, int matricolaRelatore,
			String nomeCognomeRelatore, int idCorso, String nomeCorso, String data, String repository,
			boolean approvato) {
		super();
		this.matricolaStudente = matricolaStudente;
		this.nomeCognomeStudente = nomeCognomeStudente;
		this.matricolaRelatore = matricolaRelatore;
		this.nomeCognomeRelatore = nomeCognomeRelatore;
		this.idCorso = idCorso;
		this.nomeCorso = nomeCorso;
		this.data = data;
		this.repository = repository;
		this.approvato = approvato;
	}
	public int getMatricolaStudente() {
		return matricolaStudente;
	}
	public void setMatricolaStudente(int matricolaStudente) {
		this.matricolaStudente = matricolaStudente;
	}
	public String getNomeCognomeStudente() {
		return nomeCognomeStudente;
	}
	public void setNomeCognomeStudente(String nomeCognomeStudente) {
		this.nomeCognomeStudente = nomeCognomeStudente;
	}
	public int getMatricolaRelatore() {
		return matricolaRelatore;
	}
	public void setMatricolaRelatore(int matricolaRelatore) {
		this.matricolaRelatore = matricolaRelatore;
	}
	public String getNomeCognomeRelatore() {
		return nomeCognomeRelatore;
	}
	public void setNomeCognomeRelatore(String nomeCognomeRelatore) {
		this.nomeCognomeRelatore = nomeCognomeRelatore;
	}
	public int getIdCorso() {
		return idCorso;
	}
	public void setIdCorso(int idCorso) {
		this.idCorso = idCorso;
	}
	public String getNomeCorso() {
		return nomeCorso;
	}
	public void setNomeCorso(String nomeCorso) {
		this.nomeCorso = nomeCorso;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getRepository() {
		if (this.repository == null)
			return "";
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public boolean isApprovato() {
		return approvato;
	}
	public void setApprovato(boolean approvato) {
		this.approvato = approvato;
	}
	
	
}
