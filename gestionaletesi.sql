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
	ruolo int not NULL
)AUTO_INCREMENT=10000;

CREATE TABLE corso(
	id_corso int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) NOT NULL,
	presidente int NOT NULL,
	FOREIGN KEY (presidente) REFERENCES utente(matricola)
)AUTO_INCREMENT=20000;

CREATE TABLE appello(
	id_appello int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	data date DEFAULT NULL,
	nota text DEFAULT NULL
)AUTO_INCREMENT=30000;

CREATE TABLE dipartimento(
	id_dipartimento int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nome varchar(50) DEFAULT NULL
)AUTO_INCREMENT=40000;

CREATE TABLE docente_dipartimento(
	id_dipartimento int NOT NULL,
	matricola int NOT NULL PRIMARY KEY,
	FOREIGN KEY (id_dipartimento) REFERENCES dipartimento(id_dipartimento),
	FOREIGN KEY (matricola) REFERENCES utente(matricola)
);

/*
CREATE TABLE appello_studente(

);

CREATE TABLE appello_docente(

);*/

CREATE TABLE domandatesi(
	matricola int NOT NULL,
	data date NOT NULL,
	id_corso int NOT NULL,
	FOREIGN KEY (matricola) REFERENCES utente(matricola),
	FOREIGN KEY (id_corso) REFERENCES corso(id_corso)
);

-- popolamento utente
INSERT INTO utente (nome, cognome, password, ruolo)
VALUES("Tizio", "Caio", 123, 0), 
("Riccardo", "Cremonesi", 123, 0), 
("Gianni", "Manfrin", 123, 1),
("Raniero", "Calabrese", 123, 2), 
("Libero", "Siciliano", 123, 3), 
("Gofreddo", "Lombardi", 123, 3), 
("Delfino", "Pirozzi", 123, 3), 
("Brancaleone", "Lucciano", 123, 3), 
("Biagio", "Bellucci", 123, 3), 
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