package org.soen387.domain.model.challenge.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class ChallengeTDG {
	public static final String TABLE_NAME = "Challenge";
	public static final String COLUMNS = "id, version, challenger, challengee, status ";
	public static final String TRUNCATE_TABLE = "TRUNCATE TABLE  " + TABLE_NAME + ";";
	public static final String DROP_TABLE = "DROP TABLE  " + TABLE_NAME + ";";
	public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" 
			+ "id BIGINT, "
			+ "version int, "
			+ "challenger BIGINT, "
			+ "challengee BIGINT, "
			+ "status TINYINT, "
			+ "PRIMARY KEY(id), "
			+ "INDEX(challenger), "
			+ "INDEX(challengee) "
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
			+ "VALUES (?,?,?,?,?);";
	public static int insert(long id, int version, long challenger, long challengee, int status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1,id);
		ps.setInt(2,version);
		ps.setLong(3,challenger);
		ps.setLong(4,challengee);
		ps.setInt(5,status);
		return ps.executeUpdate();
	}
	
	public static final String UPDATE = "UPDATE " + TABLE_NAME + " set version = version+1, challenger=?, challengee=?, status=? "
			+ " WHERE id=? AND version=?;";
	public static int update(long id, int version, long challenger, long challengee, int status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setLong(1,challenger);
		ps.setLong(2,challengee);
		ps.setInt(3,status);
		ps.setLong(4,id);
		ps.setInt(5,version);
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
	
	public static final String FIND_ALL = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + ";";
	public static ResultSet findAll() throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		return ps.executeQuery();
	}
	
	public static final String FIND_BY_PLAYER = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " WHERE challenger=? or challengee=?;";
	public static ResultSet findByPlayer(long player) throws SQLException {
    	Connection con = DbRegistry.getDbConnection();
		PreparedStatement ps = con.prepareStatement(FIND_BY_PLAYER);
		ps.setLong(1,player);
		ps.setLong(2,player);
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
