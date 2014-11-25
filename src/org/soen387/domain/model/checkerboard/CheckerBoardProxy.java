package org.soen387.domain.model.checkerboard;

import java.awt.Point;

import org.soen387.domain.model.checkerboard.mapper.CheckerBoardMapper;
import org.soen387.domain.model.player.IPlayer;


public class CheckerBoardProxy implements ICheckerBoard {
	long id;
	
	public CheckerBoardProxy(long id) {
		super();
		this.id = id;
	}

	public CheckerBoard getInner() {
		try {
			return CheckerBoardMapper.find(id);
		} catch (Exception e) {
			// It better be here! That null won't go over well!
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public int getVersion() {
		return getInner().getVersion();
	}

	@Override
	public void setVersion(int version) {
		getInner().setVersion(version);
	}


	@Override
	public long getId() {
		return id;
	}

	@Override
	public GameStatus getStatus() {
		return getInner().getStatus();
	}

	@Override
	public void setStatus(GameStatus status) {
		getInner().setStatus(status);
	}

	@Override
	public String getPieces() {
		return getInner().getPieces();
	}

	@Override
	public void setPieces(String pieces) {
		getInner().setPieces(pieces);
	}

	@Override
	public IPlayer getFirstPlayer() {
		return getInner().getFirstPlayer();
	}

	@Override
	public void setFirstPlayer(IPlayer firstPlayer) {
		getInner().setFirstPlayer(firstPlayer);
	}

	@Override
	public IPlayer getSecondPlayer() {
		return getInner().getSecondPlayer();
	}

	@Override
	public void setSecondPlayer(IPlayer secondPlayer) {
		getInner().setSecondPlayer(secondPlayer);
	}

	@Override
	public IPlayer getCurrentPlayer() {
		return getInner().getCurrentPlayer();
	}

	@Override
	public void setCurrentPlayer(IPlayer currentPlayer) {
		getInner().setCurrentPlayer(currentPlayer);
	}

	@Override
	public void move(Point source, Point target) {
		getInner().move(source, target);
	}

	@Override
	public void jump(Point source, Point... targets) {
		getInner().jump(source, targets);
	}

}
