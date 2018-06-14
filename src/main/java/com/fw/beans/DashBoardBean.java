package com.fw.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Narendra Gurjar
 *
 */

public class DashBoardBean {

	private List<LoadAndBidRequestBean> dashboard;

	public DashBoardBean() {
		dashboard = new ArrayList<LoadAndBidRequestBean>();
	}

	public List<LoadAndBidRequestBean> getDashboard() {
		return dashboard;
	}

	public void setDashboard(List<LoadAndBidRequestBean> dashboard) {
		this.dashboard = dashboard;
	}

}
