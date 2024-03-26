<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);
if(isset($json['id'])) {
    $id = intval($json['id']);

    $getNotification = $bdd->prepare("SELECT n.id, n.notification_title, n.notification, n.notification_categorie
                                      FROM notification n
                                      WHERE n.id_user = ?
                                      ORDER BY n.id DESC");
    $getNotification->execute(array($id));

    if($getNotification->rowCount() > 0) {
        $notification = array(); 

        while($row = $getNotification->fetch(PDO::FETCH_ASSOC)) {
            $notification = array(
                "id" => $row['id'],
                "notification_title" => $row['notification_title'],
                "notification" => $row['notification'],
                "notification_categorie" => $row['notification_categorie']
            );

            $notifications[] = $notification;
        }

        $result["success"] = true;
        $result["notifications"] = $notifications;
    }
    else {
        $result["success"] = false;
        $result["error"] = "Aucune notification trouvé";
    }
} 
else {
    $result["success"] = false;
    $result["error"] = "Erreur";
}

echo json_encode($result);
