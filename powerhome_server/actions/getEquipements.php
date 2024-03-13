<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);
if(isset($json['id'])) {
    $id = intval($json['id']);

    $getHabitatInfo = $bdd->prepare("SELECT appliance.id, appliance.name, appliance.wattage, habitat.consommation 
                                      FROM appliance 
                                      INNER JOIN habitat ON appliance.habitat_id = habitat.id 
                                      WHERE appliance.habitat_id = ?");
    $getHabitatInfo->execute(array($id));

    if($getHabitatInfo->rowCount() > 0) {
        $appliances = array(); 

        while($row = $getHabitatInfo->fetch(PDO::FETCH_ASSOC)) {
            $appliance = array(
                "id" => $row['id'],
                "name" => $row['name'],
                "wattage" => $row['wattage']
            );

            $appliances[] = $appliance;
            $consommation = $row['consommation'];
        }

        $result["success"] = true;
        $result["appliances"] = $appliances;
        $result["consommation"] = $consommation;
    }
    else {
        $result["success"] = false;
        $result["error"] = "Aucun équipement trouvé";
    }
} 
else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);
