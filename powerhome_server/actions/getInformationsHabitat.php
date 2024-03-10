<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['id'])) {
    $id = intval($json['id']);

    $getUser = $bdd->prepare("SELECT * FROM users WHERE id = ?");
    $getUser->execute(array($id));

    $getHabitat = $bdd->prepare("SELECT * FROM habitat WHERE id = ?");
    $getHabitat->execute(array($id));

    if($getUser->rowCount() > 0) {
        $user = $getUser->fetch(); 

        $result["prenom"] = $user['firstname'];

        if($getHabitat->rowCount() > 0) {
            $habitat = $getHabitat->fetch(); 
            
            $result["habitat"] = $habitat['area'];
            $result["bonus"] = $habitat['bonus'];
            $result["malus"] = $habitat['malus'];
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
