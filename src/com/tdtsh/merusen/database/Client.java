package com.tdtsh.merusen.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Client.java サンプルコード( 使用方法 ). 
 *
 * データベース接続クラス群AbstractDbConを使用する為のサンプルコード。
 *	   java -cp .;../lib/ojdbc14.jar com.tdtsh.merusen.database.Client
 * 
 * @brief データベース接続クラス群AbstractDbCon使用サンプルコード
 * @author tdtsh
 * @date 2004-12-10
 * @version $Id:$
 */
public class Client
{

	public static void main(String[] args)
	{
		Connection con = null;
		try
		{
			String sql = "";
			ResultSet rs = null;
			// まずは、ConcreteDbCon を newして、
			ConcreteDbCon dbCon = new ConcreteDbCon();
			// ConcreteDbConのセッターで接続情報をセットする。
			dbCon.setDbType("com.tdtsh.merusen.database.MySqlCon");
			dbCon.setHostName("172.10.20.21");
			dbCon.setDbName("mailmagazine");
			dbCon.setDbEncoding("sjis");
			dbCon.setUserName("root");
			dbCon.setPassword("hoge");
			/*
			 * セッターを使わないで、loadProp()一発で、 set.propertiesファイルの以下の内容を読み込む事も可能
			 * 
			 * ■ファイル set.properties に必要な内容 database.ConcreteDbCon.dbType =
			 * com.tdtsh.merusen.database.OracleCon
			 * database.ConcreteDbCon.hostname = 172.10.10.20
			 * database.ConcreteDbCon.dbname = [sns] database.ConcreteDbCon.user
			 * = crane database.ConcreteDbCon.pass = p*****n
			 * database.ConcreteDbCon.encoding = none
			 */
			dbCon.loadProp();
			// あとは、getConnect()でConnectionを取得する
			con = dbCon.getConnect();
			sql = "select count(*) from SnsMember";
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
			System.out.print(rs.getString(1) + " ");
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
			con.close();
			}
			catch (Exception e)
			{
			e.printStackTrace();
			}
		}
	}

}
