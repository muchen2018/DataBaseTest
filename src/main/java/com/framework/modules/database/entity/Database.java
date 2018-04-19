package com.framework.modules.database.entity;

public class Database {
	
	/**
	 * 实例Id
	 */
	private String DBInstanceId;
	
	/**
	 * 数据库名
	 */
	private String DBName;
	
	/**
	 * 字符集
	 */
	private String characterSetName;
	
	/**
	 * 描述
	 */
	private String DBDescription;

	public String getDBInstanceId() {
		return DBInstanceId;
	}

	public void setDBInstanceId(String dBInstanceId) {
		this.DBInstanceId = dBInstanceId;
	}

	public String getDBName() {
		return DBName;
	}

	public void setDBName(String dBName) {
		this.DBName = dBName;
	}

	public String getCharacterSetName() {
		return characterSetName;
	}

	public void setCharacterSetName(String characterSetName) {
		this.characterSetName = characterSetName;
	}

	public String getDBDescription() {
		return DBDescription;
	}

	public void setDBDescription(String dBDescription) {
		this.DBDescription = dBDescription;
	}

}
