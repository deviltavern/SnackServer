package com.etao.mobile.client;

import com.etao.mobile.websocket.WebSocketServer;
import com.etao.mobile.websocket.WebSocketServerHandler;
import net.sf.json.JSONObject;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Date;

public class SingleMessager {

    public static Date lastTime = new Date();
    public static float tempDate  = 0;
    public static void send(WebSocketServerHandler handler,int mainCode,int subCode, Object object){

        if (handler.context.getChannel().isWritable() == true){

            JSONObject so = new JSONObject();
            so.put("main_code",mainCode);
            so.put("sub_code",subCode);
            so.put("message",object);

            handler.context.getChannel().write(new TextWebSocketFrame(so.toString()));
           // tempDate = new Date().getTime() - lastTime.getTime();
            //System.out.println(tempDate);
           // lastTime = new Date();


        }



    }

    public static void sendJSONValue(WebSocketServerHandler handler, String object){

        handler.context.getChannel().write(new TextWebSocketFrame(object));



    }
}
