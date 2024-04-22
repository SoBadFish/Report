package org.sobadfish.report.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static String[] splitMsg(String msg){
        String[] rel = msg.split("&");
        if(rel.length <= 1){
            String rr = rel[0].trim();
            if("".equalsIgnoreCase(rr)){
                rr = "-";
            }
            rel = new String[]{rr,"-"};
        }
        return rel;
    }

    public static String getCentontString(String input,int lineWidth){
        input = input.replace(' ','$');
        return justify(input,lineWidth,'c').replace('$',' ');
    }
    /**
     * 字符串居中算法
     *
     * @param input 输入的字符串
     * @param lineWidth 一共多少行
     * @param just 对齐方法 l: 左对齐 c: 居中 r右对齐
     *
     * @return 对齐的字符串
     * */
    public static String justify(String input, int lineWidth, char just) {
        StringBuilder sb = new StringBuilder("");
        char[] inputText = input.toCharArray();
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < inputText.length; i++) {
            if (inputText[i] != ' ' && inputText[i] != '\n') {
                sb.append(inputText[i]);
            } else {
                inputText[i] = '\n';
                words.add(sb.toString());
                //clear content
                sb = new StringBuilder("");
            }
        }
        //add last word because the last char is not space/'\n'.
        words.add(sb.toString());
        for (String s : words) {
            if (s.length() >= lineWidth) {
                lineWidth = s.length();
            }
        }
        char[] output = null;
        StringBuilder sb2 = new StringBuilder("");
        StringBuilder line;
        for (String word : words) {
            line = new StringBuilder();
            for (int i = 0; i < lineWidth; i++) {
                line.append(" ");
            }
            switch (just) {
                case 'l':
                    line.replace(0, word.length(), word);
                    break;
                case 'r':
                    line.replace(lineWidth - word.length(), lineWidth, word);
                    break;
                case 'c':
                    //all the spaces' length
                    int rest = lineWidth - word.length();
                    int begin = 0;
                    if (rest % 2 != 0) {
                        begin = (rest / 2) + 1;
                    } else {
                        begin = rest / 2;
                    }
                    line.replace(begin, begin + word.length(), word);
                    break;
                default:break;
            }

            line.append('\n');
            sb2.append(line);
        }
        return sb2.toString();

    }
}
