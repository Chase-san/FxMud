package org.csdgn.fxm.controller;

import org.csdgn.fxm.Config;
import org.csdgn.fxm.net.InputHandler;
import org.csdgn.fxm.net.Session;
import org.csdgn.fxm.model.Character;
import org.csdgn.util.IOUtils;

import com.google.gson.Gson;

public class CharacterNew implements InputHandler {
	private Character character = new Character();
	private StringInput input;
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
					if(character.givenName == null) {
						session.writeLn("You must specify a given name.");
						break;
					}
					if(character.familyName == null) {
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
					session.pushMessageHandler(input = new StringInput("Given Name: "));
					return;
				case '1': //Surname
					inputType = c;
					session.pushMessageHandler(input = new StringInput("Family Name: "));
					return;
				case '2':
					//toggle gender
					character.isFemale = !character.isFemale;
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
			character.givenName = java.lang.Character.toUpperCase(value.charAt(0)) + value.substring(1);
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
			character.familyName = java.lang.Character.toUpperCase(value.charAt(0)) + value.substring(1);
			break;
		}
		
		displayMenu(session);
		session.write("Option: ");
	}
	
	public void writeCharacter(Session session) {
		String json = new Gson().toJson(character);
		String filename = String.format("%s%s/%s_%s", Config.FOLDER_CHARACTER,session.username,character.familyName,character.givenName);
		IOUtils.setFileContents(filename, json);
	}
	
	private void displayMenu(Session session) {
		session.writeLn("--------------------------------","Create New Character","");
		
		session.writeLn(String.format("\t0 - Given Name: %s", character.givenName == null ? "" : character.givenName));
		session.writeLn(String.format("\t1 - Family Name: %s", character.familyName == null ? "" : character.familyName));
		session.writeLn(String.format("\t2 - Gender: %s", character.isFemale ? "Female" : "Male"));
		
		session.writeLn("","\tL - Redisplay Menu","\tC - Create","\tX - Exit","");
	}
}
