package org.soen387.domain.model.challenge;

import java.sql.SQLException;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.soen387.domain.model.challenge.mapper.ChallengeMapper;
import org.soen387.domain.model.player.IPlayer;

public class ChallengeProxy extends DomainObjectProxy<Long, Challenge> implements IChallenge{
	
	public ChallengeProxy(long id) {
		super(id);
	}

	@Override
	public IPlayer getChallenger() {
		return getInnerObject().getChallenger();
	}

	@Override
	public void setChallenger(IPlayer challenger) {
		getInnerObject().setChallenger(challenger);
		
	}

	@Override
	public IPlayer getChallengee() {
		return getInnerObject().getChallengee();
	}

	@Override
	public void setChallengee(IPlayer challengee) {
		getInnerObject().setChallengee(challengee);
		
	}

	@Override
	public ChallengeStatus getStatus() {
		return getInnerObject().getStatus();
	}

	@Override
	public void setStatus(ChallengeStatus status) {
		getInnerObject().setStatus(status);
	}

	// FIXME: try to have method not need try/catch and throw either Mapper/DomainObjectCreation Exception instead of SQL
	@Override
	protected Challenge getFromMapper(Long id) throws MapperException,
			DomainObjectCreationException {
		Challenge c = null;
		try {
			c = ChallengeMapper.find(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

}
