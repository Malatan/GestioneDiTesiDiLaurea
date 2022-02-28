package domainModel;

public class AppelloTesi {
	
	private int id;
	private String data;
	private String nota;
	
	public AppelloTesi(int idAppello, String data, String nota) {
		this.id = idAppello;
		if (data != null)
			this.data = data;
		else
			this.data = "-";
		if (nota != null)
			this.nota = nota;
		else
			this.nota = "-";
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

	public String getNota() {
		return nota;
	}

	public void setNota(String informazione) {
		this.nota = informazione;
	}
}
