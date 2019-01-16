package com.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.websocket.bean.HttpRequstBean;
import com.websocket.bean.WebSocketFrameBean;
public class WebsocketHandler extends ChannelInboundHandlerAdapter{
//private HttpRequstHandler   httpRequstHandler=new HttpRequstHandler();
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
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}
	
	
	
}
