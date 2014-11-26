package org.soen387.domain.model.checkerboard;

import java.awt.Point;
import java.sql.SQLException;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.soen387.domain.model.checkerboard.mapper.CheckerBoardMapper;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerMapper;


public class CheckerBoardProxy extends DomainObjectProxy<Long, CheckerBoard>implements ICheckerBoard {
	
	public CheckerBoardProxy(long id) {
		super(id);
	}

	@Override
	public GameStatus getStatus() {
		return getInnerObject().getStatus();
	}

	@Override
	public void setStatus(GameStatus status) {
		getInnerObject().setStatus(status);
	}

	@Override
	public String getPieces() {
		return getInnerObject().getPieces();
	}

	@Override
	public void setPieces(String pieces) {
		getInnerObject().setPieces(pieces);
	}

	@Override
	public IPlayer getFirstPlayer() {
		return getInnerObject().getFirstPlayer();
	}

	@Override
	public void setFirstPlayer(IPlayer firstPlayer) {
		getInnerObject().setFirstPlayer(firstPlayer);
	}

	@Override
	public IPlayer getSecondPlayer() {
		return getInnerObject().getSecondPlayer();
	}

	@Override
	public void setSecondPlayer(IPlayer secondPlayer) {
		getInnerObject().setSecondPlayer(secondPlayer);
	}

	@Override
	public IPlayer getCurrentPlayer() {
		return getInnerObject().getCurrentPlayer();
	}

	@Override
	public void setCurrentPlayer(IPlayer currentPlayer) {
		getInnerObject().setCurrentPlayer(currentPlayer);
	}

	@Override
	public void move(Point source, Point target) {
		getInnerObject().move(source, target);
	}

	@Override
	public void jump(Point source, Point... targets) {
		getInnerObject().jump(source, targets);
	}

	// FIXME: try to have method not need try/catch and throw either Mapper/DomainObjectCreation Exception instead of SQL
	@Override
	protected CheckerBoard getFromMapper(Long id) throws MapperException,
			DomainObjectCreationException {
		CheckerBoard c = null;
		try {
			c = CheckerBoardMapper.find(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

}
