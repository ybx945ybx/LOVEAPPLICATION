package kyf.loveapplication.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kyf.loveapplication.R;
import kyf.loveapplication.data.event.ChateTakePhotoEvent;
import kyf.loveapplication.utils.FileUtils;
import kyf.loveapplication.utils.ToastUtils;

/**
 * 聊天拍照
 */
public class CaptureActivity extends FragmentActivity implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private Camera        mCamera;
    SurfaceView    mSurfaceView;
    RelativeLayout layoutRoot;
    private ProgressDialog mDialog;
    private boolean        isPause;

    private String picPath;

    private ImageView ivReturn;
    private ImageView ivSelect;

    private static final int PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);

        initVars();
        initView();
    }

    private void initVars() {
    }

    private void initView() {
        ivReturn = (ImageView) findViewById(R.id.iv_return);
        ivSelect = (ImageView) findViewById(R.id.iv_select);
        initSurfaceView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            openCamera();
        }
    }

    @OnClick({R.id.ib_left, R.id.btn_take_picture, R.id.iv_return, R.id.iv_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_left:
                onBackPressed();
                break;
            case R.id.btn_take_picture:
                identification();
                break;
            case R.id.iv_return:
                releaseCamera();
                mSurfaceView.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.VISIBLE);
                ivReturn.setVisibility(View.GONE);
                ivSelect.setVisibility(View.GONE);

                break;
            case R.id.iv_select:
                // 通知
                EventBus.getDefault().post(new ChateTakePhotoEvent(picPath));
                finish();
                break;
        }
    }

    /**
     * 初始化相机预览
     */
    private void initSurfaceView() {
        layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceView);
        mSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(this);
    }

    /**
     * 获取权限后初始化相机
     */
    protected void doPermissionAction() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.stopPreview();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Camera.Parameters parameters = mCamera.getParameters();
        //照相机宽高参数
        Camera.Size size = getSupportedSize(parameters.getSupportedPreviewSizes(), 0);
        if (size != null) {
            parameters.setPreviewSize(size.width, size.height);
        }
        size = getSupportedSize(parameters.getSupportedPictureSizes(), 1280);
        if (size != null) {
            parameters.setPictureSize(size.width, size.height);
        }

        //        parameters.set("orientation", "portrait");
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90); /* 设置一下preview的大小 */

        mCamera.startPreview();
    }


    /**
     * 拍照发送图片
     */
    private boolean isControl = false;

    protected final void identification() {
        try {
            if (mCamera != null && !isControl) {
                isControl = true;
                mCamera.takePicture(null, null, (bytes, camera) -> {
                    isControl = false;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    picPath = FileUtils.saveBitmap(CaptureActivity.this, bitmap, System.currentTimeMillis() + ".png");
                    ivReturn.setVisibility(View.VISIBLE);
                    ivSelect.setVisibility(View.VISIBLE);
                });
            }
        } catch (Exception e) {
            isControl = false;
        }
    }

    /**
     * 释放摄像头
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 打开摄像头
     */
    private void openCamera() {
        if (mCamera != null) {
            releaseCamera();
        }
        checkAndDoAction();
    }

    private void checkAndDoAction() {
        String[] mPermissionGroup = getPermissionGroup();
        if (!(mPermissionGroup != null && mPermissionGroup.length > 0
                && Build.VERSION.SDK_INT >= 23
                && !handPermission(mPermissionGroup, this, PERMISSION_REQUEST_CODE))) {
            //6.0.0以下权限判断，或者6.0以上但是有禁止权限选择的手机
            doActionCatch();
        }
    }

    private void doActionCatch() {
        try {
            doPermissionAction();
        } catch (Exception e) {
            showPermissonDialog();
        }
    }

    /**
     * 6。0以上需要的权限
     *
     * @return
     */
    protected String[] getPermissionGroup() {
        return new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
    }

    /**
     * 6。0以上权限判断
     *
     * @param mPermissionGroup
     * @param context
     * @param requestCode
     * @return
     */
    private boolean handPermission(String[] mPermissionGroup, Activity context, int requestCode) {
        // 过滤已持有的权限
        List<String> mRequestList = new ArrayList<>();
        for (String permission : mPermissionGroup) {
            if ((ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED)) {
                mRequestList.add(permission);
            }
        }
        // 申请未持有的权限
        if (Build.VERSION.SDK_INT >= 23 && !mRequestList.isEmpty()) {
            ActivityCompat.requestPermissions(context, mRequestList.toArray(
                    new String[mRequestList.size()]), requestCode);
        } else {
            // 权限都有了，就可以继续后面的操作
            return true;
        }
        return false;
    }

    /**
     * 优先获取1280*720
     *
     * @param list
     * @return
     */
    private Camera.Size getSupportedSize(List<Camera.Size> list, int maxSize) {
        if (list == null || list.size() == 0) {
            return null;
        }
        for (Camera.Size s : list) {
            if (s.width == 1280 && s.height == 720) {
                return s;
            }
        }
        int i = 0;
        if (maxSize > 0) {
            int size = list.size();
            for (i = 0; i < size; i++) {
                Camera.Size s = list.get(i);
                if (maxSize >= s.width) {
                    return s;
                }
            }
            i--;
        }
        return list.get(i);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isPause = true;
        releaseCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults != null && grantResults.length > 0 && requestCode == PERMISSION_REQUEST_CODE) {
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    showPermissonDialog();
                    return;
                }
            }
            doActionCatch();
        }
    }

    public static void launch(Activity context, int requestCode, int type) {
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void launch(Activity context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivity(intent);
    }

    private void showPermissonDialog() {
        ToastUtils.showToast(CaptureActivity.this, "请先打开权限");
    }

    private void showProgressDialog(String text) {
        showProgressDialog(text, false);
    }

    private void showProgressDialog(String text, boolean cancelable) {
        try {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = new ProgressDialog(this);
                mDialog.setCanceledOnTouchOutside(cancelable);
                mDialog.setCancelable(cancelable);
                mDialog.setTitle(null);
            }
            mDialog.setMessage(text);
            if (!isFinishing())
                mDialog.show();
        } catch (Exception ex) {
        }
    }

    private boolean isShowing() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                return true;
            }
        } catch (Exception ex) {
        }

        return false;
    }

    private void dismissProgressDialog() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception ex) {
        }
    }
}

