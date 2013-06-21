package org.csdgn.fxm.controller;

import org.csdgn.fxm.net.InputHandler;
import org.csdgn.fxm.net.Session;

public class StringInput implements InputHandler {
	private String prompt;
	private String value;
	
	public String getValue() {
		return value;
	}
	
	public StringInput(String prompt) {
		this.prompt = prompt;
	}
	
	@Override
	public void enter(Session session) {
		session.write(prompt);
	}

	@Override
	public void received(Session session, String request) {
		value = request;
		session.popMessageHandler();
	}

	@Override
	public void exit(Session session) {}

	@Override
	public void reenter(Session session) {}
	
}