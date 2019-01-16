package com.handler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.SessionChannle;
public class BusinessHandler extends SimpleChannelInboundHandler<String> {
	private final static Logger logger = LoggerFactory.getLogger(BusinessHandler.class);
	private SessionChannle sessionChannle=new SessionChannle();

	/**
	 * 读取消息
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		logger.info("收到消息===>"+msg);
		
	}

	/**
	 * 异常处理
	 */
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("---捕获异常---" + cause);
		super.exceptionCaught(ctx, cause);
	}

	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelRegistered-------");
		logger.info("新建Channel id是：" + ctx.channel().id());
		logger.info("新设备上线");
		sessionChannle.creatSession(ctx);
		logger.info("session创建");
		super.channelRegistered(ctx);
	}

	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("连接断开Channel id是：" + ctx.channel().id());
		Map session = sessionChannle.getSession(ctx);
		logger.info("设备下线：下线设备id===>" + session.get("deviceId"));
		sessionChannle.destorySession(ctx);
		logger.info("session销毁");
		super.channelUnregistered(ctx);
	}
	
	/**
	 * 心跳维护
	 * @throws Exception 
	 */
	public void userEventTriggered(ChannelHandlerContext ctx, Object e) throws Exception {
		  
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

	/*
     * 
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     * 
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("RamoteAddress= : " + ctx.channel().remoteAddress() + " active========== !");
    }




}