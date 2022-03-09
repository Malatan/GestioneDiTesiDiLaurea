package domainModel;

import utils.Pair;

public class Studente extends Utente{
	private Pair<Integer, String> corso;
	private Pair<Integer, String> relatore;
	private String repository;
	private int statusTesi;
	
	public Studente(String nome, String cognome, String matricola) {
		super(matricola, nome, cognome);
	}
	
	public Studente(String nome, String cognome, String matricola, String repository) {
		super(matricola, nome, cognome);
		this.repository = repository;
	}

	public int getStatusTesi() {
		return statusTesi;
	}

	public void setStatusTesi(int statusTesi) {
		this.statusTesi = statusTesi;
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

	
