package domainModel;

import utils.Pair;

public class Docente extends Utente{
	private Pair<Integer, String> dipartimento;
	
	public Docente(String matricola, String nome, String cognome) {
		super(matricola, nome, cognome);
		this.dipartimento = null;
	}
	
	public Docente(String matricola, String nome, String cognome, Pair<Integer, String> dipartimento) {
		super(matricola, nome, cognome);
		this.dipartimento = dipartimento;
	}


	public Pair<Integer, String> getDipartimento() {
		return dipartimento;
	}

	public void setDipartimento(Pair<Integer, String> dipartimento) {
		this.dipartimento = dipartimento;
	}

}