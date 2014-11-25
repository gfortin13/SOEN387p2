package org.soen387.domain.model.challenge.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.challenge.Challenge;
import org.soen387.domain.model.challenge.ChallengeStatus;
import org.soen387.domain.model.challenge.IChallenge;
import org.soen387.domain.model.challenge.tdg.ChallengeTDG;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.PlayerProxy;

public class ChallengeMapper {
	public static ThreadLocal<Map<Long, Challenge>> IM = new ThreadLocal<Map<Long,Challenge>>() {
		protected java.util.Map<Long,Challenge> initialValue() {
			return new HashMap<Long, Challenge>();
		};
	};
	
	public static void insert(IChallenge c) throws SQLException {
		ChallengeTDG.insert(c.getId(), c.getVersion(), c.getChallenger().getId(), c.getChallengee().getId(), c.getStatus().getId());
	}

	public static void update(IChallenge c) throws SQLException, LostUpdateException {
		int count = ChallengeTDG.update(c.getId(), c.getVersion(), c.getChallenger().getId(), c.getChallengee().getId(), c.getStatus().getId());
		if(count==0) throw new LostUpdateException("Lost Update editing Challenge with id " + c.getId());
		c.setVersion(c.getVersion()+1);
	}
	
	public static void delete(IChallenge c) throws SQLException, LostUpdateException {
		int count = ChallengeTDG.delete(c.getId(), c.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting Challenge with id " + c.getId());

	}
	
	
	public static Challenge find(long id) throws SQLException, DomainObjectNotFoundException {
		Challenge c = IM.get().get(id);
		if(c!=null) return c;
		
		ResultSet rs = ChallengeTDG.find(id);
		if(rs.next()) {
			c = buildChallenge(rs);
			rs.close();
			IM.get().put(id, c);
			return c;
		}
		throw new DomainObjectNotFoundException("Could not create a Challenge with id \""+id+"\"");
	}

	public static List<IChallenge> buildCollection(ResultSet rs)
		    throws SQLException {
		    ArrayList<IChallenge> l = new ArrayList<IChallenge>();
		    while(rs.next()) {
		    	long id = rs.getLong("id");
		    	Challenge c = IM.get().get(id);
		    	if(c == null) {
		    		c = buildChallenge(rs);
		    		IM.get().put(id, c);
		    	}
		    	l.add(c);
		    }
		    return l;
		}

	public static List<IChallenge> find(IPlayer p) throws MapperException {
        try {
            ResultSet rs = ChallengeTDG.findByPlayer(p.getId());
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	public static List<IChallenge> findAll() throws MapperException {
        try {
            ResultSet rs = ChallengeTDG.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	

	private static Challenge buildChallenge(ResultSet rs) throws SQLException  {
		// TODO Auto-generated method stub
		return new Challenge(rs.getLong("id"),
				rs.getInt("version"),
				new PlayerProxy(rs.getLong("challenger")),
				new PlayerProxy(rs.getLong("challengee")),
				ChallengeStatus.values()[rs.getInt("status")]
				);
	}
	
}
