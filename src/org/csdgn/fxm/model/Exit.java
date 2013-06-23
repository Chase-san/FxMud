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

import java.util.UUID;

public class Exit extends Thing {
	private transient Room room;
	public String name;
	public String customExit;
	public String customEnter;
	public UUID roomUUID;
	public UUID exitUUID;
	
	public Exit(String name, Room room) {
		this.name = name;
		this.roomUUID = room.uuid;
		this.room = room;
	}
	
	public Room getRoom() {
		if(room == null) {
			//lazy initialize
			room = World.instance.getRoomByUUID(roomUUID);			
			//TODO if target is still null, throw error?
		}
		return room;
	}
}
