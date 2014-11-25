package org.soen387.domain.model.user.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class UserTDG {
	public static final String TABLE_NAME = "User";
	public static final String COLUMNS = "id, version, username, password ";
	public static final String TRUNCATE_TABLE = "TRUNCATE TABLE  " + TABLE_NAME + ";";
	public static final String DROP_TABLE = "DROP TABLE  " + TABLE_NAME + ";";
	public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" 
			+ "id BIGINT, "
			+ "version int, "
			+ "username VARCHAR(32), "
			+ "password VARCHAR(64), "
			+ "PRIMARY KEY(id), "
			+ "UNIQUE (username) "
			+ ");";



	public static void createTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		Statement update = con.createStatement();
		update.execute(CREATE_TABLE);
	}

	public static void dropTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		Statement update = con.createStatement();
		update.execute(TRUNCATE_TABLE);
		update = con.createStatement();
		update.execute(DROP_TABLE);
	}
	
	
	public static final String INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMNS + ") "
			+ "VALUES (?,?,?,PASSWORD(?));";
	public static int insert(long id, int version, String usernmae, String password) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1,id);
		ps.setInt(2,version);
		ps.setString(3,usernmae);
		ps.setString(4,password);
		System.out.println(ps.toString());
		return ps.executeUpdate();
	}
	
	public static final String UPDATE = "UPDATE " + TABLE_NAME + " set version = version+1, username=?, password=password(?) "
			+ " WHERE id=? AND version=?;";
	public static int update(long id, int version, String usernmae, String password) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setString(1,usernmae);
		ps.setString(2,password);
		ps.setLong(3,id);
		ps.setInt(4,version);
		return ps.executeUpdate();
	}
	
	public static final String DELETE = "DELETE FROM " + TABLE_NAME + " "
			+ "WHERE id=? AND version=?;";
	public static int delete(long id, int version) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1,id);
		ps.setInt(2,version);
		return ps.executeUpdate();
	}
	
	public static final String FIND = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " WHERE id=?;";
	public static ResultSet find(long id) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND);
		ps.setLong(1,id);
		return ps.executeQuery();
	}

	public static final String FIND_BY_USERNAME_AND_PASSWORD = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " WHERE username=? AND password=password(?);";
	public static ResultSet find(String username, String password) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_BY_USERNAME_AND_PASSWORD);
		ps.setString(1,username);
		ps.setString(2,password);
		return ps.executeQuery();
	}

	private static long maxId = 0;
	public static final String GET_MAX_ID = "SELECT max(id) AS max FROM " + TABLE_NAME + ";";
	public static synchronized long getMaxId() throws SQLException {
		if(maxId==0) {
			Connection con = DbRegistry.getDbConnection();
			PreparedStatement ps = con.prepareStatement(GET_MAX_ID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				maxId = rs.getLong("max");
			}
		}
		return ++maxId;
	}
	
}
