package com.handler;

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.SessionChannle;
import com.util.HexString;
/**
 * 发送消息的通用类
 * @author Administrator
 *
 */
@Component
public class SendMessageHandler {
	private final static Logger logger = LoggerFactory.getLogger(SendMessageHandler.class);
	 @Autowired
	 private SessionChannle sessionChannle;

    /**
     * 
     * @param Mess
     * @param deviceId
     * @return
     */
	public void sendMess(byte[] Mess,String deviceId){
		  Channel channel = sessionChannle.getChannel(deviceId);
		  if(channel==null){
			  logger.error("客户端已经失去连接");
			  return;
		  }
			  this.response(channel, Mess);
	}
	
	/**
	 * 消息应答
	 * @param ctx
	 * @param mess
	 */
	public void response(Channel channel ,byte [] mess){
		try {
			channel.writeAndFlush(mess);
			logger.info("应答消息成功==["+HexString.bytesToHexString(mess)+"]");
		} catch (Exception e) {
			logger.error("应答消息失败==>["+ e.toString()+"]");
		}
	}
}