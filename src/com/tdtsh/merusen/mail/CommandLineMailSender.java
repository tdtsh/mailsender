package com.tdtsh.merusen.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.*;
import java.io.UnsupportedEncodingException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.sql.SQLException;
import java.lang.Boolean;

/**
 * メール送信クラス.
 *
 * @author tdtsh
 * @date 2007-04-25
 * @version $Id$
 */
public class CommandLineMailSender
{

	public static Log log = LogFactory.getLog(CommandLineMailSender.class);

	private String smtp_ = null;
	private String port_ = null;
	private String envelopefrom_ = null;
	private Properties mailProp_; // メールプロパティ
	private Session session_; // TCP/IPセッション
	private Transport transport_; // Transportオブジェクト
	private boolean smtpAuth_ = false; // SMTP-Authを使用するか
	private String smtpAuthId_ = null; // SMTP-AuthのログインID
	private String smtpAuthPass_ = null; // SMTP-Authのパスワード

	/* コンストラクタ. */
	CommandLineMailSender()
	{
		this.init();
		this.setTransport();
	}

	public String getSmtp()
	{
		return smtp_;
	}

	public void setSmtp(String smtp)
	{
		smtp_ = smtp;
	}

	public String getPort()
	{
		return port_;
	}

	public void setPort(String port)
	{
		port_ = port;
	}

	public String getEnvelopefrom()
	{
		return envelopefrom_;
	}

	public void setEnvelopefrom(String envelopefrom)
	{
		envelopefrom_ = envelopefrom;
	}

	public void setSmtpAuth(boolean smtpAuth)
	{
		smtpAuth_ = smtpAuth;
	}

	public boolean isSmtpAuth()
	{
		return smtpAuth_;
	}

	public void setSmtpAuthId(String id)
	{
		smtpAuthId_ = id;
	}

	public String getSmtpAuthId()
	{
		return smtpAuthId_;
	}

	public void setSmtpAuthPass(String pass)
	{
		smtpAuthPass_ = pass;
	}

	public String getSmtpAuthPass()
	{
		return smtpAuthPass_;
	}

	private void init()
	{
		try
		{
			// set.propertiesファイルの中身を取得
			ResourceBundle res = ResourceBundle.getBundle("set");
			this.setSmtp(res.getString("CommandLineMailSender.smtp"));
			this.setPort(res.getString("CommandLineMailSender.port"));
			this.setSmtpAuthId(res.getString("CommandLineMailSender.smtpid"));
			this.setEnvelopefrom(res.getString("CommandLineMailSender.envelope.from"));
			this.setSmtpAuth(Boolean.parseBoolean(res.getString("CommandLineMailSender.smtpauth")));
			this.setSmtpAuthPass(res.getString("CommandLineMailSender.smtppass"));
			log.debug("init() prop - smtp: " + this.getSmtp());
			log.debug("init() prop - port: " + this.getPort());
			log.debug("init() prop - envelope.from: " + this.getEnvelopefrom());
			if (this.isSmtpAuth())
			{
				log.debug("init() prop - smtpAuth: 1");
				log.debug("init() prop - smtpAuthId: " + this.getSmtpAuthId());
				log.debug("init() prop - smtpAuthPass: " + this.getSmtpAuthPass());
			}
		}
		catch (MissingResourceException mrbe)
		{
			log.error("設定ファイル、設定が見つかりません", mrbe);
			System.exit(1);
		}
	}

	public void setTransport()
	{
		// メールセッション確立
		mailProp_ = new Properties();
		mailProp_.put("mail.smtp.host", this.getSmtp());
		mailProp_.put("mail.smtp.port", this.getPort());
		mailProp_.put("mail.smtp.from", this.getEnvelopefrom());
		mailProp_.put("mail.host", this.getSmtp());

		if (this.isSmtpAuth())
		{
			mailProp_.put("mail.smtp.auth", "true"); // SMTP authを利用する
		}
		session_ = session_.getDefaultInstance(mailProp_, null);

		log.info("open smtp session to " + this.getSmtp() + ":"
			+ this.getPort() + ".....");
		// Transportオブジェクト生成
		try
		{
			transport_ = session_.getTransport("smtp");
		}
		catch (NoSuchProviderException nspe)
		{
			nspe.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * メールサーバへ接続する.
	 */
	public void connect() throws MessagingException
	{
		if (this.isSmtpAuth())
		{
			transport_.connect(this.getSmtp(), this.getSmtpAuthId(),
				this.getSmtpAuthPass());
		}
		else
		{
			transport_.connect();
		}
	}

	/**
	 * メールサーバから切断する.
	 */
	public void close() throws MessagingException
	{
		transport_.close();
	}

	/**
	 * メールを送信する.
	 */
	public void send(String from, String to, String replyTo, String subject,
		String body, Address[] envelopeTo)
		throws UnsupportedEncodingException, MessagingException
	{
		MimeMessage mimeMsg = new MimeMessage(session_);

		// Header TO
		InternetAddress iaTo[] = new InternetAddress[1];
		iaTo[0] = new InternetAddress(to, MimeUtility.encodeWord(to, "iso-2022-jp", "B"));
		mimeMsg.setRecipients(Message.RecipientType.TO, iaTo);

		// Header FROM
		mimeMsg.addFrom(InternetAddress.parse(from));

		// Header REPLY-TO
		if (replyTo == null)
		{
			mimeMsg.setReplyTo(InternetAddress.parse(from, false));
		}
		else
		{
			mimeMsg.setReplyTo(InternetAddress.parse(replyTo, false));
		}

		// Header Date
		mimeMsg.setSentDate(new Date());

		// Header SUBJECT
		mimeMsg.setSubject(MimeUtility.encodeText(subject, "iso-2022-jp", "B"));

		// BODY
		mimeMsg.setText(body, "ISO-2022-JP");

		// Header Content-Transfer-Encoding
		mimeMsg.setHeader("Content-Transfer-Encoding", "7bit");

		// 送信する
		if (envelopeTo == null)
		{
			transport_.sendMessage(mimeMsg, iaTo);
		}
		else
		{
			transport_.sendMessage(mimeMsg, envelopeTo);
		}
	}

}
