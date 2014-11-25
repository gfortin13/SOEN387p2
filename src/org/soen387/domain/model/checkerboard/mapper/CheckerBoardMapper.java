package org.soen387.domain.model.checkerboard.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.LostUpdateException;
import org.soen387.domain.model.checkerboard.CheckerBoard;
import org.soen387.domain.model.checkerboard.GameStatus;
import org.soen387.domain.model.checkerboard.ICheckerBoard;
import org.soen387.domain.model.checkerboard.tdg.CheckerBoardTDG;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.PlayerProxy;

public class CheckerBoardMapper {
	public static ThreadLocal<Map<Long, CheckerBoard>> IM = new ThreadLocal<Map<Long,CheckerBoard>>() {
		protected java.util.Map<Long,CheckerBoard> initialValue() {
			return new HashMap<Long, CheckerBoard>();
		};
	};
	
	public static void insert(ICheckerBoard cb) throws SQLException {

		CheckerBoardTDG.insert(cb.getId(), cb.getVersion(), cb.getStatus().getId(), cb.getPieces(), cb.getFirstPlayer().getId(), cb.getSecondPlayer().getId(), cb.getCurrentPlayer().getId());
	}

	public static void update(ICheckerBoard cb) throws SQLException, LostUpdateException {
		int count = CheckerBoardTDG.update(cb.getId(), cb.getVersion(), cb.getStatus().getId(), cb.getPieces(), cb.getFirstPlayer().getId(), cb.getSecondPlayer().getId(), cb.getCurrentPlayer().getId());
		if(count==0) throw new LostUpdateException("Lost Update editing CheckerBoard with id " + cb.getId());
		cb.setVersion(cb.getVersion()+1);
	}
	
	public static void delete(ICheckerBoard cb) throws SQLException, LostUpdateException {
		int count = CheckerBoardTDG.delete(cb.getId(), cb.getVersion());
		if(count==0) throw new LostUpdateException("Lost Update deleting CheckerBoard with id " + cb.getId());
		//
		// What's the process for deleting a CheckerBoard... do we need to delete them?
		// More on that when we discuss referential integrity.
		//
	}
	
	public static List<ICheckerBoard> buildCollection(ResultSet rs)
		    throws SQLException {
		    ArrayList<ICheckerBoard> l = new ArrayList<ICheckerBoard>();
		    while(rs.next()) {
		    	long id = rs.getLong("id");
		    	CheckerBoard cb = IM.get().get(id);
		    	if(cb == null) {
		    		cb = buildCheckerBoard(rs);
		    		IM.get().put(id, cb);
		    	}
		    	l.add(cb);
		    }
		    return l;
		}

	public static List<ICheckerBoard> findAll() throws MapperException {
        try {
            ResultSet rs = CheckerBoardTDG.findAll();
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	public static List<ICheckerBoard> find(IPlayer p) throws MapperException {
        try {
            ResultSet rs = CheckerBoardTDG.findByPlayer(p.getId());
            return buildCollection(rs);
        } catch (SQLException e) {
            throw new MapperException(e);
        }
	}
	
	
	public static CheckerBoard find(long id) throws SQLException, DomainObjectNotFoundException {
		CheckerBoard p = IM.get().get(id);
		if(p!=null) return p;
		
		ResultSet rs = CheckerBoardTDG.find(id);
		if(rs.next()) {
			p = buildCheckerBoard(rs);
			rs.close();
			IM.get().put(id, p);
			return p;
		}
		throw new DomainObjectNotFoundException("Could not create a CheckerBoard with id \""+id+"\"");
	}

	private static CheckerBoard buildCheckerBoard(ResultSet rs) throws SQLException  {
		return new CheckerBoard(rs.getLong("id"),
        		rs.getInt("version"),
        		GameStatus.values()[rs.getInt("status")],
        		rs.getString("pieces"),
        		new PlayerProxy(rs.getLong("first_player")),
        		new PlayerProxy(rs.getLong("second_player")),
        		new PlayerProxy(rs.getLong("current_player"))
        		);
	}
}
