package domainModel;

public class Docente{

	private String matricola;
	private String nome;
	private String cognome;
	
	public Docente(String matricola, String nome, String cognome) {
		super();
		this.matricola = matricola;
		this.nome = nome;
		this.cognome = cognome;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
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
	
	public String getNomeCognome() {
		return new String(nome + " " + cognome);
	}
	
	public int getMatricolaInt() {
		return Integer.parseInt(matricola);
	}
	
}