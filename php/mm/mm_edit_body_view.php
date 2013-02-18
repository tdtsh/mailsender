<?php
session_start();
include_once( "mm_config.inc" );
$body = trim($_SESSION['current_shipping'][0]['body']);
?>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;CHARSET=UTF-8">
	<title>本文編集</title>
</head>
<body onload="disableEnterKeySubmit()">

<b>メールマガジン　本文編集</b>
<br/>
<a href="index.php">メールマガジン一覧にもどる</a>
<br/><br/>

<table border="1">
	<tr>

	<tr>
		<th nowrap>タイトル</th>
		<td nowrap>
			<?php echo $_SESSION['current_shipping'][0]['subject'];?>
		</td>
	</tr>

	<tr>
		<th nowrap>本文</th>
		<td nowrap style="font:12pt'ＭＳゴシック'">
			<form action="mm_modify_body.php" method="POST">
				<input type="submit" value="変更する" name="method_updateBody"><BR />
				<textarea rows="50" cols="70" name="mailBody"><?php echo $body;?></textarea><br/>
			</form>

		</td>
	</tr>

</table>
<br/>

<?php
	if(DEBUG)print_r($_SESSION['current_shipping']);
?>

</body>
</html>
