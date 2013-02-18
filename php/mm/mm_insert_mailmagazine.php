<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$body            = trim($_POST['body']);
$subject         = $_POST['subject'];
$fromaddress     = $_POST['fromaddress'];
$shippingstatus  = '0';
$requestedstatus = '0';
$replyto         = $_POST['replyto'];

// ### #########################################################
$sql = "insert into shippinginfo  ";
$sql.= "(id,body,subject,fromaddress,returnaddress,shippingstatus,requestedstatus) ";
$sql.= " values ( ";

$sql.= " '".$_SESSION['new_id']."' ";
$sql.= ",'".$body."'  ";
$sql.= ",'".$subject."'  ";
$sql.= ",'".$fromaddress."'  ";
$sql.= ",'".$replyto."'  ";
$sql.= ",'".$shippingstatus."'  ";
$sql.= ",'".$requestedstatus."'  ";
$sql.= ") ";

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}

header( "Location: mm_init_top.php" );
?>
