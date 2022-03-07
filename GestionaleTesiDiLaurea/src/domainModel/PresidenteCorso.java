package domainModel;

import utils.Pair;

public class PresidenteCorso extends Utente{
	private Pair<Integer, String> corso;
	
	public PresidenteCorso(String matricola, String nome, String cognome, Pair<Integer, String> corso) {
		super(matricola, nome, cognome);
		this.corso = corso;
	}

	public Pair<Integer, String> getCorso() {
		return corso;
	}

	public void setCorso(Pair<Integer, String> corso) {
		this.corso = corso;
	}
}
