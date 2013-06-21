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

public class Exit {
	private transient Room target;
	private String name;
	private int targetUUID;
	
	public Exit(String name, Room room) {
		this.name = name;
		this.targetUUID = room.roomUUID;
		this.target = room;
	}
	
	public Room getTarget() {
		if(target == null) {
			//lazy initialize
			target = World.instance.roomsUUID.get(targetUUID);
			//TODO if target is still null, throw error?
		}
		return target;
	}

	public String getName() {
		return name;
	}

	public int getTargetUUID() {
		return targetUUID;
	}
}
