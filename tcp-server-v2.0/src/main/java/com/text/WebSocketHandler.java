package com.text;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class WebSocketHandler extends TextWebSocketHandler {
    private static  ConcurrentHashMap<String,WebSocketSession> map=new ConcurrentHashMap<String,WebSocketSession>();
    private static Logger logger = Logger.getLogger(WebSocketHandler.class);
    public WebSocketHandler() {
        // TODO Auto-generated constructor stub
    }

    
    /**
     * js调用方法
     */
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	session.getAttributes().put("TIME", System.currentTimeMillis());
       
    	
    	System.out.println("收到消息"+message.getPayload());
        //从消息报文中 读取到设备的id     String  Websocket_deviceId=
        //存入map.put(Websocket_deviceId,session)
        //存入session
        //session.getAttributes().put("WEBSOCKET_DEVICEID", Websocket_deviceId)
       
        
        //构造响应消息
        TextMessage textMessage = new TextMessage("响应消息==>"+message.getPayload());
        
        //响应消息
        session.sendMessage(textMessage);
        
    }
    

    
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    
		logger.info("WebSocket设备上线：" + session.getId());
		map.put(session.getId(),session);
		logger.info("session创建");
    }
    
    
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    	
    	logger.info("WebSocket设备下线：" + session.getId());
        String Websocket_deviceId= (String) session.getAttributes().get("WEBSOCKET_DEVICEID");
        logger.debug("WebSocket设备"+Websocket_deviceId+"已退出！");
       // map.remove(Websocket_deviceId);
        if(session.isOpen()){session.close();}
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
       
    	String Websocket_deviceId= (String) session.getAttributes().get("WEBSOCKET_DEVICEID");
    	map.remove(Websocket_deviceId);
    	
    	if(session.isOpen()){session.close();}
      
    }

    public boolean supportsPartialMessages() {
        return false;
    }
    
    
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String Websocket_deviceId, TextMessage message) {
    	
    	WebSocketSession session = map.get(Websocket_deviceId);
    	if(session==null){
    		 logger.debug("设备"+Websocket_deviceId+"网络状态异常！");
    		 return;
    	}
    	try {
			session.sendMessage(message);
		} catch (IOException e) {
			 logger.debug("向设备"+Websocket_deviceId+"发送消息失败！"+e.toString());
    		
		}
   
    }
    
    /**
     * 给所有在线用户发送消息
     * @param message
     */
    public synchronized void sendMessageToUsers(TextMessage message) {
       //遍历map得到每一个WebSocketSession session
    	//session.sendMessage(message);
    }
    
    

    public Map<String,WebSocketSession> getOnlieUser() {
    	 
       return map;
     }
}