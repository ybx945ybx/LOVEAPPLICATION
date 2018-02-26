package kyf.loveapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kyf.loveapplication.data.constant.ServerUrl;

/**
 * Created by a55 on 2017/11/19.
 */

public class FileUtils {
    public static String PIC_PATH = "";
    public static        String CATCH_PATH = Environment.getExternalStorageDirectory().getPath() + ServerUrl.PIC_PATH;

    public static String saveBitmap(Context context, Bitmap b, String fileName) {
        String path     = getPicPath(context);
        String filePath = path + fileName;
        Logger.d("saveBitmap:jpegName = " + filePath);

        try {
            FileOutputStream     fout = new FileOutputStream(filePath);
            BufferedOutputStream bos  = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
            Logger.d("saveBitmapSuccess");
            return filePath;
        } catch (IOException e) {
            Logger.e("saveBitmap:Fail");
            e.printStackTrace();
            return null;
        }
    }

    public static void initPicPath(Context context) {
        File f = new File(context.getExternalCacheDir() + ServerUrl.PIC_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        PIC_PATH = f.getAbsolutePath();
    }

    public static String getPicPath(Context context) {
        if (TextUtils.isEmpty(PIC_PATH)) {
            initPicPath(context);
        }
        return PIC_PATH;
    }

    public static String initPath() {
        File f = new File(CATCH_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        return CATCH_PATH;
    }
}
