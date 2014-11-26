package org.soen387.domain.model.user;

import org.dsrg.soenea.domain.DomainObject;

public class User extends DomainObject<Long> implements IUser {

	String username;
	String password;

	/* (non-Javadoc)
	 * @see org.soen387.domain.model.user.IUser#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}
	/* (non-Javadoc)
	 * @see org.soen387.domain.model.user.IUser#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	/* (non-Javadoc)
	 * @see org.soen387.domain.model.user.IUser#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}
	/* (non-Javadoc)
	 * @see org.soen387.domain.model.user.IUser#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	public User(long id, long version, String username, String password) {
		super(id, version);
		this.username = username;
		this.password = password;
	}
	
	
}
