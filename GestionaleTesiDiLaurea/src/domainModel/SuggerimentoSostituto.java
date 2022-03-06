package domainModel;

public class SuggerimentoSostituto {
	private int id;
	private int idAppello;
	private int idRichiedente;
	private int idSosituto;
	private String nomeCognomeSostituto;
	private String nota;
	private boolean approvato;
	
	public SuggerimentoSostituto(int id, int idAppello, int idRichiedente, int idSosituto, String nomeCognomeSostituto, String nota,
			boolean approvato) {
		super();
		this.id = id;
		this.idAppello = idAppello;
		this.idRichiedente = idRichiedente;
		this.idSosituto = idSosituto;
		this.nomeCognomeSostituto = nomeCognomeSostituto;
		this.nota = nota;
		this.approvato = approvato;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdAppello() {
		return idAppello;
	}
	
	public void setIdAppello(int idAppello) {
		this.idAppello = idAppello;
	}
	
	public int getIdRichiedente() {
		return idRichiedente;
	}
	
	public void setIdRichiedente(int idRichiedente) {
		this.idRichiedente = idRichiedente;
	}
	public int getIdSosituto() {
		return idSosituto;
	}
	
	public void setIdSosituto(int idSosituto) {
		this.idSosituto = idSosituto;
	}
	
	public String getNota() {
		return nota != null ? nota : "";
	}
	
	public void setNota(String nota) {
		this.nota = nota;
	}
	
	public boolean isApprovato() {
		return approvato;
	}
	
	public void setApprovato(boolean approvato) {
		this.approvato = approvato;
	}

	public String getNomeCognomeSostituto() {
		return nomeCognomeSostituto;
	}

	public void setNomeCognomeSostituto(String nomeCognomeSostituto) {
		this.nomeCognomeSostituto = nomeCognomeSostituto;
	}
	
	
}