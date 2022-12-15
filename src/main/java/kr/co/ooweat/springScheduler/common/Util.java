package kr.co.ooweat.springScheduler.common;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class Util {
    public static String getClassName() {
        return Thread.currentThread().getStackTrace()[2].getClassName()+" //  ";
    }
    public static String getMethodName() { return Thread.currentThread().getStackTrace()[2].getMethodName()+" //  ";}

    public static String getCurrentDateTime(){
        Date date =new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public static String getCurrentDate(){
        Date date =new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public static String getCurrentYYMM(){
        Date date =new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyMM");
        return sdf.format(date);
    }
    public static String getCurrentYYYYMM(){
        Date date =new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
        return sdf.format(date);
    }
    public static String getCurrentYYYYMMDD(){
        Date date =new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }
    public static String getCurrentDD(){
        Date date =new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("dd");
        return sdf.format(date);
    }
    //문자열 Util
    public static StringUtil stringUtil(String param) {
        return new StringUtil(param);
    }

    public static class StringUtil {
        private String value;

        public StringUtil(String value) {
            this.value = value;
        }

        //SQL 의 그것과 같음 in
        public boolean in(String... values) {
            for (String v : values) {
                if (v.equals(value)) return true;
            }
            return false;
        }

        //SQL 의 그것과 같음 notIn
        public boolean notIn(String... values) {
            for (String v : values) {
                if (v.equals(value)) return false;
            }
            return true;
        }
    }

}
