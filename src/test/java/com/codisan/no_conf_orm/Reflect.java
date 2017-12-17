package com.codisan.no_conf_orm;

import java.lang.reflect.Method;

import org.junit.Test;

public class Reflect {

	@Test
	public void testConstructor() {
		// User u = new User();
		
		try {
			// find one
			 User u = Dao.findOne(User.class, "where user_id = 1");
			 System.out.println(u);
			 
			 u.firstName = "--------------";
			 
			 // u.update(User.class, u);
			 
			 u.destroy(User.class, u);
			
			// insert
//			User u = new User();
//			u.firstName = "xx";
//			u.lastName = "oo";
//			u.emailId = "xx@oo.com";
//			u.password = "xx pass word";
//			
//			int row = Dao.insert(User.class, u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		// Class 
	}
	
	
	@Test
	public void xx() {
//		User u = new User();
//		Method[] methods = User.class.getMethods();
//
//		for(Method method : methods){
//			System.out.println("method = " + method.getName());
//		}
	}
}
