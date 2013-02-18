<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$id = $_SESSION['current_shipping'][0]['id'];

$mailBody = trim($_POST['mailBody']);
if(DEBUG) echo $mailBody;


// ### 対象subject更新   #########################################################
$sql = "update shippinginfo  ";
$sql.= "set body = '".$mailBody."'  ";
$sql.= "where id = '".$id."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{ 
	die("no stmt"); 
}



// ### 対象subject再取得 #########################################################
$sql = "select body ";
$sql.= "from shippinginfo  ";
$sql.= "where id = '".$id."' " ;
$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}

$arr = fetchPgArray($stmt);
$_SESSION['current_shipping'][0]['body'] = $arr[0]['body'];

header( "Location: index.php" );
?>
