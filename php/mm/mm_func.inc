<?php

function get_oid($con)
{

	$seq_name = "seq_OID";
	$sql = "select nextval( '".$seq_name."' ) ";
	$stmt = createPgStatement($con,$sql,false);	
	$arr = fetchPgArray($stmt);
	$res = $arr[0]['nextval'];
	$seq = str_pad($res, 6, "0", STR_PAD_LEFT);
	$datetime =  date("YmdHi", time() );
	$oid = $datetime."-".SERVER_NUM.$seq;
	return $oid;
}

function send_mail($szSubj,$szData,$szTo="hoge@foo.ne.jp",$szMailFrom="bar@hoge.ne.jp",$szMailInfo="hogehoge")
{
	$szData = mb_convert_encoding($szData, "iso-2022-jp", "auto");
	$rMail = mail($szTo, $szSubj, $szData,"From:".mb_encode_mimeheader( $szMailFrom )."< ".$szMailInfo." >");

	return $rMail;

}

?>
