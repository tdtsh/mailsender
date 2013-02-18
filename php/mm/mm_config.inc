<?php
include_once( "mm_func.inc" );

if($_SERVER['SERVER_NAME']=='localhost' ) {
   	//localtest用
	define( "APP_HOST", "localhost" );
	define( "BG_TOP", "white" );
	define( "BG_ARTICLE", "white" );
	define( "BG_SERIALARTICLES", "white" );
	define( "BG_AREA", "white" );
	define( "BG_CIRCLE", "white" );
	define( "BG_TASKINFO", "white" );
	define( "BG_BLOG", "white" );

	define( "PG_HOST", "localhost" );
	define( "PG_USER", "slode" ); 
	define( "PG_PASS", "mmuser" ); 
	define( "PG_DBNM", "mailmagazine" ); 

	define( "SITENAME", "メルマガ送信システム(localDB)" ); 
}



	define( "REQUESTED_STATUS_0_WAIT"  , "0" );
	define( "REQUESTED_STATUS_1_ABLE"  , "1" );
	define( "REQUESTED_STATUS_2_STOP"  , "2" );
	define( "SENDING_STATUS_0_WAIT"    , "0" );
	define( "SENDING_STATUS_1_PROGRESS", "1" );
	define( "SENDING_STATUS_2_FINISHED", "2" );
	define( "SENDING_STATUS_10_ABORT"  , "10");
define( "RETRYABLE"                , false ); 

//define( "DEBUG", true ); 
define( "DEBUG", false ); 

define( "STOP_HEADER_LOCATION", false ); 
$thisDateTime = date( "Y-m-d H:i:s",time());

define( "SERVER_NUM", "9" );
define( "PAGING_NUM_OF_ROW", "50" );

define( "T_REPLYTO","hoge@hoge.ne.jp" );
define( "T_FROM", "foo@foo.ne.jp" ); 
define( "T_SUBJ", "メルマガ No.XXX●タイトル入力" ); 
define( "T_BODY", '

$NAME 様

本文入力
' ); 


define("MM_ROOT", "/Users/yourname/workspace/merusen/php/");

?>
