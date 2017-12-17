package com.codisan.no_conf_orm;

import java.lang.reflect.Field;

public class ModelField {
	public String name;	        // userId
	public String columnname;	// user_id
	public Field field;
	
	public ModelField(String n, String cn, Field f) {
		name = n;
		columnname = cn;
		field = f;
	}
}