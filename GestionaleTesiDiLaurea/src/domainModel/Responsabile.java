package domainModel;

public class Responsabile {

	private String nome;
	private String cognome;
	private String matricola;
	
	public Responsabile(String m, String n, String c) {
		this.matricola = m;
		this.cognome = c;
		this.nome = n;
	}
	
	public String getNome() {
		return new String(nome);
	}
	
	public String getCognome() {
		return new String(cognome);
	}
	
	public String getMatricola() {
		return new String(matricola);
	}
}
