package org.soen387.domain.model.challenge;

import org.soen387.domain.model.player.IPlayer;

public interface IChallenge {

	public abstract int getVersion();

	public abstract void setVersion(int version);

	public abstract IPlayer getChallenger();

	public abstract void setChallenger(IPlayer challenger);

	public abstract IPlayer getChallengee();

	public abstract void setChallengee(IPlayer challengee);

	public abstract ChallengeStatus getStatus();

	public abstract void setStatus(ChallengeStatus status);

	public abstract long getId();

}