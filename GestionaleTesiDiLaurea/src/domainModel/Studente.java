package domainModel;

import utils.Pair;

public class Studente {

	private String nome;
	private String cognome;
	private String matricola;
	private Pair<Integer, String> corso;
	private Pair<Integer, String> relatore;
	private String repository;
	private int statusTesi;
	
	public Studente(String nome, String cognome, String matricola) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.matricola = matricola;
	}
	
	public Studente(String nome, String cognome, String matricola, String repository) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.matricola = matricola;
		this.repository = repository;
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

	public int getStatusTesi() {
		return statusTesi;
	}

	public void setStatusTesi(int statusTesi) {
		this.statusTesi = statusTesi;
	}
	
	public String getNomeCognome() {
		return new String(nome + " " + cognome);
	}

	public Pair<Integer, String> getCorso() {
		return corso;
	}

	public void setCorso(Pair<Integer, String> corso) {
		this.corso = corso;
	}

	public Pair<Integer, String> getRelatore() {
		return relatore;
	}

	public void setRelatore(Pair<Integer, String> relatore) {
		this.relatore = relatore;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}
	
}

	
