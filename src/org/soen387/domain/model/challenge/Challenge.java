package org.soen387.domain.model.challenge;

import org.soen387.domain.model.player.IPlayer;

public class Challenge implements IChallenge {
	long id;
	int version;
	IPlayer challenger;
	IPlayer challengee;
	ChallengeStatus status;

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public IPlayer getChallenger() {
		return challenger;
	}


	@Override
	public void setChallenger(IPlayer challenger) {
		this.challenger = challenger;
	}

	@Override
	public IPlayer getChallengee() {
		return challengee;
	}

	@Override
	public void setChallengee(IPlayer challengee) {
		this.challengee = challengee;
	}

	@Override
	public ChallengeStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(ChallengeStatus status) {
		this.status = status;
	}

	@Override
	public long getId() {
		return id;
	}
	public Challenge(long id, int version, IPlayer challenger,
			IPlayer challengee, ChallengeStatus status) {
		super();
		this.id = id;
		this.version = version;
		this.challenger = challenger;
		this.challengee = challengee;
		this.status = status;
	}
	
	
}
