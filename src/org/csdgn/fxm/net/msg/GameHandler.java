package org.csdgn.fxm.net.msg;

import java.util.Arrays;

import org.csdgn.fxm.model.Exit;
import org.csdgn.fxm.model.Player;
import org.csdgn.fxm.model.Room;
import org.csdgn.fxm.net.MessageHandler;
import org.csdgn.fxm.net.Session;
import org.csdgn.util.StringUtils;

public class GameHandler implements MessageHandler {

	@Override
	public void enter(Session session) {
		//display initial room
		//session.character.room
		displayRoom(session,session.player.room);
	}

	@Override
	public void received(Session session, String request) {
		//FIXME TEMPORARY! We will replace this with a proper parser eventually
		//for now lets just use the full command >.>
		if(request.length() == 0) {
			session.writeLn("","You have to type something.");
			return;
		}
		
		String[] cmd = StringUtils.split(request, ' ');
		
		if(cmd[0].equalsIgnoreCase("look")) {
			if(cmd.length > 1) {
				//check exits
				for(Exit exit : session.player.room.exits) {
					if(cmd[1].equalsIgnoreCase(exit.getName())) {
						session.writeLn("",String.format("You peer %s.",exit.getName()));
						displayRoom(session,exit.getTarget());
						break;
					}
				}
			} else {
				displayRoom(session,session.player.room);
			}
		} else if(cmd[0].equalsIgnoreCase("say")) {
			if(cmd.length > 1) {
				say(session,request.substring(4));
			} else {
				session.writeLn("Say what?");
			}
		} else if(cmd[0].charAt(0) == '\'') {
			if(request.length() > 1) {
				say(session,request.substring(1));
			} else {
				session.writeLn("Say what?");
			}
		} else {
			//check exits
			for(Exit exit : session.player.room.exits) {
				String name = exit.getName();
				if(cmd[0].equalsIgnoreCase(name)) {
					Room room = exit.getTarget();
					
					session.player.writeLnToRoom("",String.format("%s moves %s.", session.player.givenName, name));
					session.writeLn("",String.format("You move %s.",name));
					session.player.setRoom(room);
					//TODO find some way to pair exits so we can get the nicer "X arrives from Y"
					session.player.writeLnToRoom("",String.format("%s arrives.", session.player.givenName, name));
					
					displayRoom(session,room);
					break;
				}
			}
		}
	}
	
	private void say(Session session, String msg) {
		System.out.println("Saying '" + msg + "'");
		session.writeLn("",String.format("You say '%s'",msg));
		session.player.writeLnToRoom("",String.format("%s says '%s'",session.player.givenName,msg));
	}

	@Override
	public void exit(Session session) {

	}

	@Override
	public void reenter(Session session) {

	}
	
	private void displayRoom(Session session, Room room) {
		session.writeLn("",room.name + "--",room.description);
		
		int exitCount = room.exits.size(); 
		if(exitCount > 0) {
			session.write("You see an exit ");
			for(int i=0;i<exitCount;++i) {
				if (i > 0) {
					if (i == exitCount - 1) {
						session.write("and ");
					} else {
						session.write(", ");
					}
				}
				session.write(room.exits.get(i).getName());
			}
			session.write(".");
			session.writeLn();
		}
		
		//players
		if(room.players.size() > 1) {
			for(Player p : room.players) {
				if(p == session.player)
					continue;
				session.write(String.format("%s %s is standing here.", p.givenName, p.familyName));
			}
		}
		
	}
}
