package kyf.loveapplication.utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by a55 on 2017/12/2.
 */

public class ImageUtil {

    public static Bitmap getBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);

        return bm;
    }

    /**
     * 获取View的bitmap
     *
     * @param view view
     * @return bitmap
     */
    public static Bitmap getBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int           orientation   = exifInterface.getAttributeInt("Orientation", 1);
            switch (orientation) {
                case 6:
                    degree = 90;
                    break;
                case 3:
                    degree = 180;
                    break;
                case 8:
                    degree = 270;
                case 4:
                case 5:
                case 7:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * @param filePath
     * @param width
     * @param height
     * @param rotate
     * @return
     */
    public static Bitmap getRotateBitmap(String filePath, int width, int height, int rotate) {
        return rotateBitmap(getSmallBitmap(filePath, width, height), rotate);
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static String saveAsBitmap(Context context, Bitmap bitmap, String folderName, String fileName) {
        String parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/55海淘/";

        /*if ((fileName == null) || (fileName.equals(""))) {
            Date             date   = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            fileName = format.format(date) + ".jpg";
        }
        if ((folderName != null) && (!folderName.equals(""))) {
            fileName = folderName + "/" + fileName;
        }

        File file = new File(parentpath, fileName);
        file.getParentFile().mkdirs();
        Logger.d(file.getAbsolutePath());*/
        if (TextUtils.isEmpty(fileName)) {
            Date             date   = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            fileName = format.format(date) + ".jpg";
        }

        File pic = new File(parentpath, fileName);
        if (!pic.getParentFile().exists())
            pic.getParentFile().mkdirs();

        if (pic.exists())
            pic.delete();

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(pic));
            MediaScannerConnection.scanFile(context, new String[]{pic.toString()}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.recycle();
        return pic.getAbsolutePath();
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height       = options.outHeight;
        final int width        = options.outWidth;
        int       inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio  = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取图片
     *
     * @param filePath
     * @param width
     * @param height
     * @return
     */
    public static String getNomalBitmap(String filePath, int width, int height) {
        //		BitmapFactory.Options options = new BitmapFactory.Options();
        //		options.inJustDecodeBounds = true;
        //		BitmapFactory.decodeFile(filePath, options);
        //		// Calculate inSampleSize
        //		options.inSampleSize = calculateInSampleSize(options, width, height);
        //
        //		// Decode bitmap with inSampleSize set
        //		options.inJustDecodeBounds = false;
        //		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        //		if (bm == null) {
        //			return null;
        //		}
        int degree = readPictureDegree(filePath);
        //		bm = rotateBitmap(bm, degree);
        return compressImage(getRotateBitmap(filePath, width, height, degree));
    }


    /**
     * 获取图片
     *
     * @param filePath
     * @param width
     * @param height
     * @return
     */
    public static String getBitmapBase64(String filePath, int width, int height) {
        //		BitmapFactory.Options options = new BitmapFactory.Options();
        //		options.inJustDecodeBounds = true;
        //		BitmapFactory.decodeFile(filePath, options);
        //		// Calculate inSampleSize
        //		options.inSampleSize = calculateInSampleSize(options, width, height);
        //
        //		// Decode bitmap with inSampleSize set
        //		options.inJustDecodeBounds = false;
        //		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        //		if (bm == null) {
        //			return null;
        //		}
        int degree = readPictureDegree(filePath);
        //		bm = rotateBitmap(bm, degree);
        return compressImageBase64(getRotateBitmap(filePath, width, height, degree));
    }


    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap                bitmap  = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, width, height);
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    /**
     * 图片质量压缩
     *
     * @param image
     * @return
     */
    public static String compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        Log.e("tg", "baos.toByteArray().length = " + baos.toByteArray().length);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }

        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            Log.e("tg", "options = " + options);
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        Log.e("tg", "baos.toByteArray().length = " + baos.toByteArray().length);
        String path     = FileUtils.initPath();
        long   dataTake = System.currentTimeMillis();
        String jpegName = path + dataTake + ".jpg";
        try {
            FileOutputStream fos = new FileOutputStream(jpegName);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jpegName;
    }


    /**
     * 图片质量压缩
     *
     * @param image
     * @return
     */
    public static String compressImageBase64(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        Log.e("tg", "baos.toByteArray().length = " + baos.toByteArray().length);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }

        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            Log.e("tg", "options = " + options);
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        Log.e("tg", "baos.toByteArray().length = " + baos.toByteArray().length);
        String path     = FileUtils.initPath();
        long   dataTake = System.currentTimeMillis();
        String picStr   = "";
        try {
            byte[] imgBytes = baos.toByteArray();
            picStr = Base64.encodeToString(imgBytes, Base64.DEFAULT);
            image.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return picStr;
    }
}
