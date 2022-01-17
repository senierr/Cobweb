package com.senierr.cobweb;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import com.senierr.cobweb.component.IApplicationDelegate;
import com.senierr.cobweb.component.IDegrade;
import com.senierr.cobweb.component.IInterceptor;
import com.senierr.cobweb.component.IRouter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 支持应用初始化代理
 * 支持设置应用初始化代理优先级
 * 支持多种Activity跳转
 * 支持获取Intent
 * 支持获取Fragment
 * 支持获取自定义View
 * 支持同步获取数据
 * 支持异步数据观察
 * 支持自定义请求处理
 * 支持设置路由优先级
 * 支持设置单个请求降级处理
 * 支持设置全局降级处理
 * 支持设置降级处理优先级
 * 支持设置全局拦截器
 * 支持设置全局拦截器优先级
 *
 * @author chunjiezhou
 */
public class Cobweb {

    private static Application application;

    public static List<IApplicationDelegate> applicationDelegates = new ArrayList<>();
    public static List<IRouter> routers = new ArrayList<>();
    public static List<IDegrade> degrades = new ArrayList<>();
    public static List<IInterceptor> interceptors = new ArrayList<>();

    /**
     * 初始化
     */
    public static void initialize(Application app) {
        application = app;
    }

    /**
     * 获取上下文环境
     */
    public static Context getContext() {
        return application.getApplicationContext();
    }

    /**
     * 注册应用代理
     */
    private static void registerApplicationDelegate(IApplicationDelegate applicationDelegate) {
        applicationDelegates.add(applicationDelegate);
        applicationDelegate.onCreate(application);
        // 优先级排序
        Collections.sort(applicationDelegates, (o1, o2) -> o1.getPriority() - o2.getPriority());
    }

    /**
     * 注册路由
     */
    public static void registerRouter(IRouter router) {
        routers.add(router);
        router.onCreate(application);
        // 优先级排序
        Collections.sort(routers, (o1, o2) -> o1.getPriority() - o2.getPriority());
    }

    /**
     * 注销路由
     */
    public static void unregisterRouter(IRouter router) {
        routers.remove(router);
    }

    /**
     * 注册全局降级处理
     */
    public static void registerDegrade(IDegrade degrade) {
        degrades.add(degrade);
        // 优先级排序
        Collections.sort(degrades, (o1, o2) -> o1.getPriority() - o2.getPriority());
    }

    /**
     * 注销全局降级处理
     */
    public static void unregisterDegrade(IDegrade degrade) {
        degrades.remove(degrade);
    }

    /**
     * 注册路由
     */
    public static void registerInterceptor(IInterceptor interceptor) {
        interceptors.add(interceptor);
        // 优先级排序
        Collections.sort(interceptors, (o1, o2) -> o1.getPriority() - o2.getPriority());
    }

    /**
     * 注销路由
     */
    public static void unregisterInterceptor(IInterceptor interceptor) {
        interceptors.remove(interceptor);
    }

    /**
     * 创建路由请求
     *
     * @param uriStr 路由地址
     * @return 路由请求
     */
    public static RouterRequest.Builder with(String uriStr) {
        return with(Uri.parse(uriStr));
    }

    /**
     * 创建路由请求
     *
     * @param uri 路由地址
     * @return 路由请求
     */
    public static RouterRequest.Builder with(Uri uri) {
        return new RouterRequest.Builder(uri);
    }
}
