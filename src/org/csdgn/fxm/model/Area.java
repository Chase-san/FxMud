package org.csdgn.fxm.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.csdgn.fxm.Config;
import org.csdgn.util.IOUtils;

import com.google.gson.Gson;

public class Area {
	public transient HashMap<UUID, Room> roomsUUID;
	public ArrayList<Room> rooms;
	public String name;
	
	public Area() {
		rooms = new ArrayList<Room>();
		roomsUUID = new HashMap<UUID, Room>();
	}
	
	public void addRoom(Room room) {
		roomsUUID.put(room.uuid, room);
		rooms.add(room);
	}
	
	public Room getRoomByUUID(UUID uuid) {
		return roomsUUID.get(uuid);
	}
	
	public static Area load(File file) {
		Area area = new Gson().fromJson(IOUtils.getFileContents(file), Area.class);
		for(Room r : area.rooms) {
			area.roomsUUID.put(r.uuid, r);
		}
		return area;
	}
	
	public static File[] getAreaList() {
		return new File(Config.FOLDER_WORLD).listFiles();
	}
}
