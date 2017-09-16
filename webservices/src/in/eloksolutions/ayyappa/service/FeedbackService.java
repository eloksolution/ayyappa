package in.eloksolutions.ayyappa.service;

import in.eloksolutions.ayyappa.dao.FeedbackDAO;
import in.eloksolutions.ayyappa.model.Feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("feedbackService")
public class FeedbackService {
	@Autowired
	FeedbackDAO feedbackDAO;
	public String userFeedback(Feedback feed) {
		return feedbackDAO.userFeedback(feed);
			
		}
}
