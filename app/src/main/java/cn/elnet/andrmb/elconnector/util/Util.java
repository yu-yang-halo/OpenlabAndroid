/**
 * 
 * @Title: Util.java
 * @Package cn.lztech.elwsapi
 * @Description:
 *
 * <p>Copyright: Copyright (c) 2011 Hefei Lianzheng Electronic Technology, Inc</p>
 * <p>Developed By: Hefei Lianzheng Electronic Technology, Inc</p>
 *
 * @Author Kai
 * @Date 2011-11-7 下午04:20:17
 * @Version 1.0 
 *
 */
package cn.elnet.andrmb.elconnector.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	public static final int REQ_TIME_OUT = 6 * 1000;  
	public static final int READ_TIME_OUT =11 * 1000;  
	public static String formatSpeDate(Date date,int hour,int min){
		date.setHours(hour);
		date.setMinutes(min);
		String pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'+08:00'";
		DateFormat format=new SimpleDateFormat(pattern);
		return format.format(date);
	}
	public static Date formatString(String timeStr){
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date time = sdf.parse(timeStr);
			return time;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean isHttpsRequest(String url) {
		int index = url.indexOf("https://");
		if (index < 0) {
			return false;
		} else {
			return true;
		}
	}
}
