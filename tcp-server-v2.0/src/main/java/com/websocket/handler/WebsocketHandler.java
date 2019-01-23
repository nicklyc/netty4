package com.websocket.handler;

import java.util.Map;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.websocket.bean.HttpRequstBean;
import com.websocket.bean.WebSocketFrameBean;
public class WebsocketHandler extends ChannelInboundHandlerAdapter{
	static com.server.SessionChannle  sessionChannle=new com.server.SessionChannle();
	private final static Logger logger = LoggerFactory.getLogger(WebsocketHandler.class);
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if(msg instanceof HttpRequstBean){
			HttpRequstHandler httpRequstHandler = new HttpRequstHandler();
			HttpRequstBean requstBean = (HttpRequstBean) msg;
			httpRequstHandler.handle(requstBean,ctx);
			}
		else if(msg instanceof WebSocketFrameBean){
			WebSocketFrameBean frameBean = (WebSocketFrameBean) msg;
			//chrome浏览器不支持服务器返回mask data
			//WebSocketFrameBean response = new WebSocketFrameBean((byte)1,(byte)0,(byte)0,(byte)0,(byte)1,(byte)1,null);
			WebSocketFrameBean response = new WebSocketFrameBean((byte)1,(byte)0,(byte)0,(byte)0,(byte)1,(byte)0,null);
			String received = "";
			if(frameBean.getOpcode()==1){//文本
				received = new String(frameBean.getPayloadData());
				logger.info("Websocket收到的消息==>"+received);
			}
			
			
			String responseString="";//要响应的消息
			//对消息经处理：
			Map session = sessionChannle.getSession(ctx);
			//session.put(key, value);
			
			responseString="服务器原文返回==>"+received;//测试原文返回
			response.setPayloadData(responseString.getBytes());
			ctx.writeAndFlush(response);//回写消息
			
			
			
			
			//=======二进制的特殊处理===========================================
//			else if(frameBean.getOpcode()==2){//二进制
//				FileOutputStream outputStream = new FileOutputStream("d:/ws.dat");
//				outputStream.write(frameBean.getPayloadData());
//				outputStream.flush();
//				outputStream.close();
//				received = "FILE SIZE:"+frameBean.getPayloadData().length;
//				response.setPayloadData(received.getBytes());
//			}
			
		}
		
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("---捕获异常---" + cause);
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelRegistered-------");
		logger.info("新建Channel id是：" + ctx.channel().id());
		logger.info("新设备上线");
		sessionChannle.creatSession(ctx);
		logger.info("session创建");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("连接断开Channel id是：" + ctx.channel().id());
		Map session = sessionChannle.getSession(ctx);
		logger.info("设备下线：下线设备id===>" + session.get("deviceId"));
		sessionChannle.destorySession(ctx);
		logger.info("session销毁");
		super.channelUnregistered(ctx);
		super.channelUnregistered(ctx);
	}


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object e)
			throws Exception {
		if (e instanceof IdleStateEvent) {
			if(((IdleStateEvent)e).state() .equals( IdleState.ALL_IDLE)){
				 ctx.channel().close();
				 sessionChannle.destorySession(ctx);
				 logger.info("连接通道关闭 Channel id是："+ctx.channel().id());
				 logger.info("session销毁");
				 
				//在这里可以维护连接，异步回调
				ChannelFuture write = ctx.channel().write("");
				write.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						 ctx.channel().close();
					}
				});
			}
		} else {
			super.userEventTriggered(ctx, e);
		}
	}
	
	
	
}
