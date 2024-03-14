<?php
header('Content-Type: application/json');
include_once '../config/Database.php';

$json = json_decode(file_get_contents('php://input'), true);

if (
    isset($json['date_debut']) &&
    isset($json['date_fin']) &&
    isset($json['consommation']) &&
    isset($json['equipement_id'])&&
    isset($json['id'])
) {
    $dateDebut = $json['date_debut'];
    $dateFin = $json['date_fin'];
    $consommation = intval($json['consommation']);
    $equipementId = intval($json['equipement_id']);
    $id = intval($json['id']);

    try {
        $bdd->beginTransaction();

        // Vérifier si le créneau existe déjà
        $checkTimeSlot = $bdd->prepare("SELECT id, max_wattage FROM time_slot WHERE begin = :date_debut AND end = :date_fin");
        $checkTimeSlot->bindParam(':date_debut', $dateDebut);
        $checkTimeSlot->bindParam(':date_fin', $dateFin);
        $checkTimeSlot->execute();
        $timeSlot = $checkTimeSlot->fetch(PDO::FETCH_ASSOC);

        // Si le créneau n'existe pas, le créer
        if (!$timeSlot) {
            $createSlot = $bdd->prepare("INSERT INTO time_slot (begin, end, max_wattage, wattage_used) VALUES (:date_debut, :date_fin, :maxWattage, 0)");
            $createSlot->bindParam(':date_debut', $dateDebut);
            $createSlot->bindParam(':date_fin', $dateFin);
            // Remplacer :maxWattage par la valeur réelle du maxWattage
            $maxWattage = 25000; // Exemple de valeur arbitraire
            $createSlot->bindParam(':maxWattage', $maxWattage);
            $createSlot->execute();

            // Récupérer l'ID du nouveau créneau
            $timeSlotId = $bdd->lastInsertId();
        } else {
            $timeSlotId = $timeSlot['id'];
            $maxWattage = $timeSlot['max_wattage'];
        }

        // Mise à jour du wattage utilisé dans le créneau
        $updateWattage = $bdd->prepare("UPDATE time_slot SET wattage_used = wattage_used + :consommation WHERE id = :timeSlotId");
        $updateWattage->bindParam(':consommation', $consommation);
        $updateWattage->bindParam(':timeSlotId', $timeSlotId);
        $updateWattage->execute();

        // Obtenir le wattage utilisé après la mise à jour
        $getWattageUsed = $bdd->prepare("SELECT wattage_used FROM time_slot WHERE id = :timeSlotId");
        $getWattageUsed->bindParam(':timeSlotId', $timeSlotId);
        $getWattageUsed->execute();
        $wattageUsed = $getWattageUsed->fetchColumn();

        if ($wattageUsed > $maxWattage) {
            // Vérifier le nombre d'essais
            $checkHabitat = $bdd->prepare("SELECT essai, malus FROM habitat WHERE id = :habitatId");
            $checkHabitat->bindParam(':habitatId', $id);
            $checkHabitat->execute();
            $habitat = $checkHabitat->fetch(PDO::FETCH_ASSOC);

            if ($habitat) {
                $essai = $habitat['essai'];
                $malus = $habitat['malus'];

                if ($essai >= 3) {
                    // Atteint le maximum d'essais, incrémenter le malus
                    $malus++;
                    $updateHabitat = $bdd->prepare("UPDATE habitat SET malus = :malus WHERE id = :habitatId");
                    $updateHabitat->bindParam(':malus', $malus);
                    $updateHabitat->bindParam(':habitatId', $id);
                    $updateHabitat->execute();
                } else {
                    // Incrémenter le nombre d'essais
                    $essai++;
                    $updateHabitat = $bdd->prepare("UPDATE habitat SET essai = :essai WHERE id = :habitatId");
                    $updateHabitat->bindParam(':essai', $essai);
                    $updateHabitat->bindParam(':habitatId', $id);
                    $updateHabitat->execute();
                }
            }
        } else {
            // La wattageUsed est inférieure ou égale au maxWattage, incrémenter le bonus
            $updateHabitat = $bdd->prepare("UPDATE habitat SET bonus = bonus + 1 WHERE id = :habitatId");
            $updateHabitat->bindParam(':habitatId', $id);
            $updateHabitat->execute();
        }

        $bdd->commit();

        $response = array("success" => true, "message" => "Réservation réussie.");
        echo json_encode($response);
    } catch (PDOException $e) {
        $bdd->rollBack();

        $response = array("success" => false, "error" => "Erreur lors de l'opération : " . $e->getMessage());
        echo json_encode($response);
    }
} else {
    $response = array("success" => false, "error" => "Erreur : des données sont manquantes");
    echo json_encode($response);
}
?>
