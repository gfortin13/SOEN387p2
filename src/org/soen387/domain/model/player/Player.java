package org.soen387.domain.model.player;

import org.soen387.domain.model.user.IUser;

public class Player implements IPlayer {
	long id;
	int version;
	String firstName;
	String lastName;
	String email;
	IUser user;

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	@Override
	public void setUser(IUser user) {
		this.user = user;
	}

	@Override
	public long getId() {
		return id;
	}

	public Player(long id, int version, String firstName, String lastName,
			String email, IUser user) {
		super();
		this.id = id;
		this.version = version;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.user = user;
	}

	@Override
	public boolean equals(Object p) {
		return p instanceof IPlayer && this.id==((IPlayer)(p)).getId();
	}

}
