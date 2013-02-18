<?php
session_start();
include_once( "mm_config.inc" );

$_SESSION['insert_id'] = $_GET['id'];
$_SESSION['rowid'] = $_GET['rowid'];

$date = substr($_SESSION['mm'][$_SESSION['rowid']]['shippingtime'],0,16);
?>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;CHARSET=UTF-8">
	<title>送信予約予定の編集</title>
</head>
<body onload="disableEnterKeySubmit()">

<b>メールマガジン　送信開始予定の編集</b>
<br/>
<a href="index.php">メールマガジン一覧にもどる</a>
<br/><br/>

<table border="1">
	<tr>
		<th nowrap>送信開始予定</th>
		<td nowrap >
			<form action="mm_modify_shippingtime.php" method="POST">
				<input type="text" name="time" size="50" value="<?php echo $date;?>"><br/>
					下記フォーマットを厳守ください。<br />
					例: 2007-01-01 01:30<br />
				<input type="submit" value="変更する" name="change"><BR />
			</form>

		</td>
	</tr>

</table>
<br/>
</body>
</html>
