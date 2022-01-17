package com.senierr.demo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.senierr.cobweb.component.IApplicationDelegate;

/**
 * @author chunjiezhou
 */
public class ApplicationDelegate implements IApplicationDelegate {

    @Override
    public void onCreate(@NonNull Application application) {
        Log.e("Application", "onCreate");
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
