package org.soen387.domain.model.player;

import java.sql.SQLException;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.soen387.domain.model.player.mapper.PlayerMapper;
import org.soen387.domain.model.user.IUser;

public class PlayerProxy extends DomainObjectProxy<Long, Player> implements IPlayer {
	
	public PlayerProxy(long id) {
		super(id);
	}

	@Override
	public String getFirstName() {
		return getInnerObject().getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
		getInnerObject().setFirstName(firstName);
	}

	@Override
	public String getLastName() {
		return getInnerObject().getLastName();
	}

	@Override
	public void setLastName(String lastName) {
		getInnerObject().setLastName(lastName);
	}

	@Override
	public String getEmail() {
		return getInnerObject().getEmail();
	}

	@Override
	public void setEmail(String email) {
		getInnerObject().setEmail(email);
	}

	@Override
	public IUser getUser() {
		return getInnerObject().getUser();
	}

	@Override
	public void setUser(IUser user) {
		getInnerObject().setUser(user);
	}

	@Override
	public boolean equals(Object p) {
		return p instanceof IPlayer && getId()==((IPlayer)(p)).getId();
	}

	// FIXME: try to have method not need try/catch and throw either Mapper/DomainObjectCreation Exception instead of SQL
	@Override
	protected Player getFromMapper(Long id) throws MapperException,
			DomainObjectCreationException {
		Player p = null;
		try {
			p = PlayerMapper.find(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
}
