package com.tdtsh.merusen.database;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Strategyデザインパターンの PostgreSQL用データソース用実装クラス.
 *
 * @file PostgreSqlCon.java
 * @brief PostgreSQL用データソース用実装クラス
 * @author tdtsh
 * @date 2004-12-06
 * @version $Id:$
 */

public class PostgreSqlCon extends ConManager
{

	public PostgreSqlCon()
	{
	}

	/**
	 * PostgreSql用のJDBC接続用のUriを生成し、インスタンス変数へ格納する.
	 * @return JDBC接続用URI
	 */
	public void makeJdbcUri() throws IllegalStateException
	{
		String hostname = this.getHostName();
		String dbname = this.getDbName();
		String encoding = this.getDbEncoding();

		if ((hostname == null) || ("".equals(hostname)))
		{
			String err = "Please do setHostName() !!";
			throw new IllegalStateException(err);
		}
		else if ((dbname == null) || ("".equals(dbname)))
		{
			String err = "Please do setDbName() !!";
			throw new IllegalStateException(err);
		}
		else if ((encoding == null) || ("".equals(encoding)))
		{
			String err = "Please do setDbEncoding() !!";
			throw new IllegalStateException(err);
		}

		StringBuffer uriBuf = new StringBuffer();
		uriBuf.append("jdbc:postgresql://").append(hostname);
		uriBuf.append(":5432/").append(dbname);
		// uriBuf.append( "?useUnicode=true&characterEncoding=");
		// uriBuf.append( encoding );
		this.setJdbcUri(uriBuf.toString());
		this.setJdbcClass("org.postgresql.Driver");
	}

	/**
	 * Connectionオブジェクトを返す.
	 * @return  con	 Connectionオブジェクト
	 */
	public Connection getConnect() throws SQLException, ClassNotFoundException,
		InstantiationException, IllegalAccessException,
		IllegalStateException
	{
		Connection con = null;
		makeJdbcUri();
		String uri = this.getJdbcUri();
		String user = this.getUserName();
		String pass = this.getPassword();
		String clazz = this.getJdbcClass();

		if ((user == null) || ("".equals(user)))
		{
			String err = "Please do setUserName() !!";
			throw new IllegalStateException(err);
		}
		else if ((pass == null) || ("".equals(pass)))
		{
			String err = "Please do setPassword() !!";
			throw new IllegalStateException(err);
		}

		System.out.println("url:" + uri);
		Class.forName(clazz).newInstance();
		con = DriverManager.getConnection(uri, user, pass);
		return con;
	}

	public String native2Unicode(String nativeString)
		throws UnsupportedEncodingException
	{
		// SJIS -> Unicode 変換
		// byte[] aByteArray = nativeString.getBytes("8859_1");
		// String result = new String(aByteArray, "SJIS");
		String result = nativeString;
		return result;
	}

	public String unicode2Native(String unicodeString)
		throws UnsupportedEncodingException
	{
		// Unicode -> SJIS 変換
		// byte[] aByteArray = unicodeString.getBytes("SJIS");
		// String result = new String(aByteArray, "8859_1");
		String result = unicodeString;
		return result;
	}

}
