<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['nom']) and isset($json['prenom']) and isset($json['mail']) and isset($json['naissance']) and isset($json['question']) and isset($json['reponse'])) { 
    $id = intval($json['id']);
    $nom = htmlspecialchars($json['nom']);
    $prenom = htmlspecialchars($json['prenom']);    
    $naissance = htmlspecialchars($json['naissance']); 
    $mail = htmlspecialchars($json['mail']); 
    $question = htmlspecialchars($json['question']);
    $reponse = htmlspecialchars($json['reponse']);

    $getUser = $bdd->prepare("SELECT * FROM users WHERE id = ?");
    $getUser->execute(array($id));

    try {
        $createAccount = $bdd->prepare("UPDATE users SET lastname = ?, firstname = ?, birth = ?, email = ?, question = ?, response = ? WHERE id = ?");
        $createAccount->execute(array($nom, $prenom, $naissance, $mail, $question, $reponse, $id));
        $result["success"] = true;
        $result["id"] = $id;
    }
    catch (Exception $e) {
        $result["success"] = false;
        $result["error"] = "Erreur update compte";
    }
} 
else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);
