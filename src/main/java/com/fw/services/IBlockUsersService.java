package com.fw.services;

import java.util.List;

import com.fw.domain.BlockUsers;
import com.fw.exceptions.APIExceptions;

public interface IBlockUsersService {

	BlockUsers addBlockUsers(BlockUsers bidEntity) throws APIExceptions;

	void updateBlockUsersById(BlockUsers bidEntity) throws APIExceptions;

	List<BlockUsers> getAllBlockUsers() throws APIExceptions;

	BlockUsers getBlockUsersById(long bidId) throws APIExceptions;

	void deleteBlockUsersById(BlockUsers userId) throws APIExceptions;
}
