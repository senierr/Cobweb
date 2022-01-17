package com.senierr.cobweb.component;

import android.app.Application;

import androidx.annotation.NonNull;

/**
 * 应用代理接口
 *
 * @author chunjiezhou
 */
public interface IApplicationDelegate {

    /**
     * 获取优先级
     *
     * @return 值越小，优先级越高
     */
    default int getPriority() {
        return 0;
    }

    /**
     * 初始化
     */
    void onCreate(@NonNull Application application);
}
