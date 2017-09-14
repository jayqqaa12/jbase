package com.jayqqaa12.jbase.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * JDK8 里面都有了 大部分
 *
 * @author  12
 */
@Deprecated
public class DateKit
{
	private static final Logger LOG = LoggerFactory.getLogger(DateKit.class);
	public static final String HH_MM_SS = "HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String MM_DD = "MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss";

	public static String addDay(String data)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		try
		{
			cd.setTime(sdf.parse(data));
			cd.add(5, 1);
		} catch (ParseException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return sdf.format(cd.getTime());
	}

	public static String addDay(int add)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.add(5, add);
		return sdf.format(cd.getTime());
	}

	public static String addDay(String data, int add)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		try
		{
			cd.setTime(sdf.parse(data));
			cd.add(5, add);
		} catch (ParseException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return sdf.format(cd.getTime());
	}

	public static Date getAfterDate(Date date, int amount)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(5, amount);
		return cal.getTime();
	}

	public static int getMonthDay(int year, int month)
	{
		return getMonthDayByYear(year)[(--month)];
	}

	public static int[] getMonthDayByYear(int year)
	{
		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) monthDay[1] += 1;
		return monthDay;
	}

	public static long[] dateDiff(String startTime, String endTime)
	{
		return dateDiff(startTime, endTime, "yyyy-MM-dd");
	}

	public static long[] dateDiff(String startTime, String endTime, String format)
	{
		SimpleDateFormat sd = new SimpleDateFormat(format);

		long nd = 86400000L;

		long nh = 3600000L;

		long nm = 60000L;

		long ns = 1000L;

		long[] date = new long[4];
		try
		{
			long diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();

			long day = diff / nd;
			long hour = diff % nd / nh;
			long min = diff % nd % nh / nm;
			long sec = diff % nd % nh % nm / ns;

			date[0] = day;
			date[1] = hour;
			date[2] = min;
			date[3] = sec;
		} catch (ParseException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return date;
	}

	public static Date parseDate(String dateStr, String format)
	{
		Date date = null;
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);

			date = dateFormat.parse(dateStr);
		} catch (Exception localException)
		{}
		return date;
	}

	public static String formatDate(String dateStr, String format, String toFormat)
	{
		return format(parseDate(dateStr, format), toFormat);
	}

	public static Date parseDate(String dateStr)
	{
		return parseDate(dateStr, "yyyy-MM-dd");
	}

	public static String format(Date date, String format)
	{
		String result = "";
		try
		{
			if (date != null)
			{
				DateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);
			}
		} catch (Exception localException)
		{}
		return result;
	}

	public static String format(Date date)
	{
		return format(date, "yyyy-MM-dd");
	}

	public static int getYear(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(1);
	}

	public static int getMonth(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(2) + 1;
	}

	public static int getDay(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(5);
	}

	public static int getHour(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(11);
	}

	public static int getMinute(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(12);
	}

	public static int getSecond(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(13);
	}

	public static long getMillis(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	public static String getNow()
	{

		return getDate(new Date());
	}

	public static String getDate(Date date)
	{
		return format(date, "yyyy-MM-dd");
	}

	public static String getTime(Date date)
	{
		return format(date, "HH:mm:ss");
	}

	public static String getDateTime(Date date)
	{
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date addDate(Date date, int day)
	{
		Calendar calendar = Calendar.getInstance();
		long millis = getMillis(date) + day * 24L * 3600L * 1000L;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public static long diffDate(Date date, Date date1)
	{
		return (getMillis(date) - getMillis(date1)) / 86400000L;
	}

	public static String getMonthBegin(String strdate)
	{
		Date date = parseDate(strdate);
		return format(addDate(null, 0), "yyyy-MM") + "-01";
	}

	public static String getMonthEnd(String strdate)
	{
		Date date = parseDate(getMonthBegin(strdate));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(2, 1);
		calendar.add(6, -1);
		return formatDate(calendar.getTime());
	}

	public static String formatDate(Date date)
	{
		return formatDateByFormat(date, "yyyy-MM-dd");
	}

	public static String formatDateByFormat(Date date, String format)
	{
		String result = "";
		if (date != null)
		{
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static final int getCurrentDayOfWeek()
	{
		return Calendar.getInstance().get(7) - 1;
	}

	
	/**
	 * 获取 当天零点时间
	 * @return
	 */
	public static long getZeroTime()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (cal.getTimeInMillis());
	}
}
