DROP DATABASE IF EXISTS gestionaletesi;
CREATE DATABASE gestionaletesi;
USE gestionaletesi;

-- 0 = studente, 1 = responsabile,  2 = presidente scuola, 3 = presidente corso
-- 4 = docente
CREATE TABLE utente(
	matricola int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) NOT NULL,
	cognome varchar(50) DEFAULT NULL,
	password  varchar(50) NOT NULL,
	ruolo int not NULL COMMENT '0 = studente, 1 = responsabile, 2 = presidente scuola, 3 = presidente corso, 4 = docente, 10 = Sistema'
)AUTO_INCREMENT=10000;

CREATE TABLE corso(
	id_corso int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) NOT NULL,
	presidente int NOT NULL,
	FOREIGN KEY (presidente) REFERENCES utente(matricola)
)AUTO_INCREMENT=20000;

CREATE TABLE appello(
	id_appello int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	id_corso int NOT NULL,
	data date DEFAULT NULL,
	pubblicato_da int NOT NULL,
	pub_data date NOT NULL,
	teleconferenza varchar(50) DEFAULT NULL,
	nota text DEFAULT NULL,
	orario time DEFAULT NULL,
	status int DEFAULT 0 COMMENT '1 = approvato, 2 = richiedi correzione, 3 = completato, 4 = scaduto, 5 = annullato',
	FOREIGN KEY (pubblicato_da) REFERENCES utente(matricola),
	FOREIGN KEY (id_corso ) REFERENCES corso(id_corso )
)AUTO_INCREMENT=30000;

CREATE TABLE dipartimento(
	id_dipartimento int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) DEFAULT NULL
)AUTO_INCREMENT=40000;

CREATE TABLE aula(
	id_aula int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) NOT NULL
)AUTO_INCREMENT=50000;

CREATE TABLE domandatesi(
	matricola int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	relatore int NOT NULL,
	data date NOT NULL,
	id_corso int NOT NULL,
	repository varchar(50) DEFAULT NULL,
	approvato boolean DEFAULT 0,
	voto int DEFAULT NULL,
	FOREIGN KEY (matricola) REFERENCES utente(matricola),
	FOREIGN KEY (relatore) REFERENCES utente(matricola),
	FOREIGN KEY (id_corso) REFERENCES corso(id_corso)
)AUTO_INCREMENT=60000;

CREATE TABLE suggerimento_sostituto(
	id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	id_appello int NOT NULL,
	id_richiedente int NOT NULL,
	id_sostituto int NOT NULL,
	nota text DEFAULT NULL,
	status tinyint DEFAULT 0 COMMENT '0 = in attesa, 1 = approvato, 2 = rifiutato',
	FOREIGN KEY (id_appello) REFERENCES appello(id_appello),
	FOREIGN KEY (id_richiedente) REFERENCES utente(matricola),
	FOREIGN KEY (id_sostituto) REFERENCES utente(matricola)
)AUTO_INCREMENT=70000;

CREATE TABLE messaggio(
	id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	id_sorgente int NOT NULL,
	id_destinatario int NOT NULL,
	contenuto text DEFAULT NULL,
	letto boolean DEFAULT 0,
	FOREIGN KEY (id_sorgente) REFERENCES utente(matricola),
	FOREIGN KEY (id_destinatario) REFERENCES utente(matricola)
)AUTO_INCREMENT=80000;

CREATE TABLE prenotazione_aula_giorno(
	id_aula int NOT NULL,
	id_appello int NOT NULL,
	personale int NOT NULL,
	FOREIGN KEY (id_aula) REFERENCES aula(id_aula),
	FOREIGN KEY (id_appello) REFERENCES appello(id_appello),
	FOREIGN KEY (personale ) REFERENCES utente(matricola),
	PRIMARY KEY (id_appello)
);

CREATE TABLE docente_dipartimento(
	id_dipartimento int NOT NULL,
	matricola int NOT NULL PRIMARY KEY,
	FOREIGN KEY (id_dipartimento) REFERENCES dipartimento(id_dipartimento),
	FOREIGN KEY (matricola) REFERENCES utente(matricola)
);

CREATE TABLE appello_membro(
	id_appello int NOT NULL,
	matricola int NOT NULL,
	ruolo int DEFAULT 0 COMMENT '0 = Studente, 1 = Membro Della Commissione, 2 = Relatore, 3 = Presidente della commissione',
	FOREIGN KEY (id_appello) REFERENCES appello(id_appello),
	FOREIGN KEY (matricola) REFERENCES utente(matricola),
	PRIMARY KEY (id_appello, matricola)
);

DELIMITER $$
CREATE TRIGGER appello_date_change 
AFTER UPDATE ON appello FOR EACH ROW
BEGIN
IF NEW.data <> OLD.data THEN 
	BEGIN
	DELETE FROM prenotazione_aula_giorno WHERE id_appello = NEW.id_appello;
	END;
