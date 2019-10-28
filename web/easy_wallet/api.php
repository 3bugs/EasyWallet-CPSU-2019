<?php
error_reporting(E_ERROR | E_PARSE);
header('Access-Control-Allow-Origin: *');
header('Content-type: application/json; charset=utf-8');

header('Expires: Sun, 01 Jan 2014 00:00:00 GMT');
header('Cache-Control: no-store, no-cache, must-revalidate');
header('Cache-Control: post-check=0, pre-check=0', FALSE);
header('Pragma: no-cache');

$response = array();

$db = new mysqli("localhost", "root", "", "easy_wallet");

if ($db->connect_errno) {
    $response["error_code"] = 1;
    $response["error_message"] = "เกิดข้อผิดพลาดในการเชื่อมต่อฐานข้อมูล";
    $response["data_list"] = null;
    
    echo json_encode($response);
    exit();
}

$db->set_charset("utf8");

$request = explode('/', trim($_SERVER['PATH_INFO'], '/'));
$action = strtolower(array_shift($request));
$id = array_shift($request);

switch ($action) {
    case "get_ledger":
        $sql = "SELECT * FROM ledger";
        $result = $db->query($sql);

        if ($result) {
            $response["error_code"] = 0;
            $response["error_message"] = "";
            $response["data_list"] = array();
        
            while ($row = $result->fetch_assoc()) {
                $item = array();
        
                $item["id"] = (int)$row["id"];
                $item["description"] = $row["description"];
                $item["amount"] = (int)$row["amount"];
                $item["created_at"] = $row["created_at"];
        
                array_push($response["data_list"], $item);
            }
            $result->close();
        } else {
            $response["error_code"] = 2;
            $response["error_message"] = "เกิดข้อผิดพลาดในการอ่านข้อมูล";
            $response["data_list"] = null;
        }
        break;

    case "insert_ledger":
        $description = $_POST["description"];
        $amount = $_POST["amount"];

        $sql = "INSERT INTO ledger (description, amount) 
                VALUES ('$description', $amount)";
        $result = $db->query($sql);

        if ($result) {
            $response["error_code"] = 0;
            $response["error_message"] = "เพิ่มข้อมูลสำเร็จ";
        } else {
            $response["error_code"] = 3;
            $response["error_message"] = "เกิดข้อผิดพลาดในการเพิ่มข้อมูล";            
        }
        break;
}

echo json_encode($response);
$db->close();