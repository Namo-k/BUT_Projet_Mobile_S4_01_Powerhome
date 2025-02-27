<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['id']) && isset($json['idEquipement']) && isset($json['reference']) && isset($json['wattage'])) {
    $id = intval($json['id']);
    $idEquipement = intval($json['idEquipement']);
    $reference = htmlspecialchars($json['reference']);
    $wattage = intval($json['wattage']);

    try {
        $getEquipementData = $bdd->prepare("SELECT * FROM appliance WHERE id = ?");
        $getEquipementData->execute(array($idEquipement));
        $equipementData = $getEquipementData->fetch(PDO::FETCH_ASSOC);
        
        if($equipementData) {
            $updateEquipement = $bdd->prepare("UPDATE appliance SET reference = ?, wattage = ? WHERE id = ?");
            $updateEquipement->execute(array($reference, $wattage, $idEquipement));

            $getCurrentConsommation = $bdd->prepare("SELECT consommation FROM habitat WHERE id = ?");
            $getCurrentConsommation->execute(array($id));
            $currentConsommation = $getCurrentConsommation->fetch(PDO::FETCH_ASSOC)['consommation'];

            $newConsommation = $currentConsommation - $equipementData['wattage'] + $wattage;

            $updateConsommation = $bdd->prepare("UPDATE habitat SET consommation = ? WHERE id = ?");
            $updateConsommation->execute(array($newConsommation, $id));

            $result["success"] = true;
        } else {
            $result["success"] = false;
            $result["error"] = "Erreur modif";
        }
    } catch (Exception $e) {
        $result["success"] = false;
        $result["error"] = "Erreur";
    }
} else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);
?>