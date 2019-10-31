package com.demo.architect.utils.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by PC on 13-Apr-2018.
 */

public class ConvertUtils {
    public static final String APP_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static int ConvertStringMoneyToInt(String s) {
        if (s.toString().trim().equals("")) {
            return 0;
        } else {
            String result = "";
            String[] listSp = s.split("\\.");
            for (String item : listSp) {
                result = result + item;
            }
            return Integer.parseInt(result);
        }
    }



    public static String getDateTimeCurrentShort() {
        Date currentTime = Calendar.getInstance().getTime();
        String sDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sDate = formatter.format(currentTime);
        return sDate;
    }


    public static Date convertStringToDate(String s) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(APP_DATETIME_FORMAT);
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date ConvertStringToShortDate(String s) {
        Date date = null;
        String expectedPattern = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern, Locale.getDefault());
        try {
            date = formatter.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    public static Date getDateTimeCurrent() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime;
    }

    public static String formatDate(String s) {
        Date date = null;
        String sDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(APP_DATETIME_FORMAT);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            date = dateFormat.parse(s);
            sDate = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;

    }

    public static long convertStingToLong(String s) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Saigon"));
        Date date = null;
        String sDate = "";
        long mili = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            date = dateFormat.parse(s);
            mili = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mili;
    }

    public static String convertLongToString(long mili) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Saigon"));
        Date date = new Date(mili);
        String sDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        sDate = dateFormat.format(date);

        return sDate;
    }

    public static String getCodeGenerationByTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        long longId = Long.valueOf(dateFormat.format(new Date()));
        String strId = Long.toHexString(longId);

        String rand = new Random().nextInt(999) + "";

        return strId + rand;
    }

}
