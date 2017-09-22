package com.ihxlife.qyhgateway.support.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：日期工具类
 * 
 */
public class DateUtil {

	private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	
	public static final String YYYYMMDD = "yyyyMMdd";
	
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String YYYY_year_MM_month_DD_day = "yyyy年MM月dd日";
	
	public static final String HHMMSS = "HH:mm:ss";
	
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
	
	public static final String YEAR = "year";
	
	public static final String MONTH = "month";
	
	public static final String DAY = "day";

	/**
	 * 描述：字符串转换时间类型 作者: chang 修改日期：2014-07-02下午03:13:35
	 * @param str 时间字符串
	 * @param dateFormat 格式 "yyyy-MM-dd"
	 * @return
	 */
	public static Date convertStringToDate(String str, String dateFormat) {
		if (str != null && !"".equals(str)) {
			SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
			try {
				Date birthDate = sf.parse(str);
				return birthDate;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 描述：获取当前日期 作者: chang 修改日期：2014-07-02上午10:22:50
	 * @return 返回当前日期 日期格式 yyyy-MM-dd
	 */
	public static String currentDate() {
		return currentDate(YYYY_MM_DD);
	}

	/**
	 * 描述：获取当前日期 作者: chang 修改日期：2014-07-02上午10:23:26
	 * @param dateFormat 日期格式
	 * @return 返回当前日期
	 */
	public static String currentDate(String dateFormat) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
			String currentDate = sf.format(new Date());
			return currentDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：把java.util.date 转换成String类型 作者: chang 修改日期：2014-07-02上午10:46:04
	 * @param date 日期
	 * @return 格式化成yyyy-MM-dd格式的字符串类型日期
	 */
	public static String convertDate(Date date) {
		if (date == null) {
			return null;
		}
		return convertDate(date, YYYY_MM_DD);
	}

	/**
	 * 描述：把java.util.date 转换成String类型 作者: chang
	 * @param date 日期
	 * @param dateFormat 日期格式
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static String convertDate(Date date, String dateFormat) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
			String currentDate = sf.format(date);
			return currentDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：获取当前日期的后一天 date类型<br>
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static Date getNextDayDate() {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			return calendar.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：获取当前日期的后一天 yyyy-MM-dd格式<br>
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static String getNextDay() {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date date = calendar.getTime();
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：获取当前日期的后一天（传入日期格式）<br>
	 * @param dateFormat 日期格式<br>
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static String getNextDay(String dateFormat) {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date date = calendar.getTime();
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：获取当前日期的下一年(yyyy-MM-dd格式)<br>
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static String getNextYear() {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
			calendar.add(Calendar.YEAR, 1);
			Date date = calendar.getTime();
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：获取当前日期的下一年<br>
	 * @param dateFormat  日期格式<br>
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static String getNextYear(String dateFormat) {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			calendar.add(Calendar.YEAR, 1);
			Date date = calendar.getTime();
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：获取指定日期的下一年(yyyy-MM-dd格式)<br>
	 * 日期格式<br>
	 */
	public static String getNextYearByDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.YEAR, 1);
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			return sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：获取指定日期推后N天的日期(yyyy-MM-dd格式)<br>
	 * 日期格式<br>
	 */
	public static String getAfterDays(String date, int days) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.DAY_OF_YEAR, days);
			return sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 描述：获取当前时间 作者: chang 修改日期：2014-07-02上午11:09:20
	 * @return
	 */
	public static Date getDate() {
		Date date = new Date();
		return date;
	}

	/**
	 * 描述：将传入的时间转化成秒数 作者: chang 修改日期：2014-07-02下午19:09:20
	 * @return 秒数
	 */
	public static int getSecond(String value) {
		StringTokenizer s = new StringTokenizer(value, ":");
		int type = s.countTokens() - 1;
		int result = 0;
		Pattern p = Pattern.compile(":");
		if (type == 0)// ss
			result = Integer.parseInt(value);
		if (type == 1) {// mm:ss
			String strValue[] = p.split(value);
			result = Integer.parseInt(strValue[0]) * 60 + Integer.parseInt(strValue[1]);
		}
		if (type == 2) {// hh:mm:ss
			String strValue[] = p.split(value);
			result = Integer.parseInt(strValue[0]) * 3600 + Integer.parseInt(strValue[1]) * 60
					+ Integer.parseInt(strValue[2]);
		}
		return result;
	}

	/**
	 * 描述：根据日期获取年、月、日 作者: chang 修改日期：2014-07-02下午03:18:40
	 * @param birthday
	 * @param flag
	 * @return
	 */
	public static String getNumByBirthday(Date birthday, String flag) {
		String Num = null;
		String birthdayStr = convertDate(birthday, YYYY_MM_DD);
		if (StringUtils.isNotBlank(birthdayStr)) {
			String[] birthdaySplit = birthdayStr.split("-");
			if (birthdaySplit != null) {
				if (birthdaySplit.length == 3) {
					if (YEAR.equals(flag)) {
						Num = birthdaySplit[0];
					}
					if (MONTH.equals(flag)) {
						Num = birthdaySplit[1];
					}
					if (DAY.equals(flag)) {
						Num = birthdaySplit[2];
					}
				}
			}
		}
		return Num;
	}

	/**
	 * 描述：转换日期格式，把日期字符串从yyyy-MM-dd格式转换成toDateFormat格式 作者: chang
	 * @param dateString 日期
	 * @param toDateFormat  日期格式
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static String convertDate(String dateString, String toDateFormat) {
		return convertDate(dateString, YYYY_MM_DD, toDateFormat);
	}

	/**
	 * 描述：转换日期格式，把日期字符串从fromDateFormat格式转换成toDateFormat格式 作者: chang
	 * @param dateString 日期
	 * @param fromDateFormat  日期格式
	 * @param toDateFormat 日期格式
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static String convertDate(String dateString, String fromDateFormat, String toDateFormat) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(fromDateFormat);
			Date date = sf.parse(dateString);
			SimpleDateFormat sf1 = new SimpleDateFormat(toDateFormat);
			String currentDate = sf1.format(date);
			return currentDate;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 描述：把时间戳转化成指定格式的时间 作者: chang
	 * @param timestamp 时间戳
	 * @param toDateFormat 转化成的时间格式
	 * @return 指定格式的日期字符串
	 */
	public static String getTimeFromTimestamp(String timestamp, String toDateFormat) {
		try {
			// 时间戳转化为Sting或Date
			SimpleDateFormat format = new SimpleDateFormat(toDateFormat);
			Long time = new Long(timestamp);
			String d = format.format(new Date(time * 1000));
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 描述：把时间戳转化成时间 作者: chang
	 * @param timestamp 时间戳
	 * @return 日期
	 */
	public static Date getTimeFromTimestamp(String timestamp) {
		try {
			// 时间戳转化为Date
			Long time = new Long(timestamp);
			return new Date(time * 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 如果是年份加减cal.add(1, i); 如果是每日加减cal.add(5, i); 如果是月份加减cal.add(2, i);
	 * 如果是小时加减cal.add(10, i); 如果是星期加减cal.add(3, i); 如果是分钟加减cal.add(12, i);
	 * 如果是星期加减cal.add(3, i); 如果是秒的加减cal.add(13, i);
	 * @param date
	 * @param i
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Date addOrMinusYear(Date date, int i, int type) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(type, i);
		rtn = cal.getTime();
		return rtn;

	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrentTime() {
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("HH:mm:ss");
		logger.info("当前时间为【{}】", matter1.format(dt));
		String tCurrentTime = matter1.format(dt);
		return tCurrentTime;
	}

	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getCurrentDate() {
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		logger.info("当前日期为【{}】", matter1.format(dt));
		String tCurrentDate = matter1.format(dt);
		return tCurrentDate;
	}
}