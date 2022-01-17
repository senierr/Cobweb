## Cobweb

### 功能支持

* 应用初始化代理
* 设置应用初始化代理优先级
* 多种Activity跳转
* 获取Intent
* 获取Fragment
* 获取自定义View
* 同步获取数据
* 异步数据观察
* 自定义请求处理
* 设置路由优先级
* 设置单个请求降级处理
* 设置全局降级处理
* 设置降级处理优先级
* 设置全局拦截器
* 设置全局拦截器优先级

### 架包导入

#### 主工程

```groovy
// 1. 应用Maven仓库
repositories {
    mavenCentral()
}

// 2. 导入AGP插件
dependencies {
    classpath 'io.github.senirr.cobweb:register:1.0.0-SNAPSHOT'
}

// 3. 引用插件
plugins {
    id 'cobweb-register'
}
```

#### 子Module

```groovy
// 导入架包
dependencies {
    implementation 'io.github.senirr:cobweb:1.0.0-SNAPSHOT'
}
```

### 应用初始化代理

新建一个类，继承`IApplicationDelegate`。

```java
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
```

### Activity跳转

1. 提供方实现`IRouter#onStartActivity`
```
public interface IRouter {
    @Nullable
    default Class<? extends Activity> onStartActivity(@NonNull RouterRequest request) {
        return null;
    }
}

```
2. 使用方请求
```
Cobweb.with(uri)
    .build()
    .startActivity(this)    // 直接跳转
    .startActivityForResult(this, requestode) // 带回调跳转
    .obtainIntent(this)     // 获取请求Intent

```

### Fragment获取

1. 提供方实现`IRouter#onObtainFragment`
```
public interface IRouter {
    @Nullable
    default Fragment onObtainFragment(@NonNull RouterRequest request) {
        return null;
    }
}

```
2. 使用方请求
```
Cobweb.with(uri)
    .build()
    .obtainFragment()
```

### View获取

1. 提供方实现`IRouter#onObtainFragment`
```
public interface IRouter {
    @Nullable
    default View onObtainView(@NonNull RouterRequest request) {
        return null;
    }
}

```
2. 使用方请求
```
Cobweb.with(uri)
    .build()
    .obtainView()
```

### 数据获取

1. 提供方实现`IRouter#onFetchData/onObserveData`
```
public interface IRouter {
    @Nullable
    default Object onFetchData(@NonNull RouterRequest request) {
        return null;
    }
    
    default void onObserveData(@NonNull RouterRequest request, @NonNull DataObserver observer, boolean register) {
    }
}

```
2. 拉取数据
```
Cobweb.with(uri)
    .build()
    .fetchData()
```
3. 观察监听数据
```
Cobweb.with(uri)
    .build()
    .observeData(dataObserver, true)
```

### 降级处理

新建一个类，继承`IDegrade`，并实现需要处理的业务。

```java
public interface IDegrade {
    /**
     * 获取优先级
     *
     * @return 值越小，优先级越高
     */
    default int getPriority() {
        return 0;
    }

    /**
     * 处理路由请求
     */
    boolean onProcess(@NonNull RouterRequest request);
}
```

### 拦截器

新建一个类，继承`IInterceptor`，并实现需要处理的业务。

```java
public interface IInterceptor {
    /**
     * 获取优先级
     *
     * @return 值越小，优先级越高
     */
    default int getPriority() {
        return 0;
    }

    /**
     * 处理路由请求
     */
    void onProcess(@NonNull RouterRequest request);
}
```

### 动态注册

`Cobweb`所有的组件都是自动注册的，当然你也可以手动注册和注销，详见**Cobweb#**。

```
/**
 * 注册路由
 */
public static void registerRouter(IRouter router)

/**
 * 注销路由
 */
public static void unregisterRouter(IRouter router)

/**
 * 注册全局降级处理
 */
public static void registerDegrade(IDegrade degrade)

/**
 * 注销全局降级处理
 */
public static void unregisterDegrade(IDegrade degrade)

/**
 * 注册路由
 */
public static void registerInterceptor(IInterceptor interceptor)

/**
 * 注销路由
 */
public static void unregisterInterceptor(IInterceptor interceptor)
```