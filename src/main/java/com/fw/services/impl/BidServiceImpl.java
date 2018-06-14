package com.fw.services.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.AddBidsBean;
import com.fw.beans.BidStatusBean;
import com.fw.beans.BidsBean;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.beans.NotesBean;
import com.fw.beans.OnBehalfOfUserBean;
import com.fw.beans.UserDetailsBean;
import com.fw.config.AuthUserDetails;
import com.fw.dao.IBidManager;
import com.fw.dao.IBidNotesManager;
import com.fw.dao.ILoadRequestDetailManager;
import com.fw.dao.ILoadRequestManager;
import com.fw.dao.IUserManager;
import com.fw.domain.Bid;
import com.fw.domain.BidNotes;
import com.fw.domain.LoadRequest;
import com.fw.domain.LoadRequestDetail;
import com.fw.domain.Users;
import com.fw.enums.BidStatus;
import com.fw.enums.CurrencyCodes;
import com.fw.enums.LoadRequestStatus;
import com.fw.enums.UserLoginStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.exceptions.InvalidIdException;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.IBidService;
import com.fw.utils.BidUtils;
import com.fw.utils.LocalUtils;
import com.fw.utils.MessageTemplateConstants;
import com.fw.validations.DataValidations;

@Service
public class BidServiceImpl implements IBidService {

	@Autowired
	private IBidManager bidManager;
	@Autowired
	private AuthUserDetails authUser;
	@Autowired
	private IBidNotesManager bidNotesManager;
	@Autowired
	private IUserManager userManager;
	@Autowired
	ILoadRequestManager loadRequestManager;
	@Autowired
	private ILoadRequestDetailManager loadDetailManager;
	@Autowired
	private BidUtils bidUtil;
	@Autowired
	private DataValidations dataValidations;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AddBidsBean addBid(AddBidsBean requestbids) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		AddBidsBean bidsBean = null;
		OnBehalfOfUserBean onBehalfUserData = null;

		if (requestbids == null)
			throw new APIExceptions("Request body is not valid");
		if (requestbids.getLoadRequestId() == 0)
			throw new APIExceptions("Load request id is missing.");

