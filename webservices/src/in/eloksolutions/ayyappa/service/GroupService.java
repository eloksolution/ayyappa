package in.eloksolutions.ayyappa.service;

import in.eloksolutions.ayyappa.dao.GroupDAO;
import in.eloksolutions.ayyappa.dao.UserDAO;
import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.vo.GroupMember;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("groupService")
public class GroupService {
	@Autowired
	GroupDAO groupDAO;
	@Autowired
	UserDAO userDAO;
	
	public String addGroup(Group group){
		User owner=userDAO.searchById(group.getOwner());
		String groupId=groupDAO.addGroup(group,owner);
		userDAO.addUserGroup(group);
		return groupId;
	}

	public List<Group> getGroups(String userId) {
		return groupDAO.getGroups(userId);
	}
	
	public List<Group> getTopGroups(String userId) {
		return groupDAO.getTopGroups(userId);
	}

	public Group searchById(String groupid,String userId) {
		return groupDAO.searchById(groupid,userId);
	}
	public String join(GroupMember groupMem ) {
		groupMem.setImgPath(userDAO.getImagePath(groupMem.getUserId()));
		return groupDAO.join(groupMem);
	}

	public String update(Group group) {
		return groupDAO.update(group);
	}

	public List<Group> getUserGroups(String userId) {
		return groupDAO.getUserGroups(userId);
	}

	public List<Group> getJoinedGroups(String userId) {
		return groupDAO.getJoinedGroups(userId);
	}
}
