<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;CHARSET=iso-2022-jp">
	<title>$B%a!<%k%^%,%8%s%W%l%S%e!<(B</title>
</head>
<body onload="disableEnterKeySubmit()">
<?php
session_start();
include_once( "mm_config.inc" );
?>

<b>$B%a!<%k%^%,%8%s!!%W%l%S%e!<(B</b>
<br/>
<a href="index.php">$B%a!<%k%^%,%8%s0lMw$K$b$I$k(B</a>
<br/><br/>

<table border="1">
	<tr>
		<th nowrap>FROM</th>
		<td nowrap>
			<form action="mm_modify_from.php" method="POST">
				<input type="text" value="<?php echo $_SESSION['current_shipping'][0]['fromaddress'];?>" size="80" maxlength="80" name="fromaddress">
				<input type="submit" value="$BJQ99(B($B$9$0$KH?1G$5$l$^$9(B)" name="method_updateFrom">
			</form>
		</td>
	</tr>

	<tr>
		<th nowrap>$B%?%$%H%k(B</th>
		<td nowrap>
			<form action="mm_modify_subject.php" method="POST">
				<input type="text" value="<?php echo $_SESSION['current_shipping'][0]['subject_enc'];?>" size="80" maxlength="80" name="mailPreTitle">
				<input type="submit" value="$BJQ99(B($B$9$0$KH?1G$5$l$^$9(B)" name="method_updateTitle">
			</form>
		</td>
	</tr>

	<tr>
		<th nowrap>$BK\J8(B</th>
		<td nowrap style="font:12pt'$B#M#S%4%7%C%/(B'">
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
