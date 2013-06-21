package org.csdgn.fxm.cmd.impl;

import org.csdgn.fxm.cmd.Command;
import org.csdgn.fxm.net.Session;

/**
 * For those times when you don't want them to accidently use a command.
 * @author Chase
 */
public class TypeFullCommand implements Command {

	@Override
	public void execute(Session session, String input) {
		session.writeLn("Please type the full command.");
	}
}
