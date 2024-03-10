<?php 

header('Content-Type: application/json');

include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['id']) and isset($json['floor']) and isset($json['area'])) { 
    $id = $json['id']; 
    $floor = $json['floor']; 
    $area = $json['area']; 

    $getUser = $bdd->prepare("SELECT * FROM users WHERE id = ?");
    $getUser->execute(array($id));

    if($getUser->rowCount() > 0) {
        $user = $getUser->fetch(); 

        try {
            $createAccount = $bdd->prepare("INSERT INTO `habitat` (`id`, `floor`, `area`) VALUES (?, ?, ?);");
            $createAccount->execute(array($id, $floor, $area));
            $createAccount = $bdd->prepare("UPDATE users SET habitat_id = ?, firstTime = ? WHERE id = ?");
            $createAccount->execute(array($id, "NO", $id));
            $result["success"] = true;
        }
        catch (Exception $e) {
            $result["success"] = false;
            $result["error"] = "Erreur ajout habitat";
        }
    }
}
else {
    $result["success"] = false;
    $result["error"] = "Erreur ajout habitat"; 
}

echo json_encode($result);