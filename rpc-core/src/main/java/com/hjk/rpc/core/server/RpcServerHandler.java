package com.hjk.rpc.core.server;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.management.ServiceNotFoundException;

import com.hjk.rpc.common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hjk.rpc.common.bean.RpcRequest;
import com.hjk.rpc.common.bean.RpcResponse;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

/**
 * Created by hanjk on 16/9/8.
 */
public class RpcServerHandler  extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    Map<String, Object> rpcServiceMap;

    public RpcServerHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    public void channelRead(ChannelHandlerContext ctx,
                                Object o) throws Exception {
        logger.debug("server received data:{}",o);
        RpcRequest request;
        RpcResponse response = new RpcResponse();
        try {
            request = JSON.parseObject(String.valueOf(o),RpcRequest.class);
            logger.info("server received client:[{}] data:{}",ctx.channel().remoteAddress(),o);
        }catch (Exception e){
            logger.error("format request object is fail",e);
            response.setExp(new RpcException(e));
            ctx.writeAndFlush(JSON.toJSONString(response)).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        response.setRequestId(request.getRequestId());
        try {
            Object serviceBean = rpcServiceMap.get(request.getInterfac());
            if(serviceBean == null){
                throw new ServiceNotFoundException("service:"+request.getInterfac()+" not found!");
            }
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = transParTypes2Class(request.getParameterTypes());
            Object[] parameters = request.getParameters();

            FastClass serviceFastClass = FastClass.create(serviceClass);
            FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
            Object result = serviceFastMethod.invoke(serviceBean, parameters);
            response.setResult(result);
        } catch (Throwable e) {
            logger.error("server invoke is error!",e);
            if(e instanceof InvocationTargetException){
                response.setExp(new RpcException(((InvocationTargetException)e).getTargetException()));
            }else{
                response.setExp(new RpcException(e));
            }
        }
        String responseStr = JSON.toJSONString(response);
        logger.debug("server return data:{}",responseStr);
        logger.info("server return client  data:{}",responseStr);
        // 写入 RPC 响应对象并自动关闭连接
        ctx.writeAndFlush(responseStr).addListener(ChannelFutureListener.CLOSE);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("RpcServerHandler caught exception", cause);
        ctx.close();
    }

    public Class<?>[] transParTypes2Class(String[] parameterTypes) throws ClassNotFoundException {
        Class<?>[] clazzs = new Class<?>[parameterTypes.length];
        for (int i=0;i<parameterTypes.length;i++){
            clazzs[i] = Class.forName(parameterTypes[i]);
        }
        return clazzs;
    }
}
