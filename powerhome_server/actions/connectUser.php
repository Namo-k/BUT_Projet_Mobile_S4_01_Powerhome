<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['mail']) and isset($json['password'])) {
    $mail = htmlspecialchars($json['mail']);
    $password = htmlspecialchars($json['password']);

    $getUser = $bdd->prepare("SELECT * FROM users WHERE email = ?");
    $getUser->execute(array($mail));

    if($getUser->rowCount() > 0) {
        $user = $getUser->fetch(); 

        if (password_verify($password, $user['password'])) {
            $result["id"] = $user['id'];
            $result["firstTime"] = $user['firstTime'];
            $result["success"] = true;
        }
        else {
            $result["success"] = false;
            $result["error"] = "Mot de passe incorrect";
        }
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
