package com.fw.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.AddressBean;
import com.fw.beans.BidsBean;
import com.fw.beans.DashBoardBean;
import com.fw.beans.LoadAndBidRequestBean;
import com.fw.beans.LoadPackageDetailsBean;
import com.fw.beans.LoadRequestDetailsBean;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.beans.NotesBean;
import com.fw.beans.PaymentFilters;
import com.fw.beans.PaymentHistoryBean;
import com.fw.beans.ResetFilterBean;
import com.fw.beans.UserDashBoardBean;
import com.fw.beans.UserLoadRequestBean;
import com.fw.config.AuthUserDetails;
import com.fw.dao.IBidManager;
import com.fw.dao.IDashboardManager;
import com.fw.dao.ILoadPackageManager;
import com.fw.dao.ILoadRequestCitiesManager;
import com.fw.dao.ILoadRequestDetailManager;
import com.fw.dao.ILoadRequestManager;
import com.fw.dao.IPostalAddressesManager;
import com.fw.dao.IRequestNotesManager;
import com.fw.dao.IUserManager;
import com.fw.domain.LoadRequest;
import com.fw.domain.PostalAddresses;
import com.fw.domain.Users;
import com.fw.enums.InBetweenCitiesExist;
import com.fw.enums.LoadRequestStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidIdException;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.IDashboardService;
import com.fw.utils.LoadRequestUtils;
import com.fw.utils.MLogisticsPropertyReader;
import com.fw.validations.DataValidations;

/**
 * 
 * @author Faber
 *
 */
@Service
public class DashboardServiceImpl implements IDashboardService {

	@Autowired
	private IDashboardManager dashBoardManager;
	@Autowired
	private ILoadRequestManager loadRequestManager;
	@Autowired
	private ILoadRequestDetailManager loadRequestDetailManager;
	@Autowired
	private ILoadRequestCitiesManager loadRequestCitiesManager;
	@Autowired
	private ILoadPackageManager loadPackageManager;
	@Autowired
	private IBidManager bidManager;
	@Autowired
	IPostalAddressesManager postalManager;
	@Autowired
	private IUserManager userManager;
	@Autowired
	private IRequestNotesManager reqNotesManager;
	@Autowired
	private AuthUserDetails authUser;
	@Autowired
	private DataValidations dataValidations;

	@Autowired
	private MLogisticsPropertyReader mLogiticsPropertyReader;

	private Logger log = Logger.getLogger(DashboardServiceImpl.class);

