package com.etao.mobile.websocket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import com.etao.mobile.glutton.RandomCubeTimer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * A HTTP server which serves Web Socket requests at:
 * 
 * http://localhost:8080/websocket
 * 
 * Open your browser at http://localhost:8080/, then the demo page will be
 * loaded and a Web Socket connection will be made automatically.
 * 
 * This server illustrates support for the different web socket specification
 * versions and will work with:
 * 
 * <ul>
 * <li>Safari 5+ (draft-ietf-hybi-thewebsocketprotocol-00)
 * <li>Chrome 6-13 (draft-ietf-hybi-thewebsocketprotocol-00)
 * <li>Chrome 14+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * <li>Chrome 16+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17)
 * <li>Firefox 7+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * </ul>
 */
public class WebSocketServer {

	private final int port;

	public static ChannelGroup recipients = new DefaultChannelGroup();

	public WebSocketServer(int port) {
		this.port = port;
	}

	public void run() throws UnknownHostException {
		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors
						.newCachedThreadPool(), Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new WebSocketServerPipelineFactory());

		System.out.println(InetAddress.getLocalHost());
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(InetAddress.getByName("39.106.188.64"),port));

		System.out.println("Web socket server started at port " + port + '.');
		System.out.println("Open your browser and navigate to http://localhost:"
						+ port + '/');
	}

	public static void main(String[] args) throws UnknownHostException {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 9001;
		}


		new WebSocketServer(9001).run();

	}
}