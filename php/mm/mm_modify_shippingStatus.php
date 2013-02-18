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
$sql.= "set shippingStatus = '".$req."'  ";
$sql.= "where id = '".$_SESSION['insert_id']."' " ;

$stmt = CreatePgStatement($con,$sql,true);	

if(!$stmt)
{ 
	die("no stmt"); 
}



// ### 対象再取得 #########################################################
$sql = "select shippingStatus ";
$sql.= "from shippinginfo  ";
$sql.= "where id = '".$_SESSION['insert_id']."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{ 
	die("no stmt"); 
}

$arr = fetchPgArray($stmt);
$_SESSION['current_shipping'][0]['subject_enc'] = $arr[0]['shippingstatus'];

$_SESSION['mm'][$rowid]['shippingstatus'] = $arr[0]['shippingstatus'];

for($j=0;$j<count($_SESSION['mm'][$rowid]);$j++)
{
	switch ($_SESSION['mm'][$rowid]['shippingstatus'])
	{
		case 0:
			$_SESSION['mm'][$rowid]['shippingstatus_ja'] = '0配信待';
			break;
		case 1:
			$_SESSION['mm'][$rowid]['shippingstatus_ja'] = '1配信中';
			break;
		case 2:
			$_SESSION['mm'][$rowid]['shippingstatus_ja'] = '2完了';
			break;
		case 10:
			$_SESSION['mm'][$rowid]['shippingstatus_ja'] = '10途中終了';
			break;
		default:
			$_SESSION['mm'][$rowid]['shippingstatus_ja'] = ' - ';
			break;
	}
}

header( "Location: index.php" );

?>
