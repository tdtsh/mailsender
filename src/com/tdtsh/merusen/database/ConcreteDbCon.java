package com.tdtsh.merusen.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * TemplateMethodデザインパターンの実装クラス.
 *
 * 抽象クラスAbstractDbConの実装クラス。
 * (TemplateMethodデザインパターンの実装クラス
 * 
 * @file ConcreteDbCon.java
 * @brief データベース接続 実装クラス
 * @author tdtsh
 * @date 2004-12-10
 * @version $Id$
 */
public class ConcreteDbCon extends AbstractDbCon
{

	/*
	 * データ・ソース用実装クラス.
	 * 
	 * com.tdtsh/merusen/database/PostgreSqlCon
	 * com.tdtsh/merusen/database/OracleCon
	 * com.tdtsh/merusen/database/SqlServerCon
	 * com.tdtsh/merusen/database/MySqlCon
	 */
	private String dbType__ = null;

	/*
	 * データベース・サーバのIPアドレス/ホスト名.
	 */
	private String hostName__ = null;

	/*
	 * データベース名.
	 */
	private String dbName__ = null;

	/*
	 * MySQLに接続時の文字エンコーディング. 他のデータベースでは何もセットしない
	 */
	private String dbEncoding__ = null;

	/*
	 * RDBMSの接続ユーザ名.
	 */
	private String userName__ = null;

	/*
	 * RDBMSの接続ユーザ名のパスワード.
	 */
	private String password__ = null;

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
	public HashMap getResource() throws SQLException, ClassNotFoundException
	{
		HashMap map = new HashMap();
		Class clazz = Class.forName(dbType__);
		map.put("hostName", (Object) hostName__);
		map.put("dbName", (Object) dbName__);
		map.put("dbEncoding", (Object) dbEncoding__);
		map.put("userName", (Object) userName__);
		map.put("password", (Object) password__);
		map.put("clazz", (Object) clazz);
		return map;
	}

	/**
	 * プロパティファイルから以下を取得する.
	 * 
	 *		  database.ConcreteDbCon.dbType = com.tdtsh.merusen.database.MySqlCon
	 *		  database.ConcreteDbCon.hostname = 127.0.0.1
	 *		  database.ConcreteDbCon.dbname = [mailmagazine]
	 *		  database.ConcreteDbCon.user = slode
	 *		  database.ConcreteDbCon.pass = mmuser
	 *		  database.ConcreteDbCon.encoding = utf-8
	 * 
	 */
	public void loadProp()
	{
		ResourceBundle resource = ResourceBundle.getBundle("set");
		dbType__ = resource.getString("database.ConcreteDbCon.dbType");
		hostName__ = resource.getString("database.ConcreteDbCon.hostname");
		dbName__ = resource.getString("database.ConcreteDbCon.dbname");
		dbEncoding__ = resource.getString("database.ConcreteDbCon.encoding");
		userName__ = resource.getString("database.ConcreteDbCon.user");
		password__ = resource.getString("database.ConcreteDbCon.pass");
	}

	public void setDbType(String dbType)
	{
		dbType__ = dbType;
	}

	public void setHostName(String hostName)
	{
		hostName__ = hostName;
	}

	public void setDbName(String dbName)
	{
		dbName__ = dbName;
	}

	public void setDbEncoding(String dbEncoding)
	{
		dbEncoding__ = dbEncoding;
	}

	public void setUserName(String userName)
	{
		userName__ = userName;
	}

	public void setPassword(String password)
	{
		password__ = password;
	}

}
