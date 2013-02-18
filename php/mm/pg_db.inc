<?php
/**
 *  @file pg_db.inc
 *  @brief PostgreSQL用手抜き関数
 *  @author tdtsh
 *  @date 2006-10-13
 *  @version $Id$
 */
function createPgConnection($host="localhost" , $user="slode",$pass="mmuser",$db="mailmagazine",$err_display = false )
{
	$con = pg_connect( "host=".$host." port=5432 dbname=".$db." user=".$user." password=".$pass);
	if(!$con)
	{
		if ( $err_display )
		{
			die( "unable connect to ".$tns );
		}
		else
		{
			return false;
		}
	}
	return $con;
}

function createPgStatement($con,$sql,$err_display=false)
{
	$stmt = pg_query( $con, $sql );

	if(!$stmt)
	{
		if ( $err_display )
		{
			die( 'parse() failed. unable to execute '.$sql );
		}
		else
		{
			return false;
		}
	}
	return $stmt;
}

function fetchPgArray($stmt)
{
	$rows = pg_num_rows( $stmt );
	$columns = pg_num_fields( $stmt );

	// データ
	for( $j=0; $j <= $rows; $j++ )
	{
		for( $i = 0; $i < $columns; $i++ )
		{
			$field = pg_fieldname( $stmt, $i );
			$data = @pg_result( $stmt , $j, $i );
			$arr[$j][$field] = $data;
			$arr[$j][$i] = $data;
		}
	}
	pg_free_result( $stmt );
	return $arr;
}

