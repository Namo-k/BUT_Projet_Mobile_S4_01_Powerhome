-- phpMyAdmin SQL Dump
-- version 4.5.4.1
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Jeu 14 Mars 2024 à 05:05
-- Version du serveur :  5.7.11
-- Version de PHP :  5.6.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `powerhome_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `appliance`
--

CREATE TABLE `appliance` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `reference` varchar(100) NOT NULL,
  `wattage` int(11) NOT NULL,
  `habitat_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='http://localhost:8000/powerhome_server/adminer.php?file=plus.gif&version=4.8.1';

--
-- Contenu de la table `appliance`
--

INSERT INTO `appliance` (`id`, `name`, `reference`, `wattage`, `habitat_id`) VALUES
(62, 'Television', 'Samsung82729', 800, 38),
(63, 'Aspirateur', 'Miele918012', 1200, 38),
(64, 'Four électrique', 'SIEMENS28391', 1400, 38),
(65, 'Radiateur électrique', '18317', 800, 38),
(66, 'Four électrique', '9B273A', 900, 39),
(67, 'Fer à repasser', '0D92E', 700, 39),
(68, 'Aspirateur', 'D9283JD', 900, 39),
(69, 'Radiateur électrique', 'D27DE', 900, 41),
(78, 'Aspirateur', 'Asp1444', 180, 43);

-- --------------------------------------------------------

--
-- Structure de la table `appliance_time_slot`
--

