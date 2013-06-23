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

import org.csdgn.telnet.TelnetHandler;
import org.csdgn.telnet.TelnetSocket;

/**
 * Handles a server-side channel.
 */
public class ServerHandler implements TelnetHandler {
	private int SESSION_KEY = 35892;
	@Override
	public void connected(TelnetSocket sock) {
		Session session = new Session(sock);
		sock.put(SESSION_KEY, session);
        System.out.println("New connection from " + sock.remoteAddress() + ".");
	}

	@Override
	public void disconnected(TelnetSocket sock) {
		Session session = (Session)sock.get(SESSION_KEY);
    	if(!session.disconnected)
    		session.disconnect();
    	sock.remove(SESSION_KEY);
    	
    	System.out.println(session.username + ": " + sock.remoteAddress() + " disconnected.");
	}

	@Override
	public void message(TelnetSocket sock, String msg) {
		Session session = (Session)sock.get(SESSION_KEY);
    	session.received(msg);
	}
}