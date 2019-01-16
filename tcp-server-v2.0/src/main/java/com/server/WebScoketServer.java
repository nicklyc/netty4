package com.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.websocket.decoder.HttpDecoder;
import com.websocket.decoder.HttpEncoder;
import com.websocket.handler.WebsocketHandler;
/**
 * WebSocket 服务
 * @author Administrator
 *@time  2019-01-04
 */
@Component
public class WebScoketServer {

	private final static Logger logger = LoggerFactory.getLogger(WebScoketServer.class);	
	
	@Autowired
	private ServerStarter serverStarter;
    private static final int portNumber = 10087;
	public void start(String servername) {
		ChannelInitializer initializer =new ChannelInitializer<Channel>() {
			protected void initChannel(Channel ch) throws Exception {
				   ChannelPipeline pipeline = ch.pipeline();
				    pipeline.addLast("http-decoder",new HttpDecoder());
			        pipeline.addLast("http-encoder",new HttpEncoder());
			        pipeline.addLast(new WebsocketHandler());
			}
		};
		try {
			serverStarter.startServer(servername,portNumber, initializer );
		} catch (InterruptedException e) {
			logger.info("Websocket 服务启动失败 "+e.toString());
		}
		
	}

}
