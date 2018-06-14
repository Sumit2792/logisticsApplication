package com.fw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.beans.FromToAddressBean;
import com.fw.domain.PostalAddresses;
import com.fw.exceptions.APIExceptions;

public interface PostalAddressesController {

	void savePostalAddressesInfo(PostalAddresses postalAddressesForm) throws APIExceptions;

	ResponseEntity<List<PostalAddresses>> getAllPostalAddresses() throws APIExceptions;

	void removePostalAddresses(PostalAddresses deletePostalAddresses) throws APIExceptions;

	void modifyPostalAddressesByPostalId(PostalAddresses postalAddressesForm) throws APIExceptions;

	ResponseEntity<PostalAddresses> getPostalAddressesInfoById(Long id) throws APIExceptions;

	ResponseEntity<List<PostalAddresses>> getPostalAddressesByUserId(long userId) throws APIExceptions;
	
	ResponseEntity<Long> savePostalAddresses(FromToAddressBean postalAddressesEntity) throws APIExceptions;
}
