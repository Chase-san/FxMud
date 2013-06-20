package org.csdgn.fxm.net.msg;

import org.csdgn.fxm.Config;
import org.csdgn.fxm.net.MessageHandler;
import org.csdgn.fxm.net.Session;
import org.csdgn.fxm.model.Player;
import org.csdgn.util.IOUtils;

import com.google.gson.Gson;

public class NewCharacterHandler implements MessageHandler {
	private Player player = new Player();
	private StringInputHandler input;
	private char inputType = '\0';
	
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
				case 'x':
					session.popMessageHandler();
					return;
				case 'l':
					displayMenu(session);
					break;
				case 'c':
					if(player.givenName == null) {
						session.writeLn("You must specify a given name.");
						break;
					}
					if(player.familyName == null) {
						session.writeLn("You must specify a family name.");
						break;
					}
					
					session.writeLn("","Character Created!");
					
					//okay, cool, write it
					writeCharacter(session);
					session.popMessageHandler();
					return;
				case '0': //Given Name
					inputType = c;
					session.pushMessageHandler(input = new StringInputHandler("Given Name: "));
					return;
				case '1': //Surname
					inputType = c;
					session.pushMessageHandler(input = new StringInputHandler("Family Name: "));
					return;
				case '2':
					//toggle gender
					player.isFemale = !player.isFemale;
					displayMenu(session);
					break;
				default:
			}
		}
		
		session.write("Option: ");
	}

	@Override
	public void exit(Session session) {
		
	}

	@Override
	public void reenter(Session session) {
		String value = input.getValue().toLowerCase();
		input = null;
		switch(inputType) {
		case '0': //Given Name
			if(value.length() < 3) {
				session.writeLn("Your given name must be at least 3 characters long.");
				break;
			}
			if(!value.matches("[-a-z]{3,}")) {
				session.writeLn("Your given name may only have alphabetic characters in it.");
				break;
			}
			player.givenName = Character.toUpperCase(value.charAt(0)) + value.substring(1);
			break;
		case '1': //Surname
			if(value.length() < 3) {
				session.writeLn("Your family name must be at least 3 characters long.");
				break;
			}
			if(!value.matches("[-a-z]{3,}")) {
				session.writeLn("Your family name may only have alphabetic characters in it.");
				break;
			}
			player.familyName = Character.toUpperCase(value.charAt(0)) + value.substring(1);
			break;
		}
		
		displayMenu(session);
		session.write("Option: ");
	}
	
	public void writeCharacter(Session session) {
		String json = new Gson().toJson(player);
		String filename = String.format("%s%s/%s_%s", Config.FOLDER_CHARACTER,session.username,player.familyName,player.givenName);
		IOUtils.setFileContents(filename, json);
	}
	
	private void displayMenu(Session session) {
		session.writeLn("--------------------------------","Create New Character","");
		
		session.writeLn(String.format("\t0 - Given Name: %s", player.givenName == null ? "" : player.givenName));
		session.writeLn(String.format("\t1 - Family Name: %s", player.familyName == null ? "" : player.familyName));
		session.writeLn(String.format("\t2 - Gender: %s", player.isFemale ? "Female" : "Male"));
		
		session.writeLn("","\tL - Redisplay Menu","\tC - Create","\tX - Exit","");
	}
}
