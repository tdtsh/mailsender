package com.tdtsh.merusen.database;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Strategyデザインパターンの の抽象クラス.
 *
 * @file ConManager.java
 * @brief データソース用抽象クラス 
 * @author tdtsh
 * @date 2004-12-02
 * @version $Id:$
 */
abstract public class ConManager
{

	/* データベースサーバのIPアドレスもしくはHost名 */
	private String hostName_;
	/* デフォルトのデータベース名 */
	private String dbName_;
	/* データベースサーバの文字エンコーディング */
	private String dbEncoding_;
	/* データベースサーバのユーザ名 */
	private String userName_;
	/* データベースサーバのユーザパスワード */
	private String password_;
	/* ロードするJDBCのクラス */
	private String jdbcClass_;
	/* JDBCのURI */
	private String jdbcUri_;

	/**
	 * データベースサーバのIPアドレスもしくはホスト名を設定する.
	 *
	 * @param hostName	  データベースサーバのホスト名
	 */
	public void setHostName(String hostName)
	{
		this.hostName_ = hostName;
	}

	/**
	 * デフォルトのデータベース名を設定する.
	 *
	 * @param dbName		データベース名
	 */
	public void setDbName(String dbName)
	{
		this.dbName_ = dbName;
	}

	/**
	 * データベースサーバの文字エンコーディングを設定する.
	 *
	 * @param dbEncoding	データベースの文字エンコーディング
	 */
	public void setDbEncoding(String dbEncoding)
	{
		this.dbEncoding_ = dbEncoding;
	}

	/**
	 * データベースのユーザ名を設定する.
	 *
	 * @param userName	  データベースログインの為のユーザ名
	 */
	public void setUserName(String userName)
	{
		this.userName_ = userName;
	}

	/**
	 * データベースのパスワードを設定する.
	 *
	 * @param password	  データベースログインのパスワード
	 */
	public void setPassword(String password)
	{
		this.password_ = password;
	}

	/**
	 * JDBCのURIを設定する.
	 *
	 * @param jdbcUri	   JDBCのURI
	 */
	public void setJdbcUri(String jdbcUri)
	{
		this.jdbcUri_ = jdbcUri;
	}

	/**
	 * ロードするJDBCのクラスを設定する.
	 *
	 * @param jdbcClass	 JDBCドライバのクラス名
	 */
	public void setJdbcClass(String jdbcClass)
	{
		this.jdbcClass_ = jdbcClass;
	}

	/**
	 * データベースサーバのIPアドレスもしくはHost名を取得する.
	 * @return hostName	 データベースサーバのホスト名
	 */
	public String getHostName()
	{
		return this.hostName_;
	}

	/**
	 * デフォルトのデータベースサーバ名を取得する.
	 *
	 * @return dbName	   データベース名
	 */
	public String getDbName()
	{
		return this.dbName_;
	}

	/**
	 * データベースサーバの文字エンコーディングを取得する.
	 *
	 * @return dbEncoding   データベースの文字エンコーディング
	 */
	public String getDbEncoding()
	{
		return this.dbEncoding_;
	}

	/**
	 * データベースのユーザ名を取得する.
	 *
	 * @return userName	 データベースログインの為のユーザ名
	 */
	public String getUserName()
	{
		return this.userName_;
	}

	/**
	 * データベースのパスワードを取得する.
	 *
	 * @return password	データベースログインのパスワード
	 */
	public String getPassword()
	{
		return this.password_;
	}

	/**
	 * JDBCのURIを取得する.
	 *
	 * @return jdbcuri	 JDBCのURI
	 */
	public String getJdbcUri()
	{
		return this.jdbcUri_;
	}

	/**
	 * ロードするJDBCのクラス名URIを取得する.
	 *
	 * @return jdbcuri	 JDBCのURI
	 */
	public String getJdbcClass()
	{
		return this.jdbcClass_;
	}

	/**
	 * JDBC接続用のUriを生成し、インスタンス変数に格納する.
	 *
	 * @return JDBC接続用URI
	 */
	abstract public void makeJdbcUri() throws IllegalStateException;

	/**
	 * Connectionオブジェクトを返す.
	 *
	 * @return  Connectionオブジェクト
	 */
	abstract public Connection getConnect() throws SQLException,
		ClassNotFoundException, InstantiationException,
		IllegalAccessException, IllegalStateException;

	/**
	 * DBのエンコーディングからUTF-8に変換する.
	 *
	 * @param   arg	 DBエンコーディング文字列
	 * @return		  UTF-8文字列
	 */
	abstract public String native2Unicode(String arg)
		throws UnsupportedEncodingException;

	/**
	 * UTF-8からDBのエンコーディングに変換する.
	 *
	 * @param   arg	 UTF-8文字列
	 * @return		  DBエンコーディング文字列
	 */
	abstract public String unicode2Native(String arg)
		throws UnsupportedEncodingException;

}