CREATE TABLE `appliance_time_slot` (
  `appliance_id` int(11) NOT NULL,
  `time_slot_id` int(11) NOT NULL,
  `order` int(11) NOT NULL,
  `booked_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Contenu de la table `appliance_time_slot`
--

INSERT INTO `appliance_time_slot` (`appliance_id`, `time_slot_id`, `order`, `booked_at`) VALUES
(68, 4, 1, '2024-03-14 04:00:11'),
(68, 25, 1, '2024-03-14 04:00:52'),
(66, 26, 1, '2024-03-14 04:01:48'),
(67, 9, 1, '2024-03-14 04:11:30'),
(66, 8, 1, '2024-03-14 04:12:30'),
(66, 6, 1, '2024-03-14 04:23:07'),
(68, 7, 1, '2024-03-14 04:23:46'),
(68, 6, 2, '2024-03-14 04:29:32'),
(68, 6, 3, '2024-03-14 04:30:52'),
(68, 6, 4, '2024-03-14 04:31:08'),
(68, 6, 5, '2024-03-14 04:31:09'),
(68, 6, 6, '2024-03-14 04:31:10'),
(68, 6, 7, '2024-03-14 04:31:26'),
(68, 6, 8, '2024-03-14 04:31:56'),
(68, 6, 9, '2024-03-14 04:32:30'),
(68, 6, 10, '2024-03-14 04:32:46'),
(68, 6, 11, '2024-03-14 04:32:55'),
(66, 5, 1, '2024-03-14 04:33:25'),
(67, 5, 2, '2024-03-14 04:34:16'),
(68, 6, 12, '2024-03-14 04:34:57'),
(66, 28, 1, '2024-03-14 04:39:39'),
(67, 27, 1, '2024-03-14 04:40:41'),
(66, 29, 1, '2024-03-14 04:42:17');

-- --------------------------------------------------------

--
-- Structure de la table `habitat`
--

CREATE TABLE `habitat` (
  `id` int(100) NOT NULL,
  `floor` int(11) NOT NULL,
  `area` int(11) NOT NULL,
  `bonus` int(11) NOT NULL DEFAULT '0',
  `malus` int(11) NOT NULL DEFAULT '0',
  `essai` int(11) NOT NULL DEFAULT '0',
  `consommation` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Contenu de la table `habitat`
--

INSERT INTO `habitat` (`id`, `floor`, `area`, `bonus`, `malus`, `essai`, `consommation`) VALUES
(38, 2, 3, 0, 0, 0, 4200),
(39, 5, 5, 13, 1, 3, 2675),
(41, 4, 4, 0, 0, 0, 900),
(43, 2, 2, 0, 0, 0, 180);

-- --------------------------------------------------------

--
-- Structure de la table `time_slot`
--

CREATE TABLE `time_slot` (
  `id` int(11) NOT NULL,
  `begin` datetime NOT NULL,
  `end` datetime NOT NULL,
  `max_wattage` int(11) NOT NULL DEFAULT '5000',
  `wattage_used` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Contenu de la table `time_slot`
--

INSERT INTO `time_slot` (`id`, `begin`, `end`, `max_wattage`, `wattage_used`) VALUES
(1, '2024-03-14 00:00:00', '2024-03-14 01:00:00', 25000, 0),
(2, '2024-03-14 01:00:00', '2024-03-14 02:00:00', 25000, 0),
(3, '2024-03-14 02:00:00', '2024-03-14 03:00:00', 25000, 0),
(4, '2024-03-14 03:00:00', '2024-03-14 04:00:00', 25000, 900),
(5, '2024-03-14 04:00:00', '2024-03-14 05:00:00', 25000, 1600),
(6, '2024-03-14 05:00:00', '2024-03-14 06:00:00', 25000, 30800),
(7, '2024-03-14 06:00:00', '2024-03-14 07:00:00', 25000, 1800),
(8, '2024-03-14 07:00:00', '2024-03-14 08:00:00', 25000, 900),
(9, '2024-03-14 08:00:00', '2024-03-14 09:00:00', 25000, 700),
(10, '2024-03-14 09:00:00', '2024-03-14 10:00:00', 25000, 0),
(11, '2024-03-14 10:00:00', '2024-03-14 11:00:00', 25000, 0),
(12, '2024-03-14 11:00:00', '2024-03-14 12:00:00', 25000, 0),
(13, '2024-03-14 12:00:00', '2024-03-14 13:00:00', 25000, 0),
(14, '2024-03-14 13:00:00', '2024-03-14 14:00:00', 25000, 0),
(15, '2024-03-14 14:00:00', '2024-03-14 15:00:00', 25000, 0),
(16, '2024-03-14 15:00:00', '2024-03-14 16:00:00', 25000, 0),
(17, '2024-03-14 16:00:00', '2024-03-14 17:00:00', 25000, 0),
(18, '2024-03-14 17:00:00', '2024-03-14 18:00:00', 25000, 0),
(19, '2024-03-14 18:00:00', '2024-03-14 19:00:00', 25000, 0),
(20, '2024-03-14 19:00:00', '2024-03-14 20:00:00', 25000, 0),
(21, '2024-03-14 20:00:00', '2024-03-14 21:00:00', 25000, 0),
(22, '2024-03-14 21:00:00', '2024-03-14 22:00:00', 25000, 0),
(23, '2024-03-14 22:00:00', '2024-03-14 23:00:00', 25000, 0),
(24, '2024-03-14 23:00:00', '2024-03-15 00:00:00', 25000, 0),
(25, '2024-03-15 03:00:00', '2024-03-15 04:00:00', 25000, 900),
(26, '2024-03-16 05:00:00', '2024-03-16 06:00:00', 25000, 900),
(27, '2024-03-15 05:00:00', '2024-03-15 06:00:00', 25000, 700),
(28, '2024-03-15 06:00:00', '2024-03-15 07:00:00', 25000, 900),
(29, '2024-03-16 04:00:00', '2024-03-16 05:00:00', 25000, 900),
(30, '2024-03-18 02:00:00', '2024-03-18 03:00:00', 25000, 900),
(31, '2024-03-19 05:00:00', '2024-03-19 06:00:00', 25000, 1600),
(32, '2024-03-18 04:00:00', '2024-03-18 05:00:00', 25000, 900);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` int(150) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `firstname` varchar(100) NOT NULL,
  `birth` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `question` varchar(100) NOT NULL,
  `response` varchar(100) NOT NULL,
  `firstTime` varchar(20) NOT NULL DEFAULT 'YES',
  `habitat_id` int(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`id`, `lastname`, `firstname`, `birth`, `email`, `password`, `question`, `response`, `firstTime`, `habitat_id`) VALUES
(38, 'Carounanithi', 'Alexandre', '17/12/2004', 'a@gmail.com', '$2y$10$cIf3HGBpUb.TvnftkntiauGxttbTTALl8TP34u7N2ptevCwdNyPPm', 'Quel est le premier livre lu ?', 'Hunger games', 'NO', 38),
(39, 'Kaliamoorthy', 'Namodacane', '01/01/2000', 'k@gmail.com', '$2y$10$2EUuMKalUEH50kRaSgoEB.4tR4fz3oalo28Gv3U.5dgg2EfvGS8Q.', 'Quel est le nom de ma peluche ?', 'Teddy', 'NO', 39),
(41, 'Carou', 'Alice', '18/02/2000', 'alice@gmail.com', '$2y$10$hcGFjvAXKHKzT.SwbElQ1O3X.9LASStownJSrNng9ysZ7avsb.ICm', 'Quel serait mon rêve ?', 'Voyager', 'NO', 41),
(43, 'Chaouche', 'Ahmed', '15/08/1998', 'ac@gmail.com', '$2y$10$MEDEPvp5sUzQ6jtCIgIKA.Qg7OfZB3jjbqJDMsounGByGz.aCb4Aq', 'Quel serait mon rêve ?', 'Enseigner', 'NO', 43);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `appliance`
--
ALTER TABLE `appliance`
  ADD PRIMARY KEY (`id`),
  ADD KEY `habitat_id` (`habitat_id`);

--
-- Index pour la table `appliance_time_slot`
--
ALTER TABLE `appliance_time_slot`
  ADD KEY `appliance_id` (`appliance_id`),
  ADD KEY `time_slot_id` (`time_slot_id`);

--
-- Index pour la table `habitat`
--
ALTER TABLE `habitat`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `time_slot`
--
ALTER TABLE `time_slot`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `habitat_id` (`habitat_id`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `appliance`
--
ALTER TABLE `appliance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;
--
-- AUTO_INCREMENT pour la table `habitat`
--
ALTER TABLE `habitat`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;
--
-- AUTO_INCREMENT pour la table `time_slot`
--
ALTER TABLE `time_slot`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(150) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `appliance`
--
ALTER TABLE `appliance`
  ADD CONSTRAINT `appliance_ibfk_1` FOREIGN KEY (`habitat_id`) REFERENCES `habitat` (`id`);

--
-- Contraintes pour la table `appliance_time_slot`
--
ALTER TABLE `appliance_time_slot`
  ADD CONSTRAINT `appliance_time_slot_ibfk_1` FOREIGN KEY (`appliance_id`) REFERENCES `appliance` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `appliance_time_slot_ibfk_2` FOREIGN KEY (`time_slot_id`) REFERENCES `time_slot` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `user_habitat` FOREIGN KEY (`habitat_id`) REFERENCES `habitat` (`id`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
