package com.text;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.print.attribute.standard.SheetCollate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	 @Value("${websocket.heatTime}")
   private int heatTime;
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	if(!System.getProperty("os.name").contains("Windows")) return;
		
    	registry.addHandler(webSocketHandler(), "/sockjs").addInterceptors(new WebSocketHandlerInterceptor())
       .setAllowedOrigins("*").withSockJS().setHeartbeatTime(10*1000);
		
    	registry.addHandler(webSocketHandler(),"/ws").addInterceptors(new WebSocketHandlerInterceptor()).setAllowedOrigins("*");
    	
    }
    
    @Bean
    public TextWebSocketHandler webSocketHandler(){
    
    	WebSocketHandler webSocketHandler = new WebSocketHandler();
    	Map<String, WebSocketSession> map = webSocketHandler.getOnlieUser();
    	ThreadPoolTaskScheduler scheduler=new ThreadPoolTaskScheduler();
    	scheduler.setPoolSize(2);
    	scheduler.initialize();
    	scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				 Set<String> set = map.keySet();
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
				
					 Object time = map.get(key).getAttributes().get("TIME");
					 if(time==null){
						 break;
					 }
					 if(System.currentTimeMillis()-(long)time-heatTime*1000>0){
						 try {
							map.get(key).close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					 }
				}
				
			}
		}, heatTime*500);
        return webSocketHandler;
    }

}