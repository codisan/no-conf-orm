package com.codisan.no_conf_orm;

public enum Operator {
	gt, // > 6
	gte, // >= 6
	lt, // < 10
	lte, // <= 10
	
	
	and, // AND (a = 5)
	or, // (a = 5 OR a = 6)
	
	
	ne, // != 20
	eq, // = 3
	not, // IS NOT TRUE
	between, // BETWEEN 6 AND 10
	notBetween, // NOT BETWEEN 11 AND 15
	in, // IN (1,2)
	notIn, // NOT IN (1,2)
	like, // LIKE '%hat'
	notLike, // NOT LIKE '%hat'
	regexp, // last_name regexp '^T';
	notRegexp, // last_name not regexp '^T';
}