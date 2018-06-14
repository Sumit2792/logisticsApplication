package com.fw.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.dao.IUserSourcesManager;
import com.fw.domain.UserSources;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IUserSourcesService;

@Service
public class UserSourcesServiceImpl implements IUserSourcesService {

	@Autowired
	IUserSourcesManager UserSourcesManager;
	
	@Override
	@Transactional
	public UserSources addUserSources(UserSources logEntity)  throws APIExceptions {
		if(logEntity!=null) {
			return UserSourcesManager.persistUserSources(logEntity);
		}
		else
			return null;
	}

	@Override
	@Transactional
	public void updateUserSourcesById(UserSources logEntity)  throws APIExceptions {
	
		UserSourcesManager.updateUserSourcesById(logEntity);
		
	}

	@Override
	@Transactional
	public void deleteUserSourcesById(UserSources id)  throws APIExceptions{

		UserSourcesManager.deleteUserSourcesById(id);
	}

	@Override
	@Transactional
	public List<UserSources> getAllUserSources() throws APIExceptions {
		
		return UserSourcesManager.getAllUserSourcesRowMapper();
	}

	@Override
	@Transactional
	public UserSources getUserSourcesById(long userSourceId) throws APIExceptions {
		// TODO Auto-generated method stub
		return UserSourcesManager.getUserSourcesById(userSourceId);
	}

}
