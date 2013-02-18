package com.tdtsh.merusen.util;

import java.util.*;
import java.text.*;

/**
 * @file DateUtil.java
 * @brief 日付を扱うユーティリティクラス
 * @author tdtsh
 * @date 2006-08-18
 * @version $Id$
 */
public class DateUtil
{
	DateUtil()
	{
	}

	/**
	 * 今日の日付のdef日前をyyyy-MM-dd HH:mm:ssのStringで返す.
	 *
	 * @param def
	 *            何日前
	 */
	public static String makeSqlDate(int def)
	{
		java.util.Date date = new java.util.Date();
		String ret = makeSqlDate(def, date);
		return ret;
	}

	/**
	 * 今日の日付のdef日前をyyyy-MM-dd HH:mm:ssのStringで返す.
	 *
	 * @param def            何日前
	 * @param Date           Dateオブジェクト
	 */
	public static String makeSqlDate(int def, java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
		cal.add(Calendar.DATE, def);
		date = cal.getTime();
		String ret = makeDateTime(date, "yyyy-MM-dd HH:mm:ss");
		return ret;
	}

	/**
	 * 今日の日付のdef日前の00時00分のyyyy-MM-dd HH:mm:ssのStringで返す.
	 *
	 * @param def            何日前
	 */
	public static String makeSqlDate00(int def)
	{
		java.util.Date date = new java.util.Date();
		String ret = makeSqlDate00(def, date);
		return ret;
	}

	/**
	 * 今日の日付のdef日前の00時00分のyyyy-MM-dd HH:mm:ssのStringで返す.
	 *
	 * @param def             何日前
	 * @param Date             Dateオブジェクト
	 */
	public static String makeSqlDate00(int def, java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
		cal.add(Calendar.DATE, def);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
			cal.get(Calendar.DATE), 0, 0, 0);
		date = cal.getTime();
		String ret = makeDateTime(date, "yyyy-MM-dd HH:mm:ss");
		return ret;
	}

	/**
	 * 今日の日付のdef日前の00時00分のyyyy-MM-ddのStringで返す.
	 *
	 * @param def
	 *            何日前
	 */
	public static String makeDate(int def)
	{
		java.util.Date date = new java.util.Date();
		String ret = makeDate(def, date);
		return ret;
	}

	/**
	 * 今日の日付のdef日前の00時00分のyyyy-MM-ddのStringで返す.
	 *
	 * @param def
	 *            何日前
	 * @param date
	 *            java.util.Dateオブジェクト
	 */
	public static String makeDate(int def, java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
		cal.add(Calendar.DATE, def);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
			cal.get(Calendar.DATE), 0, 0, 0);
		date = cal.getTime();
		String ret = makeDateTime(date, "yyyy-MM-dd");
		return ret;
	}

	/**
	 * 今日の日付のdefヶ月前の00時00分のyyyy_MMのStringで返す
	 *
	 * @param def            何ヶ月前
	 * @param date            java.util.Dateオブジェクト
	 */
	public static String makeTableDateM(int def)
	{
		java.util.Date date = new java.util.Date();
		String ret = makeTableDateM(def, date);
		return ret;
	}

	/**
	 * 今日の日付のdefヶ月前の00時00分のyyyy_MMのStringで返す
	 *
	 * @param def            何ヶ月前
	 * @param date            java.util.Dateオブジェクト
	 */
	public static String makeTableDateM(int def, java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
		cal.add(Calendar.DATE, -1);
		cal.add(Calendar.MONTH, def);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
			cal.get(Calendar.DATE), 0, 0, 0);
		date = cal.getTime();
		String ret = makeDateTime(date, "yyyyMM");
		return ret;
	}

	/**
	 * 今日の日付のdef日前の00時00分のyyyy_MMのStringで返す
	 *
	 * @param def             何日前
	 * @param date            java.util.Dateオブジェクト
	 */
	public static String makeTableDateD(int def)
	{
		java.util.Date date = new java.util.Date();
		String ret = makeTableDateD(def, date);
		return ret;
	}

	/**
	 * 今日の日付のdef日前の00時00分のyyyy_MMのStringで返す
	 *
	 * @param def             何日前
	 * @param date            java.util.Dateオブジェクト
	 */
	public static String makeTableDateD(int def, java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
		cal.add(Calendar.DATE, def);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
		cal.get(Calendar.DATE), 0, 0, 0);
		date = cal.getTime();
		String ret = makeDateTime(date, "yyyyMM");
		return ret;
	}

	/**
	 * 今日の日付のdef日前をyyyy/MM/ddのStringで返す.
	 *
	 * @param def             何日前
	 */
	public static String makeDate2(int def)
	{
		java.util.Date date = new java.util.Date();
		String ret = makeDate2(def, date);
		return ret;
	}

	/**
	 * 今日の日付のdef日前をyyyy/MM/ddのStringで返す.
	 *
	 * @param def             何日前
	 * @param Date            Dateオブジェクト
	 */
	public static String makeDate2(int def, java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
		cal.add(Calendar.DATE, def);
		date = cal.getTime();
		String ret = makeDateTime(date, "yyyy/MM/dd");
		return ret;
	}

	/**
	 * 日付(String)を任意の形式で返す.
	 *
	 * @param date          java.util.Dateオブジェクト
	 */
	public static String makeDateTime(java.util.Date date, String format)
	{
		SimpleDateFormat sdf = null;
		sdf = new SimpleDateFormat(format);
		String ret = sdf.format(date);
		return ret;
	}

	/**
	 * 今日の日付のdef日前の00時00分のyyyyMMddのStringで返す
	 *
	 * @param def             何日前
	 * @param date            java.util.Dateオブジェクト
	 */
	public static String makeTableDateD2(int def)
	{
		java.util.Date date = new java.util.Date();
		String ret = makeTableDateD2(def, date);
		return ret;
	}

	/**
	 * 今日の日付のdef日前の00時00分のyyyyMMddのStringで返す
	 *
	 * @param def             何日前
	 * @param date            java.util.Dateオブジェクト
	 */
	public static String makeTableDateD2(int def, java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
		cal.add(Calendar.DATE, def);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
			cal.get(Calendar.DATE), 0, 0, 0);
		date = cal.getTime();
		String ret = makeDateTime(date, "yyyyMMdd");
		return ret;
	}

}
