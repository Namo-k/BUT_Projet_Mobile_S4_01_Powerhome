<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['reference']) && isset($json['habitat_id'])) {
    $reference = htmlspecialchars($json['reference']);
    $habitat_id = htmlspecialchars($json['habitat_id']);

    try {
        $getEquipement = $bdd->prepare("SELECT * FROM appliance WHERE reference = ? AND habitat_id = ?");
        $getEquipement->execute(array($reference, $habitat_id));

        if($getEquipement->rowCount() > 0) {
            $equipement = $getEquipement->fetch(PDO::FETCH_ASSOC);
            $wattage = $equipement['wattage'];

            $getCurrentConsommation = $bdd->prepare("SELECT consommation FROM habitat WHERE id = ?");
            $getCurrentConsommation->execute(array($habitat_id));
            $currentConsommation = $getCurrentConsommation->fetch(PDO::FETCH_ASSOC)['consommation'];

            $newConsommation = $currentConsommation - $wattage;

            $updateConsommation = $bdd->prepare("UPDATE habitat SET consommation = ? WHERE id = ?");
            $updateConsommation->execute(array($newConsommation, $habitat_id));

            $deleteEquipement = $bdd->prepare("DELETE FROM appliance WHERE reference = ? AND habitat_id = ?");
            $deleteEquipement->execute(array($reference, $habitat_id));

            $result["success"] = true;
        } 
        else {
            $result["success"] = false;
            $result["error"] = "L'équipement n'existe pas dans votre liste";
        }
    }
    catch (Exception $e) {
        $result["success"] = false;
        $result["error"] = "Erreur";
    }
} else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);