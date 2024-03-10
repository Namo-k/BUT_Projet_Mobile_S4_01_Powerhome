<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['mail']) && isset($json['password'])) {
    $mail = htmlspecialchars($json['mail']);
    $password = htmlspecialchars($json['password']);
    $passwordHashed = password_hash($password, PASSWORD_DEFAULT); 

    $getUser = $bdd->prepare("SELECT * FROM users WHERE email = ?");
    $getUser->execute(array($mail));

    if($getUser->rowCount() > 0) {
        $user = $getUser->fetch(); 
        try {
            $changePassword = $bdd->prepare("UPDATE users SET password = ? WHERE id = ?");
            $changePassword->execute(array($passwordHashed, $user['id']));
            $result["success"] = true;
        }
        catch (Exception $e) {
            $result["success"] = false;
            $result["error"] = "Erreur modif mdp";
        }
    }
} else {
    $result["success"] = false;
    $result["error"] = "Veuillez fournir l'adresse e-mail et le nouveau mot de passe";
}

echo json_encode($result);
