<?php

try {
    include_once '../config/Database.php';
    $bdd->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    echo 'Erreur de connexion : ' . $e->getMessage();
    exit();
}

$bdd->exec("DELETE FROM time_slot");
$resetIds = "ALTER TABLE time_slot AUTO_INCREMENT = 1";
$bdd->exec($resetIds);

$beginTime = strtotime('00:00:00');

$maxWattage = 25000;

for ($i = 0; $i < 24; $i++) {
    $endTime = strtotime('+1 hour', $beginTime);

    $beginFormatted = date('Y-m-d H:i:s', $beginTime);
    $endFormatted = date('Y-m-d H:i:s', $endTime);

    $sql = "INSERT INTO time_slot (begin, end, max_wattage, wattage_used) VALUES (:begin, :end, :maxWattage, '0')";
    $stmt = $bdd->prepare($sql);
    $stmt->execute(array(':begin' => $beginFormatted, ':end' => $endFormatted, ':maxWattage' => $maxWattage));

    $beginTime = strtotime('+1 hour', $beginTime);
}

echo "Les créneaux horaires ont été ajoutés avec succès.";

?>
