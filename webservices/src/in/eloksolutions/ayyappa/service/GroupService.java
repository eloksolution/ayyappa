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
	
	public void addGroup(Group group){
		String groupId=groupDAO.addGroup(group);
		GroupMember gm=new GroupMember(groupId,group.getOwner(),group.getName());
		userDAO.addUserGroup(gm);
	}

	public List<Group> getGroup() {
		return groupDAO.getGroup();
	}

	public Group searchById(String groupid) {
		return groupDAO.searchById(groupid);
	}
	
	public String join(GroupMember groupMem ) {
		return groupDAO.join(groupMem);
	}
}
