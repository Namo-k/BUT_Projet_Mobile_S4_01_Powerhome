-- phpMyAdmin SQL Dump
-- version 4.5.4.1
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Mar 26 Mars 2024 à 11:55
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
(69, 'Aspirateur', 'MIELE827398', 900, 40),
(70, 'Television', 'mziduz', 900, 40),
(74, 'Aspirateur', 'FF', 500, 44),
(77, 'Aspirateur', 'FFFF', 500, 39),
(79, 'Climatiseur', '44kkDD', 500, 39);

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
(65, 9, 1, '2024-03-14 06:59:46'),
(65, 10, 1, '2024-03-14 06:59:52'),
(65, 11, 1, '2024-03-14 06:59:57'),
(62, 11, 2, '2024-03-14 07:00:05'),
(62, 9, 2, '2024-03-14 07:00:12'),
(64, 18, 1, '2024-03-14 07:01:21'),
(63, 18, 2, '2024-03-14 07:01:26'),
(63, 19, 1, '2024-03-14 07:01:32'),
(65, 23, 1, '2024-03-14 07:01:42'),
(65, 22, 1, '2024-03-14 07:01:47'),
(65, 25, 1, '2024-03-14 07:02:32'),
(66, 13, 1, '2024-03-14 07:03:25'),
(66, 18, 3, '2024-03-14 07:03:31'),
(67, 17, 1, '2024-03-14 07:03:39'),
(68, 21, 1, '2024-03-14 07:03:52'),
(68, 22, 2, '2024-03-14 07:03:58'),
(68, 26, 1, '2024-03-14 07:04:15'),
(67, 27, 1, '2024-03-14 07:05:44'),
(67, 28, 1, '2024-03-14 07:05:50'),
(70, 29, 1, '2024-03-15 12:05:53'),
(74, 32, 1, '2024-03-25 11:23:14'),
(66, 36, 1, '2024-03-25 11:58:26'),
(67, 39, 1, '2024-03-25 14:12:12'),
(67, 40, 1, '2024-03-25 14:12:45'),
(67, 40, 2, '2024-03-25 14:12:45'),
(66, 42, 1, '2024-03-25 14:22:14'),
(66, 42, 2, '2024-03-25 14:22:14'),
(66, 42, 3, '2024-03-25 14:22:14'),
(67, 44, 1, '2024-03-25 14:36:45'),
(67, 48, 1, '2024-03-25 15:41:16'),
(68, 47, 1, '2024-03-25 15:43:29'),
(67, 49, 1, '2024-03-25 15:55:49'),
(67, 50, 1, '2024-03-25 15:55:49'),
(67, 49, 2, '2024-03-25 16:03:45'),
(66, 51, 1, '2024-03-25 16:08:18'),
(66, 52, 1, '2024-03-25 16:08:18'),
(66, 53, 1, '2024-03-25 16:08:18'),
(66, 44, 2, '2024-03-25 16:08:18'),
(66, 50, 2, '2024-03-25 17:25:19'),
(66, 54, 1, '2024-03-25 17:25:19'),
(66, 55, 1, '2024-03-26 12:32:13'),
(66, 56, 1, '2024-03-26 12:32:13'),
(66, 57, 1, '2024-03-26 12:32:13'),
(66, 58, 1, '2024-03-26 12:32:13'),
(66, 59, 1, '2024-03-26 12:32:13'),
(66, 60, 1, '2024-03-26 12:34:31'),
(66, 61, 1, '2024-03-26 12:34:31');

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
(38, 2, 3, 21, 0, 0, 4200),
(39, 5, 5, 46, 0, 0, 3675),
(40, 2, 2, 1, 0, 0, 1800),
(42, 2, 2, 2, 0, 0, 1650),
(43, 2, 4, 0, 0, 0, 0),
(44, 2, 2, 1, 0, 0, 500),
(45, 2, 2, 0, 0, 0, 500),
(46, 1, 1, 0, 0, 0, 500);

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

CREATE TABLE `notification` (
  `id` int(11) NOT NULL,
  `notification_title` varchar(255) NOT NULL,
  `notification` varchar(255) NOT NULL,
  `notification_categorie` varchar(255) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `notification`
--

INSERT INTO `notification` (`id`, `notification_title`, `notification`, `notification_categorie`, `id_user`) VALUES
(1, 'Sélection d\'un créneau', 'Vous avez réservé le créneau de 9h à 15h pour votre EQUIPEMENT. Vous avez gagné/écopé d’1 bonus/malus.', 'creneau', 39),
(22, 'Ajout d\'un équipement', 'Vous avez ajouté un Aspirateur Ref. FFFF de 500W à votre logement.', 'equipement', 39),
(23, 'Ajout d\'un équipement', 'Vous avez ajouté un Climatiseur Ref. HHHH de 500W à votre logement.', 'equipement', 39),
(24, 'Modification d\'un équipement', 'Vous avez modifié : Climatiseur Ref. HHHHHHH de 500W de votre logement.', 'equipement', 39),
(25, 'Supression d\'un équipement', 'Vous avez supprimé : Climatiseur de votre logement.', 'equipement', 39),
(26, 'Ajout d\'un équipement', 'Vous avez ajouté un Climatiseur Ref. 44kkkk de 500W à votre logement.', 'equipement', 39),
(32, 'Sélection d\'un créneau', 'Vous avez réservé le créneau de 02h à 04h pour votre 66 Four électrique.', 'creneau', 39),
(33, 'Configuration', 'Vous avez modifié votre profil.', 'configuration', 39),
(34, 'Modification d\'un équipement', 'Vous avez modifié : Climatiseur Ref. 44kkDD de 500W de votre logement.', 'equipement', 39);

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
(4, '2024-03-14 03:00:00', '2024-03-14 04:00:00', 25000, 0),
(5, '2024-03-14 04:00:00', '2024-03-14 05:00:00', 25000, 0),
(6, '2024-03-14 05:00:00', '2024-03-14 06:00:00', 25000, 0),
(7, '2024-03-14 06:00:00', '2024-03-14 07:00:00', 25000, 0),
(8, '2024-03-14 07:00:00', '2024-03-14 08:00:00', 25000, 0),
(9, '2024-03-14 08:00:00', '2024-03-14 09:00:00', 25000, 1600),
(10, '2024-03-14 09:00:00', '2024-03-14 10:00:00', 25000, 800),
(11, '2024-03-14 10:00:00', '2024-03-14 11:00:00', 25000, 1600),
(12, '2024-03-14 11:00:00', '2024-03-14 12:00:00', 25000, 0),
(13, '2024-03-14 12:00:00', '2024-03-14 13:00:00', 25000, 900),
(14, '2024-03-14 13:00:00', '2024-03-14 14:00:00', 25000, 0),
(15, '2024-03-14 14:00:00', '2024-03-14 15:00:00', 25000, 0),
(16, '2024-03-14 15:00:00', '2024-03-14 16:00:00', 25000, 0),
(17, '2024-03-14 16:00:00', '2024-03-14 17:00:00', 25000, 700),
(18, '2024-03-14 17:00:00', '2024-03-14 18:00:00', 25000, 3500),
(19, '2024-03-14 18:00:00', '2024-03-14 19:00:00', 25000, 1200),
(20, '2024-03-14 19:00:00', '2024-03-14 20:00:00', 25000, 0),
(21, '2024-03-14 20:00:00', '2024-03-14 21:00:00', 25000, 900),
(22, '2024-03-14 21:00:00', '2024-03-14 22:00:00', 25000, 1700),
(23, '2024-03-14 22:00:00', '2024-03-14 23:00:00', 25000, 800),
(24, '2024-03-14 23:00:00', '2024-03-15 00:00:00', 25000, 0),
(25, '2024-03-16 21:00:00', '2024-03-16 22:00:00', 25000, 800),
(26, '2024-03-15 21:00:00', '2024-03-15 22:00:00', 25000, 900),
(27, '2024-03-17 21:00:00', '2024-03-17 22:00:00', 25000, 700),
(28, '2024-03-17 16:00:00', '2024-03-17 17:00:00', 25000, 700),
(29, '2024-03-16 06:00:00', '2024-03-16 07:00:00', 25000, 900),
(30, '2024-03-22 16:00:00', '2024-03-22 17:00:00', 25000, 150),
(31, '2024-03-22 17:00:00', '2024-03-22 18:00:00', 25000, 150),
(32, '2024-03-25 12:00:00', '2024-03-25 13:00:00', 25000, 500),
(36, '2024-03-25 17:00:00', '2024-03-25 18:00:00', 25000, 900),
(39, '2024-03-26 19:00:00', '2024-03-26 20:00:00', 25000, 700),
(40, '2024-03-26 20:00:00', '2024-03-26 22:00:00', 25000, 700),
(42, '2024-03-26 00:00:00', '2024-03-26 03:00:00', 25000, 900),
(43, '2024-03-26 04:00:00', '2024-03-26 08:00:00', 25000, 900),
(44, '2024-03-26 04:00:00', '2024-03-26 05:00:00', 25000, 1600),
(45, '2024-03-26 05:00:00', '2024-03-26 06:00:00', 25000, 0),
(46, '2024-03-26 06:00:00', '2024-03-26 07:00:00', 25000, 0),
(47, '2024-03-26 07:00:00', '2024-03-26 08:00:00', 25000, 900),
(48, '2024-03-26 16:00:00', '2024-03-26 17:00:00', 25000, 700),
(49, '2024-03-27 02:00:00', '2024-03-27 03:00:00', 25000, 1400),
(50, '2024-03-27 03:00:00', '2024-03-27 04:00:00', 25000, 1600),
(51, '2024-03-26 01:00:00', '2024-03-26 02:00:00', 25000, 900),
(52, '2024-03-26 02:00:00', '2024-03-26 03:00:00', 25000, 900),
(53, '2024-03-26 03:00:00', '2024-03-26 04:00:00', 25000, 900),
(54, '2024-03-27 04:00:00', '2024-03-27 05:00:00', 25000, 900),
(55, '2024-03-27 06:00:00', '2024-03-27 07:00:00', 25000, 900),
(56, '2024-03-27 07:00:00', '2024-03-27 08:00:00', 25000, 900),
(57, '2024-03-27 08:00:00', '2024-03-27 09:00:00', 25000, 900),
(58, '2024-03-27 09:00:00', '2024-03-27 10:00:00', 25000, 900),
(59, '2024-03-27 10:00:00', '2024-03-27 11:00:00', 25000, 900),
(60, '2024-03-30 02:00:00', '2024-03-30 03:00:00', 25000, 900),
(61, '2024-03-30 03:00:00', '2024-03-30 04:00:00', 25000, 900);

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
(39, 'Kaliamoorthy', 'Namodacane', '01/01/2004', 'k@gmail.com', '$2y$10$2EUuMKalUEH50kRaSgoEB.4tR4fz3oalo28Gv3U.5dgg2EfvGS8Q.', 'Quel est le nom de ma peluche ?', 'Teddy', 'NO', 39),
(40, 'Lambaret', 'Oum kaltoum', '12/02/2003', 'o@gmail.com', '$2y$10$N31eGiZOOKKihXKuuvPR1uvII/U8mBU7lQdE7OnuC2pfirr0XnkcW', 'Quel est le nom de ma peluche ?', 'Goj', 'NO', 40);

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
-- Index pour la table `notification`
--
ALTER TABLE `notification`
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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=80;
--
-- AUTO_INCREMENT pour la table `habitat`
--
ALTER TABLE `habitat`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;
--
-- AUTO_INCREMENT pour la table `notification`
--
ALTER TABLE `notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
--
-- AUTO_INCREMENT pour la table `time_slot`
--
ALTER TABLE `time_slot`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;
--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(150) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;
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
