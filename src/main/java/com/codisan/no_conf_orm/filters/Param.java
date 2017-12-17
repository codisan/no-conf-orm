package com.codisan.no_conf_orm.filters;

import com.codisan.no_conf_orm.Operator;

public class Param {
	private String field;
	private Operator op;
	private Object value;
	
	public Param(String f, Operator op, Object v) {
		this.field = f;
		this.op = op;
		this.value = v;
	}
}
