package in.eloksolutions.ayyappa.controller;

import in.eloksolutions.ayyappa.model.Feedback;
import in.eloksolutions.ayyappa.service.FeedbackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/feedback")
public class FeedbackController {
	@Autowired
	FeedbackService feedbackService;
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String userFeedback(@RequestBody Feedback feed){
		System.out.println("Request is coming "+feed);
		feedbackService.userFeedback(feed);
		return "success";
	}
}
