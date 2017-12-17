package com.codisan.no_conf_orm;

import java.util.List;

public class Model {
	public String tablename;
	public List<ModelField> fields;
	
	public Model(String tn, List<ModelField> fs) {
		tablename = tn;
		fields = fs;
	}
}
