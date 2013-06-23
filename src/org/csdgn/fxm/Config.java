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
package org.csdgn.fxm;

import java.util.UUID;

/**
 * @author Chase
 */
public class Config {
	public static final String FOLDER_DATABASE = "db/";
	public static final String FOLDER_USER = FOLDER_DATABASE  + "user/";
	public static final String FOLDER_CHARACTER = FOLDER_DATABASE  + "characters/";
	public static final String FOLDER_WORLD = FOLDER_DATABASE  + "world/";
	public static final String FILE_WELCOME = FOLDER_DATABASE  + "welcome";
	/** Temporary Solution */
	public static final UUID START_ROOM_UUID = UUID.fromString("c58c7c53-7722-4e40-a649-375b6e199a8f");
}
