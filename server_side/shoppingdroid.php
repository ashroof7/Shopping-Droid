<?php

include_once 'db_config.php';
include_once 'db_connection.php';


$baseUrl = "http://localhost/shoppingdroid/";
$db = new DB_CONNECTION();
$uri = $_SERVER["REQUEST_URI"];
$path = explode("?", $uri);
$parts = explode("/", $path[0]."///");

/**
supported queries 
product // select product in a specific store
product_global // select product from all stores
store // selects a store 
stores // return all data of stores
stores_locations // return store name and location(longitude, latitude)
product_range // select products of same type that are from a threshold from current product price 
prodcut_range_global // select products of same type that are from a threshold from current product price  from all stores
stores_in_range // select the stores in range of current latitude and longitude
*/

$q_type = $parts[2];

// route to the target method 
if ($q_type=="product") {

	if (!isset($_GET["barcode"]) || !isset($_GET["store_id"])) 
		sendResponseAndExit(false, 400, "Bad Request");

	$response = $db->product($_GET["barcode"], $_GET["store_id"]);    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else if ($q_type=="product_global") {

	if (!isset($_GET["barcode"])) 
		sendResponseAndExit(false, 400, "Bad Request");

	$response = $db->product_global($_GET["barcode"]);    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else if ($q_type=="store"){

	if (!isset($_GET["store_id"])) 
		sendResponseAndExit(false, 400, "Bad Request");

	$response = $db->store($_GET["store_id"]);    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else if ($q_type=="stores") {

	$response = $db->stores();    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else if ($q_type=="stores_locations") {

	$response = $db->stores_locations();    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else if ($q_type=="product_range") {

	if (!isset($_GET["barcode"]) || !isset($_GET["store_id"]) || !isset($_GET["diff_amount"])) 
		sendResponseAndExit(false, 400, "Bad Request");

	$response = $db->product_range($_GET["barcode"], $_GET["store_id"], $_GET["diff_amount"]);    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else if ($q_type=="product_range_global") {

	if (!isset($_GET["barcode"]) || !isset($_GET["store_id"]) || !isset($_GET["diff_amount"])) 
		sendResponseAndExit(false, 400, "Bad Request");

	$response = $db->product_range_global($_GET["barcode"], $_GET["store_id"], $_GET["diff_amount"]);    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else if ($q_type=="product_range_global") {
    if (!isset($_GET["latitude"]) || !isset($_GET["longitude"]) || !isset($_GET["range"])) 
		sendResponseAndExit(false, 400, "Bad Request");

	$response = $db->product_range_global($_GET["latitude"], $_GET["longitude"], $_GET["range"]);    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else if ($q_type=="stores_in_range") {
    if (!isset($_GET["latitude"]) || !isset($_GET["longitude"]) || !isset($_GET["range"])) 
		sendResponseAndExit(false, 400, "Bad Request");

	$response = $db->stores_in_range($_GET["latitude"], $_GET["longitude"], $_GET["range"]);    
	sendResponseAndExit(true, 200, "OK", false, $response);

} else {

	sendResponseAndExit(false, 404, "Not Found");
}
$db->close();
