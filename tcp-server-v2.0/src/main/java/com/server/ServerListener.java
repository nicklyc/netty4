package com.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 服务启动监听类
 *
 * @author Administrator
 * @time 2019-01-06
 */
@Component
public class ServerListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    TCPServer TCPServer;
    @Autowired
    WebScoketServer WebScoketServer;
    private static String Websocket = "Websocket";
    private static String TCP = "TCP";
    private final static Logger logger = LoggerFactory.getLogger(ServerListener.class);

    /**
     * 随项目启动而启动
     */

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        TCPServer.start(TCP);
        if (System.getProperty("os.name").contains("Windows")) return;
        new Thread(new Runnable() {
            public void run() {
                WebScoketServer.start(Websocket);
            }
        })
                .start();
    }
}
