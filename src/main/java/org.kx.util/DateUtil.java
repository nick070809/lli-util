package org.kx.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    /**
     * 指定时间
     *  2014-02-03 12:12:12 yyyy-MM-dd HH:mm:ss
     * @return Date
     */
    public static Date getDate(String datestr, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.setLenient(false);
            return dateFormat.parse(datestr);
        } catch (ParseException e) {
            throw  e;
        }
    }

    public static String getDateTimeStr(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 根据传入日期生成指定天数后的日期
     */
    public static Date createByDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return (Date) cal.getTime();
    }
    /**
     * 几天前的开始时间
     */
    public static Date getHisdayBegin(Date date,int days) {
    	Date d = createByDays(date,days);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
    /**
     * 几天前的结束时间
     */
    public static Date getHisdayEnd(Date date,int days) {
    	Date d = createByDays(date,days);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
    /**
     * 几天前的开始时间
     */
    public static Date getHisdayHour(Date date,int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hours);
        return calendar.getTime();
    }

    /**
     * 获取两个时间段内的相隔秒数
     * 时间比较 返回秒
     */
    public static Long getCostTime(Date end,Date start) {
        Long v = end.getTime() - start.getTime();
        Long result = (v / (1000));
        return result;
    }

    /**
     * 获取月底
     * @param
     * @throws InterruptedException
     */
    public static Date getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date lastDate = calendar.getTime();
        lastDate.setDate(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        lastDate.setHours(calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        lastDate.setMinutes(calendar.getActualMaximum(Calendar.MINUTE));
        lastDate.setSeconds(calendar.getActualMaximum(Calendar.SECOND));
        return lastDate;
    }


    /**
     * 月减模式
     * now , -1, 12*5
     */
    public static List<String> getMonthReduces(Date now,int interval,int allMonths) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < allMonths; i++) {
            Date lastMonth =  DateUtils.addMonths(now, i*interval);
            String ddd = DateUtil.getDateTimeStr(lastMonth,"yyyyMM");
            list.add(ddd);
        }
        return list;
    }

    /**
     * 日减模式
     * now , -1, 12*5*30
     */
    public static List<String> getDayReduces(Date now,int interval,int allDays) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < allDays; i++) {
            Date lastDay =  DateUtils.addDays(now, i*interval);
            String ddd = DateUtil.getDateTimeStr(lastDay,"yyyyMMdd");
            list.add(ddd);
        }
        return list;
    }



    public static void main(String ...s) throws InterruptedException, ParseException {
        Date dd= DateUtil.getDate("20160930","yyyyMMdd");
        List<String> das = getDayReduces(dd,-1,20);
        StringBuilder sc = new StringBuilder();
        das.stream().forEach(e ->sc.append(e).append(","));
        System.out.println(sc.toString());
     }
}
