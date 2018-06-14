package com.fw.services;

import java.util.List;

import com.fw.beans.FromToAddressBean;
import com.fw.domain.PostalAddresses;
import com.fw.exceptions.APIExceptions;

public interface IPostalAddressesService {

	void savePostalAddressesInfo(PostalAddresses postalAddressesEntity) throws APIExceptions;

	List<PostalAddresses> getPostalAddresses()throws APIExceptions;

	void deletePostalAddresses(PostalAddresses postalAddressesEntity)throws APIExceptions;

	void updatePostalAddresses(PostalAddresses postalAddressesEntity)throws APIExceptions;

	PostalAddresses getPostalAddressesInfoById(Long postalAddressesEntity)throws APIExceptions;

	List<PostalAddresses> getPostalAddressesByUserId(long userId) throws APIExceptions;
	
	Long savePostalAddresses(FromToAddressBean postalAddressesEntity) throws APIExceptions;
}
