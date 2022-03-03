package domainModel;

public class PresidenteScuola {
	private String nome;
	private String cognome;
	private String matricola;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return new String(cognome);
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getMatricola() {
		return new String(matricola);
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}
	
	public PresidenteScuola(String nome, String cognome, String matricola) {
		this.nome = nome;
		this.cognome = cognome;
		this.matricola = matricola;
	}
}
