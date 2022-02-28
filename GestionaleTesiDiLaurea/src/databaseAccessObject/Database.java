package databaseAccessObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import domainModel.*;
import utils.*;

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
	
	public Boolean isConnected() {
		try {
			if(DriverManager.getConnection(connectionString) != null) {
				Console.print("Connessione al db con successo", "db");
				return true;
			}
			else
				return false;
		} catch (SQLException e) {
			Console.print("Connessione al db fallita", "db");
			e.printStackTrace();
			return false;
		}
	}
	
	public String[] verificaCredenziali(String matricola, String password) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT * from utente " + "WHERE matricola = '" + matricola + "' AND password= '" + password +"'";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				String[] info = new String[4];
				info[0] = rs.getString("matricola");
				info[1] = rs.getString("nome");
				info[3] = rs.getString("cognome");
				info[2] = rs.getString("ruolo");
				return info;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/*0 = non e' presenta la domanda, 
	  1 = e' presente la domanda ma non ancora in appello,
	  2 = e' stato assegnato ad un appello*/
	public int getStatusTesi(int matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT * FROM DOMANDATESI WHERE MATRICOLA = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return 1;
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return 0;
	}
	
	public void iscrizioneTesi(Studente studente, String data, int id_corso) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "insert into domandatesi (matricola, data, id_corso) values (?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, studente.getMatricolaInt());
			prepared.setString(2, data);
			prepared.setInt(3, id_corso);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			Console.print("Duplicate entry key: " + studente.getMatricola(), "db");
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public void ritiraDomanda(String matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "DELETE FROM DOMANDATESI WHERE MATRICOLA = " + matricola;
			Console.print(query, "sql");
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public boolean prenotaAula(String data, int id_aula, String matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "INSERT INTO prenotazione_aula_giorno (id_aula, data, personale) VALUES(?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_aula);
			prepared.setString(2, data);
			prepared.setInt(3, Integer.parseInt(matricola));
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	
	public void addRepo(String file, String matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "UPDATE DOMANDATESI SET repository = '" + file + "' WHERE MATRICOLA = " + matricola;
			Console.print(query, "sql");
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public String getRepository(String matricola) {
		Connection connection = null;
		String s = "";
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT repository FROM DOMANDATESI WHERE MATRICOLA = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				s = rs.getString("repository");
				System.out.println(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public boolean aggiungeAppello(int matricola, String data) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "insert into appello (pubblicato_da, pub_data) values (?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, matricola);
			prepared.setString(2, data);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public AppelloTesi[] getAppelli() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT * from appello";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			int rowsCount = rs.getRow();
			rs.beforeFirst();
			AppelloTesi[] appelli = new AppelloTesi[rowsCount];
			int index = 0;
			while (rs.next()) {
				appelli[index] = new AppelloTesi(rs.getInt("id_appello"),
						rs.getString("data"),
						rs.getString("nota"));
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
	
	public ArrayList<Pair<Integer, String>> getAule() {
		Connection connection = null;
		ArrayList<Pair<Integer, String>> aule = new ArrayList<Pair<Integer, String>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT * FROM AULA";
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				aule.add(Pair.of(rs.getInt("id_aula"), rs.getString("nome")));
			}
			connection.close();
			return aule;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return aule;
	}
	
	public ArrayList<Pair<Integer, String>> getCorsi() {
		Connection connection = null;
		ArrayList<Pair<Integer, String>> corsi = new ArrayList<Pair<Integer, String>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT * FROM CORSO";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				corsi.add(Pair.of(rs.getInt("id_corso"), 
									rs.getString("nome"))
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return corsi;
	}

	public Boolean programmaInformazioniPerAppello(int idAppello, String informazioni) {
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
	
	public String getInformazioniAppello(int idAppello) {
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
	
}
