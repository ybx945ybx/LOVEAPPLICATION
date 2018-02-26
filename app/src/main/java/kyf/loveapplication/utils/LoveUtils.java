package kyf.loveapplication.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by a55 on 2017/11/2.
 */

public class LoveUtils {


    public static int getSize(ArrayList arrayList) {
        return arrayList == null ? 0 : arrayList.size();
    }

    public static int getSize(List arrayList) {
        return arrayList == null ? 0 : arrayList.size();
    }

    public static int getSize(String[] list) {
        return list == null ? 0 : list.length;
    }

    public static int getSize(String string) {
        return string == null ? 0 : string.length();
    }

    /**
     * 格式化为应用 常见显示格式 当前天显示时间，其他显示年月日
     *
     * @param strTime
     * @return
     */
    public static String formatLatelyTime(String strTime) {
        if (null == strTime || "".equals(strTime))
            return "";
        SimpleDateFormat dfs         = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date             currentTime = null;
        Date             commentTime = null;
        String           str         = null;
        try {
            currentTime = Calendar.getInstance().getTime();
            commentTime = dfs.parse(strTime);
            if (currentTime.getYear() == commentTime.getYear()
                    && currentTime.getMonth() == commentTime.getMonth()
                    && currentTime.getDate() == commentTime.getDate()) {
                dfs = new SimpleDateFormat("HH:mm");
                str = dfs.format(commentTime);
            } else {
                dfs = new SimpleDateFormat("yyyy-MM-dd");
                str = dfs.format(commentTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
