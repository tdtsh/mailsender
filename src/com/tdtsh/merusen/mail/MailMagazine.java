package com.tdtsh.merusen.mail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tdtsh.merusen.database.ConcreteDbCon;
import com.tdtsh.merusen.util.DateUtil;

/**
 * 送信対象メールマガジンの具象クラス.
 *
 * @author tdtsh
 * @date 2007-04-25
 * @version $Id$
 */
public class MailMagazine extends AbstractMail implements MailIf
{

	public static Log log = LogFactory.getLog(MailMagazine.class);
	private String dbType_ = null;
	private String hostName_ = null;
	private String dbName_ = null;
	private String dbEncoding_ = null;
	private String userName_ = null;
	private String password_ = null;
	private Connection con_ = null;
	private String shippingId_ = null;
	private String sql_ = null;
	private static final int TODAY = 0;
	private boolean RETRYABLE = false;
	private HashMap repMap_ = null;

	/** デフォルトコンストラクタ. */
	MailMagazine()
	{
		log.debug("Constructor new MailMagazine Object ");
		this._getResource(); // DB接続先情報をset.propertiesから取得
		try
		{
			con_ = _conDatabase();
			boolean isExistsList = this._makeToList();
			if (isExistsList)
			{
			this._getReplaceInfo(); // 個別配信情報の作成
			this._makeBody(); // メール本文を作成
			}
		}
		catch (ClassNotFoundException cnfe)
		{
			log.error("ClassNotFoundException :", cnfe);
			System.exit(1);
		}
		catch (InstantiationException inse)
		{
			log.error("InstantiationException :", inse);
			System.exit(1);
		}
		catch (IllegalAccessException iae)
		{
			log.error("IllegalAccessException :", iae);
			System.exit(1);
		}
		catch (SQLException sqle)
		{
			log.error("SQLException :" + sqle.getMessage());
			log.error("	SQLState :" + sqle.getSQLState());
			log.error("VendorErrCode:" + sqle.getErrorCode());
			log.error("SQLException :", sqle);
			System.exit(1);
		}
		finally
		{
			//
		}
	}

	public String getSendingId()
	{
		return shippingId_;
	}

	public void setSendigId(String shippingId)
	{
		shippingId_ = shippingId;
	}

	/**
	 * 本文の特定文字列を置換する.
	 */
	public String replaceBody(String body, String[] list)
	{
		// 本文中の $NAME を置換する
		StringBuffer bb = new StringBuffer();
		String repKey = null;
		bb.append(body);
		int rep = 0;
		while (rep >= 0)
		{
			rep = bb.indexOf("$NAME");
			if (rep < 0)
			{
			break;
			}
			bb.replace(rep, rep + 5, list[1]);
			if (log.isDebugEnabled())
			{
			log.debug("replaceBody: $NAME replace to " + list[1]);
			}
		}
		log.debug("replace : list.length " + Integer.toString(list.length - 1));

		/*
		 * 本文中の $REPLACE1～10 を置換する
		 *
		 * REPLACE10 を REPLACE1より先に置換する必要がある為、 必ずforループは大きい方から
		 */
		for (int j = list.length - 1; j >= 2; j--)
		{
			rep = 0;
			while (rep >= 0)
			{

			repKey = "REPLACE" + Integer.toString(j - 1);
			rep = bb.indexOf("$" + repKey);
			if (rep < 0)
			{
				break;
			}
			String repSt = (String) repMap_.get(repKey + list[j]);
			bb.replace(rep, rep + 9, repSt);

			if (log.isDebugEnabled())
			{
				log.debug("replace : repKey " + repKey);
				log.debug("replace : repSt : " + repSt);
				log.debug("replace : " + repKey + " replace to " + list[j]);
				log.debug("replace do replace : " + Integer.toString(rep)
					+ Integer.toString(rep + 9) + repSt);
			}
			}
		}
		body_ = bb.toString();
		// body_ = body_.replace( '\uff0d', '-' ); // － 0xFF0d を置換
		body_ = body_.replace('\uff0d', '\u2212'); // － 0xFF0d を置換
		body_ = body_.replace('\uff5e', '\u301c'); // ～ 0xFF5E を置換
		body_ = body_.replace('\ufa11', '崎'); // 﨑 0xFA11 を置換
		body_ = body_.replace('\u5f45', '薙'); // 彅 0x5F45 を置換
		body_ = body_.replace('\u9ad9', '高'); // 髙 0x9AD9 を置換
		body_ = body_.replace('\u6852', '桑'); // 桒 0x6852 を置換
		log.trace("replaceBody: " + body_);
		return body_;
	}

