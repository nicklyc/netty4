package com.server;
import io.netty.channel.ChannelFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TCP 服务
 * @author Administrator
 *@time  2019-01-04
 */
@Component
public class TCPServer  {
	private final static Logger logger = LoggerFactory.getLogger(TCPServer.class);	
	@Autowired
	private ServerInitializer tcpServerInitializer;
	@Autowired
	private ServerStarter serverStarter;
    private static final int portNumber = 10086;


	public void start(String servername) {
		try {
			  serverStarter.startServer(servername,portNumber, tcpServerInitializer);
		} catch (InterruptedException e) {
			logger.info("TCP 服务启动失败"+e.toString());
		
		}
		
	}
	    



 
	
}
