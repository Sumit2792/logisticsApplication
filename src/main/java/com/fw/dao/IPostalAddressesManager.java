package com.fw.dao;

import java.util.List;

import com.fw.beans.FromToAddressBean;
import com.fw.beans.MarkatingAddressBean;
import com.fw.domain.PostalAddresses;
import com.fw.exceptions.APIExceptions;

public interface IPostalAddressesManager {

	/**
	 * Persist the object normally.
	 * @return 
	 * @throws APIExceptions 
	 */
	public PostalAddresses persist(PostalAddresses postalAddressesEntity) throws APIExceptions;

	void updatePostalAddressesById(PostalAddresses postalAddressesEntity) throws APIExceptions;

	void deletePostalAddressesById(PostalAddresses id) throws APIExceptions;

	List<PostalAddresses> getAllPostalAddressesRowMapper();

	PostalAddresses getPostalAddressesById(Long id);

	List<PostalAddresses> getPostalAddressesByUserId(Long userId);
	
	List<PostalAddresses> getPostalAddressesByIdList(List<Long> ids);

	List<MarkatingAddressBean> getPostalAddressesLinesByUserId(Long userId); 
}
