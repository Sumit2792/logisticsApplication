package com.fw.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.Fact;
import com.fw.beans.UserFactsBean;
import com.fw.config.AuthUserDetails;
import com.fw.dao.IUserFactsManager;
import com.fw.domain.UserFacts;
import com.fw.domain.Users;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.IUserFactsService;

@Service
public class UserFactsServiceImpl implements IUserFactsService {

	@Autowired
	IUserFactsManager userFactsManager;

	@Autowired
	private AuthUserDetails authUser;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUserFactsById(long factId) throws APIExceptions {

		userFactsManager.deleteUserFactsById(factId, false);
	}

	@Override
	@Transactional
	public List<UserFacts> getUserFactsById(long userId) throws APIExceptions {
		try {
			return userFactsManager.getUserFactsByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUserFacts(UserFactsBean facts) throws APIExceptions {

		validateUserFacts(facts);

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();

		if (user.getUserRole() != UserRoles.CSR && userId != facts.getUserId())
			throw new UnAuthorizedActionException("Unauthorized action.");

		HashMap<String, List<UserFacts>> factsMap = convertToUserFacts(userId, facts);
		Set<Entry<String, List<UserFacts>>> factsSet = factsMap.entrySet();

		for (Entry<String, List<UserFacts>> entry : factsSet) {
			String key = entry.getKey();
			List<UserFacts> userFactsList = entry.getValue();
			if (key.contains("ADD"))
				userFactsManager.persistUserFacts(userFactsList, false);
			else
				userFactsManager.updateUserFactsByUserId(userFactsList);
		}
	}

	private HashMap<String, List<UserFacts>> convertToUserFacts(long loggedInUserId, UserFactsBean facts)
			throws APIExceptions {
		HashMap<String, List<UserFacts>> map = new HashMap<>();
		List<UserFacts> addUserFactsList = new ArrayList<>();
		List<UserFacts> updateUserFactsList = new ArrayList<>();
		List<Fact> factList = facts.getFacts();

		for (Fact fact : factList) {

			UserFacts userFacts = new UserFacts();
			userFacts.setUserId(facts.getUserId());
			userFacts.setFact(fact.getFact());
			if (fact.getValue() == null || fact.getValue().trim().isEmpty())
				throw new APIExceptions(fact.getFact() + " can not be empty.");
			userFacts.setValue(fact.getValue());
			userFacts.setCreatedBy(loggedInUserId);
			userFacts.setModifiedBy(loggedInUserId);
			userFacts.setUserFactsId(fact.getUserFactsId());
			if (fact.getUserFactsId() != 0) {
				updateUserFactsList.add(userFacts);
				map.put("EDIT", updateUserFactsList);
			} else {
				addUserFactsList.add(userFacts);
				map.put("ADD", addUserFactsList);
			}
		}
		return map;
	}

	private void validateUserFacts(UserFactsBean facts) throws APIExceptions {
		if (facts == null)
			throw new APIExceptions("Invalid request");
		if (facts.getUserId() == 0)
			throw new APIExceptions("user id is missing.");
		List<Fact> factList = facts.getFacts();
		if (factList.isEmpty())
			throw new APIExceptions("user facts missing.");
	}

}
