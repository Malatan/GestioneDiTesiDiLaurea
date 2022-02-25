-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Feb 25, 2022 alle 13:03
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
-- Indici per le tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`matricola`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `users`
--
ALTER TABLE `users`
  MODIFY `matricola` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
