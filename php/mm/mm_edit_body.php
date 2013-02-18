<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$id = $_GET['id'];

// ### 対象Shipping #########################################################
$sql = "select * ";
$sql.= "from shippinginfo  ";
$sql.= "where id = '".$id."' " ;

$stmt = CreatePgStatement($con,$sql,true);	
if(!$stmt)
{ 
	die("no stmt"); 
}

$_SESSION['current_shipping'] = fetchPgArray($stmt);

if(STOP_HEADER_LOCATION)
{
	for($i=0;$i<count($_SESSION['current_shipping']);$i++)
	{
		for($j=0;$j<count($_SESSION['current_shipping'][$i]);$j++)
		{
			echo $_SESSION['current_shipping'][$i][$j];
			echo " ";
		}
		echo "<BR />";
	}
	echo "<BR />";
	echo "<BR />";
	echo $sql;
	echo "<BR />";
	echo "<BR />";
	echo "<a href=\"mm_edit_subject_view.php\" >next</a>";
}
else
{
	header( "Location: mm_edit_body_view.php" );
}

?>
