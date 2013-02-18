<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$id = $_SESSION['current_shipping'][0]['id'];

$fromaddress = $_POST['fromaddress'];
if(DEBUG) echo $fromaddress;
$fromaddress = mb_convert_encoding( $fromaddress , 'utf-8', 'iso-2022-jp' );

// ### 対象subject更新   #########################################################
$sql = "update shippinginfo  ";
$sql.= "set fromaddress = '".$fromaddress."'  ";
$sql.= "where id = '".$id."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{ 
	die("no stmt"); 
}

// ### 対象subject再取得 #########################################################
$sql = "select fromaddress ";
$sql.= "from shippinginfo  ";
$sql.= "where id = '".$id."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}
$arr = fetchPgArray($stmt);

// Subject
$str = mb_convert_encoding( $arr[0]['fromaddress'], 'iso-2022-jp', 'utf-8' );
$_SESSION['current_shipping'][0]['fromaddress'] = $str;

header( "Location: mm_init_top.php" );
?>
