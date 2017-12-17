package com.codisan.no_conf_orm;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedParamStatement {
	private int getIndex(String name) {
        return fields.indexOf(name)+1;
    }
    private PreparedStatement prepStmt;
    private List<String> fields = new ArrayList<String>();
    
    public NamedParamStatement(Connection conn, String sql) throws SQLException {
        Pattern findParametersPattern = Pattern.compile("(?<!')(:[\\w]*)(?!')");
        Matcher matcher = findParametersPattern.matcher(sql);
        while (matcher.find()) {
            fields.add(matcher.group().substring(1));
        }
        prepStmt = conn.prepareStatement(sql.replaceAll(findParametersPattern.pattern(), "?"));
    }
    
    public PreparedStatement getPreparedStatement() {
        return prepStmt;
    }
    public ResultSet executeQuery() throws SQLException {
        return prepStmt.executeQuery();
    }
    public void close() throws SQLException {
        prepStmt.close();
    }
    
    public void setArray(String name, Array x) throws SQLException {
    		prepStmt.setArray(getIndex(name), x);
    }
    
    public void setAsciiStream(String name, InputStream x) throws SQLException {
    		prepStmt.setAsciiStream(getIndex(name), x);
    }
    
    public void setAsciiStream(String name, InputStream x, int length) throws SQLException {
    		prepStmt.setAsciiStream(getIndex(name), x, length);
    }

    public void setAsciiStream(String name, InputStream x, long length) throws SQLException {
        prepStmt.setAsciiStream(getIndex(name), x, length);
    }

    public void setBigDecimal(String name, BigDecimal x)  throws SQLException {
        prepStmt.setBigDecimal(getIndex(name), x);
    }

    public void  setBinaryStream(String name, InputStream x)  throws SQLException {
        prepStmt.setBinaryStream(getIndex(name), x);
    }

    public void setBinaryStream(String name, InputStream x, int length)  throws SQLException{
        prepStmt.setBinaryStream(getIndex(name), x, length);
    }

    public void setBinaryStream(String name, InputStream x, long length)  throws SQLException{
        prepStmt.setBinaryStream(getIndex(name), x, length);
    }

    public void	setBlob(String name, Blob x)  throws SQLException{
        prepStmt.setBlob(getIndex(name), x);
    }
    
    public void	setBlob(String name, InputStream x)  throws SQLException{
        prepStmt.setBlob(getIndex(name), x);
    }
    
    public void	setBlob(String name, InputStream x, long length)  throws SQLException{
        prepStmt.setBlob(getIndex(name), x, length);
    }
    
    public void	setBoolean(String name, boolean x)  throws SQLException{
        prepStmt.setBoolean(getIndex(name), x);
    }
    
    public void	setByte(String name, byte x)  throws SQLException{
        prepStmt.setByte(getIndex(name), x);
    }
    public void	setBytes(String name, byte[] x)  throws SQLException{
        prepStmt.setBytes(getIndex(name), x);
    }
    public void	setCharacterStream(String name, Reader x)  throws SQLException{
        prepStmt.setCharacterStream(getIndex(name), x);
    }
    
    public void	setCharacterStream(String name, Reader x, int length)  throws SQLException{
        prepStmt.setCharacterStream(getIndex(name), x, length);
    }
    public void	setCharacterStream(String name, Reader x, long length)  throws SQLException{
        prepStmt.setCharacterStream(getIndex(name), x, length);
    }
    public void	setClob(String name, Clob x)  throws SQLException{
        prepStmt.setClob(getIndex(name), x);
    }
    public void	setClob(String name, Reader x)  throws SQLException{
        prepStmt.setClob(getIndex(name), x);
    }
    public void	setClob(String name, Reader x, long length)  throws SQLException{
        prepStmt.setClob(getIndex(name), x, length);
    }
    public void	setDate(String name, Date x)  throws SQLException{
        prepStmt.setDate(getIndex(name), x);
    }
    public void	setDate(String name, Date x, Calendar cal)  throws SQLException{
        prepStmt.setDate(getIndex(name), x, cal);
    }
    public void	setDouble(String name, double x)  throws SQLException{
        prepStmt.setDouble(getIndex(name), x);
    }

    public void	setFloat(String name, float x)  throws SQLException{
        prepStmt.setFloat(getIndex(name), x);
    }
    public void	setInt(String name, int x)  throws SQLException{
        prepStmt.setInt(getIndex(name), x);
    }
    public void	setLong(String name, long x)  throws SQLException{
        prepStmt.setLong(getIndex(name), x);
    }
    public void	setNCharacterStream(String name, Reader x)  throws SQLException{
        prepStmt.setNCharacterStream(getIndex(name), x);
    }
    public void	setNCharacterStream(String name, Reader x, long length)  throws SQLException{
        prepStmt.setNCharacterStream(getIndex(name), x, length);
    }
    public void	setNClob(String name, NClob x)  throws SQLException{
        prepStmt.setNClob(getIndex(name), x);
    }
    public void	setNClob(String name, Reader x)  throws SQLException{
        prepStmt.setNClob(getIndex(name), x);
    }
    public void	setNClob(String name, Reader x, long length)  throws SQLException{
        prepStmt.setNClob(getIndex(name), x, length);
    }
    public void	setNString(String name, String x)  throws SQLException{
        prepStmt.setNString(getIndex(name), x);
    }
    public void	setNull(String name, int x)  throws SQLException{
        prepStmt.setNull(getIndex(name), x);
    }
    public void	setNull(String name, int x, String typeName)  throws SQLException{
        prepStmt.setNull(getIndex(name), x, typeName);
    }
    public void	setObject(String name, Object x)  throws SQLException{
        prepStmt.setObject(getIndex(name), x);
    }
    public void	setObject(String name, Object x, int targetSqlType)  throws SQLException{
        prepStmt.setObject(getIndex(name), x, targetSqlType);
    }
    public void	setObject(String name, Object x, int targetSqlType, int scaleOrLength)  throws SQLException{
        prepStmt.setObject(getIndex(name), x, targetSqlType, scaleOrLength);
    }
    public void	setRef(String name, Ref x)  throws SQLException{
        prepStmt.setRef(getIndex(name), x);
    }
    public void	setRowId(String name, RowId x)  throws SQLException{
        prepStmt.setRowId(getIndex(name), x);
    }
    public void	setShort(String name, short x)  throws SQLException{
        prepStmt.setShort(getIndex(name), x);
    }
    public void	setSQLXML(String name, SQLXML x)  throws SQLException{
        prepStmt.setSQLXML(getIndex(name), x);
    }
    public void	setString(String name, String x)  throws SQLException{
        prepStmt.setString(getIndex(name), x);
    }
    public void	setTime(String name, Time x)  throws SQLException{
        prepStmt.setTime(getIndex(name), x);
    }
    public void	setTime(String name, Time x, Calendar cal)  throws SQLException{
        prepStmt.setTime(getIndex(name), x, cal);
    }
    public void	setTimestamp(String name, Timestamp x)  throws SQLException{
        prepStmt.setTimestamp(getIndex(name), x);
    }
    public void	setTimestamp(String name, Timestamp x, Calendar cal)  throws SQLException{
        prepStmt.setTimestamp(getIndex(name), x, cal);
    }
    public void	setUnicodeStream(String name, InputStream x, int length)  throws SQLException{
        prepStmt.setUnicodeStream(getIndex(name), x, length);
    }
    public void	setURL(String name, URL x)  throws SQLException{
        prepStmt.setURL(getIndex(name), x);
    }
}
