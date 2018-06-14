package com.fw.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fw.beans.BidsBean;
import com.fw.beans.LoginResultBean;
import com.fw.beans.UserDetailsBean;
import com.fw.dao.IUserManager;
import com.fw.domain.Users;
import com.fw.enums.UserLoginStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

@Repository
public class UserManagerImpl implements IUserManager {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private Logger log = Logger.getLogger(UserManagerImpl.class);

	/**
	 * Insert login details
	 * 
	 * @throws APIExceptions
	 */
	@Override
	public Long persist(Users user) throws APIExceptions {

		Long userId = null;
		String sql = "INSERT INTO users(user_name,password,user_role,login_otp,failed_attempts,login_status, auth_token, "
				+ "reset_token,created_by,modified_by,is_deleted, review_count)	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			int result = jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "user_id" });
					ps.setString(1, user.getUserName().trim());
					ps.setString(2, user.getPassword());
					ps.setString(3, user.getUserRole().toDbString());
					if (user.getLoginOtp() != 0)
						ps.setInt(4, user.getLoginOtp());
					else
						ps.setNull(4, java.sql.Types.INTEGER);

					ps.setInt(5, user.getFailedAttempts());
					ps.setString(6, UserLoginStatus.CREATED.toDbString());
					ps.setString(7, user.getAuthToken());
					ps.setString(8, user.getResetToken());
					ps.setLong(9, (user.getCreatedBy()));
					ps.setLong(10, (user.getModifiedBy()));
					ps.setBoolean(11, false);
					ps.setInt(12, user.getReviewCount());

					return ps;
				}
			}, requestKeyHolder);
			userId = requestKeyHolder.getKey().longValue();
			return userId;
			// user.setUserId(userId);
		} catch (DataAccessException e) {

			log.error("Failed to create user. error : " + e.getMessage(), e);
			throw new APIExceptions("Internal server error : failed to create the user.");
		}

	}

	@Override
	public List<UserDetailsBean> getAllUserRowMapper(UserRoles loggedINUserRole) {

		StringBuilder sql = new StringBuilder("select * from users;");
		return jdbcTemplate.query(sql.toString(), new RowMapper<UserDetailsBean>() {
			@Override
			public UserDetailsBean mapRow(ResultSet rs, int rownumber) throws SQLException {
				UserDetailsBean user = new UserDetailsBean();
				user.setUserId(rs.getLong("user_id"));
				user.setUserName(rs.getString("user_name"));
				String userRole = rs.getString("user_role");
				if (userRole != null) {
					user.setUserRole(UserRoles.fromString(rs.getString("user_role")).toDbString());
				}
				user.setFailedAttempts(rs.getInt("failed_attempts"));
				user.setLastLoginDate(rs.getDate("last_login_date"));
				String userLoginStatus = rs.getString("login_status");
				if (userLoginStatus != null) {
					user.setUserLoginStatus(UserLoginStatus.fromString(rs.getString("login_status")));
				}
				user.setAuthToken(rs.getString("auth_token"));
				user.setResetToken(rs.getString("reset_token"));
				user.setCreatedBy(rs.getLong("created_by"));
				user.setModifiedBy(rs.getLong("modified_by"));
				user.setCreatedDate(rs.getDate("created_date"));
				user.setModifiedDate(rs.getDate("modified_date"));
				user.setRating(rs.getDouble("rating"));
				user.setReviewCount(rs.getInt("review_count"));
				user.setDeleted(rs.getBoolean("is_deleted"));
				return user;
			}
		});
	}

	@Override
	public void deleteUser(long userId, long deletedBy) {
		String sql = "UPDATE users set is_deleted=true , modified_by=? where user_id = ?;";
		jdbcTemplate.update(sql, deletedBy, userId);
	}

	/**
	 * method to get specific users details via userName from users table
	 * 
	 * @throws APIExceptions
	 */
	@Override
	public Users getUserByUserName(String userName) throws InvalidUsernameException {
		try {
			String sql = "select * from users where user_name=?";
			Users user = jdbcTemplate.queryForObject(sql, new RowMapper<Users>() {
				@Override
				public Users mapRow(ResultSet rs, int rownumber) throws SQLException {
					Users user = null;
					user = new Users();
					user.setUserId(rs.getLong("user_id"));
					user.setUserName(rs.getString("user_name"));
					user.setUserRole(UserRoles.fromString(rs.getString("user_role")));
					user.setPassword((rs.getString("password")));
					user.setLastLoginDate(rs.getDate("last_login_date"));
					user.setFailedAttempts(rs.getInt("failed_attempts"));
					user.setUserLoginStatus((rs.getString("login_status")==null)?null:UserLoginStatus.fromString(rs.getString("login_status")));
					user.setAuthToken(rs.getString("auth_token"));
					user.setResetToken(rs.getString("reset_token"));
					user.setLoginOtp(rs.getInt("login_otp"));
					user.setCreatedBy(rs.getLong("created_by"));
					user.setModifiedBy(rs.getLong("modified_by"));
					user.setCreatedDate(rs.getDate("created_date"));
					user.setModifiedDate(rs.getDate("modified_date"));
					user.setRating(rs.getDouble("rating"));
					user.setDeleted(rs.getBoolean("is_deleted"));
					user.setReviewCount(rs.getInt("review_count"));
					return user;
				}
			}, userName);

			if (user == null) {
				throw new InvalidUsernameException("Invalid username: " + userName);
			}
			return user;
		} catch (DataAccessException e) {
			log.error("User is not exist : " + e.getMessage(), e);
			throw new InvalidUsernameException("User is not exist.");
		} catch (Exception e) {
			log.error("User is not exist : " + e.getMessage(), e);
			throw new InvalidUsernameException("User is not exist.");
		}
	}
	
	/**
	 * method to get specific users details via userName from users table
	 * 
	 * @throws APIExceptions
	 */
	@Override
	public Users getUserByUserId(long userId) throws InvalidUsernameException {
		try {
			String sql = "select * from users where user_id=?";
			Users user = jdbcTemplate.queryForObject(sql, new RowMapper<Users>() {
				@Override
				public Users mapRow(ResultSet rs, int rownumber) throws SQLException {
					Users user = null;
					user = new Users();
					user.setUserId(rs.getLong("user_id"));
					user.setUserName(rs.getString("user_name"));
					user.setUserRole(UserRoles.fromString(rs.getString("user_role")));
					user.setPassword((rs.getString("password")));
					user.setLastLoginDate(rs.getDate("last_login_date"));
					user.setFailedAttempts(rs.getInt("failed_attempts"));
					user.setUserLoginStatus(UserLoginStatus.fromString(rs.getString("login_status")));
					user.setAuthToken(rs.getString("auth_token"));
					user.setResetToken(rs.getString("reset_token"));
					user.setLoginOtp(rs.getInt("login_otp"));
					user.setCreatedBy(rs.getLong("created_by"));
					user.setModifiedBy(rs.getLong("modified_by"));
					user.setCreatedDate(rs.getDate("created_date"));
					user.setModifiedDate(rs.getDate("modified_date"));
					user.setRating(rs.getDouble("rating"));
					user.setDeleted(rs.getBoolean("is_deleted"));
					user.setReviewCount(rs.getInt("review_count"));
					return user;
				}
			}, userId);

			if (user == null) {
				throw new InvalidUsernameException("Invalid userId: " + userId);
			}
			return user;
		} catch (DataAccessException e) {
			log.error("User is not exist : " + e.getMessage(), e);
			throw new InvalidUsernameException("User is not exist.");
		} catch (Exception e) {
			log.error("User is not exist : " + e.getMessage(), e);
			throw new InvalidUsernameException("User is not exist.");
		}
	}

	/**
	 * method to get specific users details via userId
	 * 
	 * @throws InvalidUsernameException
	 */
	@Override
	public UserDetailsBean getUserInfoByUserId(Long userId) throws InvalidUsernameException {
		try {
			UserDetailsBean user = jdbcTemplate.queryForObject("select * from users where user_id=?",
					new RowMapper<UserDetailsBean>() {
						@Override
						public UserDetailsBean mapRow(ResultSet rs, int rownumber) {
							UserDetailsBean user = null;
							try {
								user = new UserDetailsBean();
								user.setUserId(rs.getLong("user_id"));
								user.setUserName(rs.getString("user_name"));
								user.setUserRole(UserRoles.fromString(rs.getString("user_role")).toDbString());
								user.setRating(rs.getDouble("rating"));
								user.setReviewCount(rs.getInt("review_count"));
								/*
								 * user.setLastLoginDate(rs.getDate("last_login_date"));
								 * user.setFailedAttempts(rs.getInt("failed_attempts"));
								 * user.setStatus(UserStatus.fromInt(rs.getInt("status_id")));
								 * user.setCreatedBy(rs.getInt("created_by"));
								 * user.setModifiedBy(rs.getInt("modified_by"));
								 * user.setCreatedDate(rs.getDate("created_date"));
								 * user.setModifiedDate(rs.getDate("modified_date"));
								 */
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return user;
						}
					}, userId);

			if (user == null) {
				throw new InvalidUsernameException("Invalid userId : " + userId);
			}
			
			return user;
		} catch (DataAccessException e) {
			log.error(e.getMessage(), e);
			throw new InvalidUsernameException("User is not valid.");
		}
	}

	/**
	 * method to get specific users details via userName from users table
	 * 
	 * @throws APIExceptions
	 */
	@Override
	public LoginResultBean getUserDataByUserName(String userName) throws APIExceptions {
		try {
			String sql = "select * from users where user_name=?";
			LoginResultBean user = jdbcTemplate.queryForObject(sql, new RowMapper<LoginResultBean>() {
				@Override
				public LoginResultBean mapRow(ResultSet rs, int rownumber) {
					LoginResultBean user = null;
					try {
						user = new LoginResultBean();
						user.setUserId(rs.getLong("user_id"));
						user.setUserName(rs.getString("user_name"));
						user.setUserRole(UserRoles.fromString(rs.getString("user_role")).toDbString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return user;
				}
			}, userName);

			if (user == null) {
				throw new InvalidUsernameException("Invalid username: " + userName);
			}
			return user;
		} catch (DataAccessException e) {
			log.error(e.getMessage(), e);
			throw new InvalidUsernameException("User is not valid.");
		}
	}

	/**
	 * method to get specific users details via loginId from login.n table
	 * 
	 * @throws APIExceptions
	 */
	@Override
	public boolean isUserExist(String userName) throws APIExceptions {
		List<String> userNameDB = jdbcTemplate
				.queryForList("select user_name from users where user_name='" + userName + "'", String.class);
		if (userNameDB.isEmpty())
			return false;
		else if (userNameDB.size() > 1)
			throw new APIExceptions("Duplicate user name.");
		else
			return true;

	}

	/**
	 * method to get specific users details via loginId from login.n table
	 * 
	 * @throws APIExceptions
	 */
	@Override
	public String getUserNameById(long userId) throws APIExceptions {
		try {
			String userNameDB = jdbcTemplate.queryForObject(
					"select coalesce((select user_name from users where user_id ='" + userId + "'), 'NOTFOUND')",
					String.class);

			return userNameDB;
		} catch (DataAccessException e) {
			log.error(e.getMessage(), e);
			return null;
		}

	}

	/**
	 * method to insert OTP when existing user request for for OTP
	 */
	@Override
	public boolean updateOTP(String userName, Integer otp) {
		try {
			String sql = "UPDATE users SET login_otp=? WHERE user_name=?";
			if (jdbcTemplate.update(sql, otp, userName) == 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}

	}

	/**
	 * method to insert OTP when existing user request for for OTP
	 */
	@Override
	public boolean updatePassword(String userName, String pwd, Long updatedBy) {
		try {
			StringBuilder sql = new StringBuilder("UPDATE users SET password=? ");
			if (updatedBy != null)
				sql.append(" , modified_by=" + updatedBy);
			sql.append(" WHERE user_name=?	;");
			jdbcTemplate.update(sql.toString(), pwd, userName);
			return true;
		} catch (Exception ex) {
			return false;
		}

	}

	/**
	 * method to insert OTP when existing user request for for OTP
	 */
	@Override
	public boolean updateOTP(String userName, Integer otp, String userRole) {
		try {
			String sql = "UPDATE users SET login_otp=? , user_role=? WHERE user_name=?";
			jdbcTemplate.update(sql, otp, userRole, userName);
			return true;
		} catch (Exception ex) {
			return false;
		}

	}

	/**
	 * method to get OTP by user name
	 */
	@Override
	public Integer getOTPByUserName(String userName) throws APIExceptions {
		try {
			String sql = "select coalesce((select login_otp from users where user_name='" + userName + "'), -1)";
			Integer actualOTP = jdbcTemplate.queryForObject(sql, Integer.class);
			return actualOTP;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return -1;
		}
	}

	/**
	 * method to get Inbetween cities by load request id
	 */
	@Override
	public void updateLoginStatus(Users user, UserLoginStatus getActiveLoginStatus) {
		String sql = "UPDATE users set login_status =?, modified_by=? where user_id =?;";
		try {
			jdbcTemplate.update(sql, getActiveLoginStatus.toDbString(), user.getUserId(), user.getUserId());
		} catch (DataAccessException e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * method to get users mobile number list on the basis of city and rating
	 * 
	 * @throws InvalidUsernameException
	 * 
	 * @throws APIExceptions
	 */
	@Override
	public List<Users> getUserNameByCities(Set<String> listOfCities, int remainingCitiesCount,
			List<Long> listofUsersInSentMessage) throws InvalidUsernameException {
		String cities = Arrays.toString(listOfCities.toArray(new String[listOfCities.size()])).replaceAll(", ", "|")
				.replace("[", "").replace("]", "");
		log.info("Cities List :" + cities);
		String query = null;
		if (listofUsersInSentMessage == null || listofUsersInSentMessage.isEmpty()) {
			query = "SELECT DISTINCT  us.user_id, us.rating, us.user_name FROM users AS us LEFT JOIN postal_addresses "
					+ "AS p ON p.user_id = us.user_id where (us.user_role = 'CAPACITY_PROVIDER' or us.user_role = 'BOTH') "
					+ " AND (p.city similar to '(" + cities + ")' OR p.city similar to '%##(" + cities
					+ ")') ORDER BY rating DESC LIMIT " + remainingCitiesCount;
		} else {
			String userIdList = Arrays.toString(listofUsersInSentMessage.toArray()).replace("[", "").replace("]", "");
			query = "SELECT DISTINCT  us.user_id, us.rating, us.user_name FROM users AS us LEFT JOIN postal_addresses "
					+ "AS p ON p.user_id = us.user_id where (us.user_role = 'CAPACITY_PROVIDER' or us.user_role = 'BOTH') "
					+ " AND (p.city similar to '(" + cities + ")' OR p.city similar to '%##(" + cities
					+ ")') AND us.user_id NOT IN (" + userIdList + ")" + " ORDER BY rating DESC LIMIT "
					+ remainingCitiesCount;
		}
		try {
			return jdbcTemplate.query(query, new RowMapper<Users>() {
				@Override
				public Users mapRow(ResultSet rs, int rownumber) throws SQLException {
					Users user = new Users();
					user.setUserId(rs.getLong("user_id"));
					user.setUserName(rs.getString("user_name"));
					return user;
				}
			});
		} catch (DataAccessException e) {
			log.error("City is not found : " + e.getMessage(), e);
			throw new InvalidUsernameException("City is not valid.");
		}
	}

	@Override
	public Map<Long, String> getUsersByIds(Set<Long> userIds) {
		Map<Long, String> userMap = new HashMap<Long, String>();
		try {
			if (!userIds.isEmpty()) {
				String query = "select user_id, user_name from users where user_id in ("
						+ Arrays.toString(userIds.toArray()).replace("[", "").replace("]", "") + ")";

				return jdbcTemplate.query(query, new ResultSetExtractor<Map<Long, String>>() {

					@Override
					public Map<Long, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						while (rs.next()) {
							userMap.put(rs.getLong("user_id"), rs.getString("user_name"));
						}
						return userMap;
					}

				});
			}
		} catch (EmptyResultDataAccessException e) {
			log.error(e);
		} catch (DataAccessException e) {
			log.error(e);
		}
		return userMap;
	}

	@Override
	public boolean updateUsername(Users user) {
		String sql = "UPDATE users set user_name =?, modified_by=? where user_id =?;";
		try {
			int count = jdbcTemplate.update(sql, user.getUserName(), user.getUserId(), user.getUserId());
			if (count < 1) {
				return false;
			}
			return true;
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return false;
		}
	}

	@Override
	public List<Users> getUnAwardedCPUsersAfterLRBooked(List<Long> unAwardedBidsBean) throws InvalidUsernameException {
		try {
			String userIdList = Arrays.toString(unAwardedBidsBean.toArray()).replace("[", "").replace("]", "");
			String query = "select * from users where user_id  IN (" + userIdList + ");";

			return jdbcTemplate.query(query, new RowMapper<Users>() {
				@Override
				public Users mapRow(ResultSet rs, int rownumber) throws SQLException {
					Users user = new Users();
					user.setUserId(rs.getLong("user_id"));
					user.setUserName(rs.getString("user_name"));
					return user;
				}
			});
		} catch (DataAccessException e) {
			log.error("City is not found : " + e.getMessage(), e);
			throw new InvalidUsernameException("City is not valid.");
		}
	}
	@Override
	public void saveAverageRating(Long userId, Double currentRating) throws InvalidUsernameException
	{
		Users user = getUserByUserId(userId);
		String sql = "UPDATE users set rating =? , review_count =? where user_id =?;";
		double avgRating = 0.0;
		long reviewCount = 0;// default
		try {
			if(user.getRating() == 0) {
				avgRating = currentRating;
				reviewCount = user.getReviewCount()+1;
			}
			else {
				reviewCount = user.getReviewCount();
				avgRating = (double)(( (user.getRating()*reviewCount) + currentRating.doubleValue() ) / (reviewCount+1));
				reviewCount = reviewCount +1; // saving latest count in DB
			}
			
			jdbcTemplate.update(sql, avgRating ,reviewCount, user.getUserId());
		} catch (DataAccessException e) {
			log.error(e.getMessage());
		}
	}

}
