package org.csdgn.fxm.cmd.impl;

import org.csdgn.fxm.cmd.Command;
import org.csdgn.fxm.net.Session;

public class Quit implements Command {
	@Override
	public void execute(Session session, String input) {
		session.character.quit();
		session.writeLn("See you later.");
		session.disconnect();
	}
}
