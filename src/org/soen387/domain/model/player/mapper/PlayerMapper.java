package org.soen387.domain.model.player.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.tdg.PlayerTDG;
import org.soen387.domain.model.user.IUser;
import org.soen387.domain.model.user.UserProxy;

public class PlayerMapper {
	public static ThreadLocal<Map<Long, Player>> IM = new ThreadLocal<Map<Long,Player>>() {
		protected java.util.Map<Long,Player> initialValue() {
			return new HashMap<Long, Player>();
		};
	};
	
	public static void insert(IPlayer p) throws SQLException {
		PlayerTDG.insert(p.getId(), p.getVersion(), p.getFirstName(), p.getLastName(), p.getEmail(), p.getUser().getId());
	}

	public static void update(IPlayer p) throws SQLException, LostUpdateException {
		int count = PlayerTDG.update(p.getId(), p.getVersion(), p.getFirstName(), p.getLastName(), p.getEmail(), p.getUser().getId());
		if(count==0) throw new LostUpdateException("Lost Update editing player with id " + p.getId());
		p.setVersion(p.getVersion()+1);
	}
	
	public static void delete(IPlayer p) throws SQLException, LostUpdateException {
		int count = PlayerTDG.delete(p.getId(), p.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting player with id " + p.getId());
		//
		// What's the process for deleting a Player... do we need to delete users and games?
		// More on that when we discuss referential integrity.
		//
	}

	public static Player find(long id) throws SQLException, DomainObjectNotFoundException {
		Player p = IM.get().get(id);
		if(p!=null) return p;
		
		ResultSet rs = PlayerTDG.find(id);
		if(rs.next()) {
			p = buildPlayer(rs);
			rs.close();
			IM.get().put(id, p);
			return p;
		}
		throw new DomainObjectNotFoundException("Could not create a Player with id \""+id+"\"");
	}
	
	public static Player find(IUser u) throws SQLException, DomainObjectNotFoundException {
		ResultSet rs = PlayerTDG.findByUser(u.getId());
		if(rs.next()) {
			long id = rs.getLong("id");
			Player p = IM.get().get(id);
			if(p!=null) return p;
			p = buildPlayer(rs);
			rs.close();
			IM.get().put(id, p);
			return p;
		}
		throw new DomainObjectNotFoundException("Could not create a Player from User with id \""+u.getId()+"\"");
	}

	public static List<IPlayer> buildCollection(ResultSet rs)
		    throws SQLException {
		    ArrayList<IPlayer> l = new ArrayList<IPlayer>();
		    while(rs.next()) {
		    	long id = rs.getLong("id");
		    	Player c = IM.get().get(id);
		    	if(c == null) {
		    		c = buildPlayer(rs);
		    		IM.get().put(id, c);
		    	}
		    	l.add(c);
		    }
		    return l;
		}

	public static List<IPlayer> findAll() throws MapperException {
        try {
            ResultSet rs = PlayerTDG.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	private static Player buildPlayer(ResultSet rs) throws SQLException  {
		// TODO Auto-generated method stub
		return new Player(rs.getLong("id"),
				rs.getLong("version"),
				rs.getString("firstname"),
				rs.getString("lastname"),
				rs.getString("email"),
				new UserProxy(rs.getLong("user"))
				);
	}
	
}
