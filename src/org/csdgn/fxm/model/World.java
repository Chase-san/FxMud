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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.csdgn.fxm.Config;
import org.csdgn.fxm.net.MessageHandler;
import org.csdgn.fxm.net.msg.GameHandler;
import org.csdgn.util.IOUtils;

import com.google.gson.Gson;

public class World {
	public MessageHandler gameHandler = new GameHandler();
	public ArrayList<Player> characters = new ArrayList<Player>();
	public ArrayList<Room> rooms = new ArrayList<Room>();
	public HashMap<Integer, Room> roomsUUID = new HashMap<Integer, Room>();
	
	/**
	 * Loads all rooms from file.
	 * @throws IOException If there is a read error
	 */
	public void loadRooms() throws IOException {
		//TODO combine rooms into 'area' files that contain multiple rooms
		for(File f : new File(Config.FOLDER_WORLD).listFiles()) {
			if('r' == f.getName().charAt(0)) {
				Room r = loadRoom(f);
				rooms.add(r);
				roomsUUID.put(r.roomUUID, r);
			}
			//We will support 'a' eventually as well, for areas. Which are just lists of rooms.
		}
		
		if(rooms.size() == 0) {
			throw new RuntimeException("No rooms found! This is a problem!");
		}
	}
	
	/**
	 * Places the character in the room referenced in its roomUUID.
	 */
	public void placeCharacterInRoom(Player chara) {
		Room r = roomsUUID.get(chara.roomUUID);
		if(r == null) {
			r = roomsUUID.get(0);
			//TODO if this is still null, throw error?
		}
		chara.setRoom(r);
	}
	
	private void saveRoom(Room room) {
		IOUtils.setFileContents(Config.FOLDER_WORLD + 'r' + room.roomUUID, new Gson().toJson(room));
	}
	
	private Room loadRoom(File file) {
		return new Gson().fromJson(IOUtils.getFileContents(file), Room.class);
	}
}
