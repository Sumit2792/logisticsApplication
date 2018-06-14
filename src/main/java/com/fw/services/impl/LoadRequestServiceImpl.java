package com.fw.services.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.AddLoadRequest;
import com.fw.beans.AddressBean;
import com.fw.beans.BidStatusBean;
import com.fw.beans.BidsBean;
import com.fw.beans.FromToAddressBean;
import com.fw.beans.FromToAddressComponent;
import com.fw.beans.LoadPackageDetailsBean;
import com.fw.beans.LoadRequestBean;
import com.fw.beans.LoadRequestDetailsBean;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.beans.NotesBean;
import com.fw.beans.OnBehalfOfUserBean;
import com.fw.config.AuthUserDetails;
import com.fw.daemon.FindCitiesAndInsertInDB;
import com.fw.dao.IBidManager;
import com.fw.dao.IConfigManager;
import com.fw.dao.ILoadPackageManager;
import com.fw.dao.ILoadRequestDetailManager;
import com.fw.dao.ILoadRequestManager;
import com.fw.dao.IRequestNotesManager;
import com.fw.dao.IUserManager;
import com.fw.dao.impl.PostalAddressesManagerImpl;
import com.fw.domain.LoadRequest;
import com.fw.domain.LoadRequestDetail;
import com.fw.domain.LoadRequestNotes;
import com.fw.domain.LoadRequestPackage;
import com.fw.domain.PostalAddresses;
import com.fw.domain.Users;
import com.fw.enums.AddressComponentTypes;
import com.fw.enums.BidStatus;
import com.fw.enums.LoadRequestStatus;
import com.fw.enums.TruckType;
import com.fw.enums.UserLoginStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.exceptions.InvalidActionException;
import com.fw.exceptions.InvalidIdException;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.ILoadRequestService;
import com.fw.utils.DateTimeUtils;
import com.fw.utils.LoadRequestUtils;
import com.fw.utils.LocalUtils;
import com.fw.utils.MLogisticsPropertyReader;
import com.fw.utils.MessageTemplateConstants;
import com.fw.validations.DataValidations;

@Service
public class LoadRequestServiceImpl implements ILoadRequestService {

	public final static Logger logger = Logger.getLogger(LoadRequestServiceImpl.class);

	@Autowired
	private ILoadRequestManager loadRequestManager;
	@Autowired
	private ILoadPackageManager loadPackageManager;
	@Autowired
	private ILoadRequestDetailManager loadRequestDetailManager;
	@Autowired
	private IUserManager userManager;
	@Autowired
	private IBidManager bidManager;
	@Autowired
	private IRequestNotesManager reqNotesManager;
	@Autowired
	private AuthUserDetails authUser;
	@Autowired
	private PostalAddressesManagerImpl postalAddressManager;
	@Autowired
	private LoadRequestUtils loadRequestUtils;
	@Autowired
	private IConfigManager configManager;
	@Autowired
	private MLogisticsPropertyReader mLogiticsPropertyReader;

	@Autowired
	private DataValidations dataValidations;

