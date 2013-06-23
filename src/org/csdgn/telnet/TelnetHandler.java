package org.csdgn.telnet;

public interface TelnetHandler {
	public void connected(TelnetSocket sock);
	public void disconnected(TelnetSocket sock);
	public void message(TelnetSocket sock, String msg);
}
