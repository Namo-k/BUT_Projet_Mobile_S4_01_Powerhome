<?php
header('Content-Type: application/json');
include_once '../config/Database.php';

$json = json_decode(file_get_contents('php://input'), true);

if (
    isset($json['date_debut']) &&
    isset($json['date_fin']) &&
    isset($json['consommation']) &&
    isset($json['equipement_id']) &&
    isset($json['id'])
) {
    $dateDebut = $json['date_debut'];
    $dateFin = $json['date_fin'];
    $consommationEquipement = intval($json['consommation']);
    $equipementId = intval($json['equipement_id']);
    $habitatId = intval($json['id']);

    try {
        $bdd->beginTransaction();

        // Vérifier si le créneau existe déjà
        $checkTimeSlot = $bdd->prepare("SELECT id FROM time_slot WHERE begin = :date_debut AND end = :date_fin");
        $checkTimeSlot->bindParam(':date_debut', $dateDebut);
        $checkTimeSlot->bindParam(':date_fin', $dateFin);
        $checkTimeSlot->execute();
        $timeSlotId = $checkTimeSlot->fetchColumn();

        // Si le créneau n'existe pas, le créer
        if (!$timeSlotId) {
            $createSlot = $bdd->prepare("INSERT INTO time_slot (begin, end, max_wattage, wattage_used) VALUES (:date_debut, :date_fin, :maxWattage, 0)");
            $createSlot->bindParam(':date_debut', $dateDebut);
            $createSlot->bindParam(':date_fin', $dateFin);
            // Remplacer :maxWattage par la valeur réelle du maxWattage
            $maxWattage = 25000; // Exemple de valeur arbitraire
            $createSlot->bindParam(':maxWattage', $maxWattage);
            $createSlot->execute();

            // Récupérer l'ID du nouveau créneau
            $timeSlotId = $bdd->lastInsertId();
        }

        // Obtenir la capacité de wattage utilisée dans le créneau horaire
        $getMaxWattage = $bdd->prepare("SELECT max_wattage FROM time_slot WHERE id = :timeSlotId");
        $getMaxWattage->bindParam(':timeSlotId', $timeSlotId);
        $getMaxWattage->execute();
        $maxWattage = $getMaxWattage->fetchColumn();

        // Obtenir le nombre d'essais de l'habitat
        $getEssai = $bdd->prepare("SELECT essai FROM habitat WHERE id = :habitatId");
        $getEssai->bindParam(':habitatId', $habitatId);
        $getEssai->execute();
        $essai = $getEssai->fetchColumn();

        // Vérifier si la consommation de l'équipement est plus grande que la capacité de wattage utilisée
        if ($consommationEquipement >= $maxWattage) {
            if ($essai >= 3) {
                // Incrémenter le malus si le nombre d'essais est supérieur ou égal à 3
                $incrementMalus = $bdd->prepare("UPDATE habitat SET malus = malus + 1 WHERE id = :habitatId");
                $incrementMalus->bindParam(':habitatId', $habitatId);
                $incrementMalus->execute();
            } else {
                // Incrémenter le nombre d'essais
                $incrementEssai = $bdd->prepare("UPDATE habitat SET essai = essai + 1 WHERE id = :habitatId");
                $incrementEssai->bindParam(':habitatId', $habitatId);
                $incrementEssai->execute();
            }
        } elseif ($consommationEquipement < $wattageUsed) {
            // Incrémenter le bonus si la consommation de l'équipement est inférieure à la capacité de wattage utilisée
            $incrementBonus = $bdd->prepare("UPDATE habitat SET bonus = bonus + 1 WHERE id = :habitatId");
            $incrementBonus->bindParam(':habitatId', $habitatId);
            $incrementBonus->execute();
        }

        // Mise à jour du wattage utilisé dans le créneau horaire
        $updateWattage = $bdd->prepare("UPDATE time_slot SET wattage_used = wattage_used + :consommationEquipement WHERE id = :timeSlotId");
        $updateWattage->bindParam(':consommationEquipement', $consommationEquipement);
        $updateWattage->bindParam(':timeSlotId', $timeSlotId);
        $updateWattage->execute();

        // Obtenir l'ordre actuel
        $getCurrentOrder = $bdd->prepare('SELECT MAX(`order`) AS max_order FROM appliance_time_slot WHERE time_slot_id = :timeSlotId');
        $getCurrentOrder->bindParam(':timeSlotId', $timeSlotId);
        $getCurrentOrder->execute();
        $currentOrder = $getCurrentOrder->fetch(PDO::FETCH_ASSOC)['max_order'];

        // Incrémenter l'ordre
        $order = $currentOrder + 1;

        // Insérer dans la table appliance_time_slot
        $insertApplianceTimeSlot = $bdd->prepare('INSERT INTO appliance_time_slot (appliance_id, time_slot_id, `order`, booked_at) 
                                                  VALUES (?, ?, ?, NOW())');
        $insertApplianceTimeSlot->execute(array($equipementId, $timeSlotId, $order));

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
