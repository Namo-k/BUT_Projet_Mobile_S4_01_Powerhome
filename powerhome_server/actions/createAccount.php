<?php 

header('Content-Type: application/json');

include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['mail']) and isset($json['password']) and isset($json['nom']) and isset($json['prenom']) and isset($json['naissance']) and isset($json['question']) and isset($json['reponse'])) { 
    $mail = htmlspecialchars($json['mail']); // je les mets dans variables
    $password = htmlspecialchars($json['password']);
    $passwordHashed = password_hash($password, PASSWORD_DEFAULT); 
    $nom = htmlspecialchars($json['nom']);
    $prenom = htmlspecialchars($json['prenom']);    
    $naissance = htmlspecialchars($json['naissance']); 
    $question = htmlspecialchars($json['question']);
    $reponse = htmlspecialchars($json['reponse']);

    try {
        $createAccount = $bdd->prepare("INSERT INTO `users` (`id`, `lastname`, `firstname`, `birth`, `email`, `password`, `question`, `response`, `habitat_id`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, NULL);");
        $createAccount->execute(array($nom, $prenom, $naissance, $mail, $passwordHashed, $question, $reponse));
        $result["success"] = true;
    }
    catch (Exception $e) {
        $result["success"] = false;
        $result["success"] = "Erreur création compte";
    }
}
else {
    $result["success"] = false;
    $result["error"] = "Erreur création compte"; 
}

echo json_encode($result);