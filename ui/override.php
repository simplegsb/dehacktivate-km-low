<?php

$filename = 'override.json';

// Empty the file
file_put_contents($filename, '');

$response = array();

$x  = json_decode($_GET['x']);
$y  = json_decode($_GET['y']);
$id = json_decode($_GET['id']);


$numItems = count($id);

for($i=0; $i < $numItems; $i++){
	$plane = array(
		"plane_id" => $id[$i],
		"waypoints" => array(
			array(
				"x" => $x[$i],
				"y" => $y[$i]
			)
		)
	);
	array_push($response, $plane);
}


$fp = fopen($filename, 'w');
fwrite($fp, json_encode($response));
fclose($fp);
echo json_encode($response);



?>
