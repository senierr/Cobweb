package com.senierr.cobweb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.senierr.cobweb.component.IDegrade;
import com.senierr.cobweb.component.IInterceptor;
import com.senierr.cobweb.component.IRouter;

import java.io.Serializable;

/**
 * @author chunjiezhou
 */
public class RouterRequest {

    private static final String TAG = "RouterRequest";

    private @NonNull final Uri uri;
    private @NonNull final Bundle bundle;
    private @Nullable final IDegrade degrade;

    RouterRequest(@NonNull Uri uri, @NonNull Bundle bundle, @Nullable IDegrade degrade) {
        this.uri = uri;
        this.bundle = bundle;
        this.degrade = degrade;
    }

    /**
     * 生成新构建器
     */
    @NonNull
    public Builder newBuilder() {
        return new Builder(uri)
                .putAll(bundle)
                .degrade(degrade);
    }

    /**
     * 执行请求
     */
    public void call() {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return;
        targetRouter.onCall(this);
    }

    /**
     * 发起路由
     */
    public void startActivity(@NonNull Context context) {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return;
        Class<? extends Activity> targetActivityClass = targetRouter.onStartActivity(this);
        if (targetActivityClass == null) {
            Log.d(TAG, "start $uri, but targetActivityClass is null!");
            return;
        }
        Intent intent = new Intent(context, targetActivityClass);
        intent.putExtras(bundle);
        if (!(context instanceof Activity)) {
            Log.d(TAG, "instanceof Activity: false, set isNewTask for true.");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 发起路由
     */
    public void startActivityForResult(@NonNull Activity activity, int requestCode) {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return;
        Class<? extends Activity> targetActivityClass = targetRouter.onStartActivity(this);
        if (targetActivityClass == null) {
            Log.d(TAG, "start $uri, but targetActivityClass is null!");
            return;
        }
        Intent intent = new Intent(activity, targetActivityClass);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 发起路由
     */
    public void startActivityForResult(@NonNull Fragment fragment, int requestCode) {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return;
        Class<? extends Activity> targetActivityClass = targetRouter.onStartActivity(this);
        if (targetActivityClass == null) {
            Log.d(TAG, "start $uri, but targetActivityClass is null!");
            return;
        }
        Intent intent = new Intent(fragment.getContext(), targetActivityClass);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取意图
     */
    @Nullable
    public Intent obtainIntent(@NonNull Context context) {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return null;
        Class<? extends Activity> targetActivityClass = targetRouter.onStartActivity(this);
        if (targetActivityClass == null) {
            Log.d(TAG, "start $uri, but targetActivityClass is null!");
            return null;
        }
        Intent intent = new Intent(context, targetActivityClass);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * 获取Fragment
     */
    @Nullable
    public Fragment obtainFragment() {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return null;
        Fragment targetFragment = targetRouter.onObtainFragment(this);
        if (targetFragment == null) return null;
        targetFragment.setArguments(bundle);
        return targetFragment;
    }

    /**
     * 获取View
     */
    @Nullable
    public View obtainView() {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return null;
        return targetRouter.onObtainView(this);
    }

    /**
     * 拉取数据
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T fetchData() {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return null;
        try {
            return (T) targetRouter.onFetchData(this);
        } catch (Exception e) {
            Log.w(TAG, "onFetchData-error: " + Log.getStackTraceString(e));
        }
        return null;
    }

    /**
     * 观察监听数据
     */
    public void observeData(DataObserver observer, boolean register) {
        IRouter targetRouter = findTargetRouter();
        if (targetRouter == null) return;
        targetRouter.onObserveData(this, observer, register);
    }

    /**
     * 检查请求状态
     */
    @Nullable
    private IRouter findTargetRouter() {
        // 查找待处理路由
        IRouter targetRouter = null;
        for (IRouter router : Cobweb.routers) {
            if (router.onDispatch(uri)) {
                targetRouter = router;
                break;
            }
        }
        // 拦截器
        for (IInterceptor interceptor : Cobweb.interceptors) {
            interceptor.onProcess(this);
        }
        if (targetRouter == null) {
            // 降级处理
            if (degrade != null) {
                if (degrade.onProcess(this)) return null;
            }
            // 全局降级处理
            for (IDegrade iDegrade : Cobweb.degrades) {
                if (iDegrade.onProcess(this)) return null;
            }
        }
        return targetRouter;
    }

    @NonNull
    public Uri getUri() {
        return uri;
    }

    @NonNull
    public Bundle getBundle() {
        return bundle;
    }

    @Nullable
    public IDegrade getDegrade() {
        return degrade;
    }

    public static class Builder {

        private @NonNull final Uri uri;
        private @NonNull final Bundle bundle = new Bundle();
        private @Nullable IDegrade degrade;

        Builder(@NonNull Uri uri) {
            this.uri = uri;
        }

        /**
         * 附加数据
         */
        public Builder putString(String key, String value) {
            bundle.putString(key, value);
            return this;
        }

        /**
         * 附加数据
         */
        public Builder putBoolean(String key, boolean value) {
            bundle.putBoolean(key, value);
            return this;
        }

        /**
         * 附加数据
         */
        public Builder putInt(String key, int value) {
            bundle.putInt(key, value);
            return this;
        }

        /**
         * 附加数据
         */
        public Builder putLong(String key, long value) {
            bundle.putLong(key, value);
            return this;
        }

        /**
         * 附加数据
         */
        public Builder putDouble(String key, double value) {
            bundle.putDouble(key, value);
            return this;
        }

        /**
         * 附加数据
         */
        public Builder putFloat(String key, float value) {
            bundle.putFloat(key, value);
            return this;
        }

        /**
         * 附加数据
         */
        public Builder putSerializable(String key, Serializable value) {
            bundle.putSerializable(key, value);
            return this;
        }

        /**
         * 附加数据
         */
        public Builder putParcelable(String key, Parcelable value) {
            bundle.putParcelable(key, value);
            return this;
        }

        /**
         * 附加数据
         */
        public Builder putAll(Bundle bundle) {
            this.bundle.putAll(bundle);
            return this;
        }

        /**
         * 设置降级处理
         */
        public Builder degrade(IDegrade degrade) {
            this.degrade = degrade;
            return this;
        }

        /**
         * 构建请求
         */
        public RouterRequest build() {
            return new RouterRequest(uri, bundle, degrade);
        }
    }
}
