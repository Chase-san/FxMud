package org.csdgn.fxm.net.ctrl;

import java.util.UUID;
import org.csdgn.fxm.net.InputHandler;
import org.csdgn.fxm.net.Session;
import org.csdgn.fxm.net.User;
import org.csdgn.fxm.model.Actor.Gender;
import org.csdgn.fxm.model.Character;

public class CharacterNew implements InputHandler {
	private Character character = new Character();
	private StringInput input;
	private char inputType = '\0';
	
	private User user;
	
	public CharacterNew(User user) {
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
					
					session.writeLn("Character Created!");
					
					//okay, cool, write it
					character.uuid = UUID.randomUUID();
					character.save();
					
					user.addCharacter(character);
					user.save();
										
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
					//character.isFemale = !character.isFemale;
					if(character.gender == Gender.Male) {
						character.gender = Gender.Female;
					} else {
						character.gender = Gender.Male;
					}
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
	
	private void displayMenu(Session session) {
		session.writeLn("--------------------------------","Create New Character","");
		
		session.writeLn(String.format("\t0 - Given Name: %s", character.givenName == null ? "" : character.givenName));
		session.writeLn(String.format("\t1 - Family Name: %s", character.familyName == null ? "" : character.familyName));
		session.writeLn(String.format("\t2 - Gender: %s", character.gender.toString() ));
		
		session.writeLn("","\tL - Redisplay Menu","\tC - Create","\tX - Exit","");
	}
}
