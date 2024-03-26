<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['email'])) {
    $email = htmlspecialchars($json['email']);

    $getUser = $bdd->prepare("SELECT * FROM users WHERE email = ?");
    $getUser->execute(array($email));

    if($getUser->rowCount() > 0) {
        $user = $getUser->fetch(); 
        $result["id"] = $user['id'];
        $result["success"] = true;
    }
    else {
        $result["success"] = false;
        $result["error"] = "Il n'y a pas de compte avec cette adresse mail";
    }
} 
else {
    $result["success"] = false;
    $result["error"] = "Remplissez ces informations";
}

echo json_encode($result);
