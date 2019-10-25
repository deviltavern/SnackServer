package com.etao.mobile.websocket;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.setContentLength;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpMethod.GET;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.etao.mobile.client.BroadCastMessage;
import com.etao.mobile.client.ClientMap;
import com.etao.mobile.glutton.GlutonChatMap;
import com.etao.mobile.op.*;
import com.sun.org.apache.bcel.internal.generic.RET;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.util.CharsetUtil;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Random;

/**
 * 
 * <P>Description: TODO(用一句话描述该文件做什么) </P>
 * @ClassName: WebSocketServerHandler 
 * @author guojw  2014年5月14日 上午11:03:01 
 * @see WebSocketServerHandler
 */
public class WebSocketServerHandler extends SimpleChannelUpstreamHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketServerHandler.class);
    public Logger log = Logger.getLogger(this.getClass());
    private static final String WEBSOCKET_PATH = "/websocket";
    public String ID;
    public String setID(){

        Random random = new Random();

        String value = "";


        for (int i =0;i<10;i++)
        {
            value += ""+Math.abs( random.nextInt()%10);

        }
        this.ID = value;
        return  this.ID;
    }
    private WebSocketServerHandshaker handshaker;
    public ChannelHandlerContext context;
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		//log.debug("进来一个channel：" + ctx.getChannel().getId());


        setID();
        context = ctx;
        ClientMap.addClient(this);
        System.out.println("有一个链接进来 ：");
       // STimer.handler = this;
    }

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		log.error("关掉一个channel：" + ctx.getChannel().getId());
		ClientMap.removeClient(this.ID);
        GlutonChatMap.removeSnack(this);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",ID);
        GlutonChatMap.removeSnack(this);
        BroadCastMessage.broadCast(100,233,jsonObject);

        System.out.println("失去连接 +"+ID);

        ctx.getChannel().close();

	}

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
       // System.out.println(msg);
        try{

            if (msg instanceof HttpRequest) {

                HttpRequest rxq = (HttpRequest) msg;
               // System.out.println(rxq);
                handleHttpRequest(ctx, rxq);
                //handleWebSocketFrame(ctx,(WebSocketFrame)msg);
            } else if (msg instanceof WebSocketFrame) {


                try{
                    WebSocketFrame rq = (WebSocketFrame)msg;
                    // System.out.println(rq.getBinaryData());
                    ChannelBuffer buffer = rq.getBinaryData();
                    byte[] req = new byte[buffer.readableBytes()];
                    buffer.readBytes(req);
                    String getValue = new String(req);
                    // System.out.println(getValue);
                    //System.out.println(getValue);
                    OPStrategy opStrategy = null;
                    JSONObject object = JSONObject.fromObject(getValue);
                     //System.out.println("JSONd对象 = "+object.toString());

                    switch (new Integer(object.get("main_code").toString())) {
                        case 0:
                            opStrategy = new OP_0(this);
                            break;

                        case 100:
                            opStrategy = new OP_100(this);
                            break;

                        case 101:
                            opStrategy = new OP_101(this);
                            break;

                        case 102:
                            opStrategy = new OP_102(this);
                            break;
                        default:
                            break;
                    }
                    if(opStrategy!= null){
                        //  System.out.println(new Integer(object.get("main_code").toString()));
                        opStrategy.doSomething(object);
                    }

                }catch (Exception es){

                    System.out.println(es);


                }

            }
        }catch (Exception fs){


            System.out.println("请求异常！");
        }

    }

    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        // Allow only GET methods.
        if (req.getMethod() != GET) {
            sendHttpResponse(ctx, req, new DefaultHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }

        // Send the demo page and favicon.ico
        if (req.getUri().equals("/")) {
            HttpResponse res = new DefaultHttpResponse(HTTP_1_1, OK);

            ChannelBuffer content = WebSocketServerIndexPage.getContent(getWebSocketLocation(req));

            res.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8");
            setContentLength(res, content.readableBytes());

            res.setContent(content);
            sendHttpResponse(ctx, req, res);
            return;
        } else if (req.getUri().equals("/favicon.ico")) {
            HttpResponse res = new DefaultHttpResponse(HTTP_1_1, NOT_FOUND);
            sendHttpResponse(ctx, req, res);
            return;
        }

        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                this.getWebSocketLocation(req), null, false);
        this.handshaker = wsFactory.newHandshaker(req);
        if (this.handshaker == null) {
            wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.getChannel());
        } else {
            try{

                this.handshaker.handshake(ctx.getChannel(), req);
                System.out.println(WebSocketServer.recipients.size());
                WebSocketServer.recipients.add(ctx.getChannel());
                System.out.println(WebSocketServer.recipients.size());
                System.out.println(ctx.getChannel().getId());
            }catch (Exception hankEx){
                System.out.println("握手失败");

            }

        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // Check for closing frame
        if (frame instanceof CloseWebSocketFrame) {
            this.handshaker.close(ctx.getChannel(), (CloseWebSocketFrame) frame);
            return;
        } else if (frame instanceof PingWebSocketFrame) {
          //  ctx.getChannel().write(new PongWebSocketFrame(frame.getBinaryData()));


            ctx.getChannel().write("hello world");


            return;
        } else if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
                    .getName()));
        }

        // Send the uppercase string back.
        String request = ((TextWebSocketFrame) frame).getText();
        logger.debug(String.format("Channel %s received %s", ctx.getChannel().getId(), request));

//        WebSocketServer.recipients.write(new TextWebSocketFrame(request.toUpperCase()));
        ctx.getChannel().write(new TextWebSocketFrame(request.toUpperCase()+"是服务器消息"));
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        // Generate an error page if response status code is not OK (200).
        if (res.getStatus().getCode() != 200) {
            res.setContent(ChannelBuffers.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
            setContentLength(res, res.getContent().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.getChannel().write(res);
        if (!isKeepAlive(req) || res.getStatus().getCode() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    private String getWebSocketLocation(HttpRequest req) {
        return "ws://" + req.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
    }
}