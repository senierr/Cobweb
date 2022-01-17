package com.senierr.demo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.senierr.cobweb.RouterRequest;
import com.senierr.cobweb.component.IInterceptor;

/**
 * @author chunjiezhou
 */
public class InterceptorB implements IInterceptor {

    @Override
    public int getPriority() {
        return -3;
    }

    @Override
    public void onProcess(@NonNull RouterRequest request) {
        Log.e("InterceptorB", "onProcess: " + request.getUri());
    }
}
