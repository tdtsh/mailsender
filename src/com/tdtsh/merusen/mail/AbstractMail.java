package com.tdtsh.merusen.mail;

/**
 * 送信対象メールオブジェクトの抽象クラス.
 *
 * @author tdtsh
 * @date 2007-04-25
 * @version $Id$
 */
public class AbstractMail
{

	protected String toList_[][] = null; // 送信先リスト
	protected String to_ = null; // メールあて先
	protected String replyTo_ = null; // メール返信先
	protected String from_ = null; // メール送信元
	protected String subject_ = null; // メール件名
	protected String body_ = null; // メール本文
	protected String auth_ = null; // メール著者

	/* 送信先リストのゲッター・セッター */
	public String[][] getToList()
	{
		return toList_;
	}

	public void setToList(String[][] toList)
	{
		toList_ = toList;
	}

	/* あて先のゲッター・セッター */
	public String getTo()
	{
		return to_;
	}

	public void setTo(String to)
	{
		to_ = to;
	}

	/* 返信先のゲッター・セッター */
	public String getReplyTo()
	{
		return replyTo_;
	}

	public void setReplyTo(String replyTo)
	{
		replyTo_ = replyTo;
	}

	/* 送信元のゲッター・セッター */
	public String getFrom()
	{
		return from_;
	}

	public void setFrom(String from)
	{
		from_ = from;
	}

	/* 件名のゲッター・セッター */
	public String getSubject()
	{
		return subject_;
	}

	public void setSubject(String subject)
	{
		subject_ = subject;
	}

	/* 本文のゲッター・セッター */
	public String getBody()
	{
		return body_;
	}

	public void setBody(String body)
	{
		body_ = body;
	}

	/* 著者のゲッター・セッター */
	public String getAuth()
	{
		return auth_;
	}

	public void setAuth(String auth)
	{
		auth_ = auth;
	}

}
