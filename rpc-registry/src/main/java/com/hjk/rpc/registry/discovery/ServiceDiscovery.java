package com.hjk.rpc.registry.discovery;

import com.hjk.rpc.common.bean.ServiceObject;

/**
 * 发现服务
 * Created by hanjk on 16/9/7.
 */
public interface ServiceDiscovery {

    /**
     *
     * 根据服务名称,获取服务ip列表
     */
    String discovery(ServiceObject serviceObject);

}
