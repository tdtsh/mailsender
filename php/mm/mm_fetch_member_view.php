<head>
	<meta http-equiv="Content-Type" content="text/html;CHARSET=UTF-8">
	<title>配信予約</title>
</head>
<?php
session_start();
include_once( "mm_config.inc" );

$_SESSION['ticket'] = md5(uniqid().mt_rand());
$_SESSION['current_shippingid'] = $_GET['id'];
?>

<body onload="disableEnterKeySubmit()" bgcolor="<?php echo BG_ARTICLE;?>">
<?php echo "接続先PGデータベース : ".PG_HOST; ?>

<a name="pagetop"></a>

<h4>配信予約</h4>
<a href="index.php">メールマガジン一覧にもどる</a>
<br />
<b>メルマガ配信リストを作成し、配信予約を行います。</b>
<br />
※この手順を実行しても、すぐにメルマガは配信されません。何度でもやり直しができます。<br />
CSVファイルで指定したメールアドレス(ファイルは<font color="red">utf-8</font>、改行コードはLFで保存。フォーマットは、NAME,メールアドレス にて。
<br />

<form enctype="multipart/form-data" action="mm_fetch_member.php" method="POST">

<table>
	<tr>
		<td>
			<input type="file" NAME="userfile">
		</td>
	</tr>
	<tr>
		<td>
			<input type="submit" name="submit_button" value="次へ">
			<input type="hidden" name="ticket" value="<?php echo htmlspecialchars($_SESSION['ticket'], ENT_QUOTES); ?>">

		</td>
	</tr>
</table>
</form>

</div>
</div>

</body>
</html>
