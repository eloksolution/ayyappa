package in.eloksolutions.ayyappa.service;

import in.eloksolutions.ayyappa.dao.GroupDAO;
import in.eloksolutions.ayyappa.dao.UserDAO;
import in.eloksolutions.ayyappa.model.Group;
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
		String groupId=groupDAO.addGroup(group);
		GroupMember gm=new GroupMember(groupId,group.getOwner(),group.getName());
		userDAO.addUserGroup(gm);
		return groupId;
	}

	public List<Group> getGroups() {
		return groupDAO.getGroups();
	}
	
	public List<Group> getTopGroups() {
		return groupDAO.getTopGroups();
	}

	public Group searchById(String groupid,String userId) {
		return groupDAO.searchById(groupid,userId);
	}
	public String join(GroupMember groupMem ) {
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
