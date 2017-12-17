package com.codisan.no_conf_orm;

import java.lang.reflect.Field;
import java.util.List;

public class Model {
	public String tablename;
	public List<ModelField> fields;
	public Field primaryField;
	public String primaryFieldName;
	public String primaryFieldColumnName;
	
	public Model(String tn, List<ModelField> fs, Field pField, String pName, String pColumnname) {
		tablename = tn;
		fields = fs;
		primaryField = pField;
		primaryFieldName = pName;
		primaryFieldColumnName = pColumnname;
	}
}