		if (userRole == UserRoles.CSR) {
			onBehalfUserData = requestbids.getBidOnBehalfOfUser();
			if (onBehalfUserData == null)
				throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "phoneNumberRequired"));

			dataValidations.phoneNumberValidation(onBehalfUserData.getPhoneNumber());
		}

		requestbids.setCreatedBy(userId);
		requestbids.setModifiedBy(userId);
		requestbids.setBidderUserId(userId);

		// set defaults if not provided in pay loads
		if (requestbids.getCurrency() == null)
			requestbids.setCurrency(CurrencyCodes.INR);
		if (requestbids.getStatus() == null) {
			requestbids.setStatus(BidStatus.ACTIVE);
		}

		if (requestbids.getAmount() < 100) {
			throw new BadRequestException("Bid amount is required and can not be less than Rs. 100/- ");
		}
		requestbids.setFaberCharges(bidUtil.getFaberCharge(requestbids.getAmount()));
		requestbids.setAmount(requestbids.getAmount());

		LoadRequest loadRequest = loadRequestManager.getLoadRequestById(requestbids.getLoadRequestId());
		if (loadRequest == null)
			throw new InvalidIdException("Load request is not found.");
		
		LoadRequestDetail loadDetails = loadDetailManager.getLoadRequestDetailById(requestbids.getLoadRequestId());
		if (requestbids.getDeliveredTime() != null && loadDetails.getStartDateTime() != null) {
			if (requestbids.getDeliveredTime().before(loadDetails.getStartDateTime()))
				throw new APIExceptions("Delivery date should be greater than start shipping date.");
		}
		
		if (bidManager.isBidExistForLoadRequest(userId, loadRequest.getLoadRequestId()))
			throw new UnAuthorizedActionException("You are not allowed to bid more than one for same request id.");
		
		BidUtils.isNewBidAllowed(loadRequest);

		if (userRole == UserRoles.CAPACITY_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.LOAD_PROVIDER) {

			if (loadRequest.getUserId() == userId)
				throw new UnAuthorizedActionException("You are not authorized to bid on this load request.");
			else
				bidsBean = bidManager.addBid(requestbids);
		} else if (userRole == UserRoles.CSR) {

			if (onBehalfUserData != null && onBehalfUserData.getPhoneNumber() != null) {
				String userName = onBehalfUserData.getPhoneNumber();
				boolean isUserExist = userManager.isUserExist(userName);

				if (!isUserExist) {
					Users newuser = new Users();
					newuser.setUserName(userName);
					newuser.setUserLoginStatus(UserLoginStatus.CREATED);
					newuser.setUserRole(
							(onBehalfUserData.getUserRole() == null || onBehalfUserData.getUserRole() == UserRoles.CSR)
									? UserRoles.CAPACITY_PROVIDER
									: onBehalfUserData.getUserRole());
					newuser.setCreatedBy(userId);
					newuser.setModifiedBy(userId);
					Long newBidderId = userManager.persist(newuser);
					requestbids.setBidderUserId(newBidderId); // set bidder id
				}
				Users onBehalfUser = userManager.getUserByUserName(userName);

				if (loadRequest.getUserId() == onBehalfUser.getUserId())
					throw new UnAuthorizedActionException("You are not authorized to bid on self load request.");

				requestbids.setBidderUserId(onBehalfUser.getUserId());

				bidsBean = bidManager.addBid(requestbids);
			} else {
				throw new APIExceptions(
						"You can not make bid request until you provides phone number of on-bhalf bidder.");
			}

		} else {
			throw new UnAuthorizedActionException("Unauthorized User."); // pending -> extract this anonymous user info
		}
		if (bidsBean != null) {
			if (bidsBean.getNote() != null) {
				BidNotes notes = new BidNotes();
				notes.setCreatedBy(userId);
				notes.setModifiedBy(userId);
				notes.setNotes(bidsBean.getNote());
				notes.setBidId(bidsBean.getBidId());
				try {
					bidNotesManager.addNotes(notes);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		bidUtil.sendMessageToLoaderOnLowestBid(loadRequest, requestbids.getAmount(), bidsBean.getBidId(), MessageTemplateConstants.TO_LOAD_PROVIDER_ABOUT_THE_LATEST_BID);
		return bidsBean;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateBidById(AddBidsBean bidBean) throws APIExceptions {

		if (bidBean == null)
			throw new APIExceptions("Invalid request.");

		if (bidBean.getBidId() == 0)
			throw new APIExceptions("Bid Id is missing.");

		if (bidBean.getAmount() < 100) {
			throw new BadRequestException("Bid amount is required and can not be less than Rs. 100/- ");
		}
		BidsBean bidDB = bidManager.getBidById(bidBean.getBidId());
		if (bidDB == null)
			throw new InvalidIdException("Bid id is not valid.");

		BidUtils.canEditBid(bidDB);

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();

		bidBean.setModifiedBy(userId);
		bidBean.setFaberCharges(bidUtil.getFaberCharge(bidBean.getAmount()));
		bidBean.setAmount(bidBean.getAmount());

		LoadRequest loadRequest = loadRequestManager.getLoadRequestById(bidDB.getLoadRequestId());
		LoadRequestDetail loadDetails = loadDetailManager.getLoadRequestDetailById(bidDB.getLoadRequestId());

		if (bidBean.getDeliveredTime() != null && loadDetails.getStartDateTime() != null) {
			if (bidBean.getDeliveredTime().before(loadDetails.getStartDateTime()))
				throw new APIExceptions("Delivery date should be greater than start shipping date.");

		}

		if (userRole == UserRoles.CAPACITY_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.LOAD_PROVIDER) {

			if (bidBean.getBidId() != 0) {
				// found bidId for logged in user
				if (bidDB != null && bidDB.getBidderUserId() == userId) {
					bidManager.updateBidById(bidBean);
				} else {
					throw new UnAuthorizedActionException("You are not authorized to act on the bid.");
				}
			} else
				throw new APIExceptions("bid id is missing.");
		} else if (userRole == UserRoles.CSR) {
			bidManager.updateBidById(bidBean);
		}
		// update notes
		if (bidBean.getNote() != null) {
			BidNotes notes = new BidNotes();
			notes.setCreatedBy(userId);
			notes.setModifiedBy(userId);
			notes.setNotes(bidBean.getNote());
			notes.setBidId(bidBean.getBidId());
			try {
				bidNotesManager.addNotes(notes);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		bidUtil.sendMessageToLoaderOnLowestBid(loadRequest, bidBean.getAmount(), bidBean.getBidId(), MessageTemplateConstants.TO_LOAD_PROVIDER_ABOUT_THE_LATEST_BID);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBidById(long requestBidId) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		
		BidsBean bid = bidManager.getBidById(requestBidId);
		
		if(bid == null) throw new InvalidIdException("Bid id is not found. ID: "+requestBidId);
				
		if(bid.getStatus().equals(BidStatus.AWARDED))
			throw new UnAuthorizedActionException("Awarded Bid is not allowed to be delete.");
		
		if (userRole == UserRoles.CAPACITY_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.LOAD_PROVIDER) {
			
			if (bid.getBidId() == requestBidId) {
				bidManager.deleteBidById(requestBidId);
			} else {
				throw new UnAuthorizedActionException("You are not authorized to act on bid.");
			}
		} else if (userRole == UserRoles.CSR) {
			bidManager.deleteBidById(requestBidId);
		} else {
			throw new UnAuthorizedActionException("Unauthorized User.");
		}
	}

	@Override
	@Transactional
	public BidsBean getBidById(long bidId) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		BidsBean bids = null;
		BidsBean bidDB = bidManager.getBidById(bidId);
		if (bidDB == null) {
			throw new InvalidIdException("Bid not found. bid number : " + bidId);
		}
		LoadRequest loadRequest = loadRequestManager.getLoadRequestById(bidDB.getLoadRequestId());

		if (userRole == UserRoles.CAPACITY_PROVIDER || userRole == UserRoles.BOTH
				|| userRole == UserRoles.LOAD_PROVIDER) {

			if (bidDB.getBidderUserId() == userId || loadRequest.getUserId() == userId) {
				bids = bidManager.getBidById(bidId);
			} else {
				throw new UnAuthorizedActionException("You are not authorized to act on bid.");
			}
		} else if (userRole == UserRoles.CSR) {
			bids = bidManager.getBidById(bidId);
		} else {
			throw new UnAuthorizedActionException("Unauthorized User."); // pending -> extract this anonymous user info
		}
		if (bids != null) {
			List<NotesBean> notes = bidNotesManager.getAllNotesByBidId(bidId);
			bids.setNotes(notes);
			UserDetailsBean userDetials = userManager.getUserInfoByUserId(bidDB.getBidderUserId());
			if (userDetials != null) {
				bids.setBidderRating(userDetials.getRating());
			}
			bids.setOnlinePaymentCharges(BidUtils.getOnlineCharges(bids.getAmount(), bids.getFaberCharges()));
			return bids;
		} else {
			throw new APIExceptions("Internal server error while getting bid id :" + bidId);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateBidStatus(BidStatusBean bidStatus) throws APIExceptions {

		if (bidStatus == null)
			throw new APIExceptions("Invalid request.");

		if (bidStatus.getBidId() == 0)
			throw new APIExceptions(" Bid id is required.");

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();

		BidsBean bid = bidManager.getBidById(bidStatus.getBidId());

		if (bid == null)
			throw new InvalidIdException("Bid not found.");

		LoadRequest loadRequest = loadRequestManager.getLoadRequestById(bid.getLoadRequestId());

		if (loadRequest == null)
			throw new APIExceptions("There is no load request which is associated to the bid : " + bidStatus.getBidId());

		BidStatus statusToBeChanged = BidStatus.fromString(bidStatus.getStatus());

		switch (statusToBeChanged) 
		{
			case AWARDED:
				BidUtils.canAwardBid(loadRequest);
				if (userRole != UserRoles.CSR) {
					if (bid.getBidderUserId() == userId) {
						throw new UnAuthorizedActionException("You are not authorized to award this bid.");
					} else if (loadRequest.getUserId() != userId) {
						throw new UnAuthorizedActionException("You are not authorized to award this bid.");
					}
				}
				break;
			case ACTIVE:
				// only CSR can make active in specific case , because this is default status
				// its created default when bid is created.
				if (userRole != UserRoles.CSR && bid.getBidderUserId() == userId)
					throw new UnAuthorizedActionException("You can not make it active. Please contact to CSR.");
				break;
			default:
				throw new APIExceptions("Invalid Action.");
		}

		bidStatus.setModifiedBy(userId);
		BidStatus otherToBeChanged = null;
		if (statusToBeChanged == BidStatus.ACTIVE && bid.getStatus() == BidStatus.AWARDED) {
			otherToBeChanged = BidStatus.ACTIVE;
		} else if (statusToBeChanged == BidStatus.AWARDED) {
			otherToBeChanged = BidStatus.ACTIVE;
		} else {
			otherToBeChanged = BidStatus.ACTIVE;
		}
		List<BidsBean> bidList = bidManager.getBidsByLoadRequestId(bid.getLoadRequestId());
		List<BidStatusBean> bidsStatusList = new ArrayList<>();
		for (BidsBean bidsBean : bidList) {

			BidStatusBean bidsStatus = new BidStatusBean();
			bidsStatus.setBidId(bidsBean.getBidId());
			if (bidStatus.getBidId() != bidsBean.getBidId()) {
				bidsStatus.setStatus(otherToBeChanged.toDbString());
			} else
				bidsStatus.setStatus(statusToBeChanged.toDbString());
			bidsStatus.setModifiedBy(userId);
			bidsStatusList.add(bidsStatus);
		}
		bidManager.batchUpdateBidStatus(bidsStatusList);
		
		// update load request : set corresponding load request status to awarded
		LoadRequestStatusBean loadStatus = new LoadRequestStatusBean();
		loadStatus.setModifiedBy(loadRequest.getModifiedBy());
		loadStatus.setLoadRequestId(loadRequest.getLoadRequestId());
		if (statusToBeChanged == BidStatus.AWARDED) {
			loadStatus.setStatus(LoadRequestStatus.AWARDED.getValue());
			loadRequestManager.updateLoadRequestStatus(loadStatus);
			bidUtil.sendAwardedMessageToLoaderAndBidder(loadRequest, bid, MessageTemplateConstants.TO_LOAD_PROVIDER_AFTER_REQUEST_IS_AWARDED);
		}

	}
	

}
