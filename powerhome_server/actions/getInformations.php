<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['id'])) {
    $id = intval($json['id']);

    $getUser = $bdd->prepare("SELECT * FROM users WHERE id = ?");
    $getUser->execute(array($id));

    $getHabitat = $bdd->prepare("SELECT area FROM habitat WHERE id = ?");
    $getHabitat->execute(array($id));

    if($getUser->rowCount() > 0) {
        $user = $getUser->fetch(); 
        $result["nom"] = $user['lastname'];
        $result["prenom"] = $user['firstname'];
        $result["mail"] = $user['email'];
        $result["naissance"] = $user['birth'];
        $result["question"] = $user['question'];
        $result["reponse"] = $user['response'];
        $result["success"] = true;

        if($getHabitat->rowCount() > 0) {
            $habitat = $getHabitat->fetch(); 
            $area = $habitat['area'];
            $result["area"] = $area;
        }

        $result["success"] = true;
    }
    else {
        $result["success"] = false;
        $result["error"] = "Aucun utilisateur trouvé avec cet email";
    }
} 
else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);
