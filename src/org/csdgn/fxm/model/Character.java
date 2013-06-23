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

import java.io.File;
import java.util.UUID;

import org.csdgn.fxm.Config;
import org.csdgn.fxm.net.Session;
import org.csdgn.util.IOUtils;

import com.google.gson.Gson;

public class Character extends Thing {
	public transient Session session;
	public transient Room room;
	public UUID roomUUID;
	public boolean isFemale = false;
	public String givenName = null;
	public String familyName = null;
	
	public Character() {
		uuid = null;
		roomUUID = null;
	}
	
	/**
	 * Saves this character back to file. Seldom use only!
	 */
	public void save() {
		String json = new Gson().toJson(this);
		IOUtils.setFileContents(Config.FOLDER_CHARACTER + uuid.toString(), json);
	}
	
	public static File getFile(UUID uuid) {
		return new File(Config.FOLDER_CHARACTER + uuid.toString());
	}
	
	public static Character load(UUID uuid) {
		File file = getFile(uuid);
		String json = IOUtils.getFileContents(file);
		Character chara = new Gson().fromJson(json,Character.class);
		return chara;
	}
	
	public void quit() {
		if(room != null) {
			World.instance.leave(this);
			writeLnToRoom(String.format("%s has left.", givenName));
			setRoom(null);
			save();
		}
	}
	
	/**
	 * Sets the players current room.
	 */
	public void setRoom(Room room) {
		if(this.room != null)
			this.room.characters.remove(this);
		this.room = room;
		if(room != null) {
			room.characters.add(this);
			this.roomUUID = room.uuid;
		}
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
