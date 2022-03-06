package domainModel;

public class SuggerimentoSostituto {
	private int id;
	private int idAppello;
	private int idRichiedente;
	private int idSosituto;
	private String nomeCognomeSostituto;
	private String nomeCognomeRichiedente;
	private String nota;
	private int status;
	
	public SuggerimentoSostituto(int id, int idAppello, int idRichiedente, int idSosituto, String nomeCognomeSostituto, String nota,
			int status) {
		super();
		this.id = id;
		this.idAppello = idAppello;
		this.idRichiedente = idRichiedente;
		this.idSosituto = idSosituto;
		this.nomeCognomeSostituto = nomeCognomeSostituto;
		this.nota = nota;
		this.status = status;
	}
	
	public SuggerimentoSostituto(int id, int idAppello, int idRichiedente, int idSosituto, String nomeCognomeSostituto, 
			String nomeCognomeRichiedente, String nota,int status) {
		super();
		this.id = id;
		this.idAppello = idAppello;
		this.idRichiedente = idRichiedente;
		this.idSosituto = idSosituto;
		this.nomeCognomeSostituto = nomeCognomeSostituto;
		this.setNomeCognomeRichiedente(nomeCognomeRichiedente);
		this.nota = nota;
		this.status = status;
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

	public String getNomeCognomeSostituto() {
		return nomeCognomeSostituto;
	}

	public void setNomeCognomeSostituto(String nomeCognomeSostituto) {
		this.nomeCognomeSostituto = nomeCognomeSostituto;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNomeCognomeRichiedente() {
		return nomeCognomeRichiedente;
	}

	public void setNomeCognomeRichiedente(String nomeCognomeRichiedente) {
		this.nomeCognomeRichiedente = nomeCognomeRichiedente;
	}
	
	
}
