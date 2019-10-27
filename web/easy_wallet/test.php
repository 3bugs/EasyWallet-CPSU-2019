<!DOCTYPE html>
<html>
<body>

<h1>My First Heading</h1>
<p>My first paragraph.</p>

<p>
    เวลาขณะนี้คือ <strong><?php echo (date("H:i:s")); ?></strong>
</p>

<?php

$db = new mysqli("localhost", "root", "", "easy_wallet");

if ($db->connect_errno) {
    echo "เกิดข้อผิดพลาดในการเชื่อมต่อฐานข้อมูล";
    exit();
}

$db->set_charset("utf8");

echo "เชื่อมต่อฐานข้อมูลสำเร็จ<br>";

$sql = "SELECT * FROM ledger";
$result = $db->query($sql);
if ($result) {
    echo "อ่านข้อมูลสำเร็จ";
    while ($row = $result->fetch_assoc()) {
        echo $row["id"] . "<br>";
        echo $row["description"] . "<br>";
        echo $row["amount"] . "<br>";
        echo $row["created_at"] . "<br>";
    }
} else {
    echo "เกิดข้อผิดพลาดในการอ่านข้อมูล: " . $db->error;
}

?>

</body>
</html>