package com.codisan.no_conf_orm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.junit.Test;

public class Reflect {
	@Test
	public void select() throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, SQLException {
		User u = Dao.findOne(User.class, "where user_id = 2");
		System.out.println(u);		
	}
	
	@Test
	public void insert() throws IllegalArgumentException, IllegalAccessException, SQLException {
		User u = new User();
		u.firstName = "xx";
		u.lastName = "oo";
		u.emailId = "xx@oo.com";
		u.password = "xx pass word";
		
		int row = Dao.insert(User.class, u);
	}
	
	@Test 
	public void update() throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, SQLException {
		User u = Dao.findOne(User.class, "where user_id = 2");
		u.firstName = "333333333333";
		u.update(User.class, u);
	}
	
	@Test 
	public void destroy() throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, SQLException {
		User u = Dao.findOne(User.class, "where user_id = 6");
		u.destroy(User.class, u);
	}
}
