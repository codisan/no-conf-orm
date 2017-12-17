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
		String columnsql = modelFields.stream().map(n -> {
			return String.format("`%s` as `%s`", n.columnname, n.name);
		}).collect(Collectors.joining(" , "));	
				
		// 拼接 sql 语句
		String sql = "select :columns from `:tablename` :filter limit 1";
		
		sql = sql.replaceAll(":columns", columnsql);
		sql = sql.replaceAll(":tablename", md.tablename);
		sql = sql.replaceAll(":filter", filter);
		
		System.out.println(sql);
		
		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(sql);
		
		Object ret = clz.newInstance();	// 返回的实例
		while (result.next()) {
			for(int i = 0; i < modelFields.size(); i++) {
				ModelField f = modelFields.get(i);
				setFieldValue(f, result, ret);
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
			if(!f.autoIncrement){ 
				values.add("?");
				columns.add(String.format("`%s`", f.columnname));
			}
		});
		
		String vals = values.stream().collect(Collectors.joining(" , "));
		String cls = columns.stream().collect(Collectors.joining(" , ")); 
		sql = String.format(sql, md.tablename, cls, vals);
		
		System.out.println("sql: " + sql);
		
		PreparedStatement st = con.prepareStatement(sql);
		
		for(int i = 0, j = 1; i < fields.size(); i++) {
			ModelField f = fields.get(i);
			if(!f.autoIncrement) {
				setStatement(j, f, st, entity);
				j++;	
			}
		}
		
		return st.executeUpdate();
	}
	
	public static void update(Class clz, Object o, String filter) {
		
	}
	
	private static void destroy(Class clz, Object o) {
		
	}
	
	private static void setStatement(int j, ModelField f, PreparedStatement st, Object entity) throws IllegalArgumentException, IllegalAccessException, SQLException {
		if(f.field.getType() == Integer.class) {
			st.setInt(j, (int) f.field.get(entity));
		}
		if(f.field.getType() == String.class) {
			st.setString(j, (String) f.field.get(entity));
		}
		if(f.field.getType() == Date.class) {
			st.setDate(j, (java.sql.Date) f.field.get(entity));
		}
	}
	
	private static void setFieldValue(ModelField f, ResultSet rs, Object ret) throws IllegalArgumentException, IllegalAccessException, SQLException {
		if(f.field.getType() == Integer.class) {
			f.field.set(ret, rs.getInt(f.name));
		}
		if(f.field.getType() == String.class) {
			f.field.set(ret, rs.getString(f.name));
		}
		if(f.field.getType() == Date.class) {
			f.field.set(ret, rs.getDate(f.name));
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

		for(Field f : fs){
			Annotation[] as = f.getDeclaredAnnotations();
			String fieldname = f.getName();
			String columnname = fieldname;	// 默认等于 field name
			boolean isPrimaryKey = false;
			boolean autoIncrement = false;
			
			for(Annotation a : as){
				if(a instanceof Column){
					Column currAnno = (Column) a;
					columnname = currAnno.name();
					isPrimaryKey = currAnno.primaryKey();
					autoIncrement = currAnno.autoIncrement();
				}
			}
			
			modelFields.add(new ModelField(fieldname, columnname, f, isPrimaryKey, autoIncrement));
		}
		
		Model m = new Model(tablename, modelFields);
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
