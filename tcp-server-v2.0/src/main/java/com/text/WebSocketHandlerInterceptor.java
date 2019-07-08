package com.text;


import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Interceptor for WebSocket handshake requests
 *
 * @time 2019年2月28
 */
public class WebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            //Get the current HttpSession associated with this request
            HttpSession session = servletRequest.getServletRequest().getSession(false);

            if (session != null) {
                //使用Websocket_deviceId 绑定 WebSocketHandler
                String Websocket_deviceId = (String) session.getAttribute("WEBSOCKET_DEVICEID");
                if (Websocket_deviceId == null) {
                    Websocket_deviceId = "WEBSOCKET_DEFAULT_00X";
                }
                attributes.put("WEBSOCKET_DEVICEID", Websocket_deviceId);
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}