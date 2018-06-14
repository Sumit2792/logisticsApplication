package com.fw.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Narendra Gurjar
 *
 */

public class UserDashBoardBean {

	private List<LoadAndBidRequestBean> dashboard;

	private List<UserLoadRequestBean> bidDashboard;

	public UserDashBoardBean() {
		dashboard = new ArrayList<LoadAndBidRequestBean>();
		bidDashboard = new ArrayList<UserLoadRequestBean>();
	}

	public List<LoadAndBidRequestBean> getDashboard() {
		return dashboard;
	}

	public void setDashboard(List<LoadAndBidRequestBean> dashboard) {
		this.dashboard = dashboard;
	}

	public List<UserLoadRequestBean> getBidDashboard() {
		return bidDashboard;
	}

	public void setBidDashboard(List<UserLoadRequestBean> bidDashboard) {
		this.bidDashboard = bidDashboard;
	}

}
