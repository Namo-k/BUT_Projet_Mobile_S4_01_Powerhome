<?php 

header('Content-Type: application/json');
include_once '../config/Database.php';

$getTimeSlots = $bdd->prepare("SELECT id, begin, end, max_wattage, wattage_used FROM time_slot");
$getTimeSlots->execute();

if($getTimeSlots->rowCount() > 0) {
    $timeSlots = array(); 

    while($row = $getTimeSlots->fetch(PDO::FETCH_ASSOC)) {
        $timeSlot = array(
            "id" => $row['id'],
            "begin" => $row['begin'],
            "end" => $row['end'],
            "max_wattage" => $row['max_wattage'],
            "wattage_used" => $row['wattage_used']
        );

        $timeSlots[] = $timeSlot;
    }

    $result["success"] = true;
    $result["time_slots"] = $timeSlots;
}
else {
    $result["success"] = false;
    $result["error"] = "Aucun créneau horaire trouvé";
}

echo json_encode($result);

?>
