package com.fw.controller.impl;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fw.beans.ProviderMarketingBean;
import com.fw.beans.ProviderMarketingFiltersBean;
import com.fw.beans.ProviderMarketingSelectionBean;
import com.fw.beans.RequestMarketingBean;
import com.fw.beans.RequestMarketingDetailFiltersBean;
import com.fw.beans.RequestMarketingFiltersBean;
import com.fw.beans.RequestMarketingMessageDetailBean;
import com.fw.config.AuthUserDetails;
import com.fw.controller.IMarketingController;
import com.fw.domain.Users;
import com.fw.enums.ContactType;
import com.fw.enums.LoadRequestStatus;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.services.IMarketingService;

@Controller
@RequestMapping(value = "/mLogistics")
public class MarketingControllerImpl implements IMarketingController {

	@Autowired AuthUserDetails authUser;

	@Autowired IMarketingService marketingService;
	
	@Override
	@ResponseBody
	@RequestMapping(value = "/private/marketing/getProviders", method = { POST })
	public ResponseEntity<List<ProviderMarketingBean>> getProvidersForMarketing(@Valid @Validated @RequestBody ProviderMarketingFiltersBean filtersBean) 
			throws APIExceptions {
		if(filtersBean.getLimit() <= 0) {
			throw new BadRequestException("Invalid limit");
		}
		return new ResponseEntity<List<ProviderMarketingBean>>(marketingService.getProviderMarketingList(filtersBean), HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/marketing/sendSelectedMessage", method = { POST })
	public ResponseEntity<?> sendSelectedMessage(@Valid @Validated @RequestBody ProviderMarketingSelectionBean selectionBean) throws APIExceptions {
		if(selectionBean.getMessageTemplateId() <= 0) {
			throw new BadRequestException("Invalid template");
		} else if(!isValidUserList(selectionBean.getUsers())) {
			throw new BadRequestException("Invalid user list");
		}
		Users authuser = authUser.getAuthUserDetails();
		int [] response = marketingService.sendSelectedMarketingMessage(selectionBean, authuser);
		if(response == null) {
			return new ResponseEntity<String>("No user is having any email", HttpStatus.NOT_ACCEPTABLE);
		} else if(response.length == 0) {
			return new ResponseEntity<String>("Looks like there is an issue with database operation", HttpStatus.SERVICE_UNAVAILABLE);
		} else {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/marketing/getRequestMarketingData", method = { POST })
	public ResponseEntity<?> getRequestMarketingData(@Valid @Validated @RequestBody RequestMarketingFiltersBean filtersBean) throws APIExceptions {
		if(filtersBean.getLimit() <= 0) {
			throw new BadRequestException("Invalid limit");
		}
		Users authuser = authUser.getAuthUserDetails();
		if(filtersBean.isShowPending()) {
			RequestMarketingDetailFiltersBean filter = new RequestMarketingDetailFiltersBean();
			filter.setLimit(filtersBean.getLimit());
			filter.setLoadRequestId(filtersBean.getLoadRequestId());
			filter.setPageNumber(filtersBean.getPageNumber());
			filter.setType(ContactType.CALL);
			return new ResponseEntity<RequestMarketingMessageDetailBean>(marketingService.getRequestMarketingMessageDetails(filter, true, authuser), HttpStatus.OK);
		}
		try {
			if(!"".equals(filtersBean.getStatus())) {
				LoadRequestStatus.fromString(filtersBean.getStatus());
			}
		} catch (RuntimeException e) {
			throw new BadRequestException("Invalid Request status");
		}
		return new ResponseEntity<List<RequestMarketingBean>>(marketingService.getRequestMarketingList(filtersBean, authuser), HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/marketing/getRequestMarketingMessageDetails", method = { POST })
	public ResponseEntity<RequestMarketingMessageDetailBean> getRequestMarketingMessageDetails(
			@Valid @Validated @RequestBody RequestMarketingDetailFiltersBean filtersBean) throws APIExceptions {
		if(filtersBean.getLimit() <= 0) {
			throw new BadRequestException("Invalid limit");
		} else if(filtersBean.getLoadRequestId() <= 0) {
			throw new BadRequestException("Invalid load request id");
		}
		Users authuser = authUser.getAuthUserDetails();
		return new ResponseEntity<RequestMarketingMessageDetailBean>(marketingService.getRequestMarketingMessageDetails(filtersBean, false, authuser), HttpStatus.OK);
	}

	private boolean isValidUserList(List<Long> users) {
		if(users.isEmpty()) {
			return false;
		} else if(users.size() == 1 && users.get(0) == 0) {
			return false;
		}
		return true;
	}

//	public ResponseEntity<?> saveCallFeedback(@Valid @Validated @RequestBody CallHistoryFeedbackBean feedbackBean) throws APIExceptions {
//		Users authuser = authUser.getAuthUserDetails();
//		if(!UserRoles.CSR.equals(authuser.getUserRole()) || !UserRoles.SUPER_ADMIN.equals(authuser.getUserRole()) ) {
//			return new ResponseEntity<String>("You do not have enough permission", HttpStatus.FORBIDDEN);
//		} else if(feedbackBean.getNote() == null || "".equals(feedbackBean.getNote())) {
//			throw new BadRequestException("Invalid feedback note");
//		} else if(feedbackBean.getGivingUserId() <= 0) {
//			throw new BadRequestException("Invalid feedback users");
//		}
//		int response = marketingService.saveCallFeedback(feedbackBean, authuser);
//		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
//	}
	
}
