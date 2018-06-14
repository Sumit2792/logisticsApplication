package com.fw.daemon;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fw.beans.LoadRequestStatusBean;
import com.fw.dao.ILoadRequestManager;
import com.fw.domain.LoadRequest;
import com.fw.enums.LoadRequestStatus;
import com.fw.exceptions.APIExceptions;
import com.fw.utils.LoadRequestAlgorithm;

@Service
public class LoadRequestAlgorithmDaemon {

	private Logger log = Logger.getLogger(LoadRequestAlgorithmDaemon.class);

	@Autowired
	LoadRequestAlgorithm loadRequestAlgorithm;

	@Autowired
	private ILoadRequestManager loadRequestManager;

	@Value("${process.LoadRequestAlgorithmDaemon.isActive}")
	private boolean isActive;

	@Scheduled(fixedDelayString = "${process.LoadRequestAlgorithmDaemon.fixedDelay}") // Run in every 1 hour
	public void callLRAlgorithm() throws APIExceptions {

		if (isActive) {
			List<LoadRequest> listLoadRequest = new ArrayList<LoadRequest>();
			
			//Fetch all the load request whose status is requested
			log.info("Fetching all load rquest whose load request status is REQUESTED");
			listLoadRequest = loadRequestManager.getAllLoadRequestByStatus(LoadRequestStatus.REQUESTED);
			
			for(LoadRequest loadRequest : listLoadRequest) {
				LoadRequestStatusBean loadRequestStatus = new LoadRequestStatusBean();
				loadRequestStatus.setLoadRequestId(loadRequest.getLoadRequestId());
				loadRequestStatus.setStatus(LoadRequestStatus.BIDDING_IN_PROGRESS.getValue());
				loadRequestStatus.setModifiedBy(loadRequest.getUserId());
				
				//Update status from Requested to Bidding in progress.
				loadRequestManager.updateLoadRequestStatus(loadRequestStatus);		
			}
			
			
			log.info("Fetching all load rquest whose load request status is REQUESTED and BIDDING_IN_PROGRESS and the total bid count is less then 3");
			listLoadRequest = loadRequestManager.getAllLoadRequestByStatus();

			loadRequestAlgorithm.processLoadRequest(listLoadRequest);

		} else {
			log.info(this.getClass().getName() + " is set to false for execution.");
		}

		// Find load request which have less than 3 bids and status="" and request
		// end/start is greater than current time.

		// Then fetch all cities from load start and load end

		// then apply a logic to figure out how many message to be picked from all these
		// intermediate cities.

		// Then insert in sentMessages table.

	}

}
