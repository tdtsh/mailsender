<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$_SESSION['insert_id'] = $_GET['id'];
$rowid = $_GET['rowid'];

// ### 対象更新   #########################################################
$sql = "delete from addresslist ";
$sql.= "where shippingid = '".$_SESSION['insert_id']."' " ;
$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}

// ### 対象更新   #########################################################
$sql = "delete from shippinginfo ";
$sql.= "where id = '".$_SESSION['insert_id']."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{ 
	die("no stmt"); 
}

header( "Location: mm_init_top.php" );
?>
