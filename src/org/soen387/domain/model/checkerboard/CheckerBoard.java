package org.soen387.domain.model.checkerboard;

import java.awt.Point;

import org.soen387.domain.model.player.IPlayer;

public class CheckerBoard implements ICheckerBoard {
	public static final String initialState = "b b b b  b b b bb b b b                  r r r rr r r r  r r r r";  
	
	public CheckerBoard(long id, int version, GameStatus status, IPlayer firstPlayer, IPlayer secondPlayer,
			IPlayer currentPlayer) {
		super();

		this.id = id;
		this.version = version;
		this.status = status;
		this.pieces = initialState;
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		this.currentPlayer = currentPlayer;
	}
	
	public CheckerBoard(long id, int version, GameStatus status,
			String pieces, IPlayer firstPlayer, IPlayer secondPlayer,
			IPlayer currentPlayer) {
		super();
		this.id = id;
		this.version = version;
		this.status = status;
		this.pieces = pieces;
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		this.currentPlayer = currentPlayer;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public GameStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(GameStatus status) {
		this.status = status;
	}

	@Override
	public String getPieces() {
		return pieces;
	}

	@Override
	public void setPieces(String pieces) {
		this.pieces = pieces;
	}

	@Override
	public IPlayer getFirstPlayer() {
		return firstPlayer;
	}

	@Override
	public void setFirstPlayer(IPlayer firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	@Override
	public IPlayer getSecondPlayer() {
		return secondPlayer;
	}

	@Override
	public void setSecondPlayer(IPlayer secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	@Override
	public IPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public void setCurrentPlayer(IPlayer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	@Override
	public long getId() {
		return id;
	}

	long id;
	int version;
	GameStatus status;
	String pieces;
	IPlayer firstPlayer;
	IPlayer secondPlayer;
	IPlayer currentPlayer;


	@Override
	public void move(Point source, Point target) {
		
	}
	
	@Override
	public void jump(Point source, Point... targets) {
		
	}
}
