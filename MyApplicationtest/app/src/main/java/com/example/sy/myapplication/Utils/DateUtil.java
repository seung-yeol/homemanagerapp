package com.example.sy.myapplication.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
    //날짜 덧셈
    //DateAdd(더할 년수 ,더할 월수,더할 일수)
    private static Calendar c = Calendar.getInstance();
    public String DateAdd(int year, int month, int day){
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, year); // 1년을 더한다.
        cal.add(Calendar.MONTH, month); // 한달을 더한다.
        cal.add(Calendar.DAY_OF_YEAR, day); // 하루를 더한다.
        //cal.add(Calendar.HOUR, hour); // 시간을 더한다.

        SimpleDateFormat fm = new SimpleDateFormat(
                "yyyyMMdd");
        String strDate = fm.format(cal.getTime());
        return strDate;
    }

    //오늘날자얻어오기
    public String ToDay(){
        Calendar c = new GregorianCalendar(Locale.KOREA);
        SimpleDateFormat fm = new SimpleDateFormat(
                "yyyyMMdd");
        String s = fm.format(c.getTime());
        return s;
    }
    //경고날자 얻기 위한 날자 세팅
    public void W_Date_set(Calendar cal){
        c = cal;
    }
    //경고 날자 얻음
    public String W_Date(int i){
        c.add(Calendar.DAY_OF_YEAR,-i); // i일 만큼 뺀다.
        SimpleDateFormat fm = new SimpleDateFormat(
                "yyyyMMdd");
        String strDate = fm.format(c.getTime());
        return strDate;
    }
    //갱신시 경고날자 업데이트
    public String warning_Update(String expiry, String warning, String setting){
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");

        Date Day1 = null;  // 만기일
        try {
            Day1 = fm.parse(expiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date Day2 = null;    //경고일
        try {
            Day2 = fm.parse(warning);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long a = Day1.getTime() - Day2.getTime();
        //ms단위로 나오므로 24 * 60 * 60 * 1000로 나눠줌
        long a_day = a / (24 * 60 * 60 * 1000);
        int b = (int)a_day;

        Calendar cal = new GregorianCalendar(Locale.KOREA);

        String s = Expiry_Update(expiry,setting);
        int C_Set[] = {0,0,0};
        C_Set[0] = Integer.parseInt(s.substring(0,4));
        C_Set[1] = Integer.parseInt(s.substring(4,6))-1; //월 부분은 0을 JAN으로 11을 DEC로인식
        C_Set[2] = Integer.parseInt(s.substring(6));

        cal.set(C_Set[0],C_Set[1],C_Set[2]);
        cal.add(Calendar.DAY_OF_YEAR, -b);
        String str = fm.format(cal.getTime());

        return str;
    }
    //갱신시 만기날자 업데이트
    public String Expiry_Update(String expiry, String setting){

        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");

        Date Day1 = null;  //만기일
        try {
            Day1 = fm.parse(expiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date Day2 = null;    //설정일
        try {
            Day2 = fm.parse(setting);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long a = Day1.getTime() - Day2.getTime();
        long a_day = a / (24 * 60 * 60 * 1000);
        int b = (int)a_day;

        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR,b);
        String str = fm.format(cal.getTime());

        return str;
    }
}
