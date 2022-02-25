package domainModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	private static Database db;
	private String connectionString ;
	
	public Database() {

		this.connectionString="jdbc:mysql://localhost:3306/gestionale?user=root&password=secret";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//Connection connection = null;
		//try {
			//connection = DriverManager.getConnection(connectionString);
			//PreparedStatement prepared = connection.prepareStatement("insert into persone (cognome, nome, eta) values (?,?,?)");
			//prepared.setString(1, "Marroni");
			//prepared.setString(2, "Enrico");
			//prepared.setInt(3, 55);
			//prepared.executeUpdate();
			//Statement stm = connection.createStatement();
			//ResultSet rs = stm.executeQuery("select * from persone");
			//while (rs.next()) {
			//	System.out.println(rs.getString("cognome") + " " + rs.getString("nome") + " di anni " + rs.getInt("eta"));
			//}
		//} catch (SQLException e) {
		//	e.printStackTrace();
		//} catch (Exception e) {
		//	System.out.println(e.getMessage());
		//} finally {
		//	try {
		//		if (connection != null)
		//			connection.close();
		//	} catch (SQLException e) {
				// gestione errore in chiusura
		//	}
		//}
	}
	
	public Database getInstance() {
		if(db != null) {
			return db;
		}
		db = new Database();
		return db;
	}
	
	public Boolean VerificaCredenziali(String matricola, String password) {
		
		return true;
	}
	
	public Boolean RegistraIntentoPartecipazione() {
		return true;
	}
	
	public Boolean CaricaTesi() {
		return true;
	}
	
	public Boolean RimuoviCandidato(String matricola) {
		return true;
	}
	
	public Boolean AggiungiAppello() {
		return true;
	}
	
	public Boolean ValidaComposizioneCommissione(Boolean accettato) {
		return true;
	}
	
	public Boolean SalvaDate(String Appello) {
		return true;
	}
	
	public Boolean InserisciPresidenteCommissioneTesi(String matricola, String idAppello) {
		return true;
	}
	
	public Boolean ValidaPartecipazioneTesi(Boolean v) {
		return true;
	}
	
	public Boolean ApprovaTesi(Boolean appr) {
		return true;
	}
	
	public Boolean InserisciGiustificazione(String matricola, String note) {
		return true;
	}
	
}
