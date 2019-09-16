package com.etao.mobile.client;

import com.etao.mobile.websocket.WebSocketServerHandler;
import net.sf.json.JSONObject;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class BroadCastMessage {

    public static void broadCast(int mainCode, int subCode, Object object){
        JSONObject so = new JSONObject();
        so.put("main_code", mainCode);
        so.put("sub_code", subCode);
        so.put("message", object);
        for (WebSocketServerHandler handler : ClientMap.handlerMap.values()) {



            handler.context.getChannel().write(new TextWebSocketFrame(so.toString()));


        }
    }
}
