package com.hjk.rpc.sample.client;

import com.hjk.rpc.sample.api.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by dengd on 2016/9/10.
 */
@Component
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    @Resource(name = "user")
    Transport transport;

    public String getName(){
        return transport.getName();
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-rpc.xml");
        String[] beanNames = ctx.getBeanNamesForType(Transport.class);
        String[] beanNamess = ctx.getBeanDefinitionNames();
        final Transport transport = (Transport) ctx.getBean("user");

        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.HOUR,1);//把日期往后增加一天.整数往后推,负数往前移动
        for (int i=0;i<10;i++){
            new Thread(){
                @Override
                public void run() {
                    int i = 0;
                    while (calendar.getTimeInMillis() > System.currentTimeMillis()){
                        try {
                            transport.start("hanjiankai");
                            i++;
                            this.sleep(50);
                        }catch (Exception e){}
                    }
                    logger.error(i+"");
                }
            }.start();
        }
    }
}
