package com.codisan.no_conf_orm;

import com.codisan.no_conf_orm.annotation.Column;
import com.codisan.no_conf_orm.annotation.Table;

@Table(name="user")
public class User extends Dao {
	@Column(name = "user_id", primaryKey=true, autoIncrement=true)
	public Integer userId;
	
	@Column(name = "email_id")
	public String emailId;
	
	public String password;
	
	@Column(name = "last_name")
	public String lastName;
	
	@Column(name = "first_name")
	public String firstName;
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", emailId=" + emailId + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}	
}
