package org.csdgn.fxm.cmd.impl;

import org.csdgn.fxm.cmd.Command;
import org.csdgn.fxm.net.Session;

public class Quit implements Command {
	@Override
	public void execute(Session session, String input) {
		session.character.room.characters.remove(session.character);
		session.character.writeLnToRoom(String.format("%s has left.",
				session.character.givenName));

		session.writeLn("See you later.");
		session.disconnect();
	}
}
