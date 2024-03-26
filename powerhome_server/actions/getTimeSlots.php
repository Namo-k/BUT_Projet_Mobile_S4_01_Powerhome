<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input'), true);
$getTimesSlot = $bdd->prepare("SELECT t.id, t.begin, t.end, t.max_Wattage, t.wattage_used
                                FROM time_slot t
                                WHERE t.begin > NOW()
                                ORDER BY t.begin ASC");
$getTimesSlot->execute(array());

if ($getTimesSlot->rowCount() > 0) {
    $timeSlots = array(); 

    while ($row = $getTimesSlot->fetch(PDO::FETCH_ASSOC)) {
        $timeSlot = array(
            "id" => $row['id'],
            "begin" => $row['begin'],
            "end" => $row['end'],
            "max_wattage" => $row['max_Wattage'], 
            "wattage_used" => $row['wattage_used'] 
        );

        $timeSlots[] = $timeSlot;
    }

    $result["success"] = true;
    $result["timeSlots"] = $timeSlots;
} else {
    $result["success"] = false;
    $result["error"] = "Aucune timeSlot trouvé";
}
echo json_encode($result);
?>
