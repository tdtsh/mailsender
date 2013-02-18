<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;CHARSET=UTF-8">
	<title>メールマガジン一覧</title>
</head>
<?php
session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );
?>
<body onload="disableEnterKeySubmit()" bgcolor="<?php echo BG_ARTICLE;?>">
<center>
<form action="/hoge" method="POST">
<b>メルマガ配信システム 略して「メルシス」</b>
<br>
<a href="mm_init_top.php">リロードする </a>
&nbsp;
<a href="mm_create_mail_magazine.php"><img src="../images/newMail.gif" alt="" border="0">新規メールマガジン作成</a>
<?php echo "接続先PGデータベース : ".PG_HOST; ?>
</center>
<table border=1 align="center">
	<tr>
		<th>件名<BR>(本文プレビュー、FROM変更)</th>
		<th>配信済<BR>/予約件数</th>
		<th>送信<BR>予約<BR>/取消</th>
		<th>要求<BR>STATUS</th>
		<th>要求<BR>変更</th>
		<th>配信<br>STATUS</th>
		<th>配信Sts<BR>変更</th>
		<th>送信開始<BR>予定日時</th>
		<th>送信開始<BR>実績日時</th>
		<th>送信終了<BR>実績日時</th>
		<th>本文<BR>編集</th>
		<th>完全<br>削除</th>
	</tr>
