package com.server;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.handler.BusinessHandler;
@Component
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
	private final static Logger logger = LoggerFactory.getLogger(ServerInitializer.class);
   // @Autowired
   // private  BusinessHandler businessHandler;
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 以("\n")为结尾分割的 解码器
         //此处可拓展其他符号解码器
       // pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

         // 字符串解码 和 编码
          pipeline.addLast("decoder", new StringDecoder());
          pipeline.addLast("encoder", new StringEncoder());
        // 心跳检测
    	pipeline.addLast("idle", new IdleStateHandler(90,90, 190, TimeUnit.SECONDS));
       //业务处理
		//pipeline.addLast("handler",businessHandler);
    	pipeline.addLast("handler",new BusinessHandler());
    }
}