package org.weex.plugin.WXLottie;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.URIAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * Created by liyazhou on 2017/12/19.
 */

public class WXLottie extends WXComponent {
    private static final String TAG = "WXLottie";

    private static final String PROP_LOOP = "loop";

    private static final int CODE_DOWNLOAD_JSON = 0x01;

    private LottieAnimationView animationView = null;

    private boolean isLoop = false;

    private String jsonSrc = null;

    private JSONObject lottieJson = null;

    private byte[] jsonByte = null;

    private Handler mHandler = null;


    public WXLottie(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
    }

    @Override
    protected View initComponentHostView(@NonNull Context context) {
        animationView = new LottieAnimationView(context);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CODE_DOWNLOAD_JSON:
                        updateLottieJson();
                        break;
                }
                super.handleMessage(msg);
            }
        };


        return animationView;
    }

    @Override
    protected boolean setProperty(String key, Object param) {
        switch (key) {
            case Constants.Name.SRC:
                this.jsonSrc = WXUtils.getString(param, "");
                setJsonSrc(this.jsonSrc);
                break;
        }
        return super.setProperty(key, param);
    }


    private void updateLottieJson() {
        if (jsonByte != null) {
            try {
                JSONObject object = new JSONObject(new String(jsonByte));
                this.lottieJson = object;
                if (this.animationView != null) {
                    this.animationView.setAnimation(this.lottieJson);
                    this.animationView.playAnimation();
                }
                jsonByte = null;
            } catch (JSONException e) {
                WXLogUtils.e(TAG, "update lottie json failed" + e.toString());
            }
        }
    }

    @WXComponentProp(name="src")
    private void setJsonSrc(String jsonSrc) {
        WXLogUtils.e(TAG, "set src:" + jsonSrc);
        if (!TextUtils.isEmpty(jsonSrc)) {
            IWXHttpAdapter adapter = WXSDKEngine.getIWXHttpAdapter();
            Uri rewrited = this.getInstance().rewriteUri(Uri.parse(jsonSrc), URIAdapter.BUNDLE);

            if (Constants.Scheme.LOCAL.equals(rewrited.getScheme()) || Constants.Scheme.FILE.equals(rewrited.getScheme())) {
                loadLocalLottieJson(rewrited);
            } else if (Constants.Scheme.HTTP.equals(rewrited.getScheme()) || Constants.Scheme.HTTPS.equals(rewrited.getScheme())){
                WXRequest request = new WXRequest();
                request.url = jsonSrc;
                request.method = "GET";
                request.url = rewrited.toString();

                adapter.sendRequest(request, new WXLottieDownloadHttpListener());
            }

        }
    }

    @WXComponentProp(name="loop")
    public void setJsonLoop(boolean looped) {
        WXLogUtils.e(TAG, "isLooped:" + looped);
        if (this.animationView != null) {
            this.animationView.loop(looped);
        }
    }

    @WXComponentProp(name="speed")
    public void setSpeed(float speed) {
        WXLogUtils.e(TAG, "set speed:" + speed);
        if (this.animationView != null) {
            this.animationView.setSpeed(speed);
        }
    }

    @JSMethod
    public void play() {
        if (this.animationView != null && ViewCompat.isAttachedToWindow(this.animationView)) {
            this.animationView.playAnimation();
        }
    }

    @JSMethod
    public void pause() {
        if (this.animationView != null && ViewCompat.isAttachedToWindow(this.animationView)) {
            this.animationView.pauseAnimation();
        }
    }

    @JSMethod
    public void reset() {
        if (this.animationView != null && ViewCompat.isAttachedToWindow(this.animationView)) {
            this.animationView.cancelAnimation();
            this.animationView.setProgress(0);
        }
    }

    private void loadLocalLottieJson(Uri uri) {
        if (uri != null) {
            if (uri.getScheme().equals(Constants.Scheme.LOCAL)) {
                try {
                    InputStream is = getInstance().getContext().getAssets().open(uri.getPath().substring(1));
                    byte[] buffer = new byte[is.available()];
                    is.read(buffer);
                    is.close();
                    this.jsonByte = buffer;

                    onLottieJsonUpdated();
                } catch (IOException e) {
                    WXLogUtils.d(TAG, e.toString());
                }
            } else if (uri.getScheme().equals(Constants.Scheme.FILE)) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    try {
                        FileInputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + uri.getPath());
                        byte[] buffer = new byte[inputStream.available()];
                        inputStream.read(buffer);
                        this.jsonByte = buffer;
                        inputStream.close();

                        onLottieJsonUpdated();
                    } catch (FileNotFoundException e) {
                        WXLogUtils.e(TAG, e.toString());
                    } catch (IOException e) {
                        WXLogUtils.e(TAG, e.toString());
                    }
                }
            }
        }
    }


    private void onLottieJsonUpdated() {
        Message msg = mHandler.obtainMessage();
        msg.what = CODE_DOWNLOAD_JSON;
        mHandler.removeMessages(CODE_DOWNLOAD_JSON);
        mHandler.sendMessage(msg);
    }


    class WXLottieDownloadHttpListener implements IWXHttpAdapter.OnHttpListener {

        @Override
        public void onHttpStart() {

        }

        @Override
        public void onHeadersReceived(int statusCode, Map<String, List<String>> headers) {

        }

        @Override
        public void onHttpUploadProgress(int uploadProgress) {

        }

        @Override
        public void onHttpResponseProgress(int loadedLength) {

        }

        @Override
        public void onHttpFinish(WXResponse response) {
            if (response != null) {
                if (response.errorCode != null && response.errorCode.equals("-1")) {
                    WXLogUtils.e(TAG, "get json failed" + response.errorMsg);
                    return;
                }

                int errorCode = 200;
                if (response.errorCode != null) {
                    errorCode = Integer.getInteger(response.errorCode);
                }
                if (errorCode >= 200 && errorCode < 300) {
                    jsonByte = response.originalData;
                    onLottieJsonUpdated();
                }
            }
        }
    }


}
