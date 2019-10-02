package com.etao.mobile.websocket;

import com.etao.mobile.client.ClientMap;
import com.etao.mobile.client.SingleMessager;
import com.etao.mobile.glutton.GlutonChatMap;
import com.etao.mobile.glutton.MapInfo;
import net.sf.json.JSON;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Timer;
import java.util.TimerTask;

public class STimer extends TimerTask {


    @Override
    public void run() {

       // System.out.println("hello world");
     //   handler.context.

        try {
           // Object ob = MapInfo.insRandomPoint();
            for (WebSocketServerHandler handler: ClientMap.handlerMap.values()
            ) {
                //SingleMessager.send(handler,100,201,ob);

                SingleMessager.send(handler,100,203, GlutonChatMap.getAllSnackPosition());
            }

        }catch (Exception es){

            System.out.println("TimerException..................");
        }




    }

    public static void exacute() {

        Timer timer = new Timer("我的定时器");           // 创建一个定时器
        STimer myTimerTask = new STimer();
        timer.schedule(myTimerTask, 100, 5);    //10秒后执行，周期为2秒
      //  handler.chan
    }
}
