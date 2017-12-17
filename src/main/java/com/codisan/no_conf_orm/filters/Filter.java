package com.codisan.no_conf_orm.filters;

import java.util.ArrayList;
import java.util.List;

import com.codisan.no_conf_orm.Operator;

public class Filter {	
	List<Param> filters = new ArrayList();	
	
	public void add(String field, Operator op, Object v) {
		filters.add(new Param(field, op, v));
	}
}