package domainModel;

public class Studente {

	private String nome;
	private String cognome;
	private String matricola;
	
	public Studente(String nome, String cognome, String matricola) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.matricola = matricola;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getMatricola() {
		return matricola;
	}
	
	public int getMatricolaInt() {
		return Integer.parseInt(matricola);
	}
	
	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}
	
}

	
