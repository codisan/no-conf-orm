package com.codisan.no_conf_orm;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.codisan.no_conf_orm.annotation.Column;
import com.codisan.no_conf_orm.annotation.Table;
import com.codisan.no_conf_orm.filters.Filter;
import com.codisan.no_conf_orm.filters.Param;
import com.codisan.no_conf_orm.utils.Config;


public class Dao {
	private String findOneSql;
	private String findAndCountAllSql;
	private String insertSql;
	private String updateSql;
	private String destroySql;
	private Class cls;
	
	// user: {tablename: '', fields: [{fieldname: 'userId', fieldtype: Integer, columnname: 'user_id'}...]}
	private static HashMap<String, Model> cachedModels = new HashMap<String, Model>();
	
	private static Properties cfg;
	private static Connection con; 
	
	static {
		try {
			cfg = new Config().getPropValues("noconform.properties");
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(cfg.getProperty("url"), cfg.getProperty("username"), cfg.getProperty("password"));
			} catch (Exception e) {			
				System.out.println(e);
		}
	}
		
	public Dao() {
		cls = this.getClass();
		
		// System.out.println(cls);
		// System.out.println(this);
//		initTableName();
//		initColumns();
//		
//		
//		System.out.println(tableName);
//		System.out.println(columns);
	}
	
//	public static <T> T findOne() {
//		User u = new User();
//		return (T)u;
//	}
	
	public static <T> T findOne(Class clz, String filter) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, SQLException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		Model md = getModel(clz); 	// 将model 的信息取出来
		
		List<ModelField> modelFields = md.fields;
		List<String> columns = new ArrayList<String>();
		// 添加主键
		columns.add(String.format("`%s` as `%s`", md.primaryFieldColumnName, md.primaryFieldName));
		// 添加其他字段
		modelFields.forEach(mf -> columns.add(String.format("`%s` as `%s`", mf.columnname, mf.name)));		
		String columnSql = columns.stream().collect(Collectors.joining(" , "));
				
		// 拼接 sql 语句
		String sql = "select %s from `%s` %s limit 1";
		sql = String.format(sql, columnSql, md.tablename, filter);
		
		System.out.println(sql);
		
		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(sql);
		
