package domainModel;

public class AppelloTesi {
	
	private int id;
	private String data;
	private int idMatricolaPresidente;
	private int idAula;
	private String numAula;

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

	public int getIdMatricolaPresidente() {
		return idMatricolaPresidente;
	}

	public void setIdMatricolaPresidente(int idMatricolaPresidente) {
		this.idMatricolaPresidente = idMatricolaPresidente;
	}

	public int getIdAula() {
		return idAula;
	}

	public void setIdAula(int idAula) {
		this.idAula = idAula;
	}

	public String getNumAula() {
		return new String(numAula);
	}

	public void setNumAula(String numAula) {
		this.numAula = numAula;
	}
	
	public AppelloTesi(int idAppello, String d, int idPre, int idA, String numA) {
		this.id = idAppello;
		this.data = d;
		this.idMatricolaPresidente = idPre;
				
		this.idAula = idA;
		if(numA == null) {
			this.numAula = new String("");
		}else {
			this.numAula = new String(numA);
		}
	}
}
