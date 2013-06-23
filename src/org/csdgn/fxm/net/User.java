package org.csdgn.fxm.net;

import java.io.File;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.csdgn.fxm.Config;
import org.csdgn.fxm.model.Character;
import org.csdgn.util.IOUtils;
import org.csdgn.util.StringUtils;

import com.google.gson.Gson;

public class User {
	public String username;
	public String hash;
	public String salt;
	public ArrayList<Entry> characters;
	
	public static class Entry {
		public UUID uuid;
		public String name;
	}
	
	public User() {
		characters = new ArrayList<Entry>();
	}
	
	public Entry get(int index) {
		return characters.get(index);
	}
	
	public int size() {
		return characters.size();
	}
	
	public void addCharacter(Character ch) {
		Entry e = new Entry();
		e.uuid = ch.uuid;
		e.name = String.format("%s, %s", ch.familyName, ch.givenName);
		characters.add(e);
	}
	
	public void removeCharacter(UUID uuid) {
		characters.remove(uuid);
	}
	
	public void removeCharacter(Character ch) {
		characters.remove(ch.uuid);
	}
	
	/**
	 * Hashes the given password for this user, storing it.
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		byte[] baSalt = generateSalt(16);
		byte[] baHash = hash(password, baSalt);
		hash = StringUtils.bytesToHexString(baHash);
		salt = StringUtils.bytesToHexString(baSalt);
	}
	
	/**
	 * Returns true if this password matches against the hash/salt of this user.
	 * @param password The password to test.
	 * @return true if password matches, false otherwise
	 */
	public boolean testPassword(String password) {
		byte[] checkHash = StringUtils.hexStringToBytes(this.hash);
		byte[] salt = StringUtils.hexStringToBytes(this.salt);
		byte[] hash = hash(password, salt);
		if(Arrays.equals(checkHash, hash))
			return true;
		return false;
	}
	
	public void save() {
		IOUtils.setFileContents(Config.FOLDER_USER + username, new Gson().toJson(this, User.class));
	}
	
	public static User load(String username) {
		File file = new File(Config.FOLDER_USER + username);
		if(!file.exists())
			return null;
		return new Gson().fromJson(IOUtils.getFileContents(file), User.class);
	}
	
	/**
	 * @return An array of random bytes of the given length.
	 */
	private static byte[] generateSalt(int length) {
		byte[] salt = new byte[length];
		new Random().nextBytes(salt);
		return salt;
	}
	
	/**
	 * @return 32 byte digest, or null (if it fails to hash)
	 */
	private static byte[] hash(String text, byte[] salt) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(text.getBytes(Charset.forName("UTF-8")));
			md.update(salt);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