		Object ret = clz.newInstance();	// 返回的实例
		while (result.next()) {
			// 设置主键
			setFieldValue(md.primaryField, md.primaryFieldName, result, ret);
			
			for(int i = 0; i < modelFields.size(); i++) {
				ModelField f = modelFields.get(i);
				setFieldValue(f.field, f.name, result, ret);
			}
		}
		return (T)ret;
	}
	
	public static int insert(Class clz, Object entity) throws SQLException, IllegalArgumentException, IllegalAccessException {
		String sql = "insert into %s (%s) values (%s)";
		Model md = getModel(clz); 	// 将model 的信息取出来
		
		List<String> columns = new ArrayList<String>();
		List<ModelField> fields = md.fields;
		List<String> values = new ArrayList<String>();
		
		md.fields.forEach(f -> {
			values.add("?");
			columns.add(String.format("`%s`", f.columnname));
		});
		
		String vals = values.stream().collect(Collectors.joining(" , "));
		String cls = columns.stream().collect(Collectors.joining(" , ")); 
		sql = String.format(sql, md.tablename, cls, vals);
		
		PreparedStatement st = con.prepareStatement(sql);
		
		for(int i = 0; i < fields.size(); i++) {
			ModelField f = fields.get(i);
			setStatement(i + 1, f.field, f.name, st, entity);
		}
		
		return st.executeUpdate();
	}
	
	public static int update(Class clz, Object entity, String... filter) throws IllegalArgumentException, IllegalAccessException, SQLException {
		String sql = "UPDATE `%s` SET %s WHERE %s";
		Model md = getModel(clz); 	// 将model 的信息取出来
		String tablename = md.tablename;
		
		String where = String.format("`%s` = %d", md.primaryFieldColumnName, md.primaryField.get(entity));
		// 默认有一个主键的条件，也可以接收多个参数， 用and  连接起来
		if(filter.length > 1) {
			where = String.format("%s and %s", where, filter);
		}
		
		List<ModelField> fields = md.fields;
		List<String> columns = new ArrayList<String>();
		
		for(int i = 0; i < fields.size(); i++) {
			ModelField mf = fields.get(i);
			columns.add(String.format("`%s` = ?", mf.columnname));
		}
		
		String cls = columns.stream().collect(Collectors.joining(" , "));
		
		sql = String.format(sql, tablename, cls, where);
		
		PreparedStatement st = con.prepareStatement(sql);
		for(int i = 0; i < fields.size(); i++) {
			ModelField f = fields.get(i);
			setStatement(i + 1, f.field, f.name, st, entity);
		}
		
		return st.executeUpdate();
	}
	
	public static int destroy(Class clz, Object entity, String... filter) throws IllegalArgumentException, IllegalAccessException, SQLException {
		String sql = "DELETE FROM `%s` WHERE %s";
		Model md = getModel(clz); 	// 将model 的信息取出来
		String tablename = md.tablename;
		
		String where = String.format("`%s` = %d", md.primaryFieldColumnName, md.primaryField.get(entity));
		// 默认有一个主键的条件，也可以接收多个参数， 用and  连接起来
		if(filter.length > 1) {
			where = String.format("%s and %s", where, filter);
		}
		
		sql = String.format(sql, md.tablename, where);
		
		System.out.println("destroy sql: " + sql);
		PreparedStatement st = con.prepareStatement(sql);
		
		// setStatement(1, md.primaryField, md.primaryFieldName, st, entity);
		return st.executeUpdate();
	}
	
	private static void setStatement(int j, Field f, String fname, PreparedStatement st, Object entity) throws IllegalArgumentException, IllegalAccessException, SQLException {
		if(f.getType() == Integer.class) {
			st.setInt(j, (int) f.get(entity));
		}
		if(f.getType() == String.class) {
			st.setString(j, (String) f.get(entity));
		}
		if(f.getType() == Date.class) {
			st.setDate(j, (java.sql.Date) f.get(entity));
		}
	}
	
	private static void setFieldValue(Field f, String fname, ResultSet rs, Object ret) throws IllegalArgumentException, IllegalAccessException, SQLException {
		if(f.getType() == Integer.class) {
			f.set(ret, rs.getInt(fname));
		}
		if(f.getType() == String.class) {
			f.set(ret, rs.getString(fname));
		}
		if(f.getType() == Date.class) {
			f.set(ret, rs.getDate(fname));
		}
	}
	
	private static Model getModel(Class clz) {
		String modelname = clz.getName();
		// 首先判断缓存中是否有该 class 的缓存信息，如果有则直接从缓存中取，否则反射该 class
		if(cachedModels.containsKey(modelname)) {
			return cachedModels.get(modelname);
		}
				
		String tablename = getTablename(clz);
		List<ModelField> modelFields = new ArrayList();
		
		Field[] fs = clz.getFields();
		Field primaryField = null;
		String primaryFieldName = null;
		String primaryFieldColumnName = null;		

		for(Field f : fs){
			Annotation[] as = f.getDeclaredAnnotations();
			String fieldname = f.getName();
			String columnname = fieldname;	// 默认等于 field name
			boolean isPrimary = false;
			
			for(Annotation a : as){
				if(a instanceof Column){
					Column currAnno = (Column) a;
					columnname = currAnno.name();
					
					if(currAnno.primaryKey()) {
						isPrimary = true;
						primaryField = f;
						primaryFieldName = f.getName();
						primaryFieldColumnName = columnname;
					}
				}
			}
			// 不添加主键
			if(!isPrimary) {
				modelFields.add(new ModelField(fieldname, columnname, f));
			}
		}
		
		Model m = new Model(tablename, modelFields, primaryField, primaryFieldName, primaryFieldColumnName);
		cachedModels.put(modelname, m);	 // 将model 信息缓存起来
		return m;
	}
	
	private static String getTablename(Class clz) {
		String tablename = clz.getSimpleName();  // default table name is class name
		Annotation[] annotations = clz.getAnnotations();
		for(Annotation annotation : annotations){
		    if(annotation instanceof Table){
		        Table myAnnotation = (Table) annotation;
		        tablename = myAnnotation.name();
		    }
		}
		return tablename;
	}
}
