<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;CHARSET=iso-2022-jp">
	<title>メールマガジンプレビュー</title>
</head>
<body onload="disableEnterKeySubmit()">
<?php
session_start();
include_once( "mm_config.inc" );
?>

<b>メールマガジン　プレビュー</b>
<br/>
<a href="index.php">メールマガジン一覧にもどる</a>
<br/><br/>

<table border="1">
	<tr>
		<th nowrap>FROM</th>
		<td nowrap>
			<form action="mm_modify_from.php" method="POST">
				<input type="text" value="<?php echo $_SESSION['current_shipping'][0]['fromaddress'];?>" size="80" maxlength="80" name="fromaddress">
				<input type="submit" value="変更(すぐに反映されます)" name="method_updateFrom">
			</form>
		</td>
	</tr>

	<tr>
		<th nowrap>タイトル</th>
		<td nowrap>
			<form action="mm_modify_subject.php" method="POST">
				<input type="text" value="<?php echo $_SESSION['current_shipping'][0]['subject_enc'];?>" size="80" maxlength="80" name="mailPreTitle">
				<input type="submit" value="変更(すぐに反映されます)" name="method_updateTitle">
			</form>
		</td>
	</tr>

	<tr>
		<th nowrap>本文</th>
		<td nowrap style="font:12pt'ＭＳゴシック'">
			<?php echo $_SESSION['current_shipping'][0]['body_enc'];?>

		</td>
	</tr>
</table>
<br/>

<?php
if(DEBUG)print_r($_SESSION['current_shipping']);
?>

</body>
</html>
