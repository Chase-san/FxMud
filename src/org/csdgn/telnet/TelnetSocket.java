package org.csdgn.telnet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.HashMap;

public class TelnetSocket {
	protected java.net.Socket socket;
	protected OutputStream out;
	protected Charset charset = Charset.forName("ISO-8859-1");
	protected HashMap<Object,Object> data;
	
	protected TelnetSocket(java.net.Socket socket) throws IOException {
		this.socket = socket;
		out = socket.getOutputStream();
		data = new HashMap<Object,Object>();
	}
	
	public boolean isConnected() {
		return socket.isConnected();
	}
	
	public boolean isClosed() {
		return socket.isClosed();
	}
	
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void write(String str) {
		write(str.getBytes(charset));
	}
	
	public synchronized void write(byte[] data) {
		try {
			out.write(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int hashCode() {
		return socket.hashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof TelnetSocket) {
			return ((TelnetSocket)obj).socket.equals(socket);
		}
		return false;
	}
	
	public void setWriteCharset(Charset charset) {
		this.charset = charset;
	}
	
	public InetAddress localAddress() {
		return socket.getLocalAddress();
	}
	
	public InetAddress remoteAddress() {
		return socket.getInetAddress();
	}
	
	public Object get(Object key) {
		return data.get(key);
	}
	
	public void put(Object key, Object value) {
		data.put(key, value);
	}
	
	public Object remove(Object key) {
		return data.remove(key);
	}
}
