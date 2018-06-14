package com.fw.dao;

import java.sql.SQLException;
import java.util.List;

import com.fw.beans.NotesBean;
import com.fw.domain.LoadRequestNotes;
import com.fw.exceptions.APIExceptions;

public interface IRequestNotesManager {

	List<NotesBean> getAllNotesByRequestId(Long bidId);

	void addNotes(LoadRequestNotes notes) throws APIExceptions;

	void updateNotesById(LoadRequestNotes notes);

	void deleteAllNotes(LoadRequestNotes bidNotes);

	void deleteRequestNoteById(Long id);

	List<NotesBean> getAllNotesByBidId(Long bidId);


}
