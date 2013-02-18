<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

//define( "DEBUG", true ); 
error_reporting(0);

$shippingid = $_SESSION['current_shippingid'];

if (isset($_POST['submit_button'], $_SESSION['ticket'], $_POST['ticket']) && $_SESSION['ticket'] === $_POST['ticket']) 
{
	unset($_SESSION['ticket']);

	if( $_SESSION['insert_status'][$_SESSION['current_shippingid']] > 1 )
	{
		header( "Location: mm_fetch_member_error.php" );
		exit;
	}


	$_SESSION['insert_status'][$_SESSION['current_shippingid']] = 2;
	$_SESSION['start_time'] = date( "Y-m-d H:i:s",time());

	// ### 対象再取得 #########################################################

	$uploaddir = MM_ROOT;
	$uploadfile = $uploaddir . basename($_FILES['userfile']['name']);

	if(DEBUG)
	{
		echo "uploadfile name =" . $uploadfile."<BR>";
		echo "userfile tmp_name =". $_FILES['userfile']['tmp_name']."<BR>";
	}

	if (move_uploaded_file($_FILES['userfile']['tmp_name'], $uploadfile)) 
	{
		echo "File is valid, and was successfully uploaded.\n";
	}
   	else 
	{
		echo "Possible file upload attack!<br>";
	}

	if(DEBUG)
	{
		echo "uploadFile =" . $uploadfile."<BR>";
	}

	$row = 0;
	$handle = fopen($uploadfile, "rb");
	if(DEBUG)
	{
		echo "handle =" . $handle."<BR>";
	}
	while ( $data = fgetcsv($handle, 300, ",") ) 
	{
		$arr[$row][0] = mb_convert_encoding($data[0],'utf-8','utf-8');
		$arr[$row][1] = $data[1];
		$row++;
	}
	if(DEBUG)
	{
		echo "fopen row =" . $row."<BR>";
	}
	fclose($handle);
	unlink($uploadfile);


	for($i=0;$i<=count($arr);$i++)
	{
		if( count($arr[$i]) < 1 )
		{
			continue; 
		}
			$arr_pg[$i]['name'] = trim(ereg_replace( "'", '', $arr[$i][0] ));
		$arr_pg[$i]['mail'] = trim(ereg_replace( "'", '', $arr[$i][1] ));
	}
	$count_arr = count($arr);
	$_SESSION['count_select'] = $count_arr;

	$count_arr_pg = count($arr_pg);
	$_SESSION['count_insert'] = $count_arr_pg;

	$sqlFile = MM_ROOT.'insert_list.sql';
	if($fp = fopen( $sqlFile, "wb"))
	{
		echo "fp=".$fp;
	}

	$goodcount = 0;
	$badcount = 0;

	for($i=0;$i<=$count_arr_pg;$i++)
	{
		if( ( $arr_pg[$i]['name'] == '' ) or ( $arr_pg[$i]['mail'] == '' ) or
			( $arr_pg[$i]['name'] == null ) or ( $arr_pg[$i]['mail'] == null ) 
		)
		{ 
			//$badcount++;
			continue;
		}
		$arr_mail = explode( "@", $arr_pg[$i]['mail'], 2 );
		if( count($arr_mail) < 2 )
		{
			$badcount++;
			continue;
		}
		$account = $arr_mail[0];
		$domain  = $arr_mail[1];

		// ### 対象更新   #########################################################
		$sql = "insert into addresslist ";
		$sql.= "(id,account,domain,name,shippingid)";
		$sql.= " values (";
		$sql.= "'".get_oid($con)."'";
		$sql.= ",'".$account."'";
		$sql.= ",'".$domain."'";
		$sql.= ",'".$arr_pg[$i]['name']."'";
		$sql.= ",'".$shippingid."'";
		$sql.= ");";

		if(DEBUG)echo $sql."<BR />";
		fputs($fp, $sql."\r\n");
		$goodcount++;
	}
	fclose($fp);
	chmod($sqlFile, 0777);

	// psqlへのパス
	//$pgPath = "/usr/local/pgsql/bin/";
	$pgPath = "";
	$command = "".$pgPath."psql -U ".PG_USER." -d ".PG_DBNM." -h ".PG_HOST." -f ".$sqlFile."";
	$str = exec( $command, $arrout );
	$count =  count($arrout);
	if(DEBUG) echo "<BR>count:".$count."<BR><BR>";
	if(DEBUG) echo "<BR>command is ".$command."<BR>";
	if(DEBUG) echo "<BR>str is ".$str."<BR><BR>";
	//unlink($sqlFile);
	$_SESSION['count_insert'] = $count;
	$_SESSION['badcount'][$_SESSION['current_shippingid']] = $badcount;
	$_SESSION['goodcount'][$_SESSION['current_shippingid']] = $goodcount;

	$_SESSION['insert_status'][$_SESSION['current_shippingid']] = 1;

	$_SESSION['end_time'] = date( "Y-m-d H:i:s",time());

} 
else 
{
	header( "Location: mm_fetch_member_error.php" );
	exit;
}

if(DEBUG)
{
	echo "<font color='red'><b>";
	echo "配信対象数     : ".$_SESSION['count_select']."<br />";
	echo "内アドレス不正 : ";
	echo $_SESSION['badcount'][$_SESSION['current_shippingid']];
	echo "<br />";
	echo "送信予約数     : ";
	echo $_SESSION['count_insert']; 
	echo "<br />";
	echo "登録開始時間   : ";
	echo $_SESSION['start_time'];
	echo "<br />";
	echo "登録終了時間   : ";
	echo $_SESSION['end_time'];
	echo "<br />";
	echo "<a href='mm_init_top.php'>次のページへ</a><br />";
	echo "<br />";
	echo "</b></font>";
}
else
{
	header( "Location: mm_init_top.php" );
}
?>
