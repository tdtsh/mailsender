<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$id = $_GET['id'];
$req = $_GET['req'];
$rowid = $_GET['rowid'];
$_SESSION['insert_id'] = $id;

// ### 対象更新   #########################################################
$sql = "update shippinginfo  ";
$sql.= "set requestedstatus = '".$req."'  ";
$sql.= "where id = '".$_SESSION['insert_id']."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}



// ### 対象再取得 #########################################################
$sql = "select requestedstatus ";
$sql.= "from shippinginfo  ";
$sql.= "where id = '".$_SESSION['insert_id']."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt");
}

$arr = fetchPgArray($stmt);
$_SESSION['current_shipping'][0]['subject_enc'] = $arr[0]['requestedstatus'];

$_SESSION['mm'][$rowid]['requestedstatus'] = $arr[0]['requestedstatus'];

for($j=0;$j<count($_SESSION['mm'][$rowid]);$j++)
{
	switch ($_SESSION['mm'][$rowid]['requestedstatus'])
	{
		case 0:
			$_SESSION['mm'][$rowid]['requestedstatus_ja'] = '0作成中';
			break;
		case 1:
			$_SESSION['mm'][$rowid]['requestedstatus_ja'] = '1送信可';
			break;
		case 2:
			$_SESSION['mm'][$rowid]['requestedstatus_ja'] = '2中止';
			break;
		default:
			$_SESSION['mm'][$rowid]['requestedstatus_ja'] = ' - ';
			break;
	}
}

header( "Location: index.php" );

?>
