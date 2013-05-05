<?php

/*
 * All database connection variables
 */

define('DB_USER', "leo"); 
define('DB_PASSWORD', "leonardo"); 
define('DB_DATABASE', "shopping_droid"); 
define('DB_SERVER', "localhost"); 



function sendResponseAndExit($success, $code, $description, $extraHeader=false, $JSONResponse=false) {
    header("HTTP/1.1 {$code} {$description}");
    
    if ($success) {
        if ($extraHeader) 
            header($extraHeader);
        
        if ($JSONResponse) {
            header('Content-Type: application/json');
            header('Content-Length: ' . strlen($JSONResponse));
            echo $JSONResponse;
        }
    }else {
        die();
    }
}



?>