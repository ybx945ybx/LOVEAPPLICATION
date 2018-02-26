package kyf.loveapplication.utils;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 地址Util
 *
 * @author 陶声
 * @since 2017-08-14
 */

public class AddressUtils {
    public static ArrayList<String>                       provinceList;
    public static ArrayList<ArrayList<String>>            cityList;
    public static ArrayList<ArrayList<ArrayList<String>>> districtList;

    /**
     * 解析JSON文件
     */
    public static void parseFromJson(Context context) throws IOException, JSONException {

        if (provinceList != null && provinceList.size() > 0)
            return;

        String str = readFile(context);

        if (TextUtils.isEmpty(str)) {
            return;
        }

        ArrayList<String>            temp1;
        ArrayList<ArrayList<String>> temp2;
        ArrayList<String>            temp3;

        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        districtList = new ArrayList<>();

        JSONArray provinceJsonArray = new JSONArray(str);

        for (int i = 0; i < provinceJsonArray.length(); i++) {
            JSONObject provinceItemJSONObject = provinceJsonArray.getJSONObject(i);

            //省的名字
            String provinceName = provinceItemJSONObject.getString("region_name");
            provinceList.add(provinceName);
            temp1 = new ArrayList<>();
            temp2 = new ArrayList<>();

            JSONArray cityJSONArray = provinceItemJSONObject.getJSONArray("sub_regions");

            for (int j = 0; j < cityJSONArray.length(); j++) {

                JSONObject cityItemJSONObject = cityJSONArray.getJSONObject(j);

                String cityName = cityItemJSONObject.getString("region_name");
                temp1.add(cityName);
                temp3 = new ArrayList<>();

                JSONArray disctrictJSONArray = cityItemJSONObject.getJSONArray("sub_regions");
                for (int k = 0; k < disctrictJSONArray.length(); k++) {
                    //                    temp3.add(disctrictJSONArray.getString(k));

                    JSONObject disctrictJSONObject = disctrictJSONArray.getJSONObject(k);
                    String     districtName        = disctrictJSONObject.getString("region_name");
                    temp3.add(districtName);

                }
                temp2.add(temp3);

            }
            cityList.add(temp1);
            districtList.add(temp2);
        }
    }

    @NonNull
    private static String readFile(Context context) throws IOException {
        InputStream in        = context.getAssets().open("address.json");
        int         available = in.available();
        byte[]      b         = new byte[available];
        in.read(b);
        return new String(b, "UTF-8");
    }
}
