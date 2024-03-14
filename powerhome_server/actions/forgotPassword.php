<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['mail']) and isset($json['question']) and isset($json['reponse'])) {
    $mail = htmlspecialchars($json['mail']);
    $question = htmlspecialchars($json['question']);
    $reponse = htmlspecialchars($json['reponse']);

    $getUser = $bdd->prepare("SELECT * FROM users WHERE email = ?");
    $getUser->execute(array($mail));

    if($getUser->rowCount() > 0) {
        $user = $getUser->fetch(); 

        if ($question == $user['question'] && $reponse == $user['response']) {
            $result["success"] = true;
            $result["password"] = $user['password']; 
            $result["mail"] = $user['email']; 
        }
        else {
            $result["success"] = false;
            $result["error"] = "La question ou la réponse n'est pas bonne";
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
