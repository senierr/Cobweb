package com.senierr.demo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.senierr.cobweb.Cobweb;
import com.senierr.cobweb.DataObserver;
import com.senierr.cobweb.RouterRequest;
import com.senierr.cobweb.component.IRouter;

/**
 * @author chunjiezhou
 */
public class Router implements IRouter {

    @Override
    public void onCreate(@NonNull Context context) {

    }

    @Override
    public void onCall(@NonNull RouterRequest request) {

    }

    @Nullable
    @Override
    public Fragment onObtainFragment(@NonNull RouterRequest request) {
        return null;
    }

    @Override
    public boolean onDispatch(@NonNull Uri uri) {
        if (uri.toString().equals("/test")) {
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Nullable
    @Override
    public Class<? extends Activity> onStartActivity(@NonNull RouterRequest request) {
        if (request.getUri().toString().equals("/test")) {
            return SecondActivity.class;
        }

        return null;
    }

    @Nullable
    @Override
    public View onObtainView(@NonNull RouterRequest request) {
        return new TextView(Cobweb.getContext());
    }

    @Nullable
    @Override
    public Object onFetchData(@NonNull RouterRequest request) {
        return "testData";
    }

    @Override
    public void onObserveData(@NonNull RouterRequest request, @NonNull DataObserver observer, boolean register) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                observer.onChanged("dddddd");
            }
        }).start();
    }
}
