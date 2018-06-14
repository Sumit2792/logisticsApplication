package com.fw.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fw.beans.NotesBean;
import com.fw.dao.IBidNotesManager;
import com.fw.domain.BidNotes;
import com.fw.exceptions.APIExceptions;

@Repository
public class BidNotesManagerImpl implements IBidNotesManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger log = Logger.getLogger(BidNotesManagerImpl.class);
	@Override
	public List<NotesBean> getAllNotesByBidId(Long bidId) {
		try {
			return jdbcTemplate.query("select * from bid_notes where bid_id=?", new RowMapper<NotesBean>() {
				@Override
				public NotesBean mapRow(ResultSet rs, int rownumber) throws SQLException {
					NotesBean notes = new NotesBean();
					notes.setNote(rs.getString("notes"));
					notes.setUserId(rs.getString("created_by"));
					notes.setCreatedDate(rs.getTimestamp("created_date"));
					return notes;
				}
			}, bidId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void addNotes(BidNotes notes) throws SQLException {
		try {
			String sql = "INSERT INTO bid_notes(bid_id, notes,created_by, modified_by)	VALUES (?, ?, ?, ?);";
			jdbcTemplate.update(sql, notes.getBidId(), notes.getNotes(), notes.getCreatedBy(), notes.getModifiedBy());
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new SQLException("Failed to add notes for bid ");
		}

	}

	@Override
	public void updateNotesById(BidNotes notes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllNotes(BidNotes bidNotes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteBidNoteById(Long id) {
		// TODO Auto-generated method stub

	}

}
