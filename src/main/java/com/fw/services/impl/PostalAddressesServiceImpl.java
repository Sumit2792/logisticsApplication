package com.fw.services.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.FromToAddressBean;
import com.fw.dao.IPostalAddressesManager;
import com.fw.domain.PostalAddresses;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.services.IPostalAddressesService;
import com.fw.utils.LoadRequestUtils;;

@Service
public class PostalAddressesServiceImpl implements IPostalAddressesService {

	private Logger logger = Logger.getLogger(PostalAddressesServiceImpl.class);

	@Autowired
	IPostalAddressesManager postalAddressesManager;

	@Override
	@Transactional
	public void savePostalAddressesInfo(PostalAddresses postalAddresses) throws APIExceptions {
		postalAddressesManager.persist(postalAddresses);
	}

	@Override
	@Transactional
	public List<PostalAddresses> getPostalAddresses() {

		return postalAddressesManager.getAllPostalAddressesRowMapper();

	}

	@Override
	@Transactional
	public void deletePostalAddresses(PostalAddresses postalId) throws APIExceptions {
		postalAddressesManager.deletePostalAddressesById(postalId);
	}

	@Override
	@Transactional
	public void updatePostalAddresses(PostalAddresses postalId) throws APIExceptions {
		postalAddressesManager.updatePostalAddressesById(postalId);
	}

	@Override
	@Transactional
	public PostalAddresses getPostalAddressesInfoById(Long postalId) {
		return postalAddressesManager.getPostalAddressesById(postalId);
	}

	@Override
	public List<PostalAddresses> getPostalAddressesByUserId(long userId) throws APIExceptions {
		if (userId <= 0) {
			throw new BadRequestException("Invalid user id");
		}
		return postalAddressesManager.getPostalAddressesByUserId(userId);
	}

	@Override
	public Long savePostalAddresses(FromToAddressBean address) throws APIExceptions {
		Long addressId = null;
		try {
			if (address != null) 
				addressId = saveAddress(address, address.getUserId(), "Update");	
			if (addressId == null)
				throw new APIExceptions("Failed to save 'Update' Address.");
			return addressId;
		} catch (Exception e) {
			throw new APIExceptions("Failed to save Address.");
		}
	}

	private Long saveAddress(FromToAddressBean fromToAddressBean, long userId, String addressType)
			throws APIExceptions {

		PostalAddresses postalAddresses = LoadRequestUtils.getPostalAddressFromToAddressBean(fromToAddressBean,
				addressType);
		if (postalAddresses.getLocationJSON() == null)
			throw new BadRequestException(" Address is not valid");
		postalAddresses.setUserId((int) userId);
		postalAddresses.setCreatedBy((int) userId);
		postalAddresses.setModifiedBy((int) userId);
		postalAddresses.setDeleted(false);
		logger.info("dfvvbc");
		PostalAddresses address = postalAddressesManager.persist(postalAddresses);

		if (address.getPostalAddressId() != 0)
			return address.getPostalAddressId();

		return null;
	}

}
