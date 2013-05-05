<?php

/**
 * A class file to connect to database containg the prapared statements of the required queries by the application
 */
class DB_CONNECTION {


    private $db ;
    // constructor
    function __construct() {
        // connecting to database
        $this->connect();
    }

    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }


    private $product_range_pivot_stmt;
    private $product_range_stmt;
    private $pivot_type;
    private $pivot_price;

    private $stores_stmt;
    private $store_stmt;
    private $stores_locations_stmt;
    private $product_global_stmt;
    private $product_stmt;
    private $product_range_global_stmt;
    private $stortid;
    private $barcode_;
    private $diffamount;

   /**
     * Function to connect with database
     */
   function connect() {

        // import database connection variables
    require_once __DIR__ . '/db_config.php';
    $this->db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD,DB_DATABASE)  or die("Connect failed : ".mysqli_connect_error() );
    
    $this->stores_stmt = $this->db->prepare("select * from stores ;");
    
    $this->store_stmt = $this->db->prepare("select * from stores where store_id = ?;");
    $this->store_stmt->bind_param('i',$this->storeid);
    
    $this->stores_locations_stmt = $this->db->prepare("select store_name, latitude, longitude from stores ;");
    
    $this->product_global_stmt = $this->db->prepare("select name, type_name, price, store_name 
        from product, product_type, product_store, stores 
        where product_code = barcode and type = type_id and 
        product_store.store_id = stores.store_id  and barcode = ?;") ;
    $this->product_global_stmt->bind_param('i', $this->barcode_);
    
    $this->product_stmt = $this->db->prepare("select name, type_name, price
        from product, product_type, product_store 
        where product_code = barcode and type = type_id and
        product_store.store_id = ? and barcode = ? ;") ;

    $this->product_stmt->bind_param('ii',$this->storeid ,$this->barcode_);

    $this->product_range_pivot_stmt =  $this->db->prepare("select price, type 
        from product, product_store where  product_code = barcode and barcode = ? and store_id = ?;");
    
    $this->product_range_pivot_stmt->bind_param('ii',$this->barcode_ ,$this->storeid);      
    

    $this->product_range_stmt = $this->db->prepare(" select name, type_name, price, store_name
      from product, stores, product_type, product_store 
      where barcode = product_code and type_id = type and product_store.store_id = stores.store_id 
      and stores.store_id = ? and type_id = ? and price between ? - ? and ? + ?;");

    $this->product_range_stmt->bind_param('iidddd',$this->storeid ,$this->pivot_type,
        $this->pivot_price, $this->diffamount, $this->pivot_price, $this->diffamount);      
    
    

    $this->product_range_global_stmt = $this->db->prepare(" select name, type_name, price, store_name
      from product, stores, product_type, product_store 
      where barcode = product_code and type_id = type and product_store.store_id = stores.store_id 
      and type_id = ? and price between ? - ? and ? + ?;");

    $this->product_range_global_stmt->bind_param('idddd',$this->pivot_type,
        $this->pivot_price, $this->diffamount, $this->pivot_price, $this->diffamount);      

    return $this->db;
}


function store($store_id){    


    if($this->store_stmt)
    {
        $this->storeid = $store_id;                
        $this->store_stmt->execute();
        $this->store_stmt->bind_result($store_idi, $store_name , $latitude, $longitude, $store_address);
        
        $res = array();
        $res["stores"] = array();   
        
        while($this->store_stmt->fetch())
        {
            $currstore = array();
            $currstore["store_id"] = $store_idi;
            $currstore["store_name"] = $store_name;
            $currstore["latitude"] = $latitude;
            $currstore["longitude"] = $longitude;
            $currstore["store_address"] = $store_address;
            
            array_push($res["stores"],$currstore);                   
        }


        $result = json_encode($res, JSON_PRETTY_PRINT); 
        return $result;
        
        
    } else {
        die('Query failed: ' . mysql_error());
    }
}


function product_global($barcode){
    if($this->product_global_stmt) {

        $this->barcode_ = $barcode;
        $this->product_global_stmt->execute();
        $this->product_global_stmt->bind_result($p_name, $type_name ,  $price, $store_name);

        $res = array();
        $res["product"] = array();

        $productdata = array();
        $productdata["stores"] = array();
        $currstore = array();

        while($this->product_global_stmt->fetch()) {
            $currstore["store_name"] = $store_name; 
            $currstore["price"] = $price;
            array_push($productdata["stores"],$currstore);                   
        }            
        $productdata["name"] = $p_name;
        $productdata["type"] = $type_name;

        array_push($res["product"],$productdata);

        $result = json_encode($res, JSON_PRETTY_PRINT);
        return $result;
    } else{
        die('Query failed: ' . mysql_error());
    }

}


function product($barcode, $store_id){
    if($this->product_stmt){
        
        $this->barcode_= $barcode;
        $this->storeid = $store_id; 
        $this->product_stmt->execute();
        $this->product_stmt->bind_result($p_name, $type_name ,  $price);

        $res = array();
        $res["product"] = array();
        $productdata = array();

        while($this->product_stmt->fetch())
        {
            $productdata["name"] = $p_name;
            $productdata["type"] = $type_name;
            $productdata["price"] = $price;         
            array_push($res["product"], $productdata);                   
        }
        
        $result = json_encode($res, JSON_PRETTY_PRINT);
        return $result;
    } else{
        die('Query failed: ' . mysqli_error($this->db));
    }
}



function stores(){
    if($this->stores_stmt)
    {
        $this->stores_stmt->execute();
        $this->stores_stmt->bind_result($store_idi, $store_name , $latitude, $longitude, $store_address);

        $res = array();
        $res["stores"] = array();   
        $currstore = array();
        
        while($this->stores_stmt->fetch())
        {
            $currstore["store_id"] = $store_idi;
            $currstore["store_name"] = $store_name;
            $currstore["latitude"] = $latitude;
            $currstore["longitude"] = $longitude;
            $currstore["store_address"] = $store_address;
            array_push($res["stores"],$currstore);                   
        }

        $result = json_encode($res, JSON_PRETTY_PRINT); 
        return $result;


    } else {
        die('Query failed: ' . mysqli_error($this->db));
    }    
}



function stores_locations(){
    if($this->stores_locations_stmt)
    {
        $this->stores_locations_stmt->execute();
        $this->stores_locations_stmt->bind_result($store_name , $latitude, $longitude);

        $res = array();
        $res["stores"] = array();   
        $currstore = array();

        while($this->stores_locations_stmt->fetch())
        {
            $currstore["store_name"] = $store_name;
            $currstore["latitude"] = $latitude;
            $currstore["longitude"] = $longitude;
            array_push($res["stores"],$currstore);                   
        }

        $result = json_encode($res, JSON_PRETTY_PRINT); 
        return $result;


    } else {
        die('Query failed: ' . mysql_error());
    }
}


function product_range($barcode, $store_id, $diff_amount){
    if($this->product_range_pivot_stmt && $this->product_range_stmt) 
    {       
        $this->barcode_ = $barcode;
        $this->storeid = $store_id;
        
        $this->product_range_pivot_stmt->execute();
        $this->product_range_pivot_stmt->bind_result($price,$type_id);
        $this->product_range_pivot_stmt->fetch();

        $this->pivot_price = $price;
        $this->pivot_type = $type_id; 
        $this->diffamount = $diff_amount;

        $this->product_range_pivot_stmt->close();
        
        $this->product_range_stmt->execute();
        $this->product_range_stmt->bind_result($p_name, $type_name , $price, $store_name);
        
        $res = array();
        $res["store"] = array();
        
        $typedata = array();
        $typedata["store_name"] = "";
        $typedata["type_name"] = "";
        $typedata["products"] = array();

        $currproduct = array();
        
        while($this->product_range_stmt->fetch()) 
        {
            $currproduct["product_name"] = $p_name;
            $currproduct["price"] = $price;
            array_push($typedata["products"],$currproduct);                   
        }
        $typedata["store_name"] = $store_name; 
        $typedata["type_name"] = $type_name;

        array_push($res["store"],$typedata);

        $result = json_encode($res, JSON_PRETTY_PRINT);
        return $result;
    } 
    else
    {
        die('Query failed: ' . mysqli_error($this->db));
    }
}       

function product_range_global($barcode, $store_id, $diff_amount){
    
    if($this->product_range_global_stmt) 
    {
        
        $this->barcode_ = $barcode;
        $this->storeid = $store_id;
        
        $this->product_range_pivot_stmt->execute();
        $this->product_range_pivot_stmt->bind_result($price,$type_id);
        $this->product_range_pivot_stmt->fetch();

        $this->pivot_price = $price;
        $this->pivot_type = $type_id; 
        $this->diffamount = $diff_amount;
        $this->product_range_pivot_stmt->close();

        $this->product_range_global_stmt->execute();
        $this->product_range_global_stmt->bind_result($p_name, $type_name , $price, $store_name);

        $res = array();
        $res["type"] = array();

        $typedata = array();
        $typedata["type_name"] = "";
        $typedata["products"] = array();

        $currproduct = array();

        while($this->product_range_global_stmt->fetch()) 
        {
            $currproduct["store_name"] = $store_name; 
            $currproduct["product_name"] = $p_name;
            $currproduct["price"] = $price;

            array_push($typedata["products"],$currproduct);                   
        }
        $typedata["type_name"] = $type_name;

        array_push($res["type"],$typedata);

        $result = json_encode($res, JSON_PRETTY_PRINT);
        return $result;
    } 
    else
    {
        die('Query failed: ' . mysqli_error($this->db));
    }
}       




    /**
     * Function to close db connection
     */
    function close() {
        // closing db connection
        $this->stores_stmt->close();
        $this->store_stmt->close();
        $this->this->stores_locations_stmt->close();
        $this->product_global_stmt->close();
        $this->product_stmt->close();
        $this->product_range_global_stmt->close();
        $this->db->close();
    }       

}

?>
