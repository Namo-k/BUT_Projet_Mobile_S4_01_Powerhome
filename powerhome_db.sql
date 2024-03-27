-- phpMyAdmin SQL Dump
-- version 4.5.4.1
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Mer 27 Mars 2024 à 14:37
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
(88, 'Aspirateur', 'Bosh5800', 500, 39),
(89, 'Television', 'Sony 8000', 800, 39),
(90, 'Climatiseur', 'PanasonicV45', 1500, 39),
(91, 'Fer à repasser', 'Philips J45', 150, 39),
(92, 'Machine à laver', 'Whirpool45', 800, 39),
(93, 'Four électrique', 'Electrolux G788', 755, 39),
(94, 'Television', 'Sony 788', 800, 52),
(95, 'Aspirateur', 'Bosh V78', 150, 52),
(96, 'Radiateur électrique', 'Radiateur RM78', 780, 52),
(97, 'Television', 'Samsung928382', 700, 53);

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
(93, 5, 1, '2024-03-27 11:49:04'),
(93, 6, 1, '2024-03-27 11:49:04'),
(93, 7, 1, '2024-03-27 11:49:04'),
(93, 8, 1, '2024-03-27 11:49:04'),
(93, 19, 1, '2024-03-27 11:49:28'),
(93, 20, 1, '2024-03-27 11:49:28'),
(93, 21, 1, '2024-03-27 11:49:28'),
(93, 22, 1, '2024-03-27 11:49:28'),
(96, 17, 1, '2024-03-27 11:54:52'),
(96, 18, 1, '2024-03-27 11:54:52'),
(96, 19, 2, '2024-03-27 11:54:52'),
(95, 20, 2, '2024-03-27 11:55:36'),
(95, 25, 1, '2024-03-27 15:18:05'),
(95, 26, 1, '2024-03-27 15:18:05'),
(95, 27, 1, '2024-03-27 15:18:05');

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
(39, 5, 5, 8, 0, 0, 4505),
(40, 2, 2, 0, 0, 0, 0),
(51, 1, 2, 0, 0, 0, 0),
(52, 2, 2, 7, 0, 0, 1730),
(53, 4, 5, 0, 0, 0, 700);

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
(80, 'Ajout d\'un équipement', 'Vous avez ajouté un Aspirateur Ref. Bosh5800 de 500W à votre logement.', 'equipement', 39),
(81, 'Ajout d\'un équipement', 'Vous avez ajouté un Television Ref. Sony 8000 de 800W à votre logement.', 'equipement', 39),
(82, 'Ajout d\'un équipement', 'Vous avez ajouté un Climatiseur Ref. PanasonicV45 de 1500W à votre logement.', 'equipement', 39),
(83, 'Ajout d\'un équipement', 'Vous avez ajouté un Fer à repasser Ref. Philips J45 de 150W à votre logement.', 'equipement', 39),
(84, 'Ajout d\'un équipement', 'Vous avez ajouté un Machine à laver Ref. Whirpool45 de 800W à votre logement.', 'equipement', 39),
(85, 'Ajout d\'un équipement', 'Vous avez ajouté un Four électrique Ref. Electrolux G788 de 750W à votre logement.', 'equipement', 39),
(86, 'Modification d\'un équipement', 'Vous avez modifié : Four électrique.93 Ref. Electrolux G788 de 755W de votre logement.', 'equipement', 39),
(87, 'Sélection d\'un créneau', 'Vous avez réservé le créneau de 04h à 08h pour votre 93 Four électrique.', 'creneau', 39),
(88, 'Sélection d\'un créneau', 'Vous avez réservé le créneau de 18h à 22h pour votre 93 Four électrique.', 'creneau', 39),
(89, 'Bienvenue', 'Nous sommes ravie de vous accueillir dans votre nouveau logement', 'notification', 52),
(90, 'Ajout d\'un équipement', 'Vous avez ajouté un Television Ref. Sony 788 de 800W à votre logement.', 'equipement', 52),
(91, 'Ajout d\'un équipement', 'Vous avez ajouté un Aspirateur Ref. Bosh V78 de 150W à votre logement.', 'equipement', 52),
(92, 'Ajout d\'un équipement', 'Vous avez ajouté un Radiateur électrique Ref. RadiateurMM78 de 780W à votre logement.', 'equipement', 52),
(93, 'Modification d\'un équipement', 'Vous avez modifié : Radiateur électrique.96 Ref. Radiateur RM78 de 780W de votre logement.', 'equipement', 52),
(94, 'Sélection d\'un créneau', 'Vous avez réservé le créneau de 16h à 19h pour votre 96 Radiateur électrique.', 'creneau', 52),
(95, 'Sélection d\'un créneau', 'Vous avez réservé le créneau de 19h à 20h pour votre 95 Aspirateur.', 'creneau', 52),
(96, 'Bienvenue', 'Nous sommes ravie de vous accueillir dans votre nouveau logement', 'notification', 53),
(97, 'Ajout d\'un équipement', 'Vous avez ajouté un Television Ref. Samsung817219 de 900W à votre logement.', 'equipement', 53),
(98, 'Ajout d\'un équipement', 'Vous avez ajouté un Fer à repasser Ref. 2I8E8H278E de 500W à votre logement.', 'equipement', 53),
(99, 'Modification d\'un équipement', 'Vous avez modifié : Television.97 Ref. Samsung928382 de 700W de votre logement.', 'equipement', 53),
(100, 'Supression d\'un équipement', 'Vous avez supprimé : Fer à repasser.98 de votre logement.', 'equipement', 53),
(101, 'Sélection d\'un créneau', 'Vous avez réservé le créneau de 11h à 14h pour votre 95 Aspirateur.', 'creneau', 52),
(102, 'Configuration', 'Vous avez modifié votre profil.', 'configuration', 52);

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
(1, '2024-03-27 00:00:00', '2024-03-27 01:00:00', 25000, 0),
(2, '2024-03-27 01:00:00', '2024-03-27 02:00:00', 25000, 0),
(3, '2024-03-27 02:00:00', '2024-03-27 03:00:00', 25000, 0),
(4, '2024-03-27 03:00:00', '2024-03-27 04:00:00', 25000, 0),
(5, '2024-03-27 04:00:00', '2024-03-27 05:00:00', 25000, 755),
(6, '2024-03-27 05:00:00', '2024-03-27 06:00:00', 25000, 755),
(7, '2024-03-27 06:00:00', '2024-03-27 07:00:00', 25000, 755),
(8, '2024-03-27 07:00:00', '2024-03-27 08:00:00', 25000, 755),
(9, '2024-03-27 08:00:00', '2024-03-27 09:00:00', 25000, 0),
(10, '2024-03-27 09:00:00', '2024-03-27 10:00:00', 25000, 0),
(11, '2024-03-27 10:00:00', '2024-03-27 11:00:00', 25000, 0),
(12, '2024-03-27 11:00:00', '2024-03-27 12:00:00', 25000, 0),
(13, '2024-03-27 12:00:00', '2024-03-27 13:00:00', 25000, 0),
(14, '2024-03-27 13:00:00', '2024-03-27 14:00:00', 25000, 5400),
(15, '2024-03-27 14:00:00', '2024-03-27 15:00:00', 25000, 0),
(16, '2024-03-27 15:00:00', '2024-03-27 16:00:00', 25000, 0),
(17, '2024-03-27 16:00:00', '2024-03-27 17:00:00', 25000, 780),
(18, '2024-03-27 17:00:00', '2024-03-27 18:00:00', 25000, 10000),
(19, '2024-03-27 18:00:00', '2024-03-27 19:00:00', 25000, 26000),
(20, '2024-03-27 19:00:00', '2024-03-27 20:00:00', 25000, 10000),
(21, '2024-03-27 20:00:00', '2024-03-27 21:00:00', 25000, 755),
(22, '2024-03-27 21:00:00', '2024-03-27 22:00:00', 25000, 860),
(23, '2024-03-27 22:00:00', '2024-03-27 23:00:00', 25000, 0),
(24, '2024-03-27 23:00:00', '2024-03-28 00:00:00', 25000, 0),
(25, '2024-03-28 11:00:00', '2024-03-28 12:00:00', 25000, 150),
(26, '2024-03-28 12:00:00', '2024-03-28 13:00:00', 25000, 150),
(27, '2024-03-28 13:00:00', '2024-03-28 14:00:00', 25000, 150);

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
(39, 'Kaliamoorthy', 'Namodacane', '30/01/2004', 'k@gmail.com', '$2y$10$2EUuMKalUEH50kRaSgoEB.4tR4fz3oalo28Gv3U.5dgg2EfvGS8Q.', 'Quel est le nom de ma peluche ?', 'Teddy', 'NO', 39),
(40, 'Lambaret', 'Oum kaltoum', '12/02/2003', 'o@gmail.com', '$2y$10$N31eGiZOOKKihXKuuvPR1uvII/U8mBU7lQdE7OnuC2pfirr0XnkcW', 'Quel est le nom de ma peluche ?', 'Goj', 'NO', 40),
(51, 'Lutete', 'Junior', '01/03/2004', 'j@gmail.com', '$2y$10$sAovSPGYgUNa0uTwdhkMA.f0jMTjgrKxQr3zC2haMLx/alK2DybAe', 'Quel serait mon rêve ?', 'Avoir 2 femmes', 'NO', 51),
(52, 'Carou', 'Alex', '17/12/2004', 'a@gmail.com', '$2y$10$U6fuiJNvmmzIu8CPK/ZVUOZyWbXK6sE0KXDynWFBQtgaASYHArxO6', 'Quel est le premier livre lu ?', 'Hunger Games', 'NO', 52),
(53, 'CAROUNANITHI', 'Alexandre', '17/12/2004', 'c@gmail.com', '$2y$10$6XHgTnS/cXXPS624WPeAqO5Z60kUidKrLAgRVqZ/CWAPuYuPx40W.', 'Quel est le premier livre lu ?', 'Odyssee', 'NO', 53);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=99;
--
-- AUTO_INCREMENT pour la table `habitat`
--
ALTER TABLE `habitat`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;
--
-- AUTO_INCREMENT pour la table `notification`
--
ALTER TABLE `notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=103;
--
-- AUTO_INCREMENT pour la table `time_slot`
--
ALTER TABLE `time_slot`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(150) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;
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
