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
import org.csdgn.fxm.Config;
import org.csdgn.fxm.net.InputHandler;
import org.csdgn.fxm.net.Session;
import org.csdgn.fxm.net.User;
import org.csdgn.util.IOUtils;

/**
 * This class is a mess. I am so sorry. But its the best I could do at the time.
 * @author Chase
 *
 */
public class Login implements InputHandler {
	private static final int MAX_ATTEMPTS = 3;
	private static String MOTD = null;
	private static long MOTD_TIME = 0;
	
	private static enum LoginMode {
		LOGIN,
		PASSWORD,
		NEW_LOGIN,
		NEW_PASSWORD
	};
	
	/**
	 * Return true if the name is acceptable.
	 */
	private static final boolean checkUsername(String username) {
		//check if the username is valid
		if(!username.matches("[a-z][_a-z0-9]{2,}"))
			return false;
		
		//check if the username is in use.
		if(new File("db/user/" + username).exists())
			return false;
		return true;
	}
	
	/**
	 * return true if the password is acceptable.
	 */
	private static final boolean checkPassword(String password) {
		if(!password.matches("\\w[\\w\\p{Punct}]{4,}"))
			return false;
		return true;
	}
	
	private User user;
	private LoginMode mode = LoginMode.LOGIN;
	private int counter = 0;
	
	public Login() {
		File file = new File(Config.FILE_WELCOME);
		long time = file.lastModified();
		if(MOTD == null || time > MOTD_TIME) {
			MOTD_TIME = time;
			MOTD = IOUtils.getFileContents(file);
		}
		if(MOTD == null) {
			MOTD_TIME = -1;
			MOTD = "Login: ";
		}
	}
	
	@Override
	public void enter(Session session) {
		session.write(MOTD);
	}

	@Override
	public void received(Session session, String request) {
		switch(mode) {
		case LOGIN:
			if("new".equalsIgnoreCase(request.trim())) {
				session.write("Username: ");
				mode = LoginMode.NEW_LOGIN;
				break;
			}
			//check username
			user = User.load(request.toLowerCase());
			
			if(user != null) {
				session.write("Password: ");
				mode = LoginMode.PASSWORD;
			} else {
				session.writeLn("Invalid Username");
				session.write("Login: ");
			}
			break;
		case PASSWORD:
			if(user.testPassword(request)) {
				toCharacterSelect(session);
				
				return;
			} else {
				session.writeLn("Incorrect Login");
				if(++counter < MAX_ATTEMPTS) {
					mode = LoginMode.LOGIN;
					session.write("Login: ");
				} else {
					session.writeLn("To many failed login attempts, disconnecting.");
					session.disconnect();
				}
			}
			break;
		case NEW_LOGIN: {
			String username = request.toLowerCase();
			if(checkUsername(username)
			&& null == User.load(request.toLowerCase())) {
				user = new User();
				user.username = username;
				
				mode = LoginMode.NEW_PASSWORD;
				session.write("Password: ");
			} else {
				session.writeLn("You cannot use that name. Try again.");
				session.write("Username: ");
			}
			break;
		}
		case NEW_PASSWORD:
			if(checkPassword(request)) {
				user.setPassword(request);
				user.save();
				toCharacterSelect(session);
				return;
			} else {
				session.writeLn("You cannot use that password. Must be at least length 5 and start with a alphanumeric character.");
				session.write("Password: ");
			}
			break;
		default:
			session.writeLn("Connection Error.");
			session.disconnect();
		}
	}
	
	private void toCharacterSelect(Session session) {
		session.username = user.username;
		session.setMessageHandler(new CharacterSelect(user));
	}

	@Override
	public void exit(Session session) {
		
	}

	@Override
	public void reenter(Session session) {
		
	}
}