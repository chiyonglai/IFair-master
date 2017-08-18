package com.ifair.myUtil;

import android.util.Log;

import com.ideabus.ideabuslibrary.util.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 時間的工具
 */

public class MyTimeUtils extends TimeUtils {

    //預設時間格式
    public static final String DefaultDateType = "yyyy-MM-dd HH:mm:ss";

    /**
     * 將 時間去除
     *
     * @param date
     * @return
     */
    public static String clearTime(String date) {
        String[] strDate = date.split(" ");
        return strDate[0];
    }

    /**
     * 將時間轉換成字串
     * example
     * 1999-01-01 to 1999年01月01日
     * @param date
     * @return
     */
    public static String timeToString(String date) {
        String[] strTime = clearTime(date).split("-");

        return strTime[0] + "年" + strTime[1] + "月" + strTime[2] + "日";
    }

    /**
     * 將時間轉換成字串
     * example
     * 1999-01-01 to 1999/01/01
     * @param date
     * @return
     */
    public static String timeToString1(String date) {
        String[] strTime = clearTime(date).split("-");

        return strTime[0] + "/" + strTime[1] + "/" + strTime[2];
    }

    /**
     * 切割時間
     * @param date
     * @param type 1 : -, 2: /
     * @return
     */
    public static String[] splitDate(String date,int type) {
        if (type == 1)
            return clearTime(date).split("-");
        else if (type == 2)
            return clearTime(date).split("/");
        else
            return null;
    }

    /**
     * 取得當前時間
     *
     * @param dateType example : "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getCurrentDate(String dateType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateType, Locale.TAIWAN);
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 將字串轉換成日期格式
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String formatToDate(int year, int month, int day) {
        String strMonth = month >= 10 ? "" + month : "0" + month;
        String strDay = day >= 10 ? "" + day : "0" + day;

        return year + "/" + strMonth + "/" + strDay;
    }

    /**
     * 計算與今天相差天數是不是在限制天數內
     *
     * @param productDate 輸入要和今天比較的日期
     * @param days        輸入新品期間天數(EX:7 =>7天內是新品)
     */

    public static Boolean returnDays(String productDate, int days) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            String productdate = sdf.format(new Date(productDate));
            String today = sdf.format(date);
            Date date1 = sdf.parse(productdate);
            Date date2 = sdf.parse(today);
            if ((int) (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24) <= days)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 預購倒數時間
     *
     * @param endDate 商品預購日期結束
     * @param date    現在時間
     *                回傳時間陣列  天數、小時、分鐘、秒
     */
    public static int[] returnPreOrderReciprocal(String endDate, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        int[] num = new int[4];

        try {
          //  String productdate = sdf.format(new Date(endDate));
            String today = sdf.format(date);
            Date date1 = sdf.parse(endDate);
            Date date2 = sdf.parse(today);

            Long lo = (date1.getTime() - date2.getTime()) / 1000;
            // 依須是天數、小時、分鐘、秒
            num[0] = (int) Math.floor(lo / 60 / 60 / 24);
            num[1] = (int) Math.floor(lo / 60 / 60) % (24);
            num[2] = (int) Math.floor(lo / 60) % (60);
            num[3] = (int) Math.floor(lo % (60));
            return num;
        } catch (Exception e) {
            return num;
        }
    }

    /**
     * 是否開始計算預購
     *
     * @param startDate 預購開始日期
     * @param endDate   預購結束日期
     */
    public static Boolean returnPreorder(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date day = sdf.parse(sdf.format(new Date()));
            Date day1 = sdf.parse(startDate);
            Date day2 = sdf.parse(endDate);
            if (!day.before(day1) && !day.after(day2)) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
    }
}
