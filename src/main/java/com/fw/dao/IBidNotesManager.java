package com.fw.dao;

import java.sql.SQLException;
import java.util.List;

import com.fw.beans.NotesBean;
import com.fw.domain.BidNotes;

public interface IBidNotesManager {
	
	/**
	 * Persist the object normally.
	 */
	List<NotesBean> getAllNotesByBidId(Long bidId);

	void addNotes(BidNotes notes) throws SQLException;

	void updateNotesById(BidNotes notes);

	void deleteAllNotes(BidNotes bidNotes);

	void deleteBidNoteById(Long id);
}
