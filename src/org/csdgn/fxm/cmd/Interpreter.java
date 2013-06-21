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
package org.csdgn.fxm.cmd;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.csdgn.fxm.cmd.impl.Go;
import org.csdgn.fxm.cmd.impl.Look;
import org.csdgn.fxm.cmd.impl.Say;
import org.csdgn.fxm.model.Exit;
import org.csdgn.fxm.model.Room;

/**
 * A simple interpreter.
 * @author Chase
 *
 */
public class Interpreter {
	private TreeMap<String,Command> commands = new TreeMap<String,Command>();
	
	//special commands
	public final Say say;
	
	public Interpreter() {
		commands.put("look", new Look());
		commands.put("say", say = new Say());
		commands.put("go", new Go());
	}
	
	public Command getCommand(String ref) {
		return commands.get(ref);
	}
	
	public Command findCommand(String base) {
		base = base.trim().toLowerCase();
		for(Entry<String,Command> e : commands.entrySet()) {
			if(e.getKey().startsWith(base)) {
				return e.getValue();
			}
		}
		return null;
	}
	
	public static Exit findExit(String base, Room room) {
		base = base.trim().toLowerCase();
		for(Exit e : room.exits) {
			if(e.getName().startsWith(base)) {
				return e;
			}
		}
		return null;
	}
	
	
}
