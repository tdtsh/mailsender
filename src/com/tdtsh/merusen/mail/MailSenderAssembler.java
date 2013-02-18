package com.tdtsh.merusen.mail;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @file MailSenderAssembler.java
 * @brief メール送信エンジン アセンブラー メルマガ用
 * @author tdtsh
 * @date 2007-04-25
 * @version $Id$
 */
public class MailSenderAssembler
{
	public static Log log = LogFactory.getLog(MailSenderAssembler.class);
	private static String toAdmin_ = null;
	private static String subjectAdminPrefix_ = null;
	private static String subjectAdmin_ = null;
	private static String bodyAdmin_ = null;
	private static String fromAdmin_ = null;
	private static Integer sleepsec_ = 1000;

	/** mainメソッド. */
	public static final void main(String[] args)
	{

		log.info(" ");
		log.info("start %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		CommandLineMailSender clms = new CommandLineMailSender();

		// 管理者メールアドレスなどをset.propertiesから取得
		_getResource();

		// @@@@メールマガジンを生成
		log.debug("new MailMagazine");
		MailIf mailSender = new MailMagazine();

		String toList[][] = mailSender.getToList();
		String from = mailSender.getFrom();
		String to = mailSender.getTo();
		String replyTo = mailSender.getReplyTo();
		String subject = mailSender.getSubject();
		String body = mailSender.getBody();
		String replacedBody = null;

		log.debug("new InternetAddress ");
		InternetAddress envelopeTo[] = new InternetAddress[1];

		if (toList == null)
		{
			// if( toList.length == 0 ){
			log.info("no sending address list. system exit...........  ");
			System.exit(1);
		}

		int sendCount = 0;
		try
		{
			log.debug("connect to mailserver ");

			// メールサーバへ接続
			clms.connect();

			for (int i = 0; i < toList.length; i++)
			{
				log.debug("loop " + Integer.toString(i));

				if ((toList[i][0] == null) | ("".equals(toList[i][0])))
				{
					log.info("main - invalid mailAddress '" + toList[i][0]
							+ "'");
					continue;
				}

				envelopeTo[0] = new InternetAddress(toList[i][0]);

				// 本文の特定文字列を置換する START===============================
				String[] list = new String[toList[i].length];
				list = toList[i];
				replacedBody = mailSender.replaceBody(body, list);
				log.debug(replacedBody);
				// 本文の特定文字列を置換する END==================================

				// メール送信
				clms.send(from, to, replyTo, subject, replacedBody, envelopeTo);

				String toForLog = null;
				try
				{
					toForLog = envelopeTo[0].toString().substring(0, 7) + "...";
				}
				catch (IndexOutOfBoundsException ioobe)
				{
					log.error("IndexOutOfBoundsException ", ioobe);
					toForLog = envelopeTo[0].toString();
				}
				finally
				{
				}
				// log.info(
				// "send mail id :"+toList[i][12]+" address: "+envelopeTo[0] );
				log.info("send " + toList[i][12] + " to " + toForLog);

				// 当該会員を送信成功ステータスに変更
				mailSender.markAddressSend(toList[i][12]);
				sendCount++;

				try
				{
					//Thread.sleep(1000);
					Thread.sleep(sleepsec_);
				}
				catch(InterruptedException e)
				{
					log.error("ERROR",e);
				}

			} // END for Loop

			// 当該メールを送信成功ステータスに変更
			if (sendCount > 0)
			{
				mailSender.markMailSendFinish();
			}
			else
			{
				mailSender.markMailSendAbort();
			}

		}
		catch (UnsupportedEncodingException uee)
		{
			log.error("MessagingException", uee);
			mailSender.markMailSendAbort();
			log.info("abort   %%%%%%%%%%%%%%%");
			System.exit(1);
		}
		catch (MessagingException me)
		{
			log.error("MessagingException", me);
			log.error("メールサーバとの接続においてエラーが発生しました。");
			mailSender.markMailSendAbort();
			log.info("abort   %%%%%%%%%%%%%%%");
			System.exit(1);
		}
		finally
		{

			try
			{
				// 管理者メール送信
				if (sendCount > 0)
				{
					StringBuffer buf = new StringBuffer();
					buf.append("メルマガ配信結果 \r\n");
					buf.append("\r\n");
					buf.append("送信成功件数/送信予約件数 : ");
					buf.append(Integer.toString(sendCount));
					buf.append("/");
					buf.append(Integer.toString(toList.length));
					buf.append("件");
					buf.append("\r\n");
					buf.append(mailSender.getAuth());
					buf.append("\r\n");
					bodyAdmin_ = buf.toString();
					String subAdmin = subjectAdminPrefix_ + subjectAdmin_;
					clms.send(fromAdmin_, toAdmin_, null, subAdmin, bodyAdmin_,
							null);
				}
			}
			catch (Exception e)
			{
				log.error("MessagingException", e);
			}

			try
			{
				clms.close();
			}
			catch (MessagingException me)
			{
				log.error("MessagingException", me);
				System.exit(1);
			}
		}
		log.info("done %%");
	}

	/* set.propertiesファイルから設定を取得 */
	private static void _getResource()
	{
		try
		{
			ResourceBundle res = ResourceBundle.getBundle("set");
			toAdmin_	  = res.getString("MailSenderAssembler.adminto");
			String subpre = res.getString("MailSenderAssembler.subject_prefix");
			String subj   = res.getString("MailSenderAssembler.subject");
			fromAdmin_    = res.getString("MailSenderAssembler.from");
			sleepsec_	  = Integer.parseInt(res.getString("MailSenderAssembler.sleep"));
			subjectAdminPrefix_ = new String(subpre.getBytes("iso-8859-1"), "UTF-8");
			subjectAdmin_ = new String(subj.getBytes("iso-8859-1"), "UTF-8");
			log.debug("get prop from set.properties - admin.to:	   " + toAdmin_);
			log.debug("get prop from set.properties - subject_prefix:" + subjectAdminPrefix_);
		}
		catch (UnsupportedEncodingException uee)
		{
			log.warn("エンコーディング不正", uee);
		}
		catch (MissingResourceException mrbe)
		{
			log.error("設定ファイル、設定が見つかりません", mrbe);
			System.exit(1);
		}
	}
	
}
