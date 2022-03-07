package domainModel;

import java.util.ArrayList;

import utils.Pair;

public class Verbale {
	private int id;
	private int idAppello;
	private String data;
	private String contenuto;
	private boolean approvato;
	private ArrayList<Pair<Docente, String>> determinazioni;
	
	public Verbale(int id, int idAppello, String data, String contenuto, boolean approvato,
			ArrayList<Pair<Docente, String>> determinazioni) {
		super();
		this.id = id;
		this.idAppello = idAppello;
		this.data = data;
		this.contenuto = contenuto;
		this.approvato = approvato;
		this.determinazioni = determinazioni;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdAppello() {
		return idAppello;
	}

	public void setIdAppello(int idAppello) {
		this.idAppello = idAppello;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getContenuto() {
		return contenuto;
	}

	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}

	public boolean isApprovato() {
		return approvato;
	}

	public void setApprovato(boolean approvato) {
		this.approvato = approvato;
	}

	public ArrayList<Pair<Docente, String>> getDeterminazioni() {
		return determinazioni;
	}

	public void setDeterminazioni(ArrayList<Pair<Docente, String>> determinazioni) {
		this.determinazioni = determinazioni;
	}
	
	
}
