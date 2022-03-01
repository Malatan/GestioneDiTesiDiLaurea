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
				//Console.print("Connessione al db con successo", "db");
				return true;
			}
			else
				return false;
		} catch (SQLException e) {
			//Console.print("Connessione al db fallita", "db");
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
	public Pair<Integer, String> getStatusTesi(int matricola) {
		Connection connection = null;
		int status = -1;
		String s = "";
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT d.id_corso, c.nome as nome_corso, d.relatore, u.nome as relatore_nome, "
					+ "u.cognome as relatore_cognome, d.data, d.repository, d.approvato "
					+ "FROM domandatesi d, corso c, utente u "
					+ "where d.id_corso = c.id_corso and d.relatore = u.matricola and d.matricola = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				s = "Hai presentato la domanda di tesi per il corso " + rs.getString("nome_corso") + ".\n"
						+ "Il relatore della tesi: " + rs.getString("relatore_nome") + " " + rs.getString("relatore_cognome") + ".\n"
						+ "Data domanda tesi: " + rs.getString("data") + ".\n";
				if(rs.getString("repository") == null || rs.getString("repository").equals("")) {
					s += "Repository Tesi: Inserisci il link del tuo repository di tesi.\n";
				} else {
					s += "Repository Tesi: " + rs.getString("repository") + ".\n";
				}
				if(!rs.getBoolean("approvato")) {
					s += "Attendi l'approvazione da parte del relatore.";
				} else {
					s += "Domanda approvata. Attenta l'assegnazione all'appello.";
				}
				status = 1;
			} else {
				s = "Non hai ancora prensetato nessuna domanda";
				status = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return Pair.of(status, s);
	}
	
	public void iscrizioneTesi(Studente studente, String data, int id_corso, int matricola_relatore) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "insert into domandatesi (matricola, relatore, data, id_corso) values (?,?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, studente.getMatricolaInt());
			prepared.setInt(2, matricola_relatore);
			prepared.setString(3, data);
			prepared.setInt(4, id_corso);
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
	
	public boolean prenotaAula(String data, int id_aula, int id_appello, String matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "INSERT INTO prenotazione_aula_giorno (id_aula, id_appello, data, personale) VALUES(?,?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_aula);
			prepared.setInt(2, id_appello);
			prepared.setString(3, data);
			prepared.setInt(4, Integer.parseInt(matricola));
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	
	public void approvaDomandaTesi(DomandaTesi domanda) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "UPDATE domandatesi SET approvato = 1 where matricola = " + domanda.getMatricolaStudente();
			Console.print(query, "sql");
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
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
	
	public void addLinkTele(String link, int id_appello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "UPDATE appello SET teleconferenza = '" + link + "' WHERE id_appello = " + id_appello;
			Console.print(query, "sql");
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public void addDateToAppello(String Date, int id_appello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "UPDATE appello SET data = '" + Date + "' WHERE id_appello = " + id_appello;
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public String getLinkTele(int id_appello) {
		Connection connection = null;
		String s = "";
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT teleconferenza FROM appello WHERE id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				s = rs.getString("teleconferenza");
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
	
	public ArrayList<DomandaTesi> getDomandeTesi(int matricola_docente) {
		Connection connection = null;
		ArrayList<DomandaTesi> domande = new ArrayList<DomandaTesi>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT d.matricola, s.nome as studente_nome, s.cognome as studente_cognome, d.id_corso, c.nome as nome_corso, "
					+ "d.relatore, r.nome as relatore_nome, r.cognome as relatore_cognome, d.data, d.repository, d.approvato "
					+ "FROM domandatesi d, corso c, utente r, utente s "
					+ "where d.id_corso = c.id_corso and d.relatore = r.matricola "
					+ "and d.matricola = s.matricola and r.matricola = " + matricola_docente;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				domande.add(new DomandaTesi(rs.getInt("matricola"), rs.getString("studente_nome") + " " + rs.getString("studente_cognome"), 
						rs.getInt("relatore"), rs.getString("relatore_nome") + " " + rs.getString("relatore_cognome"),
						rs.getInt("id_corso"), rs.getString("nome_corso"), rs.getString("data"), rs.getString("repository"),
						rs.getBoolean("approvato")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return domande;
	}
	
	public AppelloTesi getAppello(int id_appello) {
		Connection connection = null;
		AppelloTesi appello = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT * FROM appello where id_appello = " + id_appello;
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				appello = new AppelloTesi(
						rs.getInt("id_appello"), rs.getString("data"), -1, null, 
						rs.getString("teleconferenza"), rs.getString("nota"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appello;
	}
	
	public ArrayList<AppelloTesi> getAppelli() {
		Connection connection = null;
		ArrayList<AppelloTesi> appelli = new ArrayList<AppelloTesi>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT id_appello, data from appello";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			
			


			while (rs.next()) {
				appelli.add(new AppelloTesi(rs.getInt("id_appello"), rs.getString("data"), -1, null, null, null));
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
	
	public ArrayList<Pair<Integer, String>> getDocenti() {
		Connection connection = null;
		ArrayList<Pair<Integer, String>> docenti = new ArrayList<Pair<Integer, String>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT matricola, nome, cognome FROM utente where ruolo = 4";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				docenti.add(Pair.of(rs.getInt("matricola"), 
									rs.getString("nome") + " " + rs.getString("cognome"))
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return docenti;
	}
	
	public String getDateFromDB(int idAppello){
		Connection connection = null;
		String dataAppello = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT id_appello, data FROM appello where id_appello = " + String.valueOf(idAppello);
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				if(rs.getString("data") != null) {
					dataAppello = new String(rs.getString("data"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataAppello;
	}
	
	public ArrayList<Studente> getStudenti(){
		Connection connection = null;
		ArrayList<Studente> studenti = new ArrayList<Studente>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT matricola, relatore, data, id_corso, repository, approvato data from domandatesi WHERE approvato = 1";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			

			while (rs.next()) {
				studenti.add(new Studente(rs.getString("nome"), rs.getString("cognome"), rs.getString("matricola")));
			}
			
			connection.close();
			return studenti;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} 
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