	@Override
	@Transactional
	public DashBoardBean getDashboardByFilters(ResetFilterBean resetFilterBean) throws APIExceptions {
		String country = null;
		String state = null;
		String city = null;
		String bidCount = null;
		InBetweenCitiesExist inBetweenCitiesExist = null;
		String cityExistFilter = null;
		// resetFilterBean=null;
		if (resetFilterBean != null) {
			country = resetFilterBean.getCountry();
			state = resetFilterBean.getState();
			city = resetFilterBean.getCity();
			bidCount = resetFilterBean.getBidCount();
			cityExistFilter = resetFilterBean.getInBetweenCitiesExist();
			if (cityExistFilter != null) {
				cityExistFilter = cityExistFilter.trim();
				try {
					inBetweenCitiesExist = InBetweenCitiesExist.fromString(cityExistFilter);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		if (inBetweenCitiesExist == null)
			inBetweenCitiesExist = InBetweenCitiesExist.ALL;

		DashBoardBean dashboard = getDashboard(country, state, city, bidCount, inBetweenCitiesExist);

		return dashboard;
	}

	/**
	 * 
	 * @param country
	 * @param state
	 * @param city
	 * @param bidCount
	 * @param cityExistFilter
	 * @return
	 */
	private DashBoardBean getDashboard(String country, String state, String city, String bidCount,
			InBetweenCitiesExist cityExistFilter) {

		//int targetTotalBids = 0;
		List<LoadRequest> requestList = dashBoardManager.getAllLoadRequestByRegion(country, state, city,
				cityExistFilter);

		List<LoadAndBidRequestBean> dashBoardLoadRequestList = new ArrayList<>();
		DashBoardBean dashboard = new DashBoardBean();

		if (requestList != null && requestList.size() > 0) {
			for (LoadRequest loadRequest : requestList) {

				if (loadRequest.getLoadRequestStatus() != null) {
					if (loadRequest.getLoadRequestStatus() == LoadRequestStatus.EXPIRED)
						continue;
				}
				List<BidsBean> bids = dashBoardManager.getBidsByLoadRequestId(loadRequest.getLoadRequestId());
				// log.info("total bids " + bids.size());
			/*	if (bidCount != null && !"".equals(bidCount)) {
					targetTotalBids = Integer.valueOf(bidCount).intValue();
					if (targetTotalBids < 9 && !(targetTotalBids == bids.size())) {
						continue;
					}
				}*/

				List<LoadPackageDetailsBean> loadPackageList = loadPackageManager
						.getLoadPackageDetailsByRequestId(loadRequest.getLoadRequestId());
				LoadRequestDetailsBean loadRequestDetail = loadRequestDetailManager
						.getLoadRequestDetailByRequestId(loadRequest.getLoadRequestId());

				LoadAndBidRequestBean lRequest = new LoadAndBidRequestBean();

				if (loadRequestDetail != null && loadRequestDetail.getLoadRequestId() != 0) {
					if (loadRequest.getLoadRequestStatus() != null && loadRequestDetail.getStartDateTime() != null) {
						if (LoadRequestUtils.isRequestExpired(loadRequestDetail.getStartDateTime(),
								loadRequest.getLoadRequestStatus())) {
							loadRequest.setLoadRequestStatus(LoadRequestStatus.EXPIRED);
							try {
								// create status data
								LoadRequestStatusBean loadStatus = new LoadRequestStatusBean();
								loadStatus.setModifiedBy(loadRequest.getCreatedBy());
								loadStatus.setStatus(LoadRequestStatus.EXPIRED.getValue());
								loadStatus.setLoadRequestId(loadRequest.getLoadRequestId());
								loadRequestManager.updateLoadRequestStatus(loadStatus);
							} catch (Exception e) {
								log.error("Failed to update status, message : " + e.getMessage(), e);
							}

							continue;
						}
					}

					lRequest.setLoadRequestDetails(loadRequestDetail);
					PostalAddresses from = postalManager.getPostalAddressesById(loadRequestDetail.getStartLocationId());
					PostalAddresses to = postalManager.getPostalAddressesById(loadRequestDetail.getEndLocationId());
					
					String startLocation = null;
					String endLocation = null;
					

					if (from != null && to != null) {
						if(from.getArea() == null || to.getArea() == null) {
							if(from.getArea() == null) {
								startLocation = LoadRequestUtils.getCityStateCountry(from.getCity(), from.getState(), from.getCountry());
								endLocation = LoadRequestUtils.getAreaCityStateCountry(to.getArea(), to.getCity(), to.getState(), to.getCountry());	
							} 
							if(to.getArea() == null) {
								startLocation = LoadRequestUtils.getAreaCityStateCountry(from.getArea(), from.getCity(), from.getState(), from.getCountry());
								endLocation = LoadRequestUtils.getCityStateCountry(to.getCity(), to.getState(), to.getCountry());	
							} 
							if(from.getArea() == null && to.getArea() == null) {
								startLocation = LoadRequestUtils.getCityStateCountry(from.getCity(), from.getState(), from.getCountry());
								endLocation = LoadRequestUtils.getCityStateCountry(to.getCity(), to.getState(), to.getCountry());	
							}
						} else {
							startLocation = LoadRequestUtils.getAreaCityStateCountry(from.getArea(), from.getCity(), from.getState(), from.getCountry());
							endLocation = LoadRequestUtils.getAreaCityStateCountry(to.getArea(), to.getCity(), to.getState(), to.getCountry());
						}

						loadRequestDetail.setStartLocation(startLocation);
						loadRequestDetail.setEndLocation(endLocation);
					} else {
						log.warn("-------no city spcified-----------");
					}
				}
				lRequest.setLoadRequestId(loadRequest.getLoadRequestId());
				if(loadRequest.getTruckType()!=null)
					lRequest.setTruckType(loadRequest.getTruckType().toDbString());
				
				lRequest.setUserId((loadRequest.getUserId()));
				List<NotesBean> requestNotes = reqNotesManager.getAllNotesByRequestId(loadRequest.getLoadRequestId());
				lRequest.setNotes(requestNotes);
				lRequest.setBidStart(loadRequest.getBiddingStartDatetime());
				lRequest.setBidEnd(loadRequest.getBiddingEndDatetime());
				lRequest.setInsuranceNeeded(loadRequest.isInsuranceNeeded());
				lRequest.setCreatedBy(loadRequest.getCreatedBy());
				lRequest.setModifiedBy(loadRequest.getModifiedBy());
				lRequest.setCreatedDate(loadRequest.getCreatedDate());
				lRequest.setModifiedDate(loadRequest.getModifiedDate());

				if (loadPackageList != null) {
					lRequest.setLoadPackageList(loadPackageList);
				}

				lRequest.setStatus(loadRequest.getLoadRequestStatus());
				lRequest.setBids(bids);

				dashBoardLoadRequestList.add(lRequest);

			}
			dashboard.setDashboard((dashBoardLoadRequestList));
		}
		return dashboard;

	}


	@Override
	@Transactional
	public UserDashBoardBean getUserDashBoard(long userId) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long loggedInUserId = user.getUserId();
		UserRoles userRole = user.getUserRole();

		UserDashBoardBean dashboard = new UserDashBoardBean();

		if (userRole != UserRoles.CSR && loggedInUserId != userId) {
			throw new UnAuthorizedActionException("Not Authorized user");
		}

		List<LoadRequest> requestList = null;
		if (userRole == UserRoles.CSR && userId == loggedInUserId)
			requestList = dashBoardManager.getAllLoadRequestCreateByCSRId(loggedInUserId); // get Request created by CSR
		else
			requestList = dashBoardManager.getAllLoadRequestByUserId(userId);

		List<LoadAndBidRequestBean> dashBoardLoadRequestList = new ArrayList<>();
		List<UserLoadRequestBean> dashBoardBidList = new ArrayList<>();

		for (LoadRequest loadRequest : requestList) {

			List<LoadPackageDetailsBean> loadPackageList = loadPackageManager
					.getLoadPackageDetailsByRequestId(loadRequest.getLoadRequestId());
			LoadRequestDetailsBean loadRequestDetail = loadRequestDetailManager
					.getLoadRequestDetailByRequestId(loadRequest.getLoadRequestId());

			List<BidsBean> bidsAndRequests = bidManager.getBidsByLoadRequestId(loadRequest.getLoadRequestId());

			LoadAndBidRequestBean lRequest = new LoadAndBidRequestBean();

			if (loadRequestDetail != null && loadRequestDetail.getLoadRequestId() != 0) {
				lRequest.setLoadRequestDetails(loadRequestDetail);
				// log.info("id : ==" + loadRequestDetail.getLoadRequestId());
				PostalAddresses from = postalManager.getPostalAddressesById(loadRequestDetail.getStartLocationId());
				PostalAddresses to = postalManager.getPostalAddressesById(loadRequestDetail.getEndLocationId());
				
				

				if (from != null && to != null) {
					String startLocation = LoadRequestUtils.getCityStateCountry(from.getCity(), from.getState(), from.getCountry());
					String endLocation = LoadRequestUtils.getCityStateCountry(to.getCity(), to.getState(), to.getCountry());
					loadRequestDetail.setStartLocation(startLocation);
					loadRequestDetail.setEndLocation(endLocation);
				} else {
					log.warn("-------no city spcified-----------");
				}
			}
			lRequest.setLoadRequestId(loadRequest.getLoadRequestId());
			if(loadRequest.getTruckType()!=null)
				lRequest.setTruckType(loadRequest.getTruckType().toDbString());
			lRequest.setUserId((loadRequest.getUserId()));
			List<NotesBean> requestNotes = reqNotesManager.getAllNotesByRequestId(loadRequest.getLoadRequestId());
			lRequest.setNotes(requestNotes);
			lRequest.setBidStart(loadRequest.getBiddingStartDatetime());
			lRequest.setBidEnd(loadRequest.getBiddingEndDatetime());
			lRequest.setInsuranceNeeded(loadRequest.isInsuranceNeeded());
			lRequest.setCreatedBy(loadRequest.getCreatedBy());
			lRequest.setModifiedBy(loadRequest.getModifiedBy());
			lRequest.setCreatedDate(loadRequest.getCreatedDate());
			lRequest.setModifiedDate(loadRequest.getModifiedDate());

			if (loadPackageList != null) {
				lRequest.setLoadPackageList(loadPackageList);
			}

			lRequest.setStatus(loadRequest.getLoadRequestStatus());
			lRequest.setBids(bidsAndRequests);

			dashBoardLoadRequestList.add(lRequest);

		}
		dashboard.setDashboard((dashBoardLoadRequestList));

		// get users bid
		List<BidsBean> bidByLoadId = new ArrayList<>();
		List<BidsBean> bidList = null;
		if (userRole == UserRoles.CSR && userId == loggedInUserId)
			bidList = dashBoardManager.getAllBidsCreatedByCSRId(loggedInUserId);
		else
			bidList = dashBoardManager.getAllBidsByUserId(userId);
		for (BidsBean bids : bidList) {
			UserLoadRequestBean associatedLoadRequest = getBidsAssociatedLoads(bids.getLoadRequestId());
			if (associatedLoadRequest != null) {
				bidByLoadId.add(bids);
				associatedLoadRequest.setBids(bidByLoadId);
				dashBoardBidList.add(associatedLoadRequest);
			}

		}
		dashboard.setBidDashboard(dashBoardBidList);

		return dashboard;

	}

	private UserLoadRequestBean getBidsAssociatedLoads(long loadRequestId) throws APIExceptions {

		LoadRequest loadRequestDB = loadRequestManager.getLoadRequestById(loadRequestId);
		if (loadRequestDB == null)
			throw new InvalidIdException("Load request is not found. Id: " + loadRequestId);

		LoadRequest loadRequest = loadRequestManager.getLoadRequestById(loadRequestId);
		List<LoadPackageDetailsBean> loadPackageList = loadPackageManager
				.getLoadPackageDetailsByRequestId(loadRequestId);
		LoadRequestDetailsBean loadRequestDetail = loadRequestDetailManager
				.getLoadRequestDetailByRequestId(loadRequestId);

		log.info("loadpackage size" + loadPackageList.size());

		AddressBean address = new AddressBean();

		if (loadRequestDetail != null) {

			try {
				PostalAddresses from = postalManager.getPostalAddressesById(loadRequestDetail.getStartLocationId());
				if (from != null) {
				//	address.setFrom(from);
					loadRequestDetail
					.setStartLocation(LoadRequestUtils.getCityStateCountry(from.getCity(), from.getState(), from.getCountry()));
					loadRequestDetail
							.setStartLocation(LoadRequestUtils.getCityStateCountry(from.getCity(), from.getState(), from.getCountry()));
				}
				PostalAddresses to = postalManager.getPostalAddressesById(loadRequestDetail.getEndLocationId());
				if (to != null) {
				//	address.setTo(to);
					loadRequestDetail.setEndLocation(LoadRequestUtils.getCityStateCountry(to.getCity(), to.getState(), to.getCountry()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new APIExceptions(e.getMessage());
			}
		}

		UserLoadRequestBean loadRequestBean = new UserLoadRequestBean();

		loadRequestBean.setLoadRequestId(loadRequestId);
		loadRequestBean.setUserId((loadRequest.getUserId()));
		// loadRequestBean.setNote(loadRequest.getNotes());
		List<NotesBean> requestNotes = reqNotesManager.getAllNotesByRequestId(loadRequest.getLoadRequestId());
		loadRequestBean.setNotes((requestNotes));
		loadRequestBean.setBidStart(loadRequest.getBiddingStartDatetime());
		loadRequestBean.setStatus(loadRequest.getLoadRequestStatus());
		loadRequestBean.setBidEnd(loadRequest.getBiddingEndDatetime());
		loadRequestBean.setInsuranceNeeded(loadRequest.isInsuranceNeeded());
		if(loadRequest.getTruckType()!=null)
			loadRequestBean.setTruckType(loadRequest.getTruckType().toDbString());
		loadRequestBean.setCreatedBy(loadRequest.getCreatedBy());
		loadRequestBean.setModifiedBy(loadRequest.getModifiedBy());
		loadRequestBean.setCreatedDate(loadRequest.getCreatedDate());
		loadRequestBean.setModifiedDate(loadRequest.getModifiedDate());
		loadRequestBean.setLoadRequestDetails((loadRequestDetail));
		loadRequestBean.setLoadPackageList(loadPackageList);
		loadRequestBean.setAddress(address);
		return loadRequestBean;
	}

	@Override
	@Transactional
	public List<PaymentHistoryBean> getPaymentHistory(PaymentFilters filters) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		UserRoles userRole = user.getUserRole();
		
		// target user for that CSR want to check payment history
		Users targetUser = null;
		
		if(filters == null ) filters = new PaymentFilters();

		if (AuthUserDetails.getInternalRoles().containsValue(userRole))
		{
			if(filters.getUserName() != null)
			{
				dataValidations.phoneNumberValidation(filters.getUserName());
				targetUser = userManager.getUserByUserName(filters.getUserName());
				if( targetUser != null) filters.setUserId(targetUser.getUserId());
			}
			return dashBoardManager.getPaymentHistory(filters);
		}
		else
		{
			throw new UnAuthorizedActionException("Unauthorized Access.");
		}
		
		

	}

}
