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

// 4. 初始化
Cobweb.initialize(getApplication())
```

#### 子Module

```groovy
// 导入架包
dependencies {
    implementation 'io.github.senirr:cobweb:1.0.0-SNAPSHOT'
}
```

### 路由分发

首先，需要指定提供方处理何种路由。通过实现`IRouter#onDispatch()`，指定处理对应的路由请求。

```java
public interface IRouter {
    /**
     * 路由分发
     *
     * @param uri 路由链接
     * @return 是否处理此路由链接
     */
    boolean onDispatch(@NonNull Uri uri);
}
```

**建议，各Module维护一套自己的路由表，方便后期导入接入文档。**

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

### 混淆

`Cobweb`无需混淆处理。如果您坚持混淆，可以添加以下混淆规则：

```
-dontwarn com.senierr.cobweb.**
-keep class com.senierr.cobweb.** { *; }
```

### License

```
Copyright 2022 senierr

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```