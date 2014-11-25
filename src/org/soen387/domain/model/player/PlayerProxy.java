package org.soen387.domain.model.player;

import org.soen387.domain.model.player.mapper.PlayerMapper;
import org.soen387.domain.model.user.IUser;

public class PlayerProxy implements IPlayer {
	long id;
	
	public PlayerProxy(long id) {
		super();
		this.id = id;
	}

	public Player getInner() {
		try {
			return PlayerMapper.find(id);
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
	public String getFirstName() {
		return getInner().getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
		getInner().setFirstName(firstName);
	}

	@Override
	public String getLastName() {
		return getInner().getLastName();
	}

	@Override
	public void setLastName(String lastName) {
		getInner().setLastName(lastName);
	}

	@Override
	public String getEmail() {
		return getInner().getEmail();
	}

	@Override
	public void setEmail(String email) {
		getInner().setEmail(email);
	}

	@Override
	public IUser getUser() {
		return getInner().getUser();
	}

	@Override
	public void setUser(IUser user) {
		getInner().setUser(user);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public boolean equals(Object p) {
		return p instanceof IPlayer && this.id==((IPlayer)(p)).getId();
	}
	
}
