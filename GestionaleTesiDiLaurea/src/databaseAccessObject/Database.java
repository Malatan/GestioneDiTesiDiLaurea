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
	public Pair<Integer, String> getStatusTesiAndString(int matricola) {
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
					s += "Attendi l'approvazione da parte del relatore.\n";
				} else {
					s += "Domanda approvata.\n";
					query = "SELECT a.id_appello, a.data, a.orario, au.id_aula, au.nome as nome_aula "
							+ "FROM appello_membro am, appello a, aula au, prenotazione_aula_giorno pag "
							+ "WHERE am.id_appello = a.id_appello AND am.id_appello = pag.id_appello "
							+ "AND pag.id_aula = au.id_aula AND am.matricola = " + matricola;
					Console.print(query, "sql");
					ResultSet rs2 = stm.executeQuery(query);
					if (!rs2.next()) {
						s += "Attendi l'assegnazione all'appello.\n";
					} else {
						status = 2;
						s += "Sei stato assegnato all'appello di:\n";
						boolean data_definita = false;
						if(rs2.getString("data") != null) {
							s += "Data: " + rs2.getString("data") + ".\n";
							data_definita = true;
						}else {
							s += "Data: INDEFINITO";
						}
						if(rs2.getString("orario") != null) {
							s += "Ore: " + rs2.getString("orario") + ".\n";
						}else {
							s += "Ore: INDEFINITO";
						}
						if(rs2.getString("id_aula") != null) {
							s += "Aula: " + rs2.getString("nome_aula") + ".\n";
						}else {
							s += "Aula: INDEFINITO";
						}
						if (data_definita) {
							s += "Puoi ritirare la domanda prima di " + Utils.dateSubtractDays(rs2.getString("data"), 7) + ".\n";
						}
					}
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
	
	public boolean getStudentiStatusTesi(ArrayList<Studente> studenti) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			for ( int i = 0 ; i < studenti.size() ; i ++) {
				String query = "SELECT A.matricola, A.approvato, B.id_appello FROM "
						+ "(SELECT d.matricola, d.approvato FROM domandatesi d WHERE d.matricola = " + studenti.get(i).getMatricola() + ") AS A "
						+ "LEFT JOIN "
						+ "(SELECT am.matricola, am.id_appello FROM appello_membro am WHERE am.matricola = " + studenti.get(i).getMatricola() + ") AS B "
						+ "ON A.matricola = B.matricola";
				Console.print(query, "sql");
				ResultSet rs = stm.executeQuery(query);
				if (rs.next()) {
					int s = -1;
					if (rs.getInt("id_appello") != 0) {
						s = 2;
					} else if (rs.getInt("matricola") != 0) {
						s = 1;
					} else {
						s = 0;
					}
					studenti.get(i).setStatusTesi(s);
				}
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
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

	public void updateMembriAppello(int id_appello, ArrayList<Integer> studenti, ArrayList<Integer> relatori, ArrayList<Integer> commissioni) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String elimination = "DELETE FROM appello_membro WHERE id_appello=" + id_appello;
			stm.executeUpdate(elimination);

			String query = "INSERT INTO appello_membro (id_appello, matricola, ruolo) VALUES(?,?,?)";
			for (Integer matricola : studenti) {
				PreparedStatement prepared = connection.prepareStatement(query);
				prepared.setInt(1, id_appello);
				prepared.setInt(2, matricola);
				prepared.setInt(3, 0);
				Console.print(prepared.toString(), "sql");
				prepared.executeUpdate();
			}
			
			for (Integer matricola : commissioni) {
				PreparedStatement prepared = connection.prepareStatement(query);
				prepared.setInt(1, id_appello);
				prepared.setInt(2, matricola);
				prepared.setInt(3, 1);
				Console.print(prepared.toString(), "sql");
				prepared.executeUpdate();
			}
			
			for (Integer matricola : relatori) {
				PreparedStatement prepared = connection.prepareStatement(query);
				prepared.setInt(1, id_appello);
				prepared.setInt(2, matricola);
				prepared.setInt(3, 2);
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
			String query = "UPDATE appello SET status = '" + val + "' WHERE id_appello = " + id_appello;
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

	public String getStatusAppello(int id_appello) {
		Connection connection = null;
		String s = "";
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT status FROM appello WHERE id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				int status = rs.getInt("status");
				s = AppelloTesi.getStatusString(status);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	public Docente getPresidenteCommissione(int id_appello) {
		Connection connection = null;
		Docente s = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT ut.nome,ut.cognome,ut.matricola FROM appello_membro aps, utente as ut WHERE aps.ruolo = 3 AND"
					+ " aps.matricola = ut.matricola AND" + " aps.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				s = new Docente (rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome"));
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
			String query = "SELECT ap.id_appello, ap.id_corso, c.nome as nome_corso, ap.data, ap.orario, ap.teleconferenza, ap.nota,"
					+ " au.id_aula,au.nome as aula, ap.status"
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
						rs.getString("teleconferenza"), rs.getString("nota"), rs.getInt("status"));
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
			String query = "SELECT ap.id_appello, ap.id_corso, c.nome as nome_corso, ap.data, ap.orario, ap.teleconferenza, ap.nota,"
					+ " au.id_aula,au.nome as aula, ap.status"
					+ " FROM appello as ap LEFT JOIN prenotazione_aula_giorno as pag ON ap.id_appello = pag.id_appello"
					+ " LEFT JOIN aula as au ON au.id_aula = pag.id_aula LEFT JOIN corso c on c.id_corso = ap.id_corso"
					+ " WHERE ap.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {

				appello = new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
						rs.getString("data"), rs.getString("orario"), Pair.of(rs.getInt("id_aula"), rs.getString("aula")), 
						rs.getString("teleconferenza"), rs.getString("nota"), rs.getInt("status"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appello;
	}
	
	// appello - ruolo
	public ArrayList<Pair<AppelloTesi, Integer>> getAppelliAndRuoloByDocente(String matricola){
		Connection connection = null;
		ArrayList<Pair<AppelloTesi, Integer>> appelli = new ArrayList<Pair<AppelloTesi, Integer>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT a.id_appello, a.id_corso, c.nome as nome_corso, a.data, a.orario, a.status, a.teleconferenza, "
					+ "a.nota, am.ruolo, pag.id_aula, au.nome as nome_aula "
					+ "FROM appello a, corso c,  appello_membro am "
					+ "LEFT JOIN prenotazione_aula_giorno pag ON pag.id_appello = am.id_appello "
					+ "LEFT JOIN aula au ON pag.id_aula = au.id_aula "
					+ "WHERE a.id_appello = am.id_appello AND a.id_corso = c.id_corso AND am.matricola = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				appelli.add(Pair.of(new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
						rs.getString("data"), rs.getString("orario"), Pair.of(rs.getInt("id_aula"), rs.getString("nome_aula")), 
						rs.getString("teleconferenza"), rs.getString("nota"), rs.getInt("status")), 
						rs.getInt("ruolo")));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appelli;
	}
	
	public ArrayList<AppelloTesi> getAppelli() {
		Connection connection = null;
		ArrayList<AppelloTesi> appelli = new ArrayList<AppelloTesi>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT a.id_appello, a.id_corso, c.nome as nome_corso, pag.id_aula, "
					+ "au.nome as nome_aula, a.data, a.orario, a.teleconferenza, a.nota, a.status "
					+ "FROM appello a "
					+ "LEFT JOIN corso c on a.id_corso = c.id_corso "
					+ "LEFT JOIN prenotazione_aula_giorno pag on pag.id_appello = a.id_appello "
					+ "LEFT JOIN aula au on pag.id_aula = au.id_aula";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				appelli.add(new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
						rs.getString("data"), rs.getString("orario"), Pair.of(rs.getInt("id_aula"), rs.getString("nome_aula")),
						rs.getString("teleconferenza"), rs.getString("nota"), rs.getInt("status")));
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
			String query = "SELECT a.id_appello, a.id_corso, c.nome as nome_corso, a.data, a.orario, a.teleconferenza, a.nota, a.status "
					+ "from appello a, corso c where a.id_corso = c.id_corso and a.id_corso = " + id_corso;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				appelli.add(new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
						rs.getString("data"), rs.getString("orario"), null,
						rs.getString("teleconferenza"), rs.getString("nota"), rs.getInt("status")));
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
				docenti.add(Pair.of(rs.getInt("matricola"), rs.getString("nome") + " " + rs.getString("Cognome")));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return docenti;
	}
	
	public ArrayList<Pair<Docente, String>> getDocentiAndDip() {
		Connection connection = null;
		ArrayList<Pair<Docente, String>> docentiDip = new ArrayList<Pair<Docente, String>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT u.matricola, u.nome, u.cognome, p.nome as nome_dip FROM utente u, dipartimento p, docente_dipartimento dd "
					+ "where u.ruolo = 4 and u.matricola = dd.matricola and dd.id_dipartimento = p.id_dipartimento";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				docentiDip.add(Pair.of(new Docente(rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome")), 
						rs.getString("nome_dip")));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return docentiDip;
	}
	
	public ArrayList<Docente> getDocentiPerSostituzione(int id_appello) {
		Connection connection = null;
		ArrayList<Docente> docenti = new ArrayList<Docente>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT u.matricola, u.nome, u.cognome, d.id_dipartimento, d.nome as nome_dip " 
					+ "FROM utente u, dipartimento d, docente_dipartimento dd "
					+ "WHERE ruolo = 4 AND dd.matricola = u.matricola AND dd.id_dipartimento = d.id_dipartimento AND u.matricola "
					+ "NOT IN (SELECT am.matricola FROM appello_membro am WHERE am.id_appello = " + id_appello + ")";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				docenti.add(new Docente(rs.getString("matricola"), rs.getString("nome"), rs.getString("Cognome"),
							Pair.of(rs.getInt("id_dipartimento"), rs.getString("nome_dip"))));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return docenti;
	}
	
	public Pair<Integer, String> getDipByDocente(String matricola){
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT dd.id_dipartimento, d.nome "
					+ "FROM docente_dipartimento dd, dipartimento d "
					+ "WHERE dd.id_dipartimento = d.id_dipartimento AND dd.matricola = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return Pair.of(rs.getInt("id_dipartimento"), rs.getString("nome"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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

	public ArrayList<Docente> getRelatoriByAppello(int id_appello) {
		Connection connection = null;
		ArrayList<Docente> relatori = new ArrayList<Docente>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT u.matricola, u.nome, u.cognome "
					+ "FROM appello_membro am, utente u "
					+ "WHERE am.ruolo = 2 AND am.matricola = u.matricola AND am.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				relatori.add(new Docente(rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome")));
			}
			connection.close();
			return relatori;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updatePresidenteCommissione(int id_appello, int matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE appello_membro SET ruolo = 1 WHERE id_appello = ? AND ruolo = 3";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_appello);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			
			query = "UPDATE appello_membro SET ruolo = 3 WHERE matricola = ? AND id_appello = ?";
			prepared = connection.prepareStatement(query);
			prepared.setInt(2, id_appello);
			prepared.setInt(1, matricola);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Studente> getMyStudenti(String matricola) {
		Connection connection = null;
		ArrayList<Studente> studenti = new ArrayList<Studente>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			/*String query = "SELECT ut.cognome, ut.nome, ut.matricola "
					+ "FROM domandatesi dt, utente as ut "
					+ "WHERE dt.approvato = 1 AND dt.matricola = ut.matricola "
					+ "AND dt.id_corso = (SELECT id_corso FROM corso WHERE presidente = " + matricola + ") "
					+ "AND ut.matricola NOT IN (SELECT matricola FROM  appello_membro WHERE RUOLO = 0)";*/
			String query = "SELECT ut.cognome, ut.nome, ut.matricola from domandatesi dt, utente as ut"
					+ " WHERE dt.approvato = 1 AND dt.matricola = ut.matricola"
					+ " AND dt.id_corso = (SELECT id_corso FROM corso WHERE presidente = " + matricola + ")";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				studenti.add(new Studente(rs.getString("nome"), rs.getString("cognome"), rs.getString("matricola")));
			}
			connection.close();
			return studenti;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Pair<Studente, Docente>> getRelatoriByStudenti(ArrayList<Studente> studenti) {
		Connection connection = null;
		ArrayList<Pair<Studente, Docente>> relatori = new ArrayList<Pair<Studente, Docente>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			for (int i = 0 ; i < studenti.size() ; i ++) {
				String query = "SELECT u.matricola, u.nome, u.cognome from utente u, domandatesi d "
						+ "where u.matricola = d.relatore and d.matricola = " + studenti.get(i).getMatricola() ;
				Console.print(query, "sql");
				ResultSet rs = stm.executeQuery(query);
				if (rs.next()) {
					relatori.add(Pair.of(studenti.get(i), 
							new Docente(rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome"))));
				}
			}
			connection.close();
			return relatori;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Pair<Studente, Docente>> getRelatoriStudentiFromAppello(int id_appello) {
		Connection connection = null;
		ArrayList<Pair<Studente, Docente>> studentiRelatori = new ArrayList<Pair<Studente, Docente>>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT us.matricola as matricola_studente, us.nome as nome_studente, us.cognome as cognome_studente, "
					+ "ur.matricola as matricola_relatore, ur.nome as nome_relatore, ur.cognome as cognome_relatore "
					+ "FROM appello_membro am, utente us, utente ur, domandatesi d "
					+ "WHERE am.matricola = us.matricola AND d.matricola = am.matricola AND ur.matricola = d.relatore "
					+ "AND am.ruolo = 0 AND am.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				studentiRelatori.add(Pair.of(new Studente(rs.getString("nome_studente"), rs.getString("cognome_studente"), 
															rs.getString("matricola_studente")), 
											new Docente(rs.getString("matricola_relatore"), rs.getString("nome_relatore"), 
															rs.getString("cognome_relatore"))));
			}
			connection.close();
			return studentiRelatori;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Studente> getStudentiFromAppello(int id_appello) {
		Connection connection = null;
		ArrayList<Studente> studenti = new ArrayList<Studente>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT ut.cognome, ut.nome, ut.matricola from appello_membro aps, utente as ut WHERE aps.matricola = ut.matricola "
					+ "AND aps.ruolo = 0 " + "AND aps.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				studenti.add(new Studente(rs.getString("nome"), rs.getString("cognome"), rs.getString("matricola")));
			}
			connection.close();
			return studenti;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Docente> getCommissioniByAppello(int id_appello) {
		Connection connection = null;
		ArrayList<Docente> commissioni = new ArrayList<Docente>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT ut.cognome, ut.nome, ut.matricola from appello_membro aps, utente as ut WHERE aps.matricola = ut.matricola "
					+ "AND aps.ruolo = 1 " + "AND aps.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				commissioni.add(new Docente(rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome")));
			}
			connection.close();
			return commissioni;
		} catch (Exception e) {
			e.printStackTrace();
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
