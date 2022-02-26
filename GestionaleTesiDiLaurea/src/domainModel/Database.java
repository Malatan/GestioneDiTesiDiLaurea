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

		this.connectionString="jdbc:mysql://localhost:3306/gestionaletesi?user=root&password=";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
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
	
	public static Database getInstance() {
		if(db != null) {
			return db;
		}
		db = new Database();
		return db;
	}
	
	public String[] VerificaCredenziali(String matricola, String password) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);

			Statement stm = connection.createStatement();
			System.out.println("SELECT matricola, nome, cognome, ruolo from users WHERE matricola = '" + matricola + "' AND password= '" + password +"'");
			ResultSet rs = stm.executeQuery("SELECT matricola, nome, cognome, ruolo from users WHERE matricola = '" + matricola + "' AND password= '" + password +"'");
			//System.out.println(rs.getString("cognome") + " " + rs.getString("nome") + " di ruolo " + rs.getInt("ruolo"));
			
			
			if (rs.next()) {
				String[] info = new String[4];
				info[0] = rs.getString("matricola");
				info[1] = rs.getString("nome");
				info[2] = rs.getString("ruolo");
				info[3] = rs.getString("cognome");
				
				
				return info;
			}
		} catch (SQLException e) {
	    	e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// gestione errore in chiusura
			}
		}
		return null;
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
	
	public Boolean AggiungiAppello(String data) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			
			Statement stm = connection.createStatement();

			System.out.println("SELECT idAppello from appelli WHERE data = " + data);
			ResultSet rs = stm.executeQuery("SELECT idAppello from appelli WHERE data = '" + data + "'" );
			//System.out.println(rs.getString("cognome") + " " + rs.getString("nome") + " di ruolo " + rs.getInt("ruolo"));
			
			
			if (rs.next()) {
				connection.close();
				return false;
			}
			
			PreparedStatement prepared = connection.prepareStatement("insert into appelli (data, idPresidente) values (?,?)");
			prepared.setString(1, data);
			prepared.setInt(2, 0);
			prepared.executeUpdate();
			

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				 //gestione errore in chiusura
			}
		}
		return true;
	}
	
	public AppelloTesi[] GetAppelli() {
				
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);


			ResultSet rs = stm.executeQuery("SELECT * from appelli as a LEFT JOIN aule as au ON a.idAula = au.id");
			//System.out.println(rs.getString("cognome") + " " + rs.getString("nome") + " di ruolo " + rs.getInt("ruolo"));
			
			rs.last();
			int rowsCount = rs.getRow();
			rs.beforeFirst();
			
			AppelloTesi[] appelli = new AppelloTesi[rowsCount];
			
			int index = 0;
			while (rs.next()) {
				System.out.print(rs.getInt("idAppello") + " " +
						rs.getString("data") + " " +
						rs.getInt("idPresidente") + " " +
						rs.getInt("idAula") + " " +
						rs.getString("numAula"));
				appelli[index] = new AppelloTesi(rs.getInt("idAppello"),
						rs.getString("data"),
						rs.getInt("idPresidente"),
						rs.getInt("idAula"),
						rs.getString("numAula")
						);
				index++;
			}
			
			
			connection.close();
			return appelli;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} 
		
	}
	
	public Aula[] GetAule() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);


			ResultSet rs = stm.executeQuery("SELECT * from aule");
			//System.out.println(rs.getString("cognome") + " " + rs.getString("nome") + " di ruolo " + rs.getInt("ruolo"));
			
			rs.last();
			int rowsCount = rs.getRow();
			rs.beforeFirst();
			
			System.out.println("How many rows of aule? " + rowsCount);
			
			Aula[] aule = new Aula[rowsCount];
			
			int index = 0;
			while (rs.next()) {
				System.out.println("Get Num Aula: " + rs.getString("numAula"));
				aule[index] = new Aula(rs.getInt("id"),rs.getString("numAula"), rs.getInt("libera"));
				index++;
			}
			
			
			connection.close();
			return aule;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} 

	}
	
	public Boolean PrenotaAula(int idAula, int idAppello, String currentAula) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			
			Statement stm = connection.createStatement();

			System.out.println("SELECT id from aule WHERE libera = 0 AND id="+idAula);
			ResultSet rs = stm.executeQuery("SELECT id from aule WHERE libera = 0 AND id="+idAula);
			//System.out.println(rs.getString("cognome") + " " + rs.getString("nome") + " di ruolo " + rs.getInt("ruolo"));
			
			
			if (rs.next()) {
				System.out.println("aula id: " + idAula+ " gia prenotata");
				connection.close();
				return false;
			}
			
			PreparedStatement prepared = connection.prepareStatement("UPDATE aule SET libera = 0 WHERE id = ?");
			prepared.setInt(1, idAula);
			prepared.executeUpdate();
			
			prepared = connection.prepareStatement("UPDATE appelli SET idAula = ? WHERE idAppello = ?");
			prepared.setInt(1, idAula);
			prepared.setInt(2, idAppello);
			prepared.executeUpdate();
			
			if(!currentAula.equals("")) {
				prepared = connection.prepareStatement("UPDATE aule SET libera = 1 WHERE numAula = ?");
				prepared.setString(1, currentAula);
				prepared.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				 //gestione errore in chiusura
			}
		}
		return true;
	}
	
	public Boolean ProgrammaInformazioniPerAppello(int idAppello, String informazioni) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			

			
			PreparedStatement prepared = connection.prepareStatement("UPDATE appelli SET informazioni = ? WHERE idAppello = ?");
			prepared.setString(1, informazioni);
			prepared.setInt(2, idAppello);
			prepared.executeUpdate();
			

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				 //gestione errore in chiusura
			}
		}
		return true;
	}
	
	public String GetInformazioniAppello(int idAppello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);


			ResultSet rs = stm.executeQuery("SELECT informazioni from appelli WHERE idAppello = " + idAppello);
			//System.out.println(rs.getString("cognome") + " " + rs.getString("nome") + " di ruolo " + rs.getInt("ruolo"));

			
			
			if (rs.next()) {
				 
				String informazioni = new String(rs.getString("informazioni"));
				connection.close();
				return informazioni;
			}
			
			
			


		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} 
		return null;
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
