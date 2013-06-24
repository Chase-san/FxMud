package org.csdgn.fxm.model;

import java.util.UUID;

import org.csdgn.fxm.net.Session;

public class Actor extends Thing {
	public transient Session session;
	public transient Room room;
	public UUID roomUUID;
	public Gender gender = Gender.Male;
	public String givenName = null;
	public String familyName = null;
	
	public static  enum Gender {
		Male,
		Female,
		Neuter
	}
	
	public Actor() {
		uuid = null;
		roomUUID = null;
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
	 * Writes a string to the channel.
	 * 
	 * @param message
	 *            The message.
	 */
	public synchronized void write(String message) {
		if(session != null)
			session.write(message);
	}

	/**
	 * Writes a line ending to the channel.
	 */
	public synchronized void writeLn() {
		if(session != null)
			session.writeLn();
	}
	
	/**
	 * Writes messages. Each message in the array is terminated with
	 * <code>\r\n</code>.
	 * 
	 * @param messages
	 *            An array of messages.
	 */
	public synchronized void writeLn(String ... messages) {
		if(session != null)
			session.writeLn(messages);
	}

	/**
	 * Writes an ascii string to the channel.
	 * 
	 * @param message
	 *            The message.
	 */
	public synchronized void writeRaw(String message) {
		if(session != null)
			session.writeRaw(message);
	}
}
