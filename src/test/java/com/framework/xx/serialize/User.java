package com.framework.xx.serialize;

public class User extends BaseUser{
	
	private static final long serialVersionUID = 1L;
	
	private String sex;

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "User [sex=" + sex + "]";
	}
	
}
