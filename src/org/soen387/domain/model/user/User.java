package org.soen387.domain.model.user;

public class User implements IUser {

	long id;
	int version;
	String username;
	String password;
	/* (non-Javadoc)
	 * @see org.soen387.domain.model.user.IUser#getVersion()
	 */
	@Override
	public int getVersion() {
		return version;
	}
	/* (non-Javadoc)
	 * @see org.soen387.domain.model.user.IUser#setVersion(int)
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}
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
	/* (non-Javadoc)
	 * @see org.soen387.domain.model.user.IUser#getId()
	 */
	@Override
	public long getId() {
		return id;
	}
	public User(long id, int version, String username, String password) {
		super();
		this.id = id;
		this.version = version;
		this.username = username;
		this.password = password;
	}
	
	
}
