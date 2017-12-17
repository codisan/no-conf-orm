package com.codisan.no_conf_orm;

import java.lang.reflect.Field;

public class ModelField {
	public String name;	        // userId
	public String columnname;	// user_id
	public Field field;
	public boolean isPrimaryKey;
	public boolean autoIncrement;
	
	public ModelField(String n, String cn, Field f, boolean isp, boolean autoIn) {
		name = n;
		columnname = cn;
		field = f;
		isPrimaryKey = isp;
		autoIncrement = autoIn;
	}
}