END IF;
END;
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER ritira_domanda 
AFTER DELETE ON domandatesi FOR EACH ROW
BEGIN
	DELETE FROM appello_membro WHERE appello_membro.matricola = old.matricola OR appello_membro.matricola = old.relatore;
END;
$$
DELIMITER ;

-- popolamento utente
INSERT INTO utente (matricola, nome, cognome, password, ruolo)
VALUES(100, "Sistema", "Sistema", 123, 10); -- Sistema
INSERT INTO utente (nome, cognome, password, ruolo)
VALUES("Tizio", "Caio", 123, 0), -- 10000 Studente
("Riccardo", "Cremonesi", 123, 0), -- 10001 Studente
("Gianni", "Manfrin", 123, 1), -- 10002 Responsabile 
("Raniero", "Calabrese", 123, 2), -- 10003 Presidente della scuola
("Libero", "Siciliano", 123, 3), -- 10004 Presidente del corso di Biomedica 
("Gofreddo", "Lombardi", 123, 3), -- 10005 Presidente del corso di Elettronica 
("Delfino", "Pirozzi", 123, 3), -- 10006 Presidente del corso di Informatica 
("Brancaleone", "Lucciano", 123, 3), -- 10007 Presidente del corso di Meccanica 
("Biagio", "Bellucci", 123, 3), -- 10008 Presidente del corso di Edile 
("Liberato", "Zetticci", 123, 4), 
("Cristiano", "Calabresi", 123, 4), 
("Marino", "Lorenzo", 123, 4), 
("Eugenio", "Nucci", 123, 4), 
("Marzio", "Milanesi", 123, 4), 
("Valentino", "Sabbatini", 123, 4), 
("Baldassarre", "Lucciano", 123, 4),
("Romeo", "Dellucci", 123, 4),
("Alvise", "Pagnotto", 123, 4),
("Pio", "Lori", 123, 4),
("Carola", "Davide", 123, 4),
("Geronimo", "Bianchi", 123, 4),
("Lino", "Toscano", 123, 4),
("Lino", "Bellucci", 123, 4),
("Caton", "Toscano", 123, 4),
("Toscano", "Rossi", 123, 4),
("Beato", "Romani", 123, 4),
("Beato", "Dellucci", 123, 4),
("Cino", "Mancini", 123, 4),
("Cino", "Esposito", 123, 4);

-- popolamento corso
INSERT INTO corso (nome, presidente)
VALUES("Biomedica", 10004), 
("Elettronica", 10005), 
("Informatica", 10006), 
("Meccanica", 10007), 
("Edile", 10008);

-- popolamento dipartimento
INSERT INTO dipartimento (nome)
VALUES("Dipartimento di Medicina Sperimentale e Clinica"), 
("Dipartimento di Architettura"), 
("Dipartimento di Ingegneria dell'Informazione"), 
("Dipartimento di Ingegneria Industriale"), 
("Dipartimento di Biologia");

-- popolamento aula
INSERT INTO aula (nome)
VALUES("Aula 101"), 
("Aula 102"), 
("Aula 103"), 
("Aula 104"),
("Aula 105"),
("Aula 106"),
("Aula 107"),
("Aula 108"),
("Aula 109"),
("Aula 110");

-- docente-dipartimento
INSERT INTO docente_dipartimento (id_dipartimento, matricola)
VALUES(40000, 10009),
(40000, 10010),
(40000, 10011),
(40000, 10012),
(40001, 10013),
(40001, 10014),
(40001, 10015),
(40001, 10016),
(40002, 10017),
(40002, 10018),
(40002, 10019),
(40002, 10020),
(40003, 10021),
(40003, 10022),
(40003, 10023),
(40003, 10024),
(40004, 10025),
(40004, 10026),
(40004, 10027),
(40004, 10028);

-- popolamento appello
INSERT INTO appello (id_corso, pubblicato_da, pub_data)
VALUES(20000, 10002, 2022-4-1), 
(20000, 10002, 2022-4-2),
(20000, 10002, 2022-4-3),
(20000, 10002, 2022-4-4),
(20001, 10002, 2022-5-5), 
(20001, 10002, 2022-5-6), 
(20001, 10002, 2022-5-7), 
(20001, 10002, 2022-5-8), 
(20002, 10002, 2022-6-9), 
(20002, 10002, 2022-6-10), 
(20002, 10002, 2022-6-11), 
(20002, 10002, 2022-6-12), 
(20003, 10002, 2022-7-13), 
(20003, 10002, 2022-7-14), 
(20003, 10002, 2022-7-15), 
(20003, 10002, 2022-7-16), 
(20004, 10002, 2022-8-17),
(20004, 10002, 2022-8-18),
(20004, 10002, 2022-8-19),
(20004, 10002, 2022-8-20);