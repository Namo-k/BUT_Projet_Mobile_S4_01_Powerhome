<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['id']) and isset($json['name']) and isset($json['reference']) and isset($json['wattage'])) {
    $id = intval($json['id']);
    $name = htmlspecialchars($json['name']); 
    $reference = htmlspecialchars($json['reference']);
    $wattage = intval($json['wattage']);

    $getReference = $bdd->prepare("SELECT * FROM appliance WHERE reference = ? AND habitat_id = ?");
    $getReference->execute(array($reference, $id));

    if($getReference->rowCount() > 0) {
        $result["success"] = false;
        $result["error"] = "Il avez déjà un équipement avec cette référence";
    } 
    else {
        try {
            $addEquipement = $bdd->prepare("INSERT INTO `appliance` (`id`, `name`, `reference`, `wattage`, `habitat_id`) VALUES (NULL, ?, ?, ?, ?);");
            $addEquipement->execute(array($name, $reference, $wattage, $id));
            $result["success"] = true;
        }
        catch (Exception $e) {
            $result["success"] = false;
            $result["error"] = "Erreur création compte";
        }
    }
} 
else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);
