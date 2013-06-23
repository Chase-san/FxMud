/**
 * Copyright (c) 2011-2013 Robert Maupin
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 *    1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 
 *    2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 
 *    3. This notice may not be removed or altered from any source
 *    distribution.
 */
package org.csdgn.fxm.net;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;
import io.netty.util.AttributeKey;


/**
 * Handles a server-side channel.
 */
@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    
    //Change this to Session
    private static final AttributeKey<Session> SESSION =
            new AttributeKey<Session>("session");
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Session session = new Session(ctx.channel());
        ctx.attr(SESSION).set(session);
        System.out.println("New connection from " + ctx.channel().remoteAddress() + ".");
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	Session session = ctx.attr(SESSION).get();
    	if(!session.disconnected)
    		session.disconnect();
    	ctx.attr(SESSION).remove();
    	System.out.println(session.username + ": " + ctx.channel().remoteAddress() + " disconnected.");
    }

    @Override
	public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> msgs) throws Exception {
    	for(Object obj : msgs)
    		ctx.attr(SESSION).get().received((String)obj);
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	System.err.println("Unexpected exception from downstream.");
    	cause.printStackTrace(System.err);
        ctx.close();
    }
}