package domainModel;

public class AppelloTesi {

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

	private int id;
	private String data;
	private int idMatricolaPresidente;
	
	public AppelloTesi(int idAppello, String d, int idPre) {
		this.id = idAppello;
		this.data = d;
		this.idMatricolaPresidente = idPre;
	}
}
