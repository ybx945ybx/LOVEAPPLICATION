package kyf.loveapplication.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import kyf.loveapplication.R;
import kyf.loveapplication.data.event.LoginStateChangeEvent;
import kyf.loveapplication.utils.UserManager;

/**
 * 通用H5容器
 * Created by a55 on 2017/11/7.
 */

public class WebFragment extends BaseFragment {

    private static final String AppModel = "AppModel";
    private String      url;
    private WebView     webView;
    private ProgressBar progressBar;

    public static WebFragment initFragment(String url) {
        WebFragment fragment = new WebFragment();
        Bundle      bundle   = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_layout, container, false);
        webView = findViewById(view, R.id.webview);
        // 调试用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        // 初始化设置
        // Init WebView Setting
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true); //显示放大缩小 controler
        webSettings.setSupportZoom(false); //可以缩放
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);//默认缩放模式
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 使用硬件加速
        webSettings.setBlockNetworkImage(true);
        // 支持js打开一个窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置编码
        webSettings.setDefaultTextEncodingName("utf-8");
        // 支持js
        webSettings.setJavaScriptEnabled(true);
        //加载网络上的图片资源
        webSettings.setBlockNetworkImage(false);
        // 设置useragent
        //        String ua = webView.getSettings().getUserAgentString();
        //        webSettings.setUserAgentString(ua + "@@@zg_Android");
        //        webSettings.setUserAgentString(ua + "@@@zg_Android" + "___" + BuildConfig.VERSION_NAME);

        // 设置 Cookie
        setCookie();

        // 设置js交互
        webView.addJavascriptInterface(new JSHook(new Gson()), AppModel);

        // 获取HTML Title
        WebChromeClient wvcc = new WebChromeClient() {

        };
        webView.setWebChromeClient(wvcc);

        // 设置 WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newUrl) {
                // 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                url = processWebUrl(newUrl);
                Logger.d(url);
                view.loadUrl(url);
                return true;
            }

        });

        // 加载网页
        if (url == null || TextUtils.isEmpty(url)) {
            // 加载失败的页面           small_icon = ERR_URL;
        }

        url = processWebUrl(url);
        Logger.d(url);

        webView.loadUrl(url);

        //        EventBus.getDefault().register(this);

        return view;
    }

    public String processWebUrl(String webUrl) {
        if (!webUrl.contains("?")) {
            webUrl += "?";
        } else {
            webUrl += "&";
        }
        if (UserManager.getInstance().isLogin()) {
            webUrl += "userId=" + UserManager.getInstance().getUserId() + "&platform=android";
        } else {
            webUrl += "platform=android";
        }
        //        if (!webUrl.contains("?")) {
        //            webUrl += "?token=";
        //        } else {
        //            webUrl += "&token=";
        //        }
        //        webUrl += UserManager.getInstance().getHtToken() + "&platform=android&fromapp=1";
        return webUrl;
    }

    public class JSHook<T> {

        private Gson gson;

        public JSHook(Gson gson) {
            this.gson = gson;
        }

        @JavascriptInterface
        public void javaMethod(String p) {
            Log.d("JAVAMETHOD", "JSHook.JavaMethod() called! + " + p);
        }

        @JavascriptInterface
        public void showAndroid() {
            final String info = "来自手机内的内容！！！";
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:show('" + info + "')");
                }
            });
        }

        @JavascriptInterface
        public void postMessage(String param) {

            if (gson == null) {
                return;
            }

            try {
                //                BaseJSObject jsobj = gson.fromJson(param, BaseJSObject.class);
                //                switch (jsobj.type) {
                //
                //                }

            } catch (Exception ex) {
            }
        }
    }

    private void setCookie() {
        // 设置 Cookie
        //        String dtkCookie = "dtk=" + HaiUtils.getDeviceToken();
        //        String tkCookie  = "tk=" + HaiUtils.getUserToken();
        //        String _tkCookie = "_tk=" + HaiUtils.getUserToken();
        //        String uidCookie = "uid=" + HaiUtils.getUserId();
        //
        //        CookieSyncManager.createInstance(this);
        //        CookieManager cookieManager = CookieManager.getInstance();
        //        if (cookieManager != null) {
        //            cookieManager.setAcceptCookie(true);
        //            cookieManager.removeSessionCookie();
        //            cookieManager.removeAllCookie();
        //            cookieManager.setCookie(".55haitao.com", dtkCookie);
        //            cookieManager.setCookie(".55haitao.com", tkCookie);
        //            cookieManager.setCookie(".55haitao.com", _tkCookie);
        //            cookieManager.setCookie(".55haitao.com", uidCookie);
        //
        //        }
        //
        //        if (Build.VERSION.SDK_INT < 21) {
        //            CookieSyncManager.getInstance().sync();
        //        } else {
        //            CookieManager.getInstance().flush();
        //        }
    }

    @Subscribe
    public void onLoginStateChange(LoginStateChangeEvent loginStateChangeEvent) {
        url = processWebUrl(url);
        Logger.d(url);
        webView.loadUrl(url);
    }
}
