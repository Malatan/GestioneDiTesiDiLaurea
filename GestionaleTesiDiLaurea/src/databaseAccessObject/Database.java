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
import system.Messaggio;
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
	public Pair<Integer, String> getStatusTesiAndString(Studente studente) {
		Connection connection = null;
		int status = -1;
		String s = "";
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT d.id_corso, c.nome as nome_corso, d.relatore, u.nome as relatore_nome, "
					+ "u.cognome as relatore_cognome, d.data, d.repository, d.approvato "
					+ "FROM domandatesi d, corso c, utente u "
					+ "where d.id_corso = c.id_corso and d.relatore = u.matricola and d.matricola = " + studente.getMatricola();
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				studente.setCorso(Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")));
				studente.setRelatore(Pair.of(rs.getInt("relatore"), rs.getString("relatore_nome") + " "
						+ rs.getString("relatore_cognome")));
				s = "Hai presentato la domanda di tesi per il corso " + rs.getString("nome_corso") + ".\n"
						+ "Il relatore della tesi: " + rs.getString("relatore_nome") + " "
						+ rs.getString("relatore_cognome") + ".\n" + "Data domanda tesi: " + rs.getString("data")
						+ ".\n";
				if (rs.getString("repository") == null || rs.getString("repository").equals("")) {
					s += "Repository Tesi: Inserisci il link del tuo repository di tesi.\n";
				} else {
					s += "Repository Tesi: " + rs.getString("repository") + ".\n";
				}
				status = 1;
				if (!rs.getBoolean("approvato")) {
					s += "Attendi l'approvazione da parte del relatore.\n";
				} else {
					s += "Domanda approvata.\n";
					query = "SELECT a.id_appello, a.data, a.orario, au.id_aula, au.nome as nome_aula "
							+ "FROM appello_membro am, appello a, aula au, prenotazione_aula_giorno pag "
							+ "WHERE am.id_appello = a.id_appello AND am.id_appello = pag.id_appello "
							+ "AND pag.id_aula = au.id_aula AND am.matricola = " + studente.getMatricola();
					Console.print(query, "sql");
					ResultSet rs2 = stm.executeQuery(query);
					if (!rs2.next()) {
						s += "Attendi l'assegnazione all'appello.\n";
					} else {
						studente.setIdAppello(rs2.getInt("id_appello"));
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
	
	public boolean mandaMessaggio(int id_sorgente, int id_destinatario, String titolo, String contenuto) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "INSERT INTO messaggio (id_sorgente, id_destinatario, titolo, contenuto, data) values (?,?,?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_sorgente);
			prepared.setInt(2, id_destinatario);
			prepared.setString(3, titolo);
			prepared.setString(4, contenuto);
			prepared.setString(5, Utils.getTodayDate());
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateMessaggioLetto(int id) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE messaggio SET letto = 1 WHERE id = ?";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Messaggio> getMessaggi(int id_destinatario){
		Connection connection = null;
		ArrayList<Messaggio> messaggi = new ArrayList<Messaggio>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT id, id_sorgente, titolo, contenuto, data, letto "
					+ "FROM messaggio "
					+ "WHERE id_destinatario = " + id_destinatario;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				messaggi.add(new Messaggio(rs.getInt("id"), rs.getInt("id_sorgente"), id_destinatario, rs.getString("data"), rs.getString("titolo"), 
						rs.getString("contenuto"), rs.getBoolean("letto")));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messaggi;
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

	public void setAppelloStatus(int id_appello, int new_status) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "UPDATE appello SET status = '" + new_status + "' WHERE id_appello = " + id_appello;
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
	
	public boolean addSuggerimentoSostituto(int id_appello, int richiedente, int sostituto, String nota) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "INSERT INTO suggerimento_sostituto (id_appello, id_richiedente, id_sostituto, nota) VALUES (?,?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_appello);
			prepared.setInt(2, richiedente);
			prepared.setInt(3, sostituto);
			prepared.setString(4, nota.isEmpty() ? null : nota);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateSuggerimentoSostituto(SuggerimentoSostituto suggerimento) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE suggerimento_sostituto SET id_sostituto = ?, nota = ? WHERE id = ?";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, suggerimento.getIdSosituto());
			prepared.setString(2, suggerimento.getNota());
			prepared.setInt(3, suggerimento.getId());
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean setSuggerimentoSostitutoStatus(SuggerimentoSostituto suggerimento, int status) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE suggerimento_sostituto SET status = ? WHERE id = ?";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, status);
			prepared.setInt(2, suggerimento.getId());
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			// approva si procede ad aggiornare appello_membro
			if (status == 1) {
				query = "UPDATE appello_membro SET matricola = ? WHERE id_appello = ? AND matricola = ?";
				prepared = connection.prepareStatement(query);
				prepared.setInt(1, suggerimento.getIdSosituto());
				prepared.setInt(2, suggerimento.getIdAppello());
				prepared.setInt(3, suggerimento.getIdRichiedente());
				Console.print(prepared.toString(), "sql");
				prepared.executeUpdate();
			}
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean revocaPropostaSostituto(int id_suggerimento) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "DELETE FROM suggerimento_sostituto WHERE id = " + id_suggerimento;
			Console.print(query, "sql");
			stm.executeUpdate(query);
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateAppelloMembriPresente(ArrayList<Docente> commissione, int id_appello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE appello_membro SET presenza = 1 WHERE id_appello = ? AND ruolo = 0";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_appello);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			
			query = "UPDATE appello_membro SET presenza = 1 WHERE id_appello = ? AND matricola = ?";
			for (int i = 0 ; i < commissione.size() ;i++) {
				prepared = connection.prepareStatement(query);
				prepared.setInt(1, id_appello);
				prepared.setInt(2, commissione.get(i).getMatricolaInt());
				Console.print(prepared.toString(), "sql");
				prepared.executeUpdate();
			}
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addEsiti(ArrayList<Pair<Studente, Integer>> esiti, int id_appello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "INSERT INTO esito_tesi (matricola, id_appello, voto) values (?,?,?)";
			for(int i = 0 ; i < esiti.size() ; i++) {
				PreparedStatement prepared = connection.prepareStatement(query);
				prepared.setInt(1, esiti.get(i).first.getMatricolaInt());
				prepared.setInt(2, id_appello);
				prepared.setInt(3, esiti.get(i).second);
				Console.print(prepared.toString(), "sql");
				prepared.executeUpdate();
			}
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean approvaVerbale(Verbale verbale) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE verbale SET approvato = 1, data_approvazione = ? WHERE id = ?";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setString(1, Utils.getTodayDate());
			prepared.setInt(2, verbale.getId());
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			
			query = "UPDATE appello SET status = 4 WHERE id_appello = ?";
			prepared = connection.prepareStatement(query);
			prepared.setInt(1, verbale.getIdAppello());
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean generaVerbale(int id_appello, String data, String contenuto) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "INSERT INTO verbale (id_appello, data, contenuto) values (?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_appello);
			prepared.setString(2, data);
			prepared.setString(3, contenuto);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addDeterminazione(int matricola, int id_appello, String contenuto) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "INSERT INTO appello_determinazione (id_docente, id_appello, ultima_modifica, contenuto) values (?,?,?,?)";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, matricola);
			prepared.setInt(2, id_appello);
			prepared.setString(3, Utils.getTodayDate());
			prepared.setString(4, contenuto);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateDeterminazione(int matricola, int id_appello, String contenuto) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "UPDATE appello_determinazione SET contenuto = ?, ultima_modifica = ? WHERE id_docente = ? AND id_appello = ?";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setString(1, contenuto);
			prepared.setString(2, Utils.getTodayDate());
			prepared.setInt(3, matricola);
			prepared.setInt(4, id_appello);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean ritiraDeterminazione(int id_determinazione) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			String query = "DELETE FROM appello_determinazione WHERE id = ? ";
			PreparedStatement prepared = connection.prepareStatement(query);
			prepared.setInt(1, id_determinazione);
			Console.print(prepared.toString(), "sql");
			prepared.executeUpdate();
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean getPresenzaDeterminazione(String matricola, int id_appello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT * "
					+ "FROM appello_determinazione "
					+ "WHERE id_docente = " + matricola + " AND id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return true;
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Determinazione getDeterminazione(String matricola, int id_appello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT ad.id, ad.id_docente, u.nome, u.cognome, ad.id_appello, ad.ultima_modifica, ad.contenuto "
					+ "FROM appello_determinazione ad, utente u "
					+ "WHERE ad.id_docente = u.matricola AND ad.id_docente = " + matricola + " AND ad.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return new Determinazione(rs.getInt("ad.id"), rs.getInt("id_docente"), rs.getInt("id_appello"), rs.getString("ultima_modifica"),
						rs.getString("nome") + " " + rs.getString("cognome"), rs.getString("contenuto"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Verbale> getVerbali(){
		Connection connection = null;
		ArrayList<Verbale> verbali = new ArrayList<Verbale>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			Statement stm2 = connection.createStatement();
			String query = "SELECT v.id, v.id_appello, v.data, v.contenuto, v.approvato "
					+ "FROM verbale v";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				Verbale v = new Verbale(rs.getInt("id"), rs.getInt("id_appello"), rs.getString("data"),
						rs.getString("contenuto"), rs.getBoolean("approvato") , null);
				String query2 = "SELECT am.matricola, u.nome, u.cognome, ad.contenuto "
						+ "FROM appello_membro am "
						+ "LEFT JOIN appello_determinazione ad ON am.id_appello = ad.id_appello AND am.matricola = ad.id_docente "
						+ "AND ad.id_appello = " + rs.getInt("id_appello") + " AND am.presenza = 1 "
						+ "LEFT JOIN utente u ON am.matricola = u.matricola "
						+ "WHERE am.ruolo = 1 OR am.ruolo = 2 OR am.ruolo = 3";
				ResultSet rs2 = stm2.executeQuery(query2);
				ArrayList<Pair<Docente, String>> d = new ArrayList<Pair<Docente, String>>();
				while (rs2.next()) {
					d.add(Pair.of(new Docente(rs2.getString("matricola"), rs2.getString("nome"), rs2.getString("cognome")), 
							rs2.getString("contenuto")));
				}
				v.setDeterminazioni(d);
				verbali.add(v);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return verbali;
	}
	
	public Pair<AppelloTesi, Integer> getEsitoTesi(int matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT a.id_appello, a.id_corso, c.nome as nome_corso, a.data, a.orario, a.nota, a.status, e.id, e.voto " 
					+"FROM appello a " 
					+"LEFT JOIN corso c ON a.id_corso = c.id_corso "
					+"LEFT JOIN appello_membro am ON am.id_appello = a.id_appello "
					+"LEFT JOIN esito_tesi e ON e.matricola = am.matricola WHERE am.matricola = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				if (rs.getInt("id") != 0) {
					return Pair.of(new AppelloTesi(rs.getInt("id_appello"), Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")), 
							rs.getString("data"), rs.getString("orario"), null, null, rs.getString("nota"), rs.getInt("status")), rs.getInt("voto"));
				}
				return null;
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	public SuggerimentoSostituto getSuggerimentoByAppelloAndDocente(int id_appello, int matricola) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT ss.id, ss.id_appello, ss.id_richiedente, ss.id_sostituto, u.nome, u.cognome, ss.nota, ss.status "
					+ " FROM suggerimento_sostituto ss, utente u "
					+ " WHERE ss.id_sostituto = u.matricola AND ss.status = 0 AND ss.id_appello = " + id_appello 
					+ " AND ss.id_richiedente = " + matricola;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return new SuggerimentoSostituto(rs.getInt("id"), rs.getInt("id_appello"), rs.getInt("id_richiedente"),rs.getInt("id_sostituto"),
						rs.getString("nome") + " " + rs.getString("cognome"), rs.getString("nota"), rs.getInt("status"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<SuggerimentoSostituto> getProposteByAppelloFromDB(int id_appello){
		Connection connection = null;
		ArrayList<SuggerimentoSostituto> proposte = new ArrayList<SuggerimentoSostituto>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT ss.id, ss.id_appello, ss.id_richiedente, uu.nome as nome_r, uu.cognome as cognome_r, "
					+ "ss.id_sostituto, u.nome as nome_s, u.cognome as cognome_s, ss.nota, ss.status "
					+ "FROM suggerimento_sostituto ss "
					+ "LEFT JOIN utente u ON ss.id_sostituto = u.matricola "
					+ "LEFT JOIN utente uu ON ss.id_richiedente = uu.matricola "
					+ "WHERE ss.status = 0 AND ss.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				proposte.add(new SuggerimentoSostituto(rs.getInt("id"), rs.getInt("id_appello"), rs.getInt("id_richiedente"),
						rs.getInt("id_sostituto"), rs.getString("nome_s") + " " + rs.getString("cognome_s"), 
						rs.getString("nome_r") + " " + rs.getString("cognome_r"), rs.getString("nota"), rs.getInt("status")));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return proposte;
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
	
	public ArrayList<AppelloTesi> getAppelliCompletati() {
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
					+ "LEFT JOIN aula au on pag.id_aula = au.id_aula "
					+ "WHERE a.status = 3";
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
	
	public PresidenteScuola getPresidenteScuola() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT matricola, nome, cognome FROM utente WHERE ruolo = 2";
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return new PresidenteScuola(rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public PresidenteCorso getPresidenteCorsoByAppello(AppelloTesi appello) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement();
			String query = "SELECT u.matricola, u.nome, u.cognome, c.id_corso, c.nome as nome_corso "
					+ "FROM utente u, corso c, appello a "
					+ "WHERE a.id_corso = c.id_corso AND c.presidente = u.matricola AND a.id_appello = " + appello.getId();
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return new PresidenteCorso(rs.getString("matricola"), rs.getString("nome"), rs.getString("cognome"),
						Pair.of(rs.getInt("id_corso"), rs.getString("nome_corso")));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
			String query = "SELECT us.matricola as matricola_studente, us.nome as nome_studente, us.cognome as cognome_studente, d.repository, "
					+ "ur.matricola as matricola_relatore, ur.nome as nome_relatore, ur.cognome as cognome_relatore "
					+ "FROM appello_membro am, utente us, utente ur, domandatesi d "
					+ "WHERE am.matricola = us.matricola AND d.matricola = am.matricola AND ur.matricola = d.relatore "
					+ "AND am.ruolo = 0 AND am.id_appello = " + id_appello;
			Console.print(query, "sql");
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				studentiRelatori.add(Pair.of(new Studente(rs.getString("nome_studente"), rs.getString("cognome_studente"), 
															rs.getString("matricola_studente"), rs.getString("repository")), 
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

	public ArrayList<Docente> getCommissioneNoRelatoreByAppello(int id_appello) {
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
	
	public ArrayList<Docente> getCommissioneByAppello(int id_appello) {
		Connection connection = null;
		ArrayList<Docente> commissioni = new ArrayList<Docente>();
		try {
			connection = DriverManager.getConnection(connectionString);
			Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT ut.cognome, ut.nome, ut.matricola from appello_membro aps, utente as ut WHERE (aps.matricola = ut.matricola "
					+ "AND aps.id_appello = " + id_appello + ") AND (aps.ruolo = 2 OR aps.ruolo = 1)";
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
