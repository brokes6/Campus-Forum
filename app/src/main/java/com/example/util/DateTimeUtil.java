package com.example.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.support.constraint.Constraints.TAG;

public class DateTimeUtil {
    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String handlerDateTime(long datetime){
        long nowDateTime = System.currentTimeMillis();
        Log.i(TAG, "handlerDateTime: "+nowDateTime);
        Log.i(TAG, "handlerDateTime: datetime="+datetime);
        String strTime="";
        long spaceTime =nowDateTime-datetime;
        if(spaceTime<86400000){
            Log.i(TAG, "handlerDateTime: 1");
            if(spaceTime>=3600000){
                strTime=(spaceTime/3600000)+"小时前";
            }else{
                strTime=(spaceTime/60000)+"分钟前";
            }
        }else{
            strTime=simpleDateFormat1.format(datetime);
        }
        return strTime;
    }
    public static String handlerDateTime(String str){
        try {
            return handlerDateTime(simpleDateFormat.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}