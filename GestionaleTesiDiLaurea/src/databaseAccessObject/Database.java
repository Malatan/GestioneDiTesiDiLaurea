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
	private String connectionString;

	public Database() {
		this.connectionString = "jdbc:mysql://localhost:3306/gestionaletesi?user=root&password=";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Database getInstance() {
		if (db != null) {
			return db;
		}
		db = new Database();
		return db;
	}

	public Boolean isConnected() {
		try {
			if (DriverManager.getConnection(connectionString) != null) {
				// Console.print("Connessione al db con successo", "db");
				return true;
			} else
				return false;
		} catch (SQLException e) {
			// Console.print("Connessione al db fallita", "db");
			e.printStackTrace();
			return false;
		}
	}

	public String[] verificaCredenziali(String matricola, String password) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT * from utente " + "WHERE matricola = '" + matricola + "' AND password= '" + password
					+ "'";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			String[] info = null;
			if (rs.next()) {
				info = new String[4];
				info[0] = rs.getString("matricola");
				info[1] = rs.getString("nome");
				info[3] = rs.getString("cognome");
				info[2] = rs.getString("ruolo");
				return info;
			}
			connection.close();
			return info;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 0 = non e' presenta la domanda, 1 = e' presente la domanda ma non ancora in
	 * appello, 2 = e' stato assegnato ad un appello
	 */
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
						+ "Il relatore della tesi: " + rs.getString("relatore_nome") + " "
						+ rs.getString("relatore_cognome") + ".\n" + "Data domanda tesi: " + rs.getString("data")
						+ ".\n";
				if (rs.getString("repository") == null || rs.getString("repository").equals("")) {
					s += "Repository Tesi: Inserisci il link del tuo repository di tesi.\n";
				} else {
					s += "Repository Tesi: " + rs.getString("repository") + ".\n";
				}
				if (!rs.getBoolean("approvato")) {
					s += "Attendi l'approvazione da parte del relatore.";
				} else {
					s += "Domanda approvata.";
				}
				status = 1;
			} else {
				s = "Non hai ancora prensetato nessuna domanda";
				status = 0;
			}
			connection.close();
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
			connection.close();
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
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean prenotaAula(int id_aula, int id_appello, String matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String elimination = "DELETE FROM prenotazione_aula_giorno WHERE id_appello=" + id_appello;
			stm.executeUpdate(elimination);

			String query = "INSERT INTO prenotazione_aula_giorno (id_aula, id_appello, personale) VALUES(?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_aula);
			prepared.setInt(2, id_appello);
			prepared.setInt(3, Integer.valueOf(matricola));
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean setOrario(int id_appello, String time) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE appello SET orario = ? WHERE id_appello = ?";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(2, id_appello);
			prepared.setString(1, time);

			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
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
			connection.close();
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
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void aggiungiStudentiDocenti(int id_appello, ArrayList<Integer> studenti,
			ArrayList<Integer> docentiRelatori) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String elimination = "DELETE FROM appello_studentedocente WHERE id_appello=" + id_appello;
			stm.executeUpdate(elimination);

			String query = "INSERT INTO appello_studentedocente (id_appello, matricola, ruolo) VALUES(?,?,?)";

			for (Integer matricolaStudente : studenti) {
				PreparedStatement prepared = connection.prepareStatement(query);
				prepared.setInt(1, id_appello);
				prepared.setInt(2, matricolaStudente);
				prepared.setInt(3, 0);

				Console.print(prepared.toString(), "sql");
				prepared.executeUpdate();
			}

			for (Integer matricolaDocentiRelatori : docentiRelatori) {
				PreparedStatement prepared = connection.prepareStatement(query);
				prepared.setInt(1, id_appello);
				prepared.setInt(2, matricolaDocentiRelatori);
				prepared.setInt(3, 1);

				Console.print(prepared.toString(), "sql");
				prepared.executeUpdate();
			}
			connection.close();
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
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setAppelloApprovazione(int id_appello, int val) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "UPDATE appello SET approvazione = '" + val + "' WHERE id_appello = " + id_appello;
			Console.print(query, "sql");
			stm.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addDateToAppello(String Date, int id_appello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "UPDATE appello SET orario = NULL, data = '" + Date + "' WHERE id_appello = " + id_appello;
			Console.print(query, "sql");
			stm.executeUpdate(query);
			connection.close();
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
			connection.close();
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
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	public String getStatusApprovazioneAppello(int id_appello) {
		Connection connection = null;
		String s = "";
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT approvazione FROM appello WHERE id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				int approvazione = rs.getInt("approvazione");
				if (approvazione == 1) {
					s = "Approvato";
				} else if (approvazione == 2) {
					s = "Correzione dell'appello";
				} else {
					s = "In Revisione";
				}
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	public Pair<Integer, String> getPresidenteCommissione(int id_appello) {
		Connection connection = null;
		Pair<Integer, String> s = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT ut.nome,ut.cognome,ut.matricola FROM appello_studentedocente aps, utente as ut WHERE aps.ruolo = 2 AND"
					+ " aps.matricola = ut.matricola AND" + " aps.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				s = Pair.of(rs.getInt("matricola"), rs.getString("cognome") + " " + rs.getString("nome"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	public boolean aggiungeAppello(int matricola, String data, int id_corso) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "insert into appello (pubblicato_da, pub_data, id_corso) values (?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, matricola);
			prepared.setString(2, data);
			prepared.setInt(3, id_corso);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
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
				domande.add(new DomandaTesi(rs.getInt("matricola"),
						rs.getString("studente_nome") + " " + rs.getString("studente_cognome"), rs.getInt("relatore"),
						rs.getString("relatore_nome") + " " + rs.getString("relatore_cognome"), rs.getInt("id_corso"),
						rs.getString("nome_corso"), rs.getString("data"), rs.getString("repository"),
						rs.getBoolean("approvato")));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return domande;
	}

	public AppelloTesi getAppelloByMatricola(int matricola) {
		Connection connection = null;
		AppelloTesi appello = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT ap.id_appello, ap.id_corso, c.nome as nome_corso, ap.data, ap.orario, ap.teleconferenza, ap.nota, au.id_aula,au.nome as aula"
					+ " FROM appello as ap INNER JOIN appello_studentedocente as aps ON aps.id_appello = aps.id_appello"
					+ " LEFT JOIN prenotazione_aula_giorno as pag ON ap.id_appello = pag.id_appello"
					+ " LEFT JOIN aula as au ON au.id_aula = pag.id_aula" 
					+ " LEFT JOIN corso c ON c.id_corso = ap.id_corso"
					+ " WHERE aps.ruolo = 0 AND aps.matricola = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {

				appello = new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
						rs.getString("data"), rs.getString("orario"), Pair.of(rs.getInt("id_aula"), rs.getString("aula")), 
						rs.getString("teleconferenza"), rs.getString("nota"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appello;
	}

	public AppelloTesi getAppello(int id_appello) {
		Connection connection = null;
		AppelloTesi appello = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT ap.id_appello, ap.id_corso, c.nome as nome_corso, ap.data, ap.orario, ap.teleconferenza, ap.nota, au.id_aula,au.nome as aula"
					+ " FROM appello as ap LEFT JOIN prenotazione_aula_giorno as pag ON ap.id_appello = pag.id_appello"
					+ " LEFT JOIN aula as au ON au.id_aula = pag.id_aula LEFT JOIN corso c on c.id_corso = ap.id_corso"
					+ " WHERE ap.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {

				appello = new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
						rs.getString("data"), rs.getString("orario"), Pair.of(rs.getInt("id_aula"), rs.getString("aula")), 
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
			String query = "SELECT a.id_appello, a.id_corso, c.nome as nome_corso, a.data, a.orario, a.teleconferenza, a.nota "
					+ "from appello a, corso c where a.id_corso = c.id_corso";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				appelli.add(new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
						rs.getString("data"), rs.getString("orario"), null,
						rs.getString("teleconferenza"), rs.getString("nota")));
			}

			connection.close();
			return appelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<AppelloTesi> getAppelliByCorso(int id_corso) {
		Connection connection = null;
		ArrayList<AppelloTesi> appelli = new ArrayList<AppelloTesi>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT a.id_appello, a.id_corso, c.nome as nome_corso, a.data, a.orario, a.teleconferenza, a.nota "
					+ "from appello a, corso c where a.id_corso = c.id_corso and a.id_corso = " + id_corso;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				appelli.add(new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
						rs.getString("data"), rs.getString("orario"), null,
						rs.getString("teleconferenza"), rs.getString("nota")));
			}

			connection.close();
			return appelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
				corsi.add(Pair.of(rs.getInt("id_corso"), rs.getString("nome")));
			}
			connection.close();
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
				docenti.add(Pair.of(rs.getInt("matricola"), rs.getString("cognome") + " " + rs.getString("nome")));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return docenti;
	}

	public String getDateFromDB(int idAppello) {
		Connection connection = null;
		String dataAppello = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT id_appello, data FROM appello where id_appello = " + String.valueOf(idAppello);
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				if (rs.getString("data") != null) {
					dataAppello = new String(rs.getString("data"));
				}
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataAppello;
	}

	public ArrayList<Pair<Integer, String>> getRelatori() {
		Connection connection = null;
		ArrayList<Pair<Integer, String>> relatori = new ArrayList<Pair<Integer, String>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT ut.cognome, ut.nome, ut.matricola from domandatesi dt, utente as ut WHERE dt.approvato = 1 AND dt.relatore = ut.matricola";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				relatori.add(Pair.of(rs.getInt("matricola"), rs.getString("cognome") + " " + rs.getString("nome")));
			}

			connection.close();
			return relatori;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void aggiungiPresidenteCorso(int id_appello, int matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE appello_studentedocente SET ruolo = ? WHERE matricola = ? AND id_appello = ?";

			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(3, id_appello);
			prepared.setInt(2, matricola);
			prepared.setInt(1, 2);

			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Pair<Integer, String>> getMyStudenti(String matricola) {
		Connection connection = null;
		ArrayList<Pair<Integer, String>> studenti = new ArrayList<Pair<Integer, String>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT ut.cognome, ut.nome, ut.matricola from domandatesi dt, utente as ut"
					+ " WHERE dt.approvato = 1 AND dt.matricola = ut.matricola"

					+ " AND dt.id_corso = (SELECT id_corso FROM corso WHERE presidente = " + matricola + ")";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				studenti.add(Pair.of(rs.getInt("matricola"), rs.getString("cognome") + " " + rs.getString("nome")));
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

	public ArrayList<Pair<Integer, String>> getStudentiFromAppello(int id_appello) {
		Connection connection = null;
		ArrayList<Pair<Integer, String>> studentimembri = new ArrayList<Pair<Integer, String>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT ut.cognome, ut.nome, ut.matricola from appello_studentedocente aps, utente as ut WHERE aps.matricola = ut.matricola "
					+ "AND aps.ruolo = 0 " + "AND aps.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				studentimembri
						.add(Pair.of(rs.getInt("matricola"), rs.getString("cognome") + " " + rs.getString("nome")));
			}

			connection.close();
			return studentimembri;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public ArrayList<Pair<Integer, String>> getMembriCommissioneById(int id_appello) {
		Connection connection = null;
		ArrayList<Pair<Integer, String>> membri = new ArrayList<Pair<Integer, String>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT ut.cognome, ut.nome, ut.matricola from appello_studentedocente aps, utente as ut WHERE aps.matricola = ut.matricola "
					+ "AND aps.ruolo = 1 " + "AND aps.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				membri.add(Pair.of(rs.getInt("matricola"), rs.getString("cognome") + " " + rs.getString("nome")));
			}

			connection.close();
			return membri;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public Pair<Integer, String> getCorsoByPresidente(int matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT id_corso, nome from corso where presidente = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			Pair<Integer, String> corso = null;
			if (rs.next()) {
				corso = Pair.of(rs.getInt("id_corso"), rs.getString("nome"));
			}
			connection.close();
			return corso;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
