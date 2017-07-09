package in.eloksolutions.ayyappa.controller;


import in.eloksolutions.ayyappa.model.Discussion;
import in.eloksolutions.ayyappa.model.Topic;
import in.eloksolutions.ayyappa.service.TopicService;
import in.eloksolutions.ayyappa.vo.DiscussionVO;
import in.eloksolutions.ayyappa.vo.TopicVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/topic")
public class TopicController {
	
	@Autowired
	TopicService topicService;
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addTopic(@RequestBody TopicVO topic){
		System.out.println("Request is coming "+topic);
		Topic mTopic=new Topic(topic.getTopic(), topic.getDescription(),topic.getOwner(), topic.getGroupId());
		topicService.addTopic(mTopic);
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateTopic(@RequestBody TopicVO topic){
		System.out.println("Request is coming "+topic);
		Topic mTopic=new Topic(topic.getTopicId(),topic.getTopic(), topic.getDescription(),topic.getOwner(), topic.getGroupId());
		topicService.updateTopic(mTopic);
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/getTopics", method = RequestMethod.GET)
	public List<Topic> gettopic( HttpServletRequest request){
		System.out.println("Request xxxx is coming "+request);
		List<Topic> topiccollection = topicService.getTopic();
		System.out.println("Colection is coming "+topiccollection);
		return topiccollection;
	}
	
	@ResponseBody
	@RequestMapping(value = "/addDiscussion",method = RequestMethod.POST)
	public String addDiscussion(@RequestBody DiscussionVO discussion) {
		System.out.println("Fetching alldiscussion " + discussion);
		topicService.addDiscussion(discussion.getTopicId(), new Discussion(discussion.getComment(),discussion.getOwnerId()));
		System.out.println("Updated the discussion ");
		return "success";
	}
	
	
}
