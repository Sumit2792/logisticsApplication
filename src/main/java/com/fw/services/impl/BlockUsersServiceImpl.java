package com.fw.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.dao.IBlockUsersManager;
import com.fw.domain.BlockUsers;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IBlockUsersService;

@Service
public class BlockUsersServiceImpl implements IBlockUsersService {

	@Autowired
	IBlockUsersManager blockUsersManager;
	
	@Override
	@Transactional
	public BlockUsers addBlockUsers(BlockUsers logEntity)  throws APIExceptions {
		if(logEntity!=null) {
			return blockUsersManager.persistBlockUsers(logEntity);
		}
		else
			return null;
	}

	@Override
	@Transactional
	public void updateBlockUsersById(BlockUsers logEntity)  throws APIExceptions {
	
		blockUsersManager.updateBlockUsersById(logEntity);
		
	}

	@Override
	@Transactional
	public void deleteBlockUsersById(BlockUsers id)  throws APIExceptions{

		blockUsersManager.deleteBlockUsersById(id);
	}

	@Override
	@Transactional
	public List<BlockUsers> getAllBlockUsers() throws APIExceptions {
		
		return blockUsersManager.getAllBlockUsersRowMapper();
	}

	@Override
	@Transactional
	public BlockUsers getBlockUsersById(long bidId) throws APIExceptions {
		// TODO Auto-generated method stub
		return blockUsersManager.getBlockUsersById(bidId);
	}

}
