<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['id'])) {
    $id = intval($json['id']);

    $getUser = $bdd->prepare("SELECT * FROM appliance WHERE habitat_id = ?");
    $getUser->execute(array($id));

    if($getUser->rowCount() > 0) {
        $user = $getUser->fetch(); 

        while($row = $getUser->fetch(PDO::FETCH_ASSOC)) {
            $appliance = array(
                "id" => $row['id'],
                "name" => $row['name'],
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
} 
else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);
