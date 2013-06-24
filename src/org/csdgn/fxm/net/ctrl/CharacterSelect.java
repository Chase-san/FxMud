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
package org.csdgn.fxm.net.ctrl;

import java.io.File;

import org.csdgn.fxm.net.InputHandler;
import org.csdgn.fxm.net.Session;
import org.csdgn.fxm.net.User;
import org.csdgn.fxm.model.Character;
import org.csdgn.fxm.model.World;

/**
 * 
 * @author Chase
 */
public class CharacterSelect implements InputHandler {
	private StringInput input;
	private boolean deleteMode = false;
	private int start = 0;
	private User user;
	
	public CharacterSelect(User user) {
		this.user = user;
	}
	
	@Override
	public void enter(Session session) {
		displayMenu(session);
		session.write("Option: ");
	}

	@Override
	public void received(Session session, String request) {
		if(request.length() > 0) {
			char c = request.toLowerCase().charAt(0);
			switch(c) {
			case 'l': //Redisplay
				displayMenu(session);
				break;
			case 'n': //New Character
				session.pushMessageHandler(new CharacterNew(user));
				return;
			case 'x':
				session.writeLn("","See you later.");
				session.disconnect();
				return;
			case 'd':
				deleteMode = true;
				session.pushMessageHandler(input = new StringInput("Delete which? "));
				return;
			case '<': //Previous
				if(start > 0) {
					start -= 10;
					if(start < 0)
						start = 0;
					displayMenu(session);
				}
				break;
			case '>': //Next
				if('>' == c && start + 10 < user.size()) {
					start += 10;
					displayMenu(session);
				}
				break;
			default:
				if(c >= '0' && c <= '9') {
					int index = ((int)c - 48) + start;
					if(index < user.size()) {
						session.character = Character.load(user.get(index).uuid);
						
						Character tmp = World.instance.getCharacter(session.character.uuid);
						
						if(tmp != null) {
							tmp.session.reconnect();
							session.character = tmp;
						}
						session.character.session = session;
						
						World.instance.join(session.character);
						
						if(session.character.room == null) {
							World.instance.placeCharacterInRoom(session.character);
						} else {
							session.writeLn("Reconnected!");
						}
						
						session.setMessageHandler(World.instance.gameHandler);
						return;
					}
				}
			}
		}
		
		session.write("Option: ");
	}

	@Override
	public void exit(Session session) {
	}
	
	private void displayMenu(Session session) {
		session.writeLn("--------------------------------","Character Select","");
		
		for(int i = start; i < user.size() && i < start + 10; ++i) {
			int index = i - start;
			session.writeLn(String.format("\t%d - %s", index, user.get(i).name ));
		}
		session.writeLn();
		if(start > 0)
			session.writeLn("\t< - Previous");
		if(start + 10 < user.size())
			session.writeLn("\t> - Next");
		session.writeLn("\tL - Redisplay Menu","\tD - Delete","\tN - Create New","\tX - Exit","");
	}

	@Override
	public void reenter(Session session) {
		//TODO add validation of deletion
		del: if(deleteMode) {
			String in = input.getValue();
			if(in.length() == 1) {
				int c = in.charAt(0);
				if(c >= 0 && c <= 9) {
					int index = ((int)c - 48) + start;
					if(index < user.size()) {
						//delete the character :(
						session.writeLn("Character deleted.");
						
						user.removeCharacter(user.get(index).uuid);
						
						File f = Character.getFile(user.get(index).uuid);
						if(f.exists())
							f.delete();
						break del;
					}
				}
			}
			session.writeLn("I don't know which one that is.");
		}
		deleteMode = false;
		enter(session);
	}
	
	
}
