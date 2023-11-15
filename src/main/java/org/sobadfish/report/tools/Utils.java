package org.sobadfish.report.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.SplittableRandom;

/**
 * @author SoBadFish
 * 2022/1/21
 */
public class Utils {

    private static final SplittableRandom RANDOM = new SplittableRandom(System.currentTimeMillis());

    public static int rand(int min, int max) {
        return min == max ? max : RANDOM.nextInt(max + 1 - min) + min;
    }

    public static double rand(double min, double max) {
        return min == max ? max : min + Math.random() * (max - min);
    }

    public static float rand(float min, float max) {
        return min == max ? max : min + (float)Math.random() * (max - min);
    }

    /**
     * 计算两个时间相差天数
     *
     * @param oldDate 终点时间
     *
     * @return 时差
     * */
    public static int getDifferDay(Date oldDate){
        if(oldDate == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(oldDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(Objects.requireNonNull(getDate(getDateToString(new Date()))));
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time1-time2)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    public static String getDateToString(Date date){
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd");
        return lsdStrFormat.format(date);
    }



    //转换String为Date

    public static Date getDate(String format){
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return lsdStrFormat.parse(format);
        }catch (ParseException e){
            return null;
        }
    }
}
