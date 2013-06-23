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
package org.csdgn.fxm.net.ctrl;

import java.util.HashMap;

import org.csdgn.fxm.cmd.Command;
import org.csdgn.fxm.cmd.Interpreter;
import org.csdgn.fxm.net.InputHandler;
import org.csdgn.fxm.net.Session;
import org.csdgn.util.StringUtils;

public class Game implements InputHandler {
	private final Interpreter interp;
	private final HashMap<String,String> directionAlias;
	
	public Game() {
		interp = new Interpreter();
		directionAlias = new HashMap<String, String>();
		directionAlias.put("n", "go north");
		directionAlias.put("s", "go south");
		directionAlias.put("e", "go east");
		directionAlias.put("w", "go west");
		directionAlias.put("d", "go down");
		directionAlias.put("u", "go up");
	}
	
	@Override
	public void enter(Session session) {
		//display initial room
		session.character.room.displayRoomTo(session);
	}

	@Override
	public void received(Session session, String request) {
		if(request.length() == 0) {
			session.writeLn("","You have to type something.");
			return;
		}
		
		//Special!
		if('\'' == request.charAt(0)) {
			interp.say.execute(session, request);
			return;
		}
		
		{ //Direction Aliases
			String alias = directionAlias.get(request);
			if(alias != null) {
				request = alias;
			}
		}
		
		//Standard
		{
			String cmdStr = StringUtils.getBefore(request, ' ');
			if(cmdStr == null)
				cmdStr = request;
			Command cmd = interp.findCommand(cmdStr);
			if(cmd != null) {
				cmd.execute(session, request);
			} else {
				session.writeLn("I don't understand that.");
			}
		}
	}

	@Override
	public void exit(Session session) {

	}

	@Override
	public void reenter(Session session) {

	}
}
