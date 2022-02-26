package domainModel;

public class Aula {
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumAula() {
		return new String(numAula);
	}

	public void setNumAula(String numAula) {
		this.numAula = numAula;
	}

	public int getLibera() {
		return libera;
	}

	public void setLibera(int libera) {
		this.libera = libera;
	}

	private int id;
	private String numAula;
	private int libera;
	
	public Aula(int idAula, String numAula, int libera) {
		this.id = idAula;
		this.numAula = numAula;
		this.libera = libera;
	}
}