	/* set.propertiesファイルから設定を取得 */
	private void _getResource()
	{
		try
		{
			ResourceBundle res = ResourceBundle.getBundle("set");
			to_		 = res.getString("MailMagazine.header.to");
			String ret  = res.getString("MailMagazine.retryable");
			dbType_	 = res.getString("MailMagazine.dbType");
			dbEncoding_ = res.getString("MailMagazine.encoding");
			hostName_   = res.getString("MailMagazine.hostname");
			userName_   = res.getString("MailMagazine.user");
			password_   = res.getString("MailMagazine.pass");
			dbName_	 = res.getString("MailMagazine.dbname");
			RETRYABLE = Boolean.parseBoolean(ret);
			log.debug("get prop from set.properties - to_:		" + to_);
			log.debug("get prop from set.properties - dbType_:	" + dbType_);
			log.debug("get prop from set.properties - hostName_:  " + hostName_);
			log.debug("get prop from set.properties - dbName_:	" + dbName_);
			log.debug("get prop from set.properties - userName_:  " + userName_);
			log.debug("get prop from set.properties - password_:  " + password_);
			log.debug("get prop from set.properties - dbEncoding_:"
				+ dbEncoding_);
			log.debug("get prop from set.properties - RETRYABLE:  " + ret);
			log.debug("get prop from set.properties - subject_:   " + subject_);
		}
		catch (MissingResourceException mrbe)
		{
			// 設定ファイルが無ければ、アボート
			log.error("設定ファイル、設定が見つかりません", mrbe);
			System.exit(1);
		}
	}

	/*
	 * データベースに接続する. Connectionオブジェクトを生成
	 */
	private Connection _conDatabase() throws SQLException,
		ClassNotFoundException, InstantiationException,
		IllegalAccessException, IllegalStateException
	{
		log.debug("connect to database - " + dbType_);
		log.info("connect to database - " + hostName_ + ":" + dbName_);
		log.debug("connect to database - " + dbEncoding_);
		log.debug("connect to database - " + userName_ + "/" + password_);
		ConcreteDbCon dbCon = new ConcreteDbCon();
		dbCon.setDbType(dbType_);
		dbCon.setHostName(hostName_);
		dbCon.setDbName(dbName_);
		dbCon.setDbEncoding(dbEncoding_);
		dbCon.setUserName(userName_);
		dbCon.setPassword(password_);
		Connection con = dbCon.getConnect();
		return con;
	}

	/* メールBODY作成 */
	private void _makeBody() throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		StringBuffer sqlBuf = null;
		String shippingId = null;
		int requestStatus = 0;
		int shippingStatus = 0;
		String fromAddress = null;
		String returnAddress = null;
		String subject = null;

		// SQL文を生成
		sqlBuf = new StringBuffer();
		sqlBuf.append("select ");
		sqlBuf.append("id, ");
		sqlBuf.append("requestedStatus, ");
		sqlBuf.append("shippingStatus, ");
		sqlBuf.append("fromAddress, ");
		sqlBuf.append("subject, ");
		sqlBuf.append("body, ");
		sqlBuf.append("returnAddress ");
		sqlBuf.append("from shippinginfo ");
		sqlBuf.append("where id = ? ");
		sql_ = sqlBuf.toString();

		log.debug("_makeBody - sql\r\n\r\n" + sql_ + "\r\n");
		// SQL文を実行
		prepSt = con_.prepareStatement(sql_);
		prepSt.setString(1, shippingId_);
		log.debug("getResult - set 1 " + shippingId_);

