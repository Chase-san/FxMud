package org.csdgn.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TelnetServer implements Runnable {
	protected Thread thread;
	protected ThreadGroup ctgroup;
	protected boolean running = false;
	protected Charset charset = Charset.forName("ISO-8859-1");
	protected ArrayList<TelnetHandler> handlers = new ArrayList<TelnetHandler>();
	protected int port;
	
	public TelnetServer(int port) {
		this.port = port;
		ctgroup = new ThreadGroup("Client Thread Group");
	}
	
	public void addSocketHandler(TelnetHandler handler) {
		handlers.add(handler);
	}
	
	public void removeSocketHandler(TelnetHandler handler) {
		handlers.remove(handler);
	}

	public void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
	}
	
	public void stop() throws InterruptedException {
		if(thread != null) {
			running = false;
			thread.join();
		}
	}
	
	public void setReadCharset(Charset charset) {
		this.charset = charset;
	}
	
	@Override
	public void run() {
		ServerSocket socket = null;
		
		try {
			socket = new ServerSocket(port);
			socket.setSoTimeout(2000);
			
			while(running) try {
				Socket sock = socket.accept();
				createSubThread(sock);
			} catch(SocketTimeoutException ste) {
				//Not important, just keeps up from blocking for to long
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null) {
				if(!socket.isClosed()) try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void createSubThread(final Socket socket) {
		new Thread(ctgroup,new Runnable() {
			@Override
			public void run() {
				TelnetSocket sock = null;
				InputStream in = null;
				byte[] buffer = new byte[1024];
				try {
					sock = new TelnetSocket(socket);
					in = socket.getInputStream();
					//connected
					for(TelnetHandler h : handlers) {
						h.connected(sock);
					}
					
					int r = -1;
					while((r = in.read(buffer)) != -1) {
						if(r > 2) {
							String string = new String(buffer,0,r-2,charset);
							for(TelnetHandler h : handlers) {
								h.message(sock, string);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(in != null) {
						try {
							in.close();
						} catch (IOException e) {
						}
					}
				}
				//disconnected
				for(TelnetHandler h : handlers) {
					h.disconnected(sock);
				}
			}
		}).start();
	}
}
