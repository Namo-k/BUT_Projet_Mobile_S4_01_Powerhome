<?php

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

$getUser = $bdd->prepare("SELECT * FROM appliance");
$getUser->execute();

if ($getUser->rowCount() > 0) {
    $appliances = array();

    while ($row = $getUser->fetch(PDO::FETCH_ASSOC)) {
        $appliance = array(
            "habitat_id" => $row['habitat_id'],
            "wattage" => $row['wattage']
        );

        $appliances[] = $appliance;
    }
    $result["success"] = true;
    $result["appliances"] = $appliances;

} 
else {
    $result["success"] = false;
    $result["error"] = "Aucun équipement trouvé";
}

echo json_encode($result);
?>