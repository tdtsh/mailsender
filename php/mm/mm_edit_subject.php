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

// 本文
$str = str_replace( "\n","<br>", $_SESSION['current_shipping'][0]['body'] );
$str = mb_convert_encoding( $str, 'iso-2022-jp', 'utf-8' );
$_SESSION['current_shipping'][0]['body_enc'] = $str;

// Subject
$str = mb_convert_encoding( $_SESSION['current_shipping'][0]['subject'], 'iso-2022-jp', 'utf-8' );
$_SESSION['current_shipping'][0]['subject_enc'] = $str;

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
	header( "Location: mm_edit_subject_view.php" );
}

?>