	String getCityFromAddress = null;
	String getCityToAddress = null;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AddLoadRequest addLoadRequest(AddLoadRequest loadRequest) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		OnBehalfOfUserBean onBehalfUserData = null;
		if (userRole == UserRoles.CSR) {

			onBehalfUserData = loadRequest.getRequestOnBehalfOfUser();
			if (onBehalfUserData == null)
				throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "phoneNumberRequired"));

			dataValidations.phoneNumberValidation(onBehalfUserData.getPhoneNumber());
		}
		validateLoadRequest(loadRequest);

		LoadRequestDetail loadReqDetail = loadRequest.getLoadRequestDetails();

		List<LoadRequestPackage> loadRequestPackage = loadRequest.getLoadPackageList();

		Long loadRequestId = null; // from DB

		loadRequest.setCreatedBy(userId);
		loadRequest.setModifiedBy(userId);
		loadRequest.setUserId(userId);
		loadRequest.setStatus(LoadRequestStatus.REQUESTED);

		AddressBean address = loadRequest.getAddress();
		Long fromAddressId = null;
		Long toAddressId = null;
		String cityFromAddress = null, cityToAddress = null;
		try {
			if (address != null) {

				fromAddressId = saveFromToAddress(address.getFrom(), userId, "From :");
				toAddressId = saveFromToAddress(address.getTo(), userId, "To :");

				FromToAddressComponent[] fromAddressComponent = address.getFrom().getLocationJSON()
						.getAddress_components();
				cityFromAddress = LoadRequestUtils.getAddressComponentByType(fromAddressComponent,
						AddressComponentTypes.administrative_area_level_2.toDbString());
				FromToAddressComponent[] toAddressComponent = address.getTo().getLocationJSON().getAddress_components();
				cityToAddress = LoadRequestUtils.getAddressComponentByType(toAddressComponent,
						AddressComponentTypes.administrative_area_level_2.toDbString());
			}
			if (toAddressId == null)
				throw new APIExceptions("Failed to save 'To' Address.");
			if (fromAddressId == null)
				throw new APIExceptions("Failed to save 'From' Address.");

		} catch (Exception e) {
			e.printStackTrace();
			throw new APIExceptions(e.getMessage());
		}

		LocalDateTime nowUTC = DateTimeUtils.getInstantDateTimeWithZoneId("UTC");
		if (loadRequest.getBidStart() == null) {
			nowUTC = (configManager.getConfigMap().get("defaultBiddingStartWaitTimeInMins") != null)
					? nowUTC.plusMinutes(Integer
							.valueOf((String) configManager.getConfigMap().get("defaultBiddingStartWaitTimeInMins")))
					: nowUTC.plusMinutes(mLogiticsPropertyReader.getDefaultBiddingStartWaitTime());
			loadRequest.setBidStart(DateTimeUtils.convertLocalDateTimeToUtilDate("UTC", nowUTC));
		}
		if (loadRequest.getBidEnd() == null && loadReqDetail.getExpectedEndDateTime() != null) {
			LocalDateTime bidEnd = DateTimeUtils.convertUtilDateToLocalDateTime("UTC",
					loadReqDetail.getExpectedEndDateTime());
			bidEnd = (configManager.getConfigMap().get("defaultBiddingEndWaitTimeInMins") != null)
					? bidEnd.minusMinutes(Integer
							.valueOf((String) configManager.getConfigMap().get("defaultBiddingEndWaitTimeInMins")))
					: bidEnd.minusMinutes(mLogiticsPropertyReader.getDefaultBiddingEndtWaitTime());
			loadRequest.setBidEnd(DateTimeUtils.convertLocalDateTimeToUtilDate("UTC", bidEnd));
		}

		if (userRole == UserRoles.LOAD_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.CAPACITY_PROVIDER) {

			loadRequestId = loadRequestManager.insertLoadRequest(loadRequest);

		} else if (userRole == UserRoles.CSR) {

			onBehalfUserData = loadRequest.getRequestOnBehalfOfUser();

			if (onBehalfUserData != null && onBehalfUserData.getPhoneNumber() != null) {

				boolean isUserExist = userManager.isUserExist(onBehalfUserData.getPhoneNumber().trim());

				if (!isUserExist) {
					Users newuser = new Users();
					newuser.setUserName(onBehalfUserData.getPhoneNumber().trim());
					newuser.setUserLoginStatus(UserLoginStatus.CREATED);
					if (onBehalfUserData.getUserRole() == null || onBehalfUserData.getUserRole() == UserRoles.CSR) {
						throw new APIExceptions("Invalid user role.");
					}
					newuser.setUserRole(onBehalfUserData.getUserRole());
					newuser.setCreatedBy(userId);
					newuser.setModifiedBy(userId);
					Long newLodaerId = userManager.persist(newuser);
					loadRequest.setUserId(newLodaerId); // set bidder id
				}
				Users onBehalfUser = userManager.getUserByUserName(onBehalfUserData.getPhoneNumber());
				loadRequest.setUserId(onBehalfUser.getUserId());
				loadRequestId = loadRequestManager.insertLoadRequest(loadRequest);
			} else {
				throw new APIExceptions(
						"CSR can not create load request unitl he/she specify/provide on bhalf loader.");
			}

		} else {
			throw new UnAuthorizedActionException("Unauthorized User."); // pending -> extract this anonymous user
																			// info

		}

		/*
		 * JSONArray inBetweenCitiesJson = LocationUtils.getCityNames(address);
		 * loadRequestCitiesManagerImpl.insertLoadRequestCitiesByLoadRequestId(
		 * getCityFromAddress, getCityToAddress, inBetweenCitiesJson, loadRequestId);
		 */

		if (loadRequestId != null && cityFromAddress != null && cityToAddress != null) {

			FindCitiesAndInsertInDB findCities = new FindCitiesAndInsertInDB();
			findCities.setAddress(address);
			findCities.setFromCity(cityFromAddress);
			findCities.setToCity(cityToAddress);
			findCities.setLoadRequestId(loadRequestId);
			/*
			 * Thread distrbuteEventProcessorThread = new Thread(findCities);
			 * distrbuteEventProcessorThread.start();
			 */
		}

		// Add load details
		loadReqDetail.setCreatedBy(userId);
		loadReqDetail.setModifiedBy(userId);
		loadReqDetail.setLoadRequestId(loadRequestId);
		loadReqDetail.setStartLocationId(fromAddressId);
		loadReqDetail.setEndLocationId(toAddressId);
		loadRequestDetailManager.persist(loadReqDetail);
		// Add load package
		for (LoadRequestPackage loadRequestPackage2 : loadRequestPackage) {

			if (loadRequestPackage2.getPackageCount() == 0)
				loadRequestPackage2.setPackageCount(1);

			loadRequestPackage2.setCreatedBy(userId);
			loadRequestPackage2.setModifiedBy(userId);
			loadRequestPackage2.setLoadRequestId(loadRequestId);
			loadPackageManager.persist(loadRequestPackage2);
		}
		if (loadRequest.getNote() != null) {
			createNotes(userId, loadRequestId, loadRequest.getNote());

		}
		loadRequest.setLoadRequestId(loadRequestId);
		return loadRequest;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateLoadRequestById(AddLoadRequest loadRequest) throws APIExceptions {

		validateLoadRequest(loadRequest);
		LoadRequest loadRequestDB = null;

		if (loadRequest.getLoadRequestId() == 0)
			throw new APIExceptions("Request id is required.");

		LoadRequestDetail loadReqDetail = loadRequest.getLoadRequestDetails();
		List<LoadRequestPackage> loadRequestPackage = loadRequest.getLoadPackageList();

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		loadRequest.setModifiedBy(userId);

		loadRequestDB = loadRequestManager.getLoadRequestById(loadRequest.getLoadRequestId());
		if (loadRequestDB == null)
			throw new APIExceptions("Load request is not found. Id: " + loadRequest.getLoadRequestId());

		// check edit allowed or not
		LoadRequestUtils.canEditLoadRequest(loadRequestDB);
		// update bid start and bid end time

		LocalDateTime nowUTC = DateTimeUtils.getInstantDateTimeWithZoneId("UTC");
		if (loadRequest.getBidStart() == null) {
			nowUTC = (configManager.getConfigMap().get("defaultBiddingStartWaitTimeInMins") != null)
					? nowUTC.plusMinutes(Integer
							.valueOf((String) configManager.getConfigMap().get("defaultBiddingStartWaitTimeInMins")))
					: nowUTC.plusMinutes(mLogiticsPropertyReader.getDefaultBiddingStartWaitTime());
			loadRequest.setBidStart(DateTimeUtils.convertLocalDateTimeToUtilDate("UTC", nowUTC));
		}
		if (loadRequest.getBidEnd() == null && loadReqDetail.getExpectedEndDateTime() != null) {
			LocalDateTime bidEnd = DateTimeUtils.convertUtilDateToLocalDateTime("UTC",
					loadReqDetail.getExpectedEndDateTime());
			bidEnd = (configManager.getConfigMap().get("defaultBiddingEndWaitTimeInMins") != null)
					? bidEnd.minusMinutes(Integer
							.valueOf((String) configManager.getConfigMap().get("defaultBiddingEndWaitTimeInMins")))
					: bidEnd.minusMinutes(mLogiticsPropertyReader.getDefaultBiddingEndtWaitTime());
			loadRequest.setBidEnd(DateTimeUtils.convertLocalDateTimeToUtilDate("UTC", bidEnd));
		}

		if (userRole == UserRoles.LOAD_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.CAPACITY_PROVIDER) {
			if (loadRequest.getLoadRequestId() != 0) {
				// found bidId for logged in user

				if (loadRequestDB.getUserId() == userId) {

					loadRequestManager.updateLoadRequestById(loadRequest);
					loadPackageManager.deleteLoadPackages(loadRequest.getLoadRequestId());

				} else {
					throw new UnAuthorizedActionException("You are not authorized to act on load request.");
				}
			} else {
				throw new APIExceptions("request id is missing.");
			}
		} else if (userRole == UserRoles.CSR) {
			loadPackageManager.deleteLoadPackages(loadRequest.getLoadRequestId());
			loadRequestManager.updateLoadRequestById(loadRequest);

		}

		// update load request details
		loadReqDetail.setModifiedBy(userId);
		loadRequestDetailManager.updateLoadRequestDetailByRequestId(loadReqDetail, loadRequest.getLoadRequestId());

		// update load package details

		for (LoadRequestPackage loadRequestPackage2 : loadRequestPackage) {

			if (loadRequestPackage2.getPackageCount() == 0)
				loadRequestPackage2.setPackageCount(1);

			loadRequestPackage2.setCreatedBy(userId);
			loadRequestPackage2.setModifiedBy(userId);
			loadRequestPackage2.setLoadRequestId(loadRequest.getLoadRequestId());
			loadPackageManager.persist(loadRequestPackage2);
		}
		AddressBean address = loadRequest.getAddress();
		logger.info("address------------------------------" + address.getFrom().getPostalAddressId());
		FromToAddressComponent[] fromAddressComponent = null;
		FromToAddressComponent[] toAddressComponent = null;
		if (loadReqDetail != null) {

			LoadRequestDetail requestDetail = loadRequestDetailManager
					.getLoadRequestDetailById(loadRequest.getLoadRequestId());
			logger.info("Start location Id------------------------------" + requestDetail.getStartLocationId());
			try {

				if (address != null) {

					updateFromToAddress(address.getFrom(), userId, requestDetail.getStartLocationId(), "From : ");
					updateFromToAddress(address.getTo(), userId, requestDetail.getEndLocationId(), "To :");
					if (address.getFrom().getLocationJSON() != null) {
						fromAddressComponent = address.getFrom().getLocationJSON().getAddress_components();
						getCityFromAddress = LoadRequestUtils.getAddressComponentByType(fromAddressComponent,
								AddressComponentTypes.administrative_area_level_2.toDbString());
					}
					if (address.getTo().getLocationJSON() != null) {
						toAddressComponent = address.getTo().getLocationJSON().getAddress_components();
						getCityToAddress = LoadRequestUtils.getAddressComponentByType(toAddressComponent,
								AddressComponentTypes.administrative_area_level_2.toDbString());
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new APIExceptions(e.getMessage());
			}
		}
		if (loadRequest.getNote() != null) {

			createNotes(userId, loadRequest.getLoadRequestId(), loadRequest.getNote());
		}

		loadRequestUtils.sendMessageToAllBidders(loadRequestDB,
				MessageTemplateConstants.TO_CAPACITY_PROVIDER_AFTER_REQUEST_IS_EDIT, false);

	}

	@Override
	@Transactional
	public void deleteLoadRequest(Long id) throws APIExceptions {

		if (id == null)
			throw new APIExceptions("Invalid request id.");

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();

		LoadRequest loadRequestDB = loadRequestManager.getLoadRequestById(id);
		if (loadRequestDB == null)
			throw new APIExceptions("Load request is not found. Id: " + id);

		if (userRole == UserRoles.LOAD_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.CAPACITY_PROVIDER) {

			if (loadRequestDB.getUserId() == userId) {
				loadRequestManager.deleteLoadRequest(id);
			} else {
				throw new UnAuthorizedActionException("You are not authorized to act on load request.");
			}
		} else if (userRole == UserRoles.CSR) {
			loadRequestManager.deleteLoadRequest(id);
		} else {
			throw new UnAuthorizedActionException("Unauthorized User.");
		}
		loadRequestUtils.sendMessageToAllBidders(loadRequestDB,
				MessageTemplateConstants.TO_ALL_CAPACITY_PROVIDERS_AFTER_LOAD_REQUEST_CANCLLED, false);
	}

	@Override
	@Transactional
	public LoadRequestBean getLoadRequestById(Long loadRequestId) throws APIExceptions {

		if (loadRequestId == null) {
			throw new APIExceptions("Load request id is null");
		}

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		Long requestId = null;

		LoadRequest loadRequest = loadRequestManager.getLoadRequestById(loadRequestId);
		if (loadRequest == null)
			throw new InvalidIdException("Load request is not found. Id: " + loadRequestId);

		requestId = loadRequest.getLoadRequestId();

		if (userRole == UserRoles.LOAD_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.CAPACITY_PROVIDER) {

			if (requestId != loadRequestId && userId != loadRequest.getUserId()) {

				throw new UnAuthorizedActionException("User not authorized to act on this load request.");
			}
		}

		List<LoadPackageDetailsBean> loadPackageList = loadPackageManager
				.getLoadPackageDetailsByRequestId(loadRequestId);
		LoadRequestDetailsBean loadRequestDetail = loadRequestDetailManager
				.getLoadRequestDetailByRequestId(loadRequestId);

		logger.info("loadpackage size" + loadPackageList.size());

		AddressBean address = new AddressBean();
		FromToAddressBean fromAddress = new FromToAddressBean();
		FromToAddressBean toAddress = new FromToAddressBean();

		if (loadRequestDetail != null) {

			try {
				PostalAddresses fromPostalAddress = postalAddressManager
						.getPostalAddressesById(loadRequestDetail.getStartLocationId());
				if (fromPostalAddress != null) {
					fromAddress.setAddressLine1(fromPostalAddress.getAddressLine1());
					// fromAddress.setLocationJSON(fromPostalAddress.getLocationJSON());
					address.setFrom(fromAddress);
					loadRequestDetail.setStartLocation(LoadRequestUtils.getAreaCityStateCountry(
							fromPostalAddress.getArea(), fromPostalAddress.getCity(), fromPostalAddress.getState(),
							fromPostalAddress.getCountry()));
				}
				PostalAddresses toPostalAddress = postalAddressManager
						.getPostalAddressesById(loadRequestDetail.getStartLocationId());
				if (toPostalAddress != null) {
					toAddress.setAddressLine1(toPostalAddress.getAddressLine1());
					// toAddress.setLocationJSON(toPostalAddress.getLocationJSON());
					address.setTo(toAddress);
					loadRequestDetail.setEndLocation(LoadRequestUtils.getAreaCityStateCountry(toPostalAddress.getArea(),
							toPostalAddress.getCity(), toPostalAddress.getState(), toPostalAddress.getCountry()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new APIExceptions(e.getMessage());
			}
		}
		List<BidsBean> bids = bidManager.getBidsByLoadRequestId(loadRequest.getLoadRequestId());

		LoadRequestBean loadRequestBean = new LoadRequestBean();

		loadRequestBean.setLoadRequestId(loadRequestId);
		loadRequestBean.setUserId((loadRequest.getUserId()));
		// loadRequestBean.setNote(loadRequest.getNotes());
		List<NotesBean> requestNotes = reqNotesManager.getAllNotesByRequestId(loadRequest.getLoadRequestId());
		loadRequestBean.setNotes((requestNotes));
		loadRequestBean.setBidStart(loadRequest.getBiddingStartDatetime());
		loadRequestBean.setStatus(loadRequest.getLoadRequestStatus());
		loadRequestBean.setBidEnd(loadRequest.getBiddingEndDatetime());
		loadRequestBean.setInsuranceNeeded(loadRequest.isInsuranceNeeded());
		if (loadRequest.getTruckType() != null)
			loadRequestBean.setTruckType(loadRequest.getTruckType().toDbString());
		loadRequestBean.setCreatedBy(loadRequest.getCreatedBy());
		loadRequestBean.setModifiedBy(loadRequest.getModifiedBy());
		loadRequestBean.setCreatedDate(loadRequest.getCreatedDate());
		loadRequestBean.setModifiedDate(loadRequest.getModifiedDate());
		loadRequestBean.setLoadRequestDetails((loadRequestDetail));
		loadRequestBean.setLoadPackageList(loadPackageList);
		loadRequestBean.setAddress(address);
		loadRequestBean.setBids(bids);
		return loadRequestBean;

	}

	private Long saveFromToAddress(FromToAddressBean fromToAddressBean, long userId, String addressType)
			throws APIExceptions {

		PostalAddresses postalAddresses = LoadRequestUtils.getPostalAddressFromToAddressBean(fromToAddressBean,
				addressType);
		if (postalAddresses.getLocationJSON() == null)
			throw new BadRequestException(" Address is not valid");
		postalAddresses.setUserId((int) userId);
		PostalAddresses from = postalAddressManager.persist(postalAddresses);

		if (from.getPostalAddressId() != 0)
			return from.getPostalAddressId();

		return null;
	}

	private void updateFromToAddress(FromToAddressBean address, long userId, long postalAddressId, String addressType)
			throws APIExceptions {

		if (postalAddressId == 0) {
			throw new InvalidIdException("[" + addressType + "]  Address ID is missing.");
		}

		PostalAddresses postalAddresses = LoadRequestUtils.getPostalAddressFromToAddressBean(address, addressType);
		if (postalAddresses.getLocationJSON() == null)
			throw new BadRequestException(" Address is not valid");

		postalAddresses.setPostalAddressId(postalAddressId);
		postalAddresses.setUserId((int) userId);
		postalAddressManager.updatePostalAddressesById(postalAddresses);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateLoadRequestStatus(LoadRequestStatusBean loadStatus) throws APIExceptions, SQLException {

		if (loadStatus == null)
			throw new BadRequestException("Invalid request.");

		if (loadStatus.getLoadRequestId() == 0)
			throw new BadRequestException("Load request id is required.");

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();

		LoadRequest loadRequest = loadRequestManager.getLoadRequestById(loadStatus.getLoadRequestId());
		if (loadRequest == null)
			throw new InvalidIdException("Load Request not found.");

		LoadRequestDetail loadRequestDetail = loadRequestDetailManager
				.getLoadRequestDetailById(loadRequest.getLoadRequestId());

		LoadRequestStatus currentLoadStatus = loadRequest.getLoadRequestStatus();

		loadStatus.setModifiedBy(userId);

		LoadRequestStatus statusToBeChanged = LoadRequestStatus.fromString(loadStatus.getStatus());

		if (userRole == UserRoles.LOAD_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.CAPACITY_PROVIDER) {

			if (loadRequest.getUserId() != userId)
				throw new UnAuthorizedActionException("You are not authorised to act on this request.");
		}

		switch (statusToBeChanged) {

		case CANCELLED:

			if (isRequestCancellable(currentLoadStatus, statusToBeChanged, userRole)) {

				updateBidStatusForCancelRequest(userId, loadStatus.getLoadRequestId(),
						BidStatus.LOAD_REQUEST_CANCELLED);
				loadRequestUtils.sendMessageToAllBidders(loadRequest,
						MessageTemplateConstants.TO_ALL_CAPACITY_PROVIDERS_AFTER_LOAD_REQUEST_CANCLLED, false);
				loadRequestUtils.sendMessageToLoader(loadRequest,
						MessageTemplateConstants.TO_LOAD_PROVIDER_AFTER_LOAD_REQUEST_CANCLLED);
			}
			break;

		case REQUESTED:

			if (currentLoadStatus == LoadRequestStatus.AWARDED || currentLoadStatus == LoadRequestStatus.DELIVERED
					|| currentLoadStatus == LoadRequestStatus.BOOKED
					|| currentLoadStatus == LoadRequestStatus.BIDDING_IN_PROGRESS) {
				throw new UnAuthorizedActionException("Request can not be changed as 'REQUESTED'");
			}
			break;
		case BLOCKED_REQUEST:
			if (isRequestBlockable(currentLoadStatus, statusToBeChanged, userRole)) {
				loadRequestUtils.sendMessageToAllBidders(loadRequest,
						MessageTemplateConstants.TO_LP_and_CPs_AFTER_REQUEST_BLOCK, false);
				loadRequestUtils.sendMessageToLoader(loadRequest,
						MessageTemplateConstants.TO_LP_and_CPs_AFTER_REQUEST_BLOCK);
			}
			break;
		case BIDDING_IN_PROGRESS:
			if (canBiddingInProgressForRequest(currentLoadStatus, statusToBeChanged, userRole)) {
				// NO SMS
			}
			break;
		case REQUEST_ON_HOLD:
			if (canPutRquestOnHold(currentLoadStatus, statusToBeChanged, userRole)) {
				loadRequestUtils.sendMessageToLoader(loadRequest,
						MessageTemplateConstants.TO_LOAD_PROVIDER_WHEN_LOAD_REQUEST_GOES_ON_HOLD);
			}
			break;
		case EXPIRED:
			if (!(LoadRequestUtils.isRequestExpired(loadRequestDetail.getStartDateTime(), currentLoadStatus)))
				throw new BadRequestException("Request can not be EXPIRED.");

			loadRequestUtils.sendMessageToLoader(loadRequest, "");
			break;
		case RESUME:
			statusToBeChanged = canResumeRequest(currentLoadStatus, loadRequest, userRole);
			loadRequestUtils.sendMessageToLoader(loadRequest,
					MessageTemplateConstants.TO_LOAD_PROVIDER_AFTER_REQUEST_IS_RESUME);
			break;
		default:
			throw new InvalidActionException("Invalid Action.");

		}

		loadStatus.setStatus(statusToBeChanged.getValue());// set again to avoid case sensitivity
		loadRequestManager.updateLoadRequestStatus(loadStatus);

	}

	/**
	 * 
	 * @param currentLoadStatus
	 * @param loadRequest
	 * @param userRole
	 * @return
	 * @throws APIExceptions
	 */
	private LoadRequestStatus canResumeRequest(LoadRequestStatus currentLoadStatus, LoadRequest loadRequest,
			UserRoles userRole) throws APIExceptions {

		BidsBean awardedBid = bidManager.getAwardedBidByLoadRequestId(loadRequest.getLoadRequestId());

		if (awardedBid != null && currentLoadStatus == LoadRequestStatus.REQUEST_ON_HOLD) {

			return LoadRequestStatus.AWARDED;

		} else if (currentLoadStatus == LoadRequestStatus.REQUEST_ON_HOLD) {

			return LoadRequestStatus.BIDDING_IN_PROGRESS;

		} else if (currentLoadStatus == LoadRequestStatus.BLOCKED_REQUEST && userRole == UserRoles.CSR) {

			return LoadRequestStatus.BIDDING_IN_PROGRESS;

		} else {
			throw new InvalidActionException("Invalid Action : Request can not be resumed.");
		}

	}

	private boolean isRequestCancellable(LoadRequestStatus currentLoadStatus, LoadRequestStatus targetStatus,
			UserRoles role) throws InvalidActionException {

		HashMap<UserRoles, UserRoles> userRoles = AuthUserDetails.getNonAdminUserRoles();

		if (currentLoadStatus == LoadRequestStatus.DELIVERED || currentLoadStatus == LoadRequestStatus.BOOKED
				|| currentLoadStatus == LoadRequestStatus.BLOCKED_REQUEST) {

			throw new InvalidActionException("Invalid Action: " + currentLoadStatus + " Request can not be CANCELLED.");

		} else if (currentLoadStatus == LoadRequestStatus.REQUEST_ON_HOLD
				|| currentLoadStatus == LoadRequestStatus.BIDDING_IN_PROGRESS
				|| currentLoadStatus == LoadRequestStatus.AWARDED && userRoles.containsValue(role)) {

			throw new InvalidActionException("Invalid Action: " + currentLoadStatus + " Request can not be CANCELLED.");
		}
		return true;
	}

	/**
	 * Methods to check that user can block the Request or not.
	 * 
	 * @param currentLoadStatus
	 * @param targetStatus
	 * @param role
	 * @return
	 * @throws UnAuthorizedActionException
	 */
	private boolean isRequestBlockable(LoadRequestStatus currentLoadStatus, LoadRequestStatus targetStatus,
			UserRoles role) throws UnAuthorizedActionException, InvalidActionException {

		HashMap<UserRoles, UserRoles> adminRoles = AuthUserDetails.getInternalRoles();

		if (currentLoadStatus != LoadRequestStatus.BIDDING_IN_PROGRESS
				&& currentLoadStatus != LoadRequestStatus.REQUESTED) {
			throw new InvalidActionException(
					"Invalid Action: Request can not be BLOCKED. Current status : " + currentLoadStatus);

		}
		if (!adminRoles.containsValue(role)) {
			throw new UnAuthorizedActionException(
					"UnAuthorized Access : You are not authorized to block this request.");
		}
		return true;
	}

	/**
	 * 
	 * @param currentLoadStatus
	 * @param targetStatus
	 * @param role
	 * @return
	 * @throws InvalidActionException
	 */
	private boolean canBiddingInProgressForRequest(LoadRequestStatus currentLoadStatus, LoadRequestStatus targetStatus,
			UserRoles role) throws InvalidActionException {

		HashMap<UserRoles, UserRoles> adminRoles = AuthUserDetails.getInternalRoles();
		if ((currentLoadStatus != LoadRequestStatus.REQUESTED || currentLoadStatus != LoadRequestStatus.REQUEST_ON_HOLD)
				&& (currentLoadStatus != LoadRequestStatus.BLOCKED_REQUEST)) {
			throw new InvalidActionException(
					"Invalid Action : Request status can not be changed to BIDDING_IN_PROGRESS.");

		}
		if (!adminRoles.containsValue(role) && currentLoadStatus == LoadRequestStatus.BLOCKED_REQUEST) {
			throw new InvalidActionException(
					"Invalid Action : Request status can not be changed to BIDDING_IN_PROGRESS.");

		}
		return true;
	}

	/**
	 * 
	 * @param currentLoadStatus
	 * @param targetStatus
	 * @param role
	 * @return
	 * @throws InvalidActionException
	 */
	private boolean canPutRquestOnHold(LoadRequestStatus currentLoadStatus, LoadRequestStatus targetStatus,
			UserRoles role) throws InvalidActionException {

		if (currentLoadStatus == LoadRequestStatus.BIDDING_IN_PROGRESS || currentLoadStatus == LoadRequestStatus.AWARDED
				|| currentLoadStatus == LoadRequestStatus.REQUESTED) {
			return true;
		} else {
			throw new InvalidActionException(
					"Invalid Action : Request can not put on HOLD. current status : " + currentLoadStatus);
		}
	}

	/**
	 * 
	 * @param userId
	 * @param loadRequestId
	 * @param status
	 * @throws APIExceptions
	 */
	private void updateBidStatusForCancelRequest(Long userId, long loadRequestId, BidStatus status)
			throws APIExceptions {
		List<BidsBean> bidList = bidManager.getBidsByLoadRequestId(loadRequestId);
		List<BidStatusBean> bidsStatusList = new ArrayList<>();
		for (BidsBean bidsBean : bidList) {

			BidStatusBean bidsStatus = new BidStatusBean();
			bidsStatus.setBidId(bidsBean.getBidId());
			bidsStatus.setModifiedBy(userId);
			bidsStatus.setStatus(status.toDbString());
			bidsStatusList.add(bidsStatus);
		}
		bidManager.batchUpdateBidStatus(bidsStatusList);

	}

	private void validateLoadRequest(AddLoadRequest loadRequest) throws BadRequestException {
		if (loadRequest == null)
			throw new BadRequestException("load request is not valid.");

		String truckType = loadRequest.getTruckType();
		if (truckType != null) {
			TruckType.fromString(truckType);
		}
		LoadRequestDetail loadReqDetail = loadRequest.getLoadRequestDetails();

		if (loadReqDetail != null) {

			if (loadReqDetail.getStartDateTime() == null)
				throw new BadRequestException("Start shipping date is required.");

			Date ExpectedEndDateTime = loadReqDetail.getExpectedEndDateTime();
			if (ExpectedEndDateTime != null) {
				if (ExpectedEndDateTime.before(loadReqDetail.getStartDateTime()))
					throw new BadRequestException("Expected end date can not be less than start date.");
			}

		} else
			throw new BadRequestException("Load request details is not mentioned.");

		List<LoadRequestPackage> loadRequestPackage = loadRequest.getLoadPackageList();

		if (loadRequestPackage != null && loadRequestPackage.size() != 0) {

			for (LoadRequestPackage loadRequestPackage2 : loadRequestPackage) {

				if (loadRequestPackage2.getWeight() == 0)
					throw new BadRequestException("weight is missing");
				if (loadRequestPackage2.getMaterialType() == null)
					throw new BadRequestException("MaterialType is missing");
				if (loadRequestPackage2.getWeightUnit() == null)
					throw new BadRequestException("weight unit is missing");

				if (loadRequestPackage2.getWeight() < 0)
					throw new BadRequestException("weight can not be negative.");

				double l = loadRequestPackage2.getPackageLength(), h = loadRequestPackage2.getPackageHeight(),
						w = loadRequestPackage2.getPackageWidth();

				if ((l != 0 || h != 0 || w != 0) && !((l != 0 && h != 0 && w != 0)))
					throw new BadRequestException("something is missing or zero from [width,height and length].");
				else {
					if ((l != 0 && h != 0 && w != 0)) {

						if (loadRequestPackage2.getLengthUnit() == null)
							throw new BadRequestException("Length unit is required.");

						if (l < 0 || h < 0 || w < 0)
							throw new BadRequestException("width,height and length can not be negative.");
					}

				}

				if (loadRequestPackage2.getPackageCount() < 0)
					throw new BadRequestException("pacakge count can not be negative.");

			}
		} else if (loadRequestPackage.size() == 0)
			throw new BadRequestException("Package details not mentioned . Minimum one Load Package is required.");
		else
			throw new BadRequestException("Package details is missing.");

	}

	private void createNotes(long userId, long loadRequestId, String note) throws APIExceptions {
		LoadRequestNotes notes = new LoadRequestNotes();
		notes.setCreatedBy(userId);
		notes.setModifiedBy(userId);
		notes.setLoadRequestId(loadRequestId);
		notes.setNotes(note);
		reqNotesManager.addNotes(notes);
	}

}
