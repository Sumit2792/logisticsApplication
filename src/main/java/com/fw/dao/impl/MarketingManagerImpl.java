package com.fw.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fw.beans.ProviderMarketingFiltersBean;
import com.fw.beans.RequestMarketingDetailFiltersBean;
import com.fw.beans.RequestMarketingFiltersBean;
import com.fw.beans.RequestMarketingMessage;
import com.fw.beans.UsersWithFactsBean;
import com.fw.dao.IMarketingManager;
import com.fw.dao.IUserFactsManager;
import com.fw.domain.LoadRequest;
import com.fw.domain.UserFacts;
import com.fw.domain.Users;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.enums.LoadRequestStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;

@Repository
public class MarketingManagerImpl implements IMarketingManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	IUserFactsManager userFactsManager;
	
	private Logger log = Logger.getLogger(MarketingManagerImpl.class);

	@Override
	public List<Users> getAllNotContectedUsers(ProviderMarketingFiltersBean filtersBean) throws APIExceptions {
		try {
			StringBuilder contectedQuery = new StringBuilder("select distinct user_id from ");
			if(filtersBean.getSearchType().equals(ContactType.CALL)) {
				contectedQuery.append("call_history where modified_date >= NOW() - INTERVAL '" + filtersBean.getDays() + " DAY'");
			} else {
				contectedQuery.append("sent_messages where modified_date >= NOW() - INTERVAL '" + filtersBean.getDays() 
					+ " DAY' and contact_type='" + filtersBean.getSearchType().toDbString() + "'");
			}
			if (filtersBean.getLoadRequestId() != 0) {
				contectedQuery.append(" and load_request_id=" + filtersBean.getLoadRequestId());
			}
			log.info("========contectedQuery=======:" + contectedQuery.toString());
			List<Long> contactedUsers = jdbcTemplate.query(contectedQuery.toString(), new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet rs, int rowNumber) throws SQLException {
					return rs.getLong("user_id");
				}
			});
			
			if(filtersBean.getPageNumber() <= 1) {
				filtersBean.setPageNumber(0);
			} else {
				filtersBean.setPageNumber((filtersBean.getPageNumber() - 1) * filtersBean.getLimit());
			}

			if (contactedUsers.isEmpty()) {
				if (filtersBean.isUseLocation()) {
					return jdbcTemplate.query(getProviderQuery(false, filtersBean), new UserRowMapperLocal());
				} else {
					return jdbcTemplate.query("select * from users where user_role='" + filtersBean.getProviderType().toDbString() 
							+ "' order by rating desc, user_id asc limit " + filtersBean.getLimit()
							+ " offset " + filtersBean.getPageNumber(), new UserRowMapperLocal());
				}
			} else {
				MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("ids", contactedUsers);
				if (filtersBean.isUseLocation()) {
					return namedParameterJdbcTemplate.query(getProviderQuery(true, filtersBean), sqlParameterSource, new UserRowMapperLocal());
				} else {
					return namedParameterJdbcTemplate.query("select * from users where user_id not in (:ids) and user_role='" + filtersBean.getProviderType().toDbString() 
							+ "' order by rating desc, user_id asc limit " + filtersBean.getLimit()
							+ " offset " + filtersBean.getPageNumber(), sqlParameterSource, new UserRowMapperLocal());
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<UsersWithFactsBean> getSelectedUsers(List<Long> userIds) throws APIExceptions {
		List<UsersWithFactsBean> users = new ArrayList<UsersWithFactsBean>();
		try {
			// Get user details by ids
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("ids", userIds);
			users = namedParameterJdbcTemplate.query("select * from users where user_id in (:ids)", sqlParameterSource, 
				new RowMapper<UsersWithFactsBean>(){
					@Override
					public UsersWithFactsBean mapRow(ResultSet rs, int rowNumber) throws SQLException {
						UsersWithFactsBean bean = new UsersWithFactsBean();
						bean.setUserId(rs.getLong("user_id"));
						bean.setUserRole(UserRoles.fromString(rs.getString("user_role")));
						bean.setUserName(rs.getString("user_name"));
						return bean;
					}
			});
			
			// Get user facts by ids
			List<UserFacts> facts = userFactsManager.getUserFactsByUserIds(userIds);
			Map<Long, List<UserFacts>> factsMap = new HashMap<Long, List<UserFacts>>();
			for(UserFacts fact : facts) {
				List<UserFacts> mapList = factsMap.get(fact.getUserId());
				if(mapList == null) {
					mapList = new ArrayList<UserFacts>();
				}
				mapList.add(fact);
				factsMap.put(fact.getUserId(), mapList);
			}
			
			// Append facts into users list
			for(UsersWithFactsBean user : users) {
				user.setFacts(factsMap.get(user.getUserId()));
			}
			
			return users;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return users;
		}
	}

	@Override
	public List<LoadRequest> getLoadRequestsForMarketing(RequestMarketingFiltersBean filtersBean) {
		try {
			StringBuilder query = new StringBuilder("select * from load_requests");
			if(filtersBean.getLoadRequestId() > 0) {
				query.append(" where load_request_id=" + filtersBean.getLoadRequestId());
			} else {
				if(filtersBean.getStatus() != "") {
					query.append(" where load_request_status='" + filtersBean.getStatus() + "'");
				}
				query.append(" order by created_date desc limit " + filtersBean.getLimit());
				if(filtersBean.getPageNumber() <=0) {
					query.append(" offset 0");
				} else {
					query.append(" offset " + ((filtersBean.getPageNumber() - 1) * filtersBean.getLimit()));
				}
			}
			return jdbcTemplate.query(query.toString(),new RowMapper<LoadRequest>(){  
			    @Override  
			    public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {  
			    	LoadRequest lr=new LoadRequest();
			    	lr.setLoadRequestId(rs.getLong("load_request_id"));
			    	lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
			    	lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
			    	lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
			    	lr.setUserId(rs.getLong("user_id"));
			    	lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
			        lr.setCreatedBy(rs.getLong("created_by"));
			        lr.setModifiedBy(rs.getLong("modified_by"));
			        lr.setCreatedDate(rs.getTimestamp("created_date"));
			        lr.setModifiedDate(rs.getTimestamp("modified_date"));
			    	return lr;
			    }  
			    });
		} catch (DataAccessException e) {
			log.error("Error : "+ e.getMessage(), e);
			return null;
		} 
	}

	@Override
	public int getRequestMessageCount(ContactType type, long requestId) throws APIExceptions {
		try {
			String query = "";
			if(type.equals(ContactType.SMS)) {
				query = "select count(*) from sent_messages where load_request_id=? and contact_type='" + ContactType.SMS.toDbString() + "'";
			} else if(type.equals(ContactType.EMAIL)) {
				query = "select count(*) from sent_messages where load_request_id=? and contact_type='" + ContactType.EMAIL.toDbString() + "'";
			} else if(type.equals(ContactType.CALL)) {
				query = "select count(*) from call_history where load_request_id=?";
			} else {
				throw new BadRequestException("Invalid contact type");
			}
			return jdbcTemplate.queryForObject(query, Integer.class, requestId);
		} catch (DataAccessException e) {
			log.error("Error : "+ e.getMessage(), e);
			return 0;
		} 
	}

	@Override
	public int getRequestBidCount(long requestId) throws APIExceptions {
		try {
			return jdbcTemplate.queryForObject("select count(*) from bid where load_request_id =?", Integer.class, requestId);
		} catch (DataAccessException e) {
			log.error("Error : "+ e.getMessage(), e);
			return 0;
		} 
	}

	@Override
	public double getRequestBidCharges(long loadRequestId, String colName) throws APIExceptions {
		try {
			String query = "select " + colName + " from bid b join load_requests l on b.load_request_id = l.load_request_id"
					+ " and l.load_request_status in ('DELIVERED','IN_TRANSIT','BOOKED','AWARDED') and b.bid_status = 'AWARDED' and l.load_request_id=?";
			return jdbcTemplate.query(query, new ResultSetExtractor<Double>(){
				@Override
				public Double extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()) {
						return rs.getDouble(colName);
					}
					return new Double(0);
				}
			}, loadRequestId);
		} catch (DataAccessException e) {
			log.error("Error : "+ e.getMessage(), e);
			return 0;
		} 
	}

	@Override
	public List<RequestMarketingMessage> getRequestMarketingMessageDetails(RequestMarketingDetailFiltersBean filtersBean) throws APIExceptions {
		StringBuilder query = new StringBuilder("select s.*, u.user_role from sent_messages s left join users u on s.created_by = u.user_id");
		query.append(" where s.contact_type='" + filtersBean.getType() + "' and load_request_id=? limit " + filtersBean.getLimit());
		if(filtersBean.getPageNumber() <= 0) {
			query.append(" offset 0");
		} else {
			query.append(" offset " + ((filtersBean.getPageNumber() - 1) * filtersBean.getLimit()));
		}
		return jdbcTemplate.query(query.toString(), new RowMapper<RequestMarketingMessage>(){
			@Override
			public RequestMarketingMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
				RequestMarketingMessage message = new RequestMarketingMessage();
				message.setMessageId(rs.getLong("sent_messages_id"));
				message.setContact(rs.getString("contact"));
				message.setMessage(rs.getString("message_body"));
				message.setStatus(ContactStatus.fromString(rs.getString("status")));
				message.setCreatedBy(rs.getLong("created_by"));
				message.setBatchId(rs.getString("batch_id"));
				String sender = rs.getString("user_role");
				if(null != sender && !"".equals(sender)) {
					message.setSender(sender);
				} else {
					message.setSender("System");
				}
				message.setUserId(rs.getString("user_id"));
				return message;
			}
		}, filtersBean.getLoadRequestId());
	}

	@Override
	public List<RequestMarketingMessage> getRequestMarketingCallDetails(RequestMarketingDetailFiltersBean filtersBean, 
			boolean showPending, Users authuser) throws APIExceptions {
		StringBuilder query = new StringBuilder("select c.*, u.user_role from call_history c join users u on c.created_by = u.user_id where ");
		if(showPending) {
			//select * from call_history where created_by=2986 limit 10 offset 10
			query.append("c.created_by=" + authuser.getUserId() + " and c.status='PENDING'");
			if(filtersBean.getLoadRequestId() > 0) {
				query.append(" and c.load_request_id=" + filtersBean.getLoadRequestId());
			}
		} else {
			//select * from call_history where load_request_id=43 limit 10 offset 10
			query.append("c.load_request_id=" + filtersBean.getLoadRequestId());
		}
		query.append(" limit " + filtersBean.getLimit());
		if(filtersBean.getPageNumber() <= 0) {
			query.append(" offset 0");
		} else {
			query.append(" offset " + ((filtersBean.getPageNumber() - 1) * filtersBean.getLimit()));
		}
		return jdbcTemplate.query(query.toString(), new RowMapper<RequestMarketingMessage>(){
			@Override
			public RequestMarketingMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
				RequestMarketingMessage message = new RequestMarketingMessage();
				message.setMessageId(rs.getLong("call_history_id"));
				message.setContact(rs.getString("phone_number"));
				message.setMessage(rs.getString("message_body"));
				message.setStatus(ContactStatus.fromString(rs.getString("status")));
				message.setCreatedBy(rs.getLong("created_by"));
				message.setBatchId(rs.getString("batch_id"));
				String sender = rs.getString("user_role");
				if(null != sender && !"".equals(sender)) {
					message.setSender(sender);
				} else {
					message.setSender("System");
				}
				message.setUserId(rs.getString("user_id"));
				message.setLoadRequestId(rs.getString("load_request_id"));
				return message;
			}
		});
	}

	private String getProviderQuery(boolean inQuery, ProviderMarketingFiltersBean filtersBean) {
		StringBuilder providerQuery = new StringBuilder("select distinct on (user_id) * from");
		providerQuery.append(" (select u.* from users as u join postal_addresses as po on u.user_id = po.user_id");
		if (inQuery) {
			providerQuery.append(" where u.user_id not in (:ids) and u.user_role='" + filtersBean.getProviderType().toDbString() + "'");
		} else {
			providerQuery.append(" where u.user_role='" + filtersBean.getProviderType().toDbString() + "'");
		}
		if (!"".equals(filtersBean.getCountry())) {
			String country = filtersBean.getCountry().substring(filtersBean.getCountry().lastIndexOf("#") + 1);
			providerQuery.append(" and (po.country similar to '%##" + country + "' or po.country similar to '" + country + "')");
		}
		if (!"".equals(filtersBean.getState())) {
			String state = filtersBean.getState().substring(filtersBean.getState().lastIndexOf("#") + 1);
			providerQuery.append(" and (po.state_province similar to '%##" + state + "' or po.state_province similar to '" + state + "')");
		}
		if (!"".equals(filtersBean.getCity())) {
			String city = filtersBean.getCity().substring(filtersBean.getCity().lastIndexOf("#") + 1);
			providerQuery.append(" and (po.city similar to '%##" + city + "' or po.city similar to '" + city + "')");
		}
		providerQuery.append(" order by rating desc, user_id asc limit " + filtersBean.getLimit() + " offset " + filtersBean.getPageNumber() + ") as providers");
		return providerQuery.toString();
	}
}

class UserRowMapperLocal implements RowMapper<Users> {
	@Override
	public Users mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Users user = new Users();
		user.setUserId(rs.getLong("user_id"));
		user.setUserRole(UserRoles.fromString(rs.getString("user_role")));
		user.setUserName(rs.getString("user_name"));
		user.setRating(rs.getLong("rating"));
		return user;
	}
}