<?php 
for($i=0; $i< count($_SESSION['mm'])-1;$i++){
	if( $_SESSION['mm'][$i]['requestedstatus'] == "2" ){
		echo "<tr bgcolor=\"dimgray\">";
	}else{
		echo "<tr>";
	}
	// 件名
	echo "<td><a href=\"mm_edit_subject.php?id="
		.$_SESSION['mm'][$i]['id']."\"><img src=\"../images/mail.gif\" alt=\"\" border=\"0\">"
		.$_SESSION['mm'][$i]['subject']."</a></td>";

	// 配信済 / 予約件数
	echo "<td align=\"center\">"
		.$_SESSION['mm'][$i]['count_send']." / ".$_SESSION['mm'][$i]['count_all']
		."</td>";

	if ( $_SESSION['mm'][$i]['count_all'] - $_SESSION['mm'][$i]['count_send']  == 0 ){
		// 送信予約
		echo "<td align=\"center\"><a href=\"mm_fetch_member_view.php?id="
			.$_SESSION['mm'][$i]['id']
			."&rowid=".$i
			."\"><img src=\"../images/send.gif\" alt=\"reg\" border=\"0\"></a></td>";
	}else{
		// 送信予約解除
		echo "<td align=\"center\"><a href=\"mm_modify_member_to_send.php?id="
			.$_SESSION['mm'][$i]['id']
			."&rowid=".$i
			."\"><img src=\"../images/trash.gif\" alt=\"del\" border=\"0\"></a></td>";
	}


	if( $_SESSION['mm'][$i]['requestedstatus'] == "2" ){
		// 要求ステータス
		echo "<td align=\"center\" bgcolor=\"dimgray\">".$_SESSION['mm'][$i]['requestedstatus_ja']."</td>";

		// 要求変更
		echo "<td align=\"center\"><a href=\"mm_modify_requestedStatus.php?req=0&id="
			.$_SESSION['mm'][$i]['id'].
			"&rowid=".$i.
			"\"><img src=\"../images/out.gif\" alt=\"Trash\" border=\"0\"></a></td>";
	}elseif( $_SESSION['mm'][$i]['requestedstatus'] == "0" ){
		// 要求ステータス
		echo "<td align=\"center\">".$_SESSION['mm'][$i]['requestedstatus_ja']."</td>";

		// 要求変更
		echo "<td align=\"center\"><a href=\"mm_modify_requestedStatus.php?req=1&id="
			.$_SESSION['mm'][$i]['id'].
			"&rowid=".$i.
			"\"><img src=\"../images/out.gif\" alt=\"Trash\" border=\"0\"></a></td>";
	}elseif( $_SESSION['mm'][$i]['requestedstatus'] == "1" ){
		// 要求ステータス
		echo "<td align=\"center\" bgcolor=\"blue\">".$_SESSION['mm'][$i]['requestedstatus_ja']."</td>";

		// 要求変更
		echo "<td align=\"center\"><a href=\"mm_modify_requestedStatus.php?req=2&id="
			.$_SESSION['mm'][$i]['id'].
			"&rowid=".$i.
			"\"><img src=\"../images/trash.gif\" alt=\"Trash\" border=\"0\"></a></td>";
	}else{
		// 要求変更
		echo "<td align=\"center\"><a href=\"mm_modify_requestedStatus.php?req=2&id="
			.$_SESSION['mm'][$i]['id'].
			"&rowid=".$i.
			"\"><img src=\"../images/trash.gif\" alt=\"Trash\" border=\"0\"></a></td>";
	}

	// 配信ステータス
	if( $_SESSION['mm'][$i]['shippingstatus'] == "2" ){
		echo "<td align=\"center\" bgcolor=\"dimgray\">";
		echo $_SESSION['mm'][$i]['shippingstatus_ja']."</td>";
	}elseif( $_SESSION['mm'][$i]['shippingstatus'] == "10" ){
		echo "<td align=\"center\" bgcolor=\"red\">";
		echo $_SESSION['mm'][$i]['shippingstatus_ja']."</td>";
	}elseif( $_SESSION['mm'][$i]['shippingstatus'] == "0" ){
		echo "<td align=\"center\" bgcolor=\"blue\" >";
		echo $_SESSION['mm'][$i]['shippingstatus_ja']."</td>";
	}elseif( $_SESSION['mm'][$i]['shippingstatus'] == "1" ){
		echo "<td align=\"center\" bgcolor=\"yellow\">";
		echo $_SESSION['mm'][$i]['shippingstatus_ja']."</td>";
	}else{
		echo "<td align=\"center\">";
		echo $_SESSION['mm'][$i]['shippingstatus_ja']."</td>";
	}
	
	if( $_SESSION['mm'][$i]['shippingstatus'] == "2" ){
		// 配信ステータス変更
		echo "<td align=\"center\"><a href=\"mm_modify_shippingStatus.php?req=0&id="
			.$_SESSION['mm'][$i]['id']
			."&rowid=".$i
			."\"><img src=\"../images/out.gif\" alt=\"Trash\" border=\"0\"></a></td>";
	}elseif( $_SESSION['mm'][$i]['shippingstatus'] == "0" ){
		// 配信ステータス変更
		echo "<td align=\"center\"><a href=\"mm_modify_shippingStatus.php?req=2&id="
			.$_SESSION['mm'][$i]['id']
			."&rowid=".$i
			."\"><img src=\"../images/trash.gif\" alt=\"Trash\" border=\"0\"></a></td>";
	}else{
		// 配信ステータス変更
		echo "<td align=\"center\"><a href=\"mm_modify_shippingStatus.php?req=0&id="
			.$_SESSION['mm'][$i]['id']
			."&rowid=".$i
			."\"><img src=\"../images/trash.gif\" alt=\"Trash\" border=\"0\"></a></td>";
	}

	echo "<td align=\"center\"><a href=\"mm_modify_shippingtime_view.php?id="
		.$_SESSION['mm'][$i]['id']
		."&rowid=".$i
		."\">"
		.$_SESSION['mm'][$i]['shippingtime_ja']
		."</a></td>";

	// 送信開始
	echo "<td align=\"center\">".$_SESSION['mm'][$i]['begintime_ja']."</td>";
	// 送信終了
	echo "<td align=\"center\">".$_SESSION['mm'][$i]['completiontime_ja']."</td>";
	// 本文編集
	echo "<td align=\"center\"><a href=\"mm_edit_body.php?id="
		.$_SESSION['mm'][$i]['id']
		."\"><img src=\"../images/colletion.gif\" alt=\"modify\" border=\"0\"></a></td>";

	// 完全削除
	echo "<td align=\"center\"><a href=\"mm_delete_member.php?req=2&id="
		.$_SESSION['mm'][$i]['id'].
		"&rowid=".$i.
		"\"><img src=\"../images/trash.gif\" alt=\"Trash\" border=\"0\"></a></td>";

	echo "</tr>";
}
?>
</table>
<input type="hidden" value="templateList" name="hiddenPageFlag" />
<input type="hidden" value="200609261916-1000984" name="hiddenTemId" />
</form>
</body>
</html>
