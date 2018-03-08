package Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 가애 on 2018-03-08.
 */

public class DateUtils {

    public static Date stringToDate(String dateStr) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date date = null;

        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String dateToString(Date date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String dateStr = dateFormat.format(date);

        return dateStr;
    }
}
