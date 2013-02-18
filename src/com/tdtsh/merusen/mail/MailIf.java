package com.tdtsh.merusen.mail;


/**
 * 送信対象メールオブジェクトのファサード.
 *
 * @author tdtsh
 * @date 2007-04-25
 * @version $Id$
 */
interface MailIf
{

	public static final int REQUESTED_STATUS_0_WAIT = 0;
	public static final int REQUESTED_STATUS_1_ABLE = 1;
	public static final int SENDING_STATUS_0_WAIT = 0;
	public static final int SENDING_STATUS_1_PROGRESS = 1;
	public static final int SENDING_STATUS_2_FINISHED = 2;
	public static final int SENDING_STATUS_10_ABORT = 10;

	String getTo();

	String[][] getToList();

	String getReplyTo();

	String getFrom();

	String getSubject();

	String getBody();

	String getAuth();

	String replaceBody(String body, String[] list);

	void setTo(String to);

	void setToList(String[][] toList);

	void setReplyTo(String replyTo);

	void setFrom(String from);

	void setSubject(String subject);

	void setBody(String body);

	void setAuth(String Auth);

	void markAddressSend(String id);

	void markMailSendFinish();

	void markMailSendAbort();

}
