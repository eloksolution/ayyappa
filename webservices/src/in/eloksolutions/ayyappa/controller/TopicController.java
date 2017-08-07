package in.eloksolutions.ayyappa.controller;


import in.eloksolutions.ayyappa.model.Discussion;
import in.eloksolutions.ayyappa.model.Topic;
import in.eloksolutions.ayyappa.service.TopicService;
import in.eloksolutions.ayyappa.vo.DiscussionVO;
import in.eloksolutions.ayyappa.vo.TopicVO;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DBObject;



@RestController
@RequestMapping("/topic")
public class TopicController {
	
	@Autowired
	TopicService topicService;
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addTopic(@RequestBody TopicVO topic){
		System.out.println("Request is coming "+topic);
		if(topic.getGroupId()==null || topic.getGroupId().trim().length()==0)
			return "Topic should be associated with Group";
		Topic mTopic=new Topic(topic.getTopic(), topic.getDescription(),topic.getOwner(), topic.getGroupId());
		topicService.addTopic(mTopic);
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateTopic(@RequestBody TopicVO reqTopic){
		System.out.println("Request is coming "+reqTopic);
		Topic topic=new Topic(reqTopic.getTopicId(),reqTopic.getTopic(),reqTopic.getDescription(),reqTopic.getGroupId(),reqTopic.getOwner(),new Date().getTime());
		topicService.updateTopic(topic);
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
		topicService.addDiscussion(discussion.getTopicId(), new Discussion(discussion.getComment(),discussion.getOwnerId(),discussion.getOwnerName()));
		System.out.println("Updated the discussion ");
		return "success";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/{topicId}")
	public Topic searchById(@PathVariable("topicId") String topicId, HttpServletRequest request) {
		System.out.println("Fetching all members with X00001 topicId " + topicId);
		Topic  topic = topicService.searchById(topicId);
		System.out.println("Fetching all Group details " + topic);
		return topic;
	}
}
