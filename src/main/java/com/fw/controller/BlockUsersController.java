package com.fw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.domain.BlockUsers;
import com.fw.exceptions.APIExceptions;

public interface BlockUsersController {

	void addBlockUsers(BlockUsers unit) throws APIExceptions;

	ResponseEntity<List<BlockUsers>> getBlockUsers() throws APIExceptions;

	ResponseEntity<BlockUsers> updateBlockUsersById(BlockUsers bidForm) throws APIExceptions;

	ResponseEntity<BlockUsers> getBlockUsersById(long bidId) throws APIExceptions;

	void removeUser(BlockUsers deleteBlockUser) throws APIExceptions;

}
