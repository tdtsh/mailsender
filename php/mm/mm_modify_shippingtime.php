<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$time = $_POST['time'];

// ### 対象更新   #########################################################
$sql = "update shippinginfo  ";
$sql.= "set shippingtime = '".$time."'  ";
$sql.= "where id = '".$_SESSION['insert_id']."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}



// ### 対象再取得 #########################################################
$sql = "select shippingtime ";
$sql.= "from shippinginfo  ";
$sql.= "where id = '".$_SESSION['insert_id']."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}

$arr = fetchPgArray($stmt);

$_SESSION['mm'][$_SESSION['rowid']]['shippingtime'] = $arr[0]['shippingtime'];

if(!$_SESSION['mm'][$_SESSION['rowid']]['shippingtime'])
{
	$_SESSION['mm'][$_SESSION['rowid']]['shippingtime_ja'] = " - ";
}
else
{
	$_SESSION['mm'][$_SESSION['rowid']]['shippingtime_ja'] = substr($_SESSION['mm'][$_SESSION['rowid']]['shippingtime'],2,10)."".substr($_SESSION['mm'][$_SESSION['rowid']]['shippingtime'],12,4);
}

header( "Location: index.php" );

?>
