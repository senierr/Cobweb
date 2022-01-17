package com.senierr.cobweb.component;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.senierr.cobweb.DataObserver;
import com.senierr.cobweb.RouterRequest;

/**
 * 拦截器接口
 *
 * @author chunjiezhou
 */
public interface IRouter {

    /**
     * 初始化
     */
    default void onCreate(@NonNull Context context) {}

    /**
     * 获取优先级
     *
     * @return 值越小，优先级越高
     */
    default int getPriority() {
        return 0;
    }

    /**
     * 路由分发
     *
     * @param uri 路由链接
     * @return 是否负责处理
     */
    boolean onDispatch(@NonNull Uri uri);

    /**
     * 当RouterRequest执行call()时触发
     *
     * @param request 路由请求
     */
    default void onCall(@NonNull RouterRequest request) {}

    /**
     * 当RouterRequest执行startActivity()、obtainIntent()时触发
     *
     * @param request 路由请求
     */
    @Nullable
    default Class<? extends Activity> onStartActivity(@NonNull RouterRequest request) {
        return null;
    }

    /**
     * 当RouterRequest执行obtainFragment()时触发
     *
     * @param request 路由请求
     */
    @Nullable
    default Fragment onObtainFragment(@NonNull RouterRequest request) {
        return null;
    }

    /**
     * 当RouterRequest执行obtainView()时触发
     *
     * @param request 路由请求
     */
    @Nullable
    default View onObtainView(@NonNull RouterRequest request) {
        return null;
    }

    /**
     * 当RouterRequest执行fetchData()时触发
     *
     * @param request 路由请求
     */
    @Nullable
    default Object onFetchData(@NonNull RouterRequest request) {
        return null;
    }

    /**
     * 当RouterRequest执行observeData()时触发
     *
     * @param request 路由请求
     * @param observer 数据观察者
     * @param register true为注册，false为注销
     */
    default void onObserveData(@NonNull RouterRequest request, @NonNull DataObserver observer, boolean register) {
    }
}
