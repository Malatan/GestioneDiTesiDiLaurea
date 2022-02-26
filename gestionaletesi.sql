-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Feb 26, 2022 alle 15:27
-- Versione del server: 10.4.22-MariaDB
-- Versione PHP: 7.4.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestionaletesi`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `appelli`
--

CREATE TABLE `appelli` (
  `idAppello` int(11) NOT NULL,
  `data` datetime NOT NULL,
  `idPresidente` int(11) NOT NULL,
  `idAula` int(11) DEFAULT NULL,
  `informazioni` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `appelli`
--

INSERT INTO `appelli` (`idAppello`, `data`, `idPresidente`, `idAula`, `informazioni`) VALUES
(6, '1999-12-13 14:13:00', 0, 8, 'L\'appello si svolgerà il 13/01/2000 alle ore 13:12 Link meet: https://....com'),
(7, '1999-12-13 14:14:00', 0, NULL, NULL),
(8, '1999-12-13 14:15:00', 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Struttura della tabella `aule`
--

CREATE TABLE `aule` (
  `id` int(11) NOT NULL,
  `numAula` varchar(512) NOT NULL,
  `libera` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `aule`
--

INSERT INTO `aule` (`id`, `numAula`, `libera`) VALUES
(1, '1', 1),
(2, '2', 1),
(5, '3', 1),
(6, '4', 1),
(7, '100', 1),
(8, 'Auditorium A', 0),
(9, '102', 1),
(10, '103', 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--

CREATE TABLE `users` (
  `matricola` int(11) NOT NULL,
  `nome` varchar(512) NOT NULL,
  `cognome` varchar(512) NOT NULL,
  `password` varchar(512) NOT NULL,
  `ruolo` int(11) NOT NULL DEFAULT 0 COMMENT '0 = studente, 1 = responsabile, 2 = presidente scuola, 3= presidente corso, 4 =relatore, 5 =membrocomm, 6 = presidentecommissione'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `users`
--

INSERT INTO `users` (`matricola`, `nome`, `cognome`, `password`, `ruolo`) VALUES
(1, 'Tizio', 'Tizio', 'test', 1),
(2, 'Caio', 'Caio', 'test', 2),
(3, 'Senpronio', 'Senpronio', 'test', 3),
(4, 'Mario', 'Rossi', 'test', 4),
(5, 'Will', 'Smith', 'test', 5),
(6, 'Test', 'Test', 'test', 6),
(22, 'Chaohao', 'Zheng', 'test', 0),
(25, 'William', 'Zhao', 'test', 0);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `appelli`
--
ALTER TABLE `appelli`
  ADD PRIMARY KEY (`idAppello`);

--
-- Indici per le tabelle `aule`
--
ALTER TABLE `aule`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`matricola`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `appelli`
--
ALTER TABLE `appelli`
  MODIFY `idAppello` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT per la tabella `aule`
--
ALTER TABLE `aule`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT per la tabella `users`
--
ALTER TABLE `users`
  MODIFY `matricola` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