		rs = prepSt.executeQuery();
		while (rs.next())
		{
			shippingId = rs.getString(1);
			requestStatus = rs.getInt(2);
			shippingStatus = rs.getInt(3);
			fromAddress = rs.getString(4);
			subject = rs.getString(5);
			body_ = rs.getString(6);
			returnAddress = rs.getString(7);
			log.info("from: " + fromAddress);
			log.info("subject :" + subject);
			log.debug("_makeBody - shippingId:	" + shippingId);
			log.debug("_makeBody - requestStatus: " + requestStatus);
			log.debug("_makeBody - shippingStatus:" + shippingStatus);
			log.debug("_makeBody - returnAddress: " + returnAddress);
			log.debug("_makeBody - body:		  \r\n" + body_);
		}
		this.setFrom(fromAddress);
		this.setReplyTo(returnAddress);
		this.setSubject(subject);
		if (rs != null)
		{
			rs.close();
			prepSt.close();
		}
	}

	/* 個別配信情報(ReplaceInfo)の取得. */
	private String _getReplaceInfo() throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		StringBuffer sqlBuf = null;
		String shippingId = null;
		repMap_ = new HashMap();
		sqlBuf = new StringBuffer();
		sqlBuf.append("select ");
		sqlBuf.append("key ");
		sqlBuf.append(",attribute ");
		sqlBuf.append(",subBody ");
		sqlBuf.append("from ReplaceInfo ");
		sqlBuf.append("where shippingId = ? ");
		sqlBuf.append("order by key asc ");
		sql_ = sqlBuf.toString();
		prepSt = con_.prepareStatement(sql_);
		log.debug("_getReplaceInfo - sql\r\n\r\n" + sql_ + "\r\n");
		prepSt.setString(1, shippingId_);
		log.debug("_getReplaceInfo - set 1 " + shippingId_);
		rs = prepSt.executeQuery();
		while (rs.next())
		{
			// key( 通常は REPLACE1 ～ REPLACE9 )
			// と、attribute( 会員固有の属性、例えば性別、星座など )
			// を連結した文字列を、HashMapのキーとする
			String hKey = rs.getString(1) + rs.getString(2);
			log.debug("_getReplaceInfo : hKey   " + hKey);
			// subBody( 会員固有の属性別の文面 )
			String hValue = rs.getString(3);
			log.debug("_getReplaceInfo : hValue " + hValue);
			repMap_.put(hKey, hValue);
		}
		rs.close();
		prepSt.close();
		return shippingId;
	}

	/* 配信対象をカウントする */
	private int _countAddressList(String shippingId) throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		StringBuffer sqlBuf = null;
		// 配信対象をカウントする
		sqlBuf = new StringBuffer();
		sqlBuf.append("select ");
		sqlBuf.append("count(*) ");
		sqlBuf.append("from AddressList ");
		sqlBuf.append("where shippingId = ? ");
		sqlBuf.append("and shippingTime is null ");
		sql_ = sqlBuf.toString();
		log.debug("_countAddressList - sql\r\n\r\n" + sql_ + "\r\n");
		prepSt = con_.prepareStatement(sql_);
		prepSt.setString(1, shippingId);
		log.debug("_countAddressList - set 1 " + shippingId);
		rs = prepSt.executeQuery();
		rs.next();
		int row = rs.getInt(1);
		rs.close();
		prepSt.close();
		log.info("number of address is " + Integer.toString(row) + ".");
		return row;
	}

	/* 配信対象リストを作成する */
	private boolean _makeToList() throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		StringBuffer sqlBuf = null;
		// 配信可能な対象( SendingId )を取得
		shippingId_ = this._getSendingId();
		if (shippingId_ == null)
		{
			return false;
		}
		log.debug("_makeToList:shippingId:" + shippingId_);
		// 対象の配信対象件数を求め、配列をnew
		int row = this._countAddressList(shippingId_);
		toList_ = new String[row][13];
		// 対象を配信中にする
		this.markMailSendStart();
		// 配信対象を取得する
		sqlBuf = new StringBuffer();
		sqlBuf.append("select ");
		sqlBuf.append("id, ");
		sqlBuf.append("account, ");
		sqlBuf.append("domain, ");
		sqlBuf.append("name, ");
		sqlBuf.append("attribute1, ");
		sqlBuf.append("attribute2, ");
		sqlBuf.append("attribute3, ");
		sqlBuf.append("attribute4, ");
		sqlBuf.append("attribute5, ");
		sqlBuf.append("attribute6, ");
		sqlBuf.append("attribute7, ");
		sqlBuf.append("attribute8, ");
		sqlBuf.append("attribute9, ");
		sqlBuf.append("attribute10 ");
		sqlBuf.append("from AddressList ");
		sqlBuf.append("where shippingId = ? ");
		sqlBuf.append("and shippingTime is null ");
		// sqlBuf.append("order by domain ");
		sqlBuf.append( "order by id " ); // Yahoo遅延対策
		sql_ = sqlBuf.toString();
		prepSt = con_.prepareStatement(sql_);
		prepSt.setString(1, shippingId_);
		log.debug("_makeToList - set 1 " + shippingId_);
		log.debug("_makeToList - sql\r\n\r\n" + sql_ + "\r\n");
		rs = prepSt.executeQuery();
		// 配信対象を多次元配列に格納する
		// データ設計、正規化して欲しかったナァ・・・
		int i = 0;
		String account = null;
		String domain = null;
		String mailaddress = null;
		while (rs.next())
		{
			account = rs.getString("account");
			domain = rs.getString("domain");
			// @が2つ以上あるものを排除
			account = account.replaceAll("@", "");
			domain = domain.replaceAll("@", "");
			// nullチェック
			if ((domain == null) | ("".equals(domain)) | (account == null)
				| ("".equals(account)))
			{
			log.info("_makeToList - no account or domain!! cant send mail '"
				+ account + "@" + domain + "'");
			continue;
			}
			// メールアドレス中の禁止文字を置換して配列の0番目に格納
			mailaddress = account + "@" + domain;
			Pattern p = Pattern.compile("[^.a-zA-Z0-9@_-]");
			Matcher m = p.matcher(mailaddress);
			if (m.find())
			{
			log.info("_makeToList - invalid mailAddress '" + account + "@"
				+ domain + "'");
			continue;
			}
			toList_[i][0] = mailaddress.replaceAll("[^.a-zA-Z0-9@_-]", "");
			// 配列の1番目に名前を格納
			toList_[i][1] = rs.getString("name");
			log.trace("mailAddress: " + toList_[i][0]);
			log.trace("name:		" + toList_[i][1]);
			// 配列の2～11番目に置換文字を格納
			for (int j = 2; j <= 11; j++)
			{
			toList_[i][j] = rs.getString("attribute"
				+ Integer.toString(j - 1));
			log.trace("attribute:   " + toList_[i][j]);
			}
			toList_[i][12] = rs.getString("id");
			log.trace("id:		  " + toList_[i][12]);
			i++;
		}
		log.debug("_makeToList - num of list :" + Integer.toString(i)
			+ " rows. ");
		rs.close();
		prepSt.close();
		if (i > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * 送信情報(shippinginfo)の取得.
	 */
	private String _getSendingId() throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		StringBuffer sqlBuf = null;
		String shippingId = null;
		// 配信対象を検索する
		sqlBuf = new StringBuffer();
		sqlBuf.append("select ");
		sqlBuf.append("distinct b.id ");
		sqlBuf.append("from AddressList as a ");
		sqlBuf.append("right outer join shippinginfo as b ");
		sqlBuf.append("on a.shippingId = b.id ");
		sqlBuf.append("where b.requestedStatus = ? ");
		sqlBuf.append("and b.shippingTime::text <= ? ");
		sqlBuf.append("and a.shippingTime::text is null ");
		if (RETRYABLE)
		{
			// リトライ可能フラグがTRUEなら、shippingStatusが 1 or10 ( 配信失敗 )も対象
			sqlBuf.append("and ( b.shippingStatus = ? ");
			sqlBuf.append("or b.shippingStatus = ? ) ");
		}
		else
		{
			// リトライ可能フラグがFALSEなら、shippingStatusが 0 ( 未配信 )のみ対象
			sqlBuf.append("and b.shippingStatus = ? ");
		}
		sqlBuf.append("order by b.id asc ");
		sql_ = sqlBuf.toString();
		prepSt = con_.prepareStatement(sql_);
		log.debug("_getSendingId - sql\r\n\r\n" + sql_ + "\r\n");
		// shippinginfoテーブルにおいて、以下の条件にマッチするSendingIdを取得
		// requestedStatusが 1 (配信可)
		prepSt.setInt(1, REQUESTED_STATUS_1_ABLE);
		log.debug("_getSendingId - set 1 " + REQUESTED_STATUS_1_ABLE);
		// かつ、shippingTimeが現在時刻よりも過去
		prepSt.setString(2, DateUtil.makeSqlDate(TODAY));
		log.debug("_getSendingId - set 2 " + DateUtil.makeSqlDate(TODAY));
		// かつ、shippingStatusが 0 ( 配信待ち )
		prepSt.setInt(3, SENDING_STATUS_0_WAIT);
		log.debug("_getSendingId - set 3 " + SENDING_STATUS_0_WAIT);
		// リトライ可能フラグがTRUEなら、shippingStatusが 10 ( 配信失敗 )も対象
		if (RETRYABLE)
		{
			prepSt.setInt(4, SENDING_STATUS_10_ABORT);
			log.debug("_getSendingId - set 4 " + SENDING_STATUS_10_ABORT);
		}
		rs = prepSt.executeQuery();
		while (rs.next())
		{
			shippingId = rs.getString(1);
			if (shippingId != null)
			{
			break;
			}
		}
		rs.close();
		prepSt.close();
		log.info("shippingId is " + shippingId + ".");
		return shippingId;
	}

	public void markAddressSend(String id)
	{
		int row = 0;
		PreparedStatement prepSt = null;
		StringBuffer sqlBuf = null;

		try
		{
			// 配信対象を検索する
			sqlBuf = new StringBuffer();
			sqlBuf.append("update ");
			sqlBuf.append("AddressList ");
			sqlBuf.append("set shippingTime = now() ");
			sqlBuf.append("where shippingId = ? ");
			sqlBuf.append("and id = ? ");
			sql_ = sqlBuf.toString();
			log.debug("markAddressSend - sql\r\n\r\n" + sql_ + "\r\n");
			prepSt = con_.prepareStatement(sql_);
			prepSt.setString(1, shippingId_);
			log.debug("markAddressSend - set 1 " + shippingId_);
			prepSt.setString(2, id);
			log.debug("markAddressSend - set 2 " + id);
			row = prepSt.executeUpdate();
			log.debug("markAddressSend - id :" + id + ": update :"
				+ Integer.toString(row) + "(1=success) ");
		}
		catch (SQLException sqle)
		{

			log.error("SQLException :" + sqle.getMessage());
			log.error("	SQLState :" + sqle.getSQLState());
			log.error("VendorErrCode:" + sqle.getErrorCode());
			log.error("SQLException :", sqle);
			System.exit(1);

		}
		finally
		{
			try
			{
			if (prepSt != null)
			{
				prepSt.close();
			}
			}
			catch (SQLException sqle)
			{
			log.error("ERROR: SQLException :", sqle);
			}
		}
	}

	/** 配信済の配信対象を配信失敗状態に更新する. */
	public void markMailSendStart()
	{

		if (shippingId_ != null)
		{
			// 対象を配信中にする
			this.updateSendingStatus(shippingId_, SENDING_STATUS_1_PROGRESS);
		}

	}

	/** 配信済の配信対象を配信成功状態に更新する. */
	public void markMailSendFinish()
	{
		if (shippingId_ != null)
		{
			// 対象を配信完了にする
			this.updateSendingStatus(shippingId_, SENDING_STATUS_2_FINISHED);
		}

		// auth_ に結果をセットする
		this.getResult();
		}

		/* 配信済の配信対象を配信失敗状態に更新する */
		public void markMailSendAbort()
		{

		if (shippingId_ != null)
		{
			// 対象を配信失敗にする
			this.updateSendingStatus(shippingId_, SENDING_STATUS_10_ABORT);
		}
	}

	/** 対象(shippinginfo)のshippingStatusを変更する */
	public int updateSendingStatus(String shippingId, int status)
	{
		int row = 0;
		PreparedStatement prepSt = null;
		StringBuffer sqlBuf = null;

		try
		{
			// 配信対象を検索する
			sqlBuf = new StringBuffer();
			sqlBuf.append("update ");
			sqlBuf.append("shippinginfo ");
			sqlBuf.append("set shippingStatus = ? ");

			if ((status == SENDING_STATUS_2_FINISHED)
				| (status == SENDING_STATUS_10_ABORT))
			{

			// 配信完了、配信失敗の場合は配信終了日時を更新
			sqlBuf.append(",completionTime = now() ");

			}
			else if (status == SENDING_STATUS_1_PROGRESS)
			{

			// 配信開始の場合は配信終了日時を更新
			sqlBuf.append(",beginTime = now() ");
			}
			else
			{

			}
			sqlBuf.append("where id = ? ");
			sql_ = sqlBuf.toString();
			log.debug("updateSendingStatus - sql\r\n\r\n" + sql_ + "\r\n");
			prepSt = con_.prepareStatement(sql_);

			prepSt.setInt(1, status);
			log.debug("updateSendingStatus - set 1 " + status);

			prepSt.setString(2, shippingId);
			log.debug("updatesendingstatus - set 2 " + shippingId);

			row = prepSt.executeUpdate();
			log.info("update status(" + status + ") result("
				+ Integer.toString(row) + ")<-1=success");

		}
		catch (SQLException sqle)
		{
			log.error("SQLException :" + sqle.getMessage());
			log.error("	SQLState :" + sqle.getSQLState());
			log.error("VendorErrCode:" + sqle.getErrorCode());
			log.error("SQLException :", sqle);
			System.exit(1);

		}
		finally
		{
			try
			{
			if (prepSt != null)
			{
				prepSt.close();
			}
			}
			catch (SQLException sqle)
			{
			log.error("SQLException :", sqle);
			}
		}
		return row;
		}

		/* (shippinginfo)のbeginTimeなどを返す */
		private void getResult()
		{
		ResultSet rs = null;
		PreparedStatement prepSt = null;
		StringBuffer sqlBuf = null;
		String shippingId = null;
		String beginTime = null;
		String completionTime = null;
		// int shippingStatus = 0;
		String shippingStatus = null;

		try
		{
			// SQL文を生成
			sqlBuf = new StringBuffer();
			sqlBuf.append("select ");
			sqlBuf.append("id ");
			sqlBuf.append(",begintime ");
			sqlBuf.append(",completiontime ");
			sqlBuf.append(",shippingStatus ");
			sqlBuf.append("from shippinginfo ");
			sqlBuf.append("where id = ? ");
			sql_ = sqlBuf.toString();
			log.debug("_getBeginTime - sql\r\n\r\n" + sql_ + "\r\n");

			// SQL文を実行
			prepSt = con_.prepareStatement(sql_);
			prepSt.setString(1, shippingId_);
			log.debug("getResult - set 1 " + shippingId_);

			rs = prepSt.executeQuery();
			while (rs.next())
			{
			shippingId = rs.getString(1);
			beginTime = rs.getString(2);
			completionTime = rs.getString(3);
			shippingStatus = Integer.toString(rs.getInt(4));
			log.debug("_makeBody - shippingId:	" + shippingId);
			log.debug("_makeBody - beginTime:	 " + beginTime);
			log.debug("_makeBody - completionTime:" + completionTime);
			log.debug("_makeBody - shippingStatus:" + shippingStatus);
			}

			if (rs != null)
			{
			rs.close();
			prepSt.close();
			}

			StringBuffer buf = new StringBuffer();
			buf.append("ShippingId				: ");
			buf.append(shippingId);
			buf.append("\r\n");
			buf.append("開始日時				  : ");
			buf.append(beginTime);
			buf.append("\r\n");
			buf.append("終了日時				  : ");
			buf.append(completionTime);
			buf.append("\r\n");
			buf.append("ステータス				: ");
			buf.append(shippingStatus);
			buf.append("\r\n");
			auth_ = buf.toString();

		}
		catch (SQLException sqle)
		{

			log.error("SQLException :" + sqle.getMessage());
			log.error("	SQLState :" + sqle.getSQLState());
			log.error("VendorErrCode:" + sqle.getErrorCode());
			log.error("SQLException :", sqle);
			System.exit(1);

		}
		finally
		{
			try
			{
			if (prepSt != null)
			{
				prepSt.close();
			}
			}
			catch (SQLException sqle)
			{
			log.error("SQLException :", sqle);
			}
		}
	}

}
