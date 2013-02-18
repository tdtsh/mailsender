<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$_SESSION['insert_id'] = $_GET['id'];
$rowid = $_GET['rowid'];

$novalue = '2100-01-01 00:00:00';

// ### 対象更新   #########################################################
$sql = "delete from addresslist ";
$sql.= "where shippingid = '".$_SESSION['insert_id']."' " ;
$sql.= "and shippingtime is null ";

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}



// ### 対象再取得 #########################################################
$sql = "select  ";
$sql.= "(select count(*) from addresslist where shippingid = '".$_SESSION['insert_id']."' " ;
$sql.= "and shippingtime is not null ) as count_send ";
$sql.= ",(select count(*) from addresslist where shippingid = '".$_SESSION['insert_id']."' ) as count_all ";

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}
$arr = fetchPgArray($stmt);


$_SESSION['mm'][$rowid]['count_send'] = $arr[0]['count_send'];
$_SESSION['mm'][$rowid]['count_all'] = $arr[0]['count_all'];
$_SESSION['insert_status'][$_SESSION['insert_id']] = 0;

header( "Location: index.php" );
?>
