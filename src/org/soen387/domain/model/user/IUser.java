package org.soen387.domain.model.user;

public interface IUser {

	public abstract int getVersion();

	public abstract void setVersion(int version);

	public abstract String getUsername();

	public abstract void setUsername(String username);

	public abstract String getPassword();

	public abstract void setPassword(String password);

	public abstract long getId();

}