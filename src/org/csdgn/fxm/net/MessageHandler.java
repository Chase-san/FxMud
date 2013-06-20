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
package org.csdgn.fxm.net;

/**
 * A handler for messages.
 * 
 * @author Chase
 */
public interface MessageHandler {
	/**
	 * Called when this handler is being entered for the first time.
	 */
	public void enter(Session session);
	/**
	 * Called when a message is received from the client.
	 */
	public void received(Session session, String request);
	/**
	 * Called when this Handler is permanently exited. Either by set or being popped off the stack.
	 */
	public void exit(Session session);
	/**
	 * Called when the handler above this handler on the stack is popped and control is returned to this handler.
	 */
	public void reenter(Session session);
}
