package com.hjk.rpc.common.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * 请求
 * Created by hanjk on 16/9/8.
 */
public class RpcRequest {

    private String requestId;
    private String interfac;
    private String methodName;
    private String[] parameterTypes;
    private Object[] parameters;

    public RpcRequest() {
    }

    public RpcRequest(String requestId, String interfac, String methodName,
                      String[] parameterTypes, Object[] parameters) {
        this.requestId = requestId;
        this.interfac = interfac;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getInterfac() {
        return interfac;
    }

    public void setInterfac(String interfac) {
        this.interfac = interfac;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", interfac='" + interfac + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
