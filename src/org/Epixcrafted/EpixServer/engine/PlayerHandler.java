package org.Epixcrafted.EpixServer.engine;

import org.Epixcrafted.EpixServer.EpixServer;
import org.Epixcrafted.EpixServer.engine.player.Session;
import org.Epixcrafted.EpixServer.misc.NotSupportedOperationException;
import org.Epixcrafted.EpixServer.protocol.Packet;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class PlayerHandler extends SimpleChannelUpstreamHandler {

	private final EpixServer server;
	
    public PlayerHandler(final EpixServer server) {
		this.server = server;
	}
    
	@Override
    public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
		org.jboss.netty.channel.Channel c = e.getChannel();
        Session session =  new Session(server, c);
		PacketWorker worker = new PacketWorker(server, session);
		session.setWorker(worker);
        ctx.setAttachment(session);
        server.addChannel(c, session);
    }
	
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    	org.jboss.netty.channel.Channel c = e.getChannel();
        Session session = (Session) ctx.getAttachment();
        server.removeChannel(c, session);
        session.dispose();
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws NotSupportedOperationException {
        if (e.getChannel().isOpen()) {
            Session session = (Session) ctx.getAttachment();
            session.packetReceived((Packet) e.getMessage());
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    	try {
        	Session session = (Session) ctx.getAttachment();
        	if (session.getPlayer() != null) {
        		session.getServer().getLogger().warning("Player " + session.getPlayer().getName() + " generated an exception: " + e.getCause());
        	} else {
        		session.getServer().getLogger().warning("Caught exception in a stream: " + e.getCause());
        	}
			session.disconnect("Internal server error");
    	} catch (Exception exc) {
    		server.getLogger().severe("Caught exception: " + exc.getCause());
    		server.getLogger().severe("Line: " + exc.getStackTrace()[0]);
    	}
    	ctx.getChannel().close().addListener(ChannelFutureListener.CLOSE);
    }
}
