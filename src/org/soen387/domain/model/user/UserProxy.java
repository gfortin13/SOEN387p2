package org.soen387.domain.model.user;

import java.sql.SQLException;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.soen387.domain.model.user.mapper.UserMapper;

public class UserProxy extends DomainObjectProxy<Long, User> implements IUser {
	
	public UserProxy(long id) {
		super(id);
	}


	@Override
	public String getUsername() {
		return getInnerObject().getUsername();
	}

	@Override
	public void setUsername(String username) {
		getInnerObject().setUsername(username);
	}

	@Override
	public String getPassword() {
		return getInnerObject().getPassword();
	}

	@Override
	public void setPassword(String password) {
		getInnerObject().setPassword(password);
	}


	// FIXME: try to have method not need try/catch and throw either Mapper/DomainObjectCreation Exception instead of SQL
	@Override
	protected User getFromMapper(Long id) throws MapperException,
			DomainObjectCreationException {
		User u = null;
		try {
			u = UserMapper.find(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}

}
