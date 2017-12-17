package com.codisan.no_conf_orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppTest {
	private static Connection con;

	@BeforeClass
	public static void setup() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo_mybatis", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@AfterClass	
	public static void teardown() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void select() {
		try {
			String query = "select * from user where user_id = :userId";
			
			NamedParamStatement stmt = new NamedParamStatement(con, query);
			
			stmt.setInt("userId", 1);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				System.out.println(rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
