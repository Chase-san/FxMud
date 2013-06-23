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
	/** The name of this exit, used in the go statement to travel along it. E.g. "go north" where this is named "north" */
	public String name;
	
	/** Custom exit message, %1$s = character name, %2$s = exit name. E.g. "%1$s walks through a clearing to the %2$s." */
	public String exitMsg;
	/** Custom enter message, %1$s = character name, %2$s = exit name. E.g. "%1$s arrives from a clearing to the %2$s." */
	public String enterMsg;
	/** Custom exit message for first person, %s = exit name. E.g. "You walk %s." */
	public String travelMsg;
	
	/** UUID of target room */
	public UUID roomUUID;
	/** UUID of matching exit in target room. */
	public UUID exitUUID;
	
	public Exit(String name, Room targetRoom) {
		uuid = UUID.randomUUID();
		this.name = name;
		this.roomUUID = targetRoom.uuid;
		this.room = targetRoom;
		travelMsg = null;
		enterMsg = null;
		exitMsg = null;
	}
	
	public Room getRoom() {
		if(room == null) {
			//lazy initialize
			room = World.instance.getRoomByUUID(roomUUID);			
			//TODO if target is still null, throw error?
		}
		return room;
	}
	
	public static Exit[] createLinkedExits(String name1, Room room1, String name2, Room room2) {
		Exit[] exits = new Exit[2];
		exits[0] = new Exit(name1, room2);
		exits[1] = new Exit(name2, room1);

		exits[0].exitUUID = exits[1].uuid;
		exits[1].exitUUID = exits[0].uuid;
		
		room1.exits.add(exits[0]);
		room2.exits.add(exits[1]);
		
		return exits;
	}
}
