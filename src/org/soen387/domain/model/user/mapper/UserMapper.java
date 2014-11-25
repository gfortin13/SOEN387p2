package org.soen387.domain.model.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.user.IUser;
import org.soen387.domain.model.user.User;
import org.soen387.domain.model.user.tdg.UserTDG;

public class UserMapper {
	public static ThreadLocal<Map<Long, User>> IM = new ThreadLocal<Map<Long,User>>() {
		protected java.util.Map<Long,User> initialValue() {
			return new HashMap<Long, User>();
		};
	};
	
	public static void insert(IUser u) throws SQLException {
		UserTDG.insert(u.getId(), u.getVersion(), u.getUsername(), u.getPassword());
	}

	public static void update(IUser u) throws SQLException, LostUpdateException {
		int count = UserTDG.update(u.getId(), u.getVersion(), u.getUsername(), u.getPassword());
		if(count==0) throw new LostUpdateException("Lost Update editing user with id " + u.getId());
		u.setVersion(u.getVersion()+1);
	}
	
	public static void delete(IUser u) throws SQLException, LostUpdateException {
		int count = UserTDG.delete(u.getId(), u.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting user with id " + u.getId());
		//
		// What's the process for deleting a User... do we need to delete players and games?
		// More on that when we discuss referential integrity.
		//
	}
	
	
	public static User find(long id) throws SQLException, DomainObjectNotFoundException {
		User u = IM.get().get(id);
		if(u!=null) return u;
		
		ResultSet rs = UserTDG.find(id);
		if(rs.next()) {
			u = buildUser(rs);
			rs.close();
			IM.get().put(id, u);
			return u;
		}
		throw new DomainObjectNotFoundException("Could not create a user with id \""+id+"\"");
	}

	public static User find(String username, String password) throws SQLException, DomainObjectNotFoundException {
		User u = null;
		
		ResultSet rs = UserTDG.find(username, password);
		if(rs.next()) {
			Long id = rs.getLong("id");
			u = IM.get().get(id);
			if(u!=null) {
				rs.close();
				return u;
			}
			
			u = buildUser(rs);
			rs.close();
			IM.get().put(id, u);
			return u;
		}
		throw new DomainObjectNotFoundException("Could not create a user with that username and password combination.");
	}	
	

	private static User buildUser(ResultSet rs) throws SQLException  {
		// TODO Auto-generated method stub
		return new User(rs.getLong("id"),
				rs.getInt("version"),
				rs.getString("username"),
				""); //we don't actually dig out the password, that'd be cray-cray.
	}
	
}
