package com.framework.xx.serialize;

import java.io.Serializable;

public class BaseUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String type="xx";
	
	private String username;
	
	private transient String password;

	public static String getType() {
		return type;
	}

	public static void setType(String type) {
		BaseUser.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "BaseUser [username=" + username + ", password=" + password + "]";
	}

}
