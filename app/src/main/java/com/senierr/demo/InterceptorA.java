package com.senierr.demo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.senierr.cobweb.RouterRequest;
import com.senierr.cobweb.component.IInterceptor;

/**
 * @author chunjiezhou
 */
public class InterceptorA implements IInterceptor {

    @Override
    public int getPriority() {
        return -10;
    }

    @Override
    public void onProcess(@NonNull RouterRequest request) {
        Log.e("InterceptorA", "onProcess: " + request.getUri());
    }
}
