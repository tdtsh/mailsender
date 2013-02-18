<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$_SESSION['mm'] = null;

// ### ShippingID一覧 #########################################################
$sql = "select  ";
$sql.= "id, begintime, completiontime,fromaddress, lastmodified,requestedstatus ";
$sql.= ",(select count(*) from addresslist where shippingid = si.id ";
$sql.= "and shippingtime is not null ) as count_send ";
$sql.= ",(select count(*) from addresslist where shippingid = si.id ) as count_all ";
$sql.= ", reservetime, returnaddress, shippingstatus, shippingtime, subject ";
$sql.= "from shippinginfo si ";
$sql.= "order by id desc ";
$sql.= "limit 10 offset 0 ";


$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{
	die("no stmt"); 
}

$_SESSION['mm'] = fetchPgArray($stmt);

for($i=0;$i<count($_SESSION['mm']);$i++)
{
	for($j=0;$j<count($_SESSION['mm'][$i]);$j++)
	{
		switch ($_SESSION['mm'][$i]['requestedstatus'])
		{
			case 0:
				$_SESSION['mm'][$i]['requestedstatus_ja'] = '0作成中';
				break;
			case 1:
				$_SESSION['mm'][$i]['requestedstatus_ja'] = '1送信可';
				break;
			case 2:
				$_SESSION['mm'][$i]['requestedstatus_ja'] = '2中止';
				break;
			default:
				$_SESSION['mm'][$i]['requestedstatus_ja'] = ' - ';
				break;
		}

		switch ($_SESSION['mm'][$i]['shippingstatus'])
		{
			case 0:
				$_SESSION['mm'][$i]['shippingstatus_ja'] = '0配信待';
				break;
			case 1:
				$_SESSION['mm'][$i]['shippingstatus_ja'] = '1配信中';
				break;
			case 2:
				$_SESSION['mm'][$i]['shippingstatus_ja'] = '2完了';
				break;
			case 10:
				$_SESSION['mm'][$i]['shippingstatus_ja'] = '10途中終了';
				break;
			default:
				$_SESSION['mm'][$i]['shippingstatus_ja'] = ' - ';
				break;
		}

		if(!$_SESSION['mm'][$i]['begintime'])
		{
			$_SESSION['mm'][$i]['begintime_ja'] = " - ";
		}
		else
		{
			$_SESSION['mm'][$i]['begintime_ja'] = substr($_SESSION['mm'][$i]['begintime'],2,10)."".substr($_SESSION['mm'][$i]['begintime'],12,4);
		}

		if(!$_SESSION['mm'][$i]['shippingtime'])
		{
			$_SESSION['mm'][$i]['shippingtime_ja'] = " - ";
		}
		else
		{
			$_SESSION['mm'][$i]['shippingtime_ja'] = substr($_SESSION['mm'][$i]['shippingtime'],2,10)."".substr($_SESSION['mm'][$i]['shippingtime'],12,4);
		}

		if(!$_SESSION['mm'][$i]['completiontime'])
		{
			$_SESSION['mm'][$i]['completiontime_ja'] = " - ";
		}
		else
		{
			$_SESSION['mm'][$i]['completiontime_ja'] = substr($_SESSION['mm'][$i]['completiontime'],2,10)."".substr($_SESSION['mm'][$i]['completiontime'],12,4);
		}

		if(!$_SESSION['mm'][$i]['lastmodified'])
		{
			$_SESSION['mm'][$i]['lastmodified_ja'] = " - ";
		}
		else
		{
			$_SESSION['mm'][$i]['lastmodified_ja'] = substr($_SESSION['mm'][$i]['lastmodified'],2,10)."".substr($_SESSION['mm'][$i]['lastmodified'],12,4);
		}
	}
}

//print_r($_SESSION['mm']);
//exit;

header( "Location: index.php" );
?>
