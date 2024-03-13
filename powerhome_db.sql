-- phpMyAdmin SQL Dump
-- version 4.5.4.1
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Mer 13 Mars 2024 à 00:22
-- Version du serveur :  5.7.11
-- Version de PHP :  7.0.3

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
(69, 'Radiateur électrique', 'D27DE', 900, 41);

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
  `consommation` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Contenu de la table `habitat`
--

INSERT INTO `habitat` (`id`, `floor`, `area`, `bonus`, `malus`, `consommation`) VALUES
(38, 2, 3, 0, 0, 4200),
(39, 5, 5, 0, 0, 2500),
(41, 4, 4, 0, 0, 900);

-- --------------------------------------------------------

--
-- Structure de la table `time_slot`
--

CREATE TABLE `time_slot` (
  `id` int(11) NOT NULL,
  `begin` datetime NOT NULL,
  `end` datetime NOT NULL,
  `max_wattage` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
(41, 'Carou', 'Alice', '18/02/2000', 'alice@gmail.com', '$2y$10$hcGFjvAXKHKzT.SwbElQ1O3X.9LASStownJSrNng9ysZ7avsb.ICm', 'Quel serait mon rêve ?', 'Voyager', 'NO', 41);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;
--
-- AUTO_INCREMENT pour la table `habitat`
--
ALTER TABLE `habitat`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;
--
-- AUTO_INCREMENT pour la table `time_slot`
--
ALTER TABLE `time_slot`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(150) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;
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
