package org.weex.plugin.WXLottie;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.alibaba.weex.plugin.annotation.WeexModule;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.URIAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.http.WXHttpUtil;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WeexComponent(names={"lottie"})
public class WXLottieComponent extends WXComponent
{
    private static final String TAG = WXLottieComponent.class.getSimpleName();
    private String lottieSrc = null;
    private JSONObject sourceJSONObject = null;
    public WXLottieComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
    }

    @Override
    protected View initComponentHostView(@NonNull Context context) {
        super.initComponentHostView(context);
        return new LottieAnimationView(context);
    }
    @JSMethod
    public void play() {
        LottieAnimationView lottieAnimationView = (LottieAnimationView)this.getHostView();
        if (ViewCompat.isAttachedToWindow(lottieAnimationView)) {
            lottieAnimationView.setProgress(0);
            lottieAnimationView.playAnimation();
        }
    }

    @JSMethod
    public void reset() {
        LottieAnimationView lottieAnimationView = (LottieAnimationView)this.getHostView();
        if (ViewCompat.isAttachedToWindow(lottieAnimationView)) {
            lottieAnimationView.cancelAnimation();
            lottieAnimationView.setProgress(0f);
        }
    }

    @WXComponentProp(name="sourceName")
    public void setSourceName( String name) {
        LottieAnimationView lottieAnimationView = (LottieAnimationView)this.getHostView();
        lottieAnimationView.setAnimation(name);
    }

    @WXComponentProp(name="src")
    public void setSrc(String src) {
        if (src.length() == 0 && src.equals(this.lottieSrc)) {
           return;
        }
        this.lottieSrc = src;
        IWXHttpAdapter adapter = WXSDKManager.getInstance().getIWXHttpAdapter();
        WXRequest wxRequest = new WXRequest();
        wxRequest.url = this.getInstance().rewriteUri(Uri.parse(src), URIAdapter.BUNDLE).toString();
        if (wxRequest.paramMap == null) {
            wxRequest.paramMap = new HashMap<String, String>();
        }
        adapter.sendRequest(wxRequest, new WXLottieDownloadHttpListener(src, System.currentTimeMillis()));
    }

    @WXComponentProp(name="sourceJson")
    public void setSourceJson(JSONObject json) {
        this.sourceJSONObject = json;
        LottieAnimationView lottieAnimationView = (LottieAnimationView)this.getHostView();
        try {
            lottieAnimationView.setAnimation(json);
        } catch (Exception e) {
            Log.e(TAG, "setSourceJsonError", e);
        }
    }

    @WXComponentProp(name="progress")
    public void setProgress(float progress) {
        ((LottieAnimationView)this.getHostView()).setProgress(progress);
    }

    @WXComponentProp(name="speed")
    public void setSpeed(LottieAnimationView view, float speed) {
        ((LottieAnimationView)this.getHostView()).setSpeed(speed);
    }

    @WXComponentProp(name="loop")
    public  void setLoop(LottieAnimationView view, boolean loop) {
        ((LottieAnimationView)this.getHostView()).loop(loop);
    }

    @WXComponentProp(name="imageAssetsFolder")
    public void setImageAssetsFolder(LottieAnimationView view, String imageAssetsFolder) {
        ((LottieAnimationView)this.getHostView()).setImageAssetsFolder(imageAssetsFolder);
    }

    class WXLottieDownloadHttpListener implements IWXHttpAdapter.OnHttpListener {
        private long startRequestTime;
        private String url;
        public WXLottieDownloadHttpListener(String url, long startRequestTime) {
            this.url = url;
            this.startRequestTime = startRequestTime;
        }

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
            long requestEndTime = System.currentTimeMillis();
            WXLogUtils.info(TAG + "total request time " + (requestEndTime-this.startRequestTime));
            if ("200".equals(response.statusCode)) {
                try {
                    setSourceJson(new JSONObject(new String(response.originalData)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}