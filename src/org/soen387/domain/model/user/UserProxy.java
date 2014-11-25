package org.soen387.domain.model.user;

import org.soen387.domain.model.user.mapper.UserMapper;

public class UserProxy implements IUser {
	long id;
	
	public UserProxy(long id) {
		super();
		this.id = id;
	}

	public User getInner() {
		try {
			return UserMapper.find(id);
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
	public String getUsername() {
		return getInner().getUsername();
	}

	@Override
	public void setUsername(String username) {
		getInner().setUsername(username);
	}

	@Override
	public String getPassword() {
		return getInner().getPassword();
	}

	@Override
	public void setPassword(String password) {
		getInner().setPassword(password);
	}

	@Override
	public long getId() {
		return id;
	}

}
