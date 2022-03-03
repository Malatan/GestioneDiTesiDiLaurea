package domainModel;

import utils.Pair;

public class PresidenteCorso {

	private String nome;
	private String cognome;
	private String matricola;
	private Pair<Integer, String> corso;
	
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
	
	public PresidenteCorso(String nome, String cognome, String matricola, Pair<Integer, String> corso) {
		this.nome = nome;
		this.corso = corso;
		this.cognome = cognome;
		this.matricola = matricola;
	}

	public Pair<Integer, String> getCorso() {
		return corso;
	}

	public void setCorso(Pair<Integer, String> corso) {
		this.corso = corso;
	}
}
