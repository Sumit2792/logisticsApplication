package com.fw.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fw.dao.IMessageTemplateManager;
import com.fw.domain.MessageTemplates;
import com.fw.enums.ContactType;
import com.fw.exceptions.APIExceptions;
import com.fw.utils.MessageTemplateConstants;

@Repository
public class MessageTemplateManagerImpl implements IMessageTemplateManager {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<MessageTemplates> getAllMessageTemplates() {
		try {
			return jdbcTemplate.query("select * from message_templates", 
					new RowMapper<MessageTemplates>() {
						@Override
						public MessageTemplates mapRow(ResultSet rs, int rowNumber) throws SQLException {
							MessageTemplates templates = new MessageTemplates();
							templates.setMessageTemplateId(rs.getInt("message_template_id"));
							templates.setSubjectLine(rs.getString("subject_line"));
							templates.setMessageBody(rs.getString("message_body"));
							templates.setType(ContactType.fromString(rs.getString("type")));
							templates.setCreatedBy(rs.getLong("created_by"));
							templates.setModifiedBy(rs.getLong("modified_by"));
							templates.setCreatedDate(rs.getTimestamp("created_date"));
							templates.setModifiedDate(rs.getTimestamp("modified_date"));
							return templates;
						}
					});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<MessageTemplates> getMessageTemplatesByType(ContactType contactType) {
		try {
			return jdbcTemplate.query("select * from message_templates where type=?", 
					new RowMapper<MessageTemplates>() {
						@Override
						public MessageTemplates mapRow(ResultSet rs, int rowNumber) throws SQLException {
							MessageTemplates templates = new MessageTemplates();
							templates.setMessageTemplateId(rs.getInt("message_template_id"));
							templates.setSubjectLine(rs.getString("subject_line"));
							templates.setMessageBody(rs.getString("message_body"));
							templates.setType(ContactType.fromString(rs.getString("type")));
							templates.setCreatedBy(rs.getLong("created_by"));
							templates.setModifiedBy(rs.getLong("modified_by"));
							templates.setCreatedDate(rs.getTimestamp("created_date"));
							templates.setModifiedDate(rs.getTimestamp("modified_date"));
							return templates;
						}
					}, contactType.toDbString());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MessageTemplates getMessageTemplateById(long templateId) {
		try {
			return jdbcTemplate.queryForObject("select * from message_templates where message_template_id=?", 
					new RowMapper<MessageTemplates>() {
						@Override
						public MessageTemplates mapRow(ResultSet rs, int rowNumber) throws SQLException {
							MessageTemplates templates = new MessageTemplates();
							templates.setMessageTemplateId(rs.getInt("message_template_id"));
							templates.setSubjectLine(rs.getString("subject_line"));
							templates.setMessageBody(rs.getString("message_body"));
							templates.setType(ContactType.fromString(rs.getString("type")));
							templates.setCreatedBy(rs.getLong("created_by"));
							templates.setModifiedBy(rs.getLong("modified_by"));
							templates.setCreatedDate(rs.getTimestamp("created_date"));
							templates.setModifiedDate(rs.getTimestamp("modified_date"));
							return templates;
						}
					}, templateId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getMessageBySubjectLine(String subjectLine) {
		try {
			String messageBody = jdbcTemplate.queryForObject(
					"select coalesce((select message_body from message_templates where subject_line ='" + subjectLine + "'), '"+MessageTemplateConstants.NOTFOUND+"')",
					String.class);
			return messageBody;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void insertMessageTemplate(MessageTemplates template) throws APIExceptions
	{
		String checkSubjectLineExistOrNot = getMessageBySubjectLine(template.getSubjectLine().trim());
		
		if( !checkSubjectLineExistOrNot.equals(MessageTemplateConstants.NOTFOUND))
		  throw new APIExceptions("Template is already exist.");
		
		String sql = "INSERT INTO message_templates (subject_line, message_body, type, created_by, modified_by) " + " VALUES(?,?,?,?,?);";
		jdbcTemplate.update(sql,template.getSubjectLine(), template.getMessageBody(), template.getType(),template.getCreatedBy(), template.getModifiedBy());
		
	}
	
	@Override
	public void updateMessageTemplate(MessageTemplates template) throws APIExceptions
	{
		StringBuilder sql = new StringBuilder("UPDATE message_templates SET modified_by=? ");
		
		try {
			if(template.getSubjectLine()!=null && !"".equals(template.getSubjectLine()))
			{
				sql.append(" , subject_line='"+template.getSubjectLine()+"'");
			}
			if(template.getMessageBody()!=null && !"".equals(template.getMessageBody()))
			{
				sql.append(" , message_body='"+template.getMessageBody()+"'");
			}
			if(template.getType() != null)
			{
				sql.append(" , type='"+template.getType().toDbString()+"'");
			}
			sql.append(" where message_template_id = ?;");
			jdbcTemplate.update(sql.toString(), template.getModifiedBy(),template.getMessageTemplateId());
		} catch (Exception e) {
			 e.printStackTrace();
			 throw new APIExceptions("Internal server error while updating message template.");
		}
		
	}
	
	@Override
	public void deleteMessageTemplate(long templateId) throws APIExceptions
	{
		try {
			String sql = " DELETE  FROM  message_templates where message_template_id = ? ;";
			jdbcTemplate.update(sql,templateId);
			
		} catch (Exception e) {
			throw new APIExceptions("Internal server error while deleting message template.");
		}
	}

}
