package com.fw.daemon;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import com.fw.beans.AddressBean;
import com.fw.dao.impl.LoadRequestCitiesManagerImpl;
import com.fw.utils.LocationUtils;

public class FindCitiesAndInsertInDB implements Runnable{
	
	public final static Logger logger = Logger.getLogger(FindCitiesAndInsertInDB.class);
	
	@Autowired
	private LoadRequestCitiesManagerImpl loadRequestCitiesManagerImpl;
	
	private AddressBean address;
	
	private Long loadRequestId;
	
	private String fromCity;
	
	private String toCity;
	
	public void setAddress(AddressBean address) {
		this.address = address;
	}

	public void setLoadRequestId(Long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	@Override
	public void run() {		
		try {
		JSONArray inBetweenCitiesJson = LocationUtils.getCityNames(address);
		loadRequestCitiesManagerImpl.insertLoadRequestCitiesByLoadRequestId(fromCity, toCity,
				inBetweenCitiesJson, loadRequestId);
		}
		catch(Exception e){
			logger.error("There is a problem in getting cities between ["+fromCity+"] and ["+toCity+"] for loadRequestId=["+loadRequestId+"]");
		}		
	}
}
