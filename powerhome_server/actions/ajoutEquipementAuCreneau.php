<?php
header('Content-Type: application/json');
include_once '../config/Database.php';

$json = json_decode(file_get_contents('php://input'), true);

if (
    isset($json['date_debut']) &&
    isset($json['date_fin']) &&
    isset($json['consommation']) &&
    isset($json['equipement_id'])
) {
    $dateDebut = $json['date_debut'];
    $dateFin = $json['date_fin'];
    $consommation = intval($json['consommation']);
    $equipementId = intval($json['equipement_id']);

    try {
        $bdd->beginTransaction();

        // Mise à jour du wattage utilisé dans la table time_slot
        $sqlUpdate = "UPDATE time_slot
                      SET wattage_used = wattage_used + :consommation
                      WHERE begin = :date_debut
                      AND end = :date_fin";

        $stmtUpdate = $bdd->prepare($sqlUpdate);
        $stmtUpdate->bindParam(':consommation', $consommation);
        $stmtUpdate->bindParam(':date_debut', $dateDebut);
        $stmtUpdate->bindParam(':date_fin', $dateFin);
        $stmtUpdate->execute();

        // Obtenir l'ordre actuel
        $getCurrentOrder = $bdd->prepare('SELECT MAX(`order`) AS max_order FROM appliance_time_slot 
                                                            INNER JOIN time_slot ON appliance_time_slot.time_slot_id = time_slot.id 
                                                            WHERE begin <= ? AND end > ?');
        $getCurrentOrder->execute(array($dateDebut, $dateFin));
        $currentOrder = $getCurrentOrder->fetch(PDO::FETCH_ASSOC)['max_order'];

        $order = $currentOrder + 1;

        // Insérer dans la table appliance_time_slot
        $insertApplianceTimeSlot = $bdd->prepare('INSERT INTO appliance_time_slot (appliance_id, time_slot_id, `order`, booked_at) 
                                                  VALUES (?, (SELECT id FROM time_slot WHERE begin = ? AND end = ?), ?, NOW())');
        $insertApplianceTimeSlot->execute(array($equipementId, $dateDebut, $dateFin, $order));

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
