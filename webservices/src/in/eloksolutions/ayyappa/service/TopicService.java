package in.eloksolutions.ayyappa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.eloksolutions.ayyappa.dao.TopicDAO;
import in.eloksolutions.ayyappa.messaging.SendNotification;
import in.eloksolutions.ayyappa.model.Discussion;
import in.eloksolutions.ayyappa.model.Topic;

@Repository("topicService")
public class TopicService {
	@Autowired
	TopicDAO topicDAO;
	
	public void addTopic(Topic topic){
		String topicId=topicDAO.addTopic(topic);
		SendNotification.sendMessageToGroup(topic.getGroupId(),topicId,topic.getTopic());
		
	}
	
	public void updateTopic(Topic topic){
		topicDAO.updateTopic(topic);
	}

	public List<Topic> getTopics() {
		return topicDAO.getTopics();
	}

	public Topic searchById(String topicid) {
		return topicDAO.searchById(topicid);
	}

	public void addDiscussion(String topicId,Discussion diss){
		 topicDAO.addDiscussion(topicId, diss);
		 SendNotification.sendMessageToGroup(topicId,topicId,diss.getComment());
	}

	public List<Topic> getGroupTopics(String groupId) {
		return topicDAO.getGroupTopics(groupId);
	}

	
}
