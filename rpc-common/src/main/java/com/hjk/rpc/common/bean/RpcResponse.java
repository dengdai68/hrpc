package com.hjk.rpc.common.bean;

import com.hjk.rpc.common.exception.RpcException;

/**
 * 响应
 * Created by hanjk on 16/9/8.
 */
public class RpcResponse {

    private String requestId;
    private Object result;
    private RpcException exp;

    public RpcResponse(String requestId, Object result, RpcException exp) {
        this.requestId = requestId;
        this.result = result;
        this.exp = exp;
    }

    public RpcResponse() {

    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public RpcException getExp() {
        return exp;
    }

    public void setExp(RpcException exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", result=" + result +
                ", exp=" + exp +
                '}';
    }
}
