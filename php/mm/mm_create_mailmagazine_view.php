<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;CHARSET=UTF-8">
	<title>メルマガ新規作成</title>
</head>
<?php
session_start();
include_once( "mm_config.inc" );
?>

<body onload="disableEnterKeySubmit()">
<b>メールマガジン　新規作成</b>
<br/>
<a href="index.php">メールマガジン一覧にもどる</a>
<br/><br/>

<form action="mm_insert_mailmagazine.php" method="POST">
<input type="submit" value="確定する" name="create">
<table border="1">
	<tr>
		<th nowrap>内部ID</th>
		<td nowrap><?php echo $_SESSION['new_id']; ?></td>
	</tr>

	<tr>
		<th nowrap>FROM</th>
		<td nowrap>
				<input type="text" value="<?php echo $_SESSION['new_fromaddress'];?>" size="80" maxlength="80" name="fromaddress">
		</td>
	</tr>

	<tr>
		<th nowrap>REPRY-TO</th>
		<td nowrap>
				<input type="text" value="<?php echo $_SESSION['new_replyto'];?>" size="80" maxlength="80" name="replyto">
		</td>
	</tr>

	<tr>
		<th nowrap>タイトル</th>
		<td nowrap>
				<input type="text" value="<?php echo $_SESSION['new_subject'];?>" size="80" maxlength="80" name="subject">
		</td>
	</tr>

	<tr>
		<th nowrap>本文</th>
		<td nowrap style="font:12pt'ＭＳゴシック'">
			<textarea rows="50" cols="70" name="body"><?php echo $_SESSION['new_body'];?></textarea><br/>
		</td>
	</tr>

</table>
</form>
<br/>
</body>
</html>
