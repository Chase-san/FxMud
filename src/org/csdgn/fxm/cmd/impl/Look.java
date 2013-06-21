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
package org.csdgn.fxm.cmd.impl;

import org.csdgn.fxm.cmd.Command;
import org.csdgn.fxm.cmd.Interpreter;
import org.csdgn.fxm.model.Exit;
import org.csdgn.fxm.net.Session;
import org.csdgn.util.StringUtils;

public class Look implements Command {
	@Override
	public void execute(Session session, String input) {
		input = StringUtils.getAfter(input, ' ');
		if(input != null) {
			//TODO add the ability to look at players
			
			//check exits
			Exit e = Interpreter.findExit(input, session.character.room);
			if(e != null) {
				session.writeLn("You peer " + e.getName());
				
				e.getTarget().displayRoomTo(session);
			} else {
				session.writeLn("You don't see that here.");
			}
		} else {
			//redisplay room
			session.character.room.displayRoomTo(session);
			return;
		}
	}
}
