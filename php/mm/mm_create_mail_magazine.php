<?php

session_start();
include_once( "mm_config.inc" );
include_once( "mm_db.inc" );

$_SESSION['new_fromaddress'] = T_FROM;
$_SESSION['new_replyto']     = T_REPLYTO;
$_SESSION['new_subject']     = T_SUBJ;
$_SESSION['new_body']        = T_BODY;
$_SESSION['new_id']          = get_oid($con);

header( "Location: mm_create_mailmagazine_view.php" );

?>
