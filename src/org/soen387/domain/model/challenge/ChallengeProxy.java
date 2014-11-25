package org.soen387.domain.model.challenge;

import org.soen387.domain.model.challenge.mapper.ChallengeMapper;
import org.soen387.domain.model.player.IPlayer;

public class ChallengeProxy implements IChallenge{
	long id;
	
	public ChallengeProxy(long id) {
		super();
		this.id = id;
	}

	public Challenge getInner() {
		try {
			return ChallengeMapper.find(id);
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
	public IPlayer getChallenger() {
		return getInner().getChallenger();
	}

	@Override
	public void setChallenger(IPlayer challenger) {
		getInner().setChallenger(challenger);
		
	}

	@Override
	public IPlayer getChallengee() {
		return getInner().getChallengee();
	}

	@Override
	public void setChallengee(IPlayer challengee) {
		getInner().setChallengee(challengee);
		
	}

	@Override
	public ChallengeStatus getStatus() {
		return getInner().getStatus();
	}

	@Override
	public void setStatus(ChallengeStatus status) {
		getInner().setStatus(status);
	}

}
