package com.framework.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理
 */
public class DateUtils {

	private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>();

	public final static String DATE_PATTERN = "yyyy-MM-dd";
	
	public static String format(Date date) {
		return format(date, DATE_PATTERN);
	}

	public static String format(Date date, String pattern) {
		if (date != null) {
			return getDateFormat().format(date);
		}
		return null;
	}
	  
    public static DateFormat getDateFormat() {  
        DateFormat dateFormat = (DateFormat) df.get();  
        if (dateFormat == null) {  
        	dateFormat = new SimpleDateFormat(DATE_PATTERN);  
            df.set(dateFormat);  
        }  
        return dateFormat;  
    }
    
    public static DateFormat getDateFormat(String pattern) {  
        DateFormat dateFormat = (DateFormat) df.get();  
        if (dateFormat == null) {  
        	dateFormat = new SimpleDateFormat(pattern);  
            df.set(dateFormat);  
        }  
        return dateFormat;  
    }

}
