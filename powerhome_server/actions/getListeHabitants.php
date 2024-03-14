<?php
header('Content-Type: application/json');
include_once '../config/Database.php';

$getUsers = $bdd->prepare("SELECT users.id, users.lastname, users.firstname, users.habitat_id, habitat.bonus, habitat.malus, habitat.consommation
                            FROM users INNER JOIN habitat ON users.habitat_id = habitat.id");
$getUsers->execute();


if ($getUsers->rowCount() > 0) {
    $users = array();

    while ($row = $getUsers->fetch(PDO::FETCH_ASSOC)) {
        $user = array(
            "id" => $row['id'],
            "nom" => $row['lastname'],
            "prenom" => $row['firstname'],
            "habitat_id" => $row['habitat_id'],
            "bonus" => $row['bonus'],
            "malus" => $row['malus'],
            "consommation" => $row['consommation']
        );

        $users[] = $user;
    }

    $result["success"] = true;
    $result["users"] = $users;
} else {
    $result["success"] = false;
    $result["error"] = "Aucun utilisateur trouvé";
}

echo json_encode($result);
?>