package com.tdtsh.merusen.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * TemplateMethodデザインパターンの テンプレートクラス.
 * 
 * StrategyデザインパターンのConManagerのインスタンスを生成する事で、
 * データソース実装クラスの切替えが容易に行える。
 * 
 * @file AbstractDbCon.java
 * @brief データベース接続 抽象クラス (TemplateMethodデザインパターンの テンプレートクラス)
 * @author tdtsh
 * @date 2004-12-10
 * @version $Id:$
 */
abstract public class AbstractDbCon
{

	/**
	 * ユーザ名、パスワード、DBサーバ、ホスト名などを取得する.
	 *
	 * @return  HashMap型　 以下のキーがセットされている
	 *					  dbType
	 *					  hostName
	 *					  dbName
	 *					  dbEncoding
	 *					  userName
	 *					  password
	 *					  clazz
	 */
	abstract public HashMap getResource() throws SQLException,
		ClassNotFoundException;

	/**
	 * Connectionオブジェクトを返すテンプレートメソッド.
	 *
	 * @return  Connectionオブジェクト
	 */
	public Connection getConnect() throws SQLException, ClassNotFoundException,
		InstantiationException, IllegalAccessException,
		IllegalStateException
	{
		HashMap resource = getResource();
		// String dbType = (String)resource.get( "dbType" );
		Class clazz = (Class) resource.get("clazz");
		ConManager Cm = (ConManager) clazz.newInstance();
		Cm.setHostName((String) resource.get("hostName"));
		Cm.setDbName((String) resource.get("dbName"));
		Cm.setDbEncoding((String) resource.get("dbEncoding"));
		Cm.setUserName((String) resource.get("userName"));
		Cm.setPassword((String) resource.get("password"));
		Connection con = Cm.getConnect();
		return con;
	}

}
