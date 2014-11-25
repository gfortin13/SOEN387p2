package org.soen387.domain.model.challenge;

public enum ChallengeStatus {
	Open,
	Accepted,
	Refused;
	
	//Let's make it play nice with Javabeans
	public int getId() { return this.ordinal(); }
}
