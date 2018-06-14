package com.fw.dao;

import java.util.List;

import com.fw.domain.BlockUsers;
import com.fw.exceptions.APIExceptions;

public interface IBlockUsersManager {

	/**
	 * Persist the object normally.
	 */
	BlockUsers persistBlockUsers(BlockUsers logEntity)  throws APIExceptions;

	void updateBlockUsersById(BlockUsers logEntity)  throws APIExceptions;

	List<BlockUsers> getAllBlockUsersRowMapper()  throws APIExceptions;

	BlockUsers getBlockUsersById(long bidId)  throws APIExceptions;

	void deleteBlockUsersById(BlockUsers Id)  throws APIExceptions;

}
