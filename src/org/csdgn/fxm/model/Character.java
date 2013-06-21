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
package org.csdgn.fxm.model;

import org.csdgn.fxm.net.Session;

public class Character {
	public transient Session session;
	public transient Room room;
	public boolean isFemale = false;
	public int roomUUID = -1;
	public String givenName = null;
	public String familyName = null;
	
	/**
	 * Sets the players current room.
	 */
	public void setRoom(Room room) {
		if(this.room != null)
			this.room.characters.remove(this);
		this.roomUUID = room.roomUUID;
		this.room = room;
		this.room.characters.add(this);
	}
	
	/**
	 * Writes messages to everyone else in the room. Each message in the array is terminated with
	 * <code>\r\n</code>.
	 * 
	 * @param messages
	 *            An array of messages.
	 */
	public void writeLnToRoom(String ... messages) {
		StringBuilder buf = new StringBuilder();
		for (String message : messages) {
			buf.append(message).append("\r\n");
		}
		String msg = buf.toString();
		
		for(Character p : room.characters) {
			if(p == this)
				continue;
			p.session.write(msg);
		}
	}
}
