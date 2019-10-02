package com.etao.mobile.websocket;
import org.jboss.netty.buffer.ChannelBuffer;
 import org.jboss.netty.buffer.ChannelBuffers;
 import org.jboss.netty.util.CharsetUtil;

 /**
  * Generates the demo HTML page which is served at http://localhost:8080/
  * <P>Description: TODO(用一句话描述该文件做什么) </P>
  * @ClassName: WebSocketServerIndexPage 
  * @author guojw  2014年5月14日 上午11:03:10 
  * @see WebSocketServerIndexPage
  */
 public final class WebSocketServerIndexPage {

     private static final String NEWLINE = "\r\n";

     public static ChannelBuffer getContent(String webSocketLocation) {
         return ChannelBuffers
                 .copiedBuffer(
                         "<html><head><title>Web Socket Test</title></head>"
                                 + NEWLINE
                                 + "<body>"
                                 + NEWLINE
                                 + "<script type=\"text/javascript\">"
                                 + NEWLINE
                                 + "var socket;"
                                 + NEWLINE
                                 + "if (!window.WebSocket) {"
                                 + NEWLINE
                                 + "  window.WebSocket = window.MozWebSocket;"
                                 + NEWLINE
                                 + "}"
                                 + NEWLINE
                                 + "if (window.WebSocket) {"
                                 + NEWLINE
                                 + "  socket = new WebSocket(\""
                                 + webSocketLocation
                                 + "\");"
                                 + NEWLINE
                                 + "  socket.onmessage = function(event) { var ta = document.getElementById('responseText'); ta.value = ta.value + '\\n' + event.data };"
                                 + NEWLINE
                                 + "  socket.onopen = function(event) { var ta = document.getElementById('responseText'); ta.value = \"Web Socket opened!\"; };"
                                 + NEWLINE
                                 + "  socket.onclose = function(event) { var ta = document.getElementById('responseText'); ta.value = ta.value + \"Web Socket closed\"; };"
                                 + NEWLINE
                                 + "} else {"
                                 + NEWLINE
                                 + "  alert(\"Your browser does not support Web Socket.\");"
                                 + NEWLINE
                                 + "}"
                                 + NEWLINE
                                 + NEWLINE
                                 + "function send(message) {"
                                 + NEWLINE
                                 + "  if (!window.WebSocket) { return; }"
                                 + NEWLINE
                                 + "  if (socket.readyState == WebSocket.OPEN) {"
                                 + NEWLINE
                                 + "    socket.send(message);"
                                 + NEWLINE
                                 + "  } else {"
                                 + NEWLINE
                                 + "    alert(\"The socket is not open.\");"
                                 + NEWLINE
                                 + "  }"
                                 + NEWLINE
                                 + "}"
                                 + NEWLINE
                                 + "</script>"
                                 + NEWLINE
                                 + "<form onsubmit=\"return false;\">"
                                 + NEWLINE
                                 + "<input type=\"text\" name=\"message\" value=\"Hello, World!\"/>"
                                 + "<input type=\"button\" value=\"Send Web Socket Data\" onclick=\"send(this.form.message.value)\" />"
                                 + NEWLINE + "<h3>Output</h3>" + NEWLINE
                                 + "<textarea id=\"responseText\" style=\"width: 500px; height:300px;\"></textarea>"
                                 + NEWLINE + "</form>" + NEWLINE + "</body>" + NEWLINE + "</html>" + NEWLINE,
                         CharsetUtil.US_ASCII);
     }

     private WebSocketServerIndexPage() {
         // Unused
     }
 }