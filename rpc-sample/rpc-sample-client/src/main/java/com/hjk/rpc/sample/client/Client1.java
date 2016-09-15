package com.hjk.rpc.sample.client;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.hjk.rpc.sample.api.Transport;

/**
 * Created by dengd on 2016/9/10.
 */
@Component
public class Client1 {

    @Resource(name = "user")
    Transport transport;

    public String getName(){
        return transport.getName();
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
        Transport transport = (Transport) ctx.getBean("user");
        System.out.println(transport.getName());
//        while (true){
//            try {
//                System.out.println(transport.getName());
//            }catch (Exception e){}
//
//        }
    }
}
