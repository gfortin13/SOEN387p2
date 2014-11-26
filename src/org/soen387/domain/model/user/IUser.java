package org.soen387.domain.model.user;

import org.dsrg.soenea.domain.interf.IDomainObject;

public interface IUser extends IDomainObject<Long>{

	public abstract String getUsername();

	public abstract void setUsername(String username);

	public abstract String getPassword();

	public abstract void setPassword(String password);


}