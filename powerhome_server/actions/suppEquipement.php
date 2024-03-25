<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['id']) && isset($json['idEquipement'])) {
    $id = intval($json['id']);
    $idEquipement = intval($json['idEquipement']);

    try {
        $getEquipementData = $bdd->prepare("SELECT * FROM appliance WHERE id = ?");
        $getEquipementData->execute(array($idEquipement));

        if($getEquipementData->rowCount() > 0) {
            $equipementData = $getEquipementData->fetch(PDO::FETCH_ASSOC);
            $reference = $equipementData['reference'];
            $wattage = $equipementData['wattage'];

            $deleteEquipement = $bdd->prepare("DELETE FROM appliance WHERE id = ?");
            $deleteEquipement->execute(array($idEquipement));

            $getCurrentConsommation = $bdd->prepare("SELECT consommation FROM habitat WHERE id = ?");
            $getCurrentConsommation->execute(array($id));
            $currentConsommation = $getCurrentConsommation->fetch(PDO::FETCH_ASSOC)['consommation'];

            $newConsommation = $currentConsommation - $wattage;

            $updateConsommation = $bdd->prepare("UPDATE habitat SET consommation = ? WHERE id = ?");
            $updateConsommation->execute(array($newConsommation, $id));

            $result["success"] = true;
        } 
        else {
            $result["success"] = false;
            $result["error"] = "Erreur";
        }
    } catch (Exception $e) {
        $result["success"] = false;
        $result["error"] = "Erreur suppression";
    }
} 
else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);
?>