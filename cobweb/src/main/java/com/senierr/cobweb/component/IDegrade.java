package com.senierr.cobweb.component;

import androidx.annotation.NonNull;

import com.senierr.cobweb.RouterRequest;

/**
 * 降级处理接口
 *
 * @author chunjiezhou
 */
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
