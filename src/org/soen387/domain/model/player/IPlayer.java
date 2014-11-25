package org.soen387.domain.model.player;

import org.soen387.domain.model.user.IUser;

public interface IPlayer {

	public abstract int getVersion();

	public abstract void setVersion(int version);

	public abstract String getFirstName();

	public abstract void setFirstName(String firstName);

	public abstract String getLastName();

	public abstract void setLastName(String lastName);

	public abstract String getEmail();

	public abstract void setEmail(String email);

	public abstract IUser getUser();

	public abstract void setUser(IUser user);

	public abstract long getId();

}