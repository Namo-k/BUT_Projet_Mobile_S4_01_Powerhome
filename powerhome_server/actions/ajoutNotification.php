<?php
header('Content-Type: application/json');
include_once '../config/Database.php';

$json = json_decode(file_get_contents('php://input'), true);

if (
    isset($json['title']) &&
    isset($json['notification']) &&
    isset($json['categorie']) 
) {
    $title = $json['title'];
    $notification = $json['notification'];
    $categorie = $json['categorie'];
    $userId = intval($json['id']);

    try {
        $bdd->beginTransaction();

        $insertApplianceTimeSlot = $bdd->prepare("INSERT INTO notification (notification_title, notification, notification_categorie, id_user) 
                                                  VALUES (?, ?, ?, ?)");
        $insertApplianceTimeSlot->execute(array($title, $notification, $categorie, $userId));

        $bdd->commit();

        $response = array("success" => true, "message" => "Ajout Notification réussie.");
        echo json_encode($response);
    } catch (PDOException $e) {
        $bdd->rollBack();

        $response = array("success" => false, "error" => "Erreur lors de l'opération : " . $e->getMessage());
        echo json_encode($response);
    }
} else {
    $response = array("success" => false, "error" => "Erreur : des données sont manquantes");
    echo json_encode($response);
}
?>
