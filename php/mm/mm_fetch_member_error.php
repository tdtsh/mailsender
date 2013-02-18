<?php
session_start();
include_once( "mm_config.inc" );
?>
<head>
	<meta http-equiv="Content-Type" content="text/html;CHARSET=UTF-8">
	<title>error</title>
</head>
<body onload="disableEnterKeySubmit()">
<a name="pagetop"></a>

<h4>ダブルクリックまたは二重投稿を検出しました</h4>
<br />
ダブルクリックまたは二重投稿を検出しましたので、処理を中断します。
ダブルクリックをするとデータが二重登録されます。
<br />
もう一度、最初からやり直してください。
<?php	echo "insert_status is invalid".$_SESSION['insert_status'][$_SESSION['current_shippingid']]; ?>
<br />
<a href="mm_init_top.php">メールマガジン一覧にもどる</a>
<br />
</body>
</html>
