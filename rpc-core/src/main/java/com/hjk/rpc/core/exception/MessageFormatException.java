package com.hjk.rpc.core.exception;

/**
 * Created by dengd on 2016/9/9.
 */
public class MessageFormatException extends RpcException {
    public MessageFormatException(String message) {
        super(message);
    }

    public MessageFormatException() {
        super();
    }
}
