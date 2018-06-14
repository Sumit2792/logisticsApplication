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
import com.fw.dao.IRequestNotesManager;
import com.fw.domain.LoadRequestNotes;
import com.fw.exceptions.APIExceptions;
@Repository
public class RequestNotesManagerImpl implements IRequestNotesManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger log = Logger.getLogger(RequestNotesManagerImpl.class);
	
	@Override
	public List<NotesBean> getAllNotesByRequestId(Long loadRequestId) {
			 try {
				return jdbcTemplate.query("select * from load_request_notes where load_request_id=?",new RowMapper<NotesBean>(){  
					    @Override  
					    public NotesBean mapRow(ResultSet rs, int rownumber) throws SQLException {  
					    	NotesBean notes=new NotesBean();
					    	notes.setNote(rs.getString("notes"));
					    	notes.setUserId(rs.getString("created_by"));
					    	notes.setCreatedDate(rs.getTimestamp("created_date"));
					    	return notes;
					    }  
					    },loadRequestId);
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				return null;
			} 
	}
	
	@Override
	public List<NotesBean> getAllNotesByBidId(Long bidId) {
			 try {
				return jdbcTemplate.query("select * from bid_notes where bid_id=?",new RowMapper<NotesBean>(){  
					    @Override  
					    public NotesBean mapRow(ResultSet rs, int rownumber) throws SQLException {  
					    	NotesBean notes=new NotesBean();
					    	notes.setNote(rs.getString("notes"));
					    	notes.setUserId(rs.getString("created_by"));
					    	notes.setCreatedDate(rs.getTimestamp("created_date"));
					    	return notes;
					    }  
					    },bidId);
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				return null;
			} 
	}

	@Override
	public void addNotes(LoadRequestNotes notes) throws APIExceptions {
		try {
			String sql = "INSERT INTO load_request_notes(load_request_id, notes,created_by, modified_by) VALUES (?, ?, ?, ?);";
			jdbcTemplate.update(sql,notes.getLoadRequestId(), notes.getNotes(), notes.getCreatedBy(),notes.getModifiedBy());
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new APIExceptions("Internal server error . Failed to add  request notes");
		}
		
	}

	@Override
	public void updateNotesById(LoadRequestNotes notes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllNotes(LoadRequestNotes bidNotes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRequestNoteById(Long id) {
		// TODO Auto-generated method stub
		
	}

}
