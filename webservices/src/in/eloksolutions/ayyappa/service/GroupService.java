package in.eloksolutions.ayyappa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.eloksolutions.ayyappa.dao.GroupDAO;
import in.eloksolutions.ayyappa.model.Group;

@Repository("groupService")
public class GroupService {
	@Autowired
	GroupDAO groupDAO;
	
	public void addGroup(Group group){
		groupDAO.addGroup(group);
	}

	public List<Group> getGroup() {
		
		return groupDAO.getGroup();
	}

	public List<Group> searchById(String groupid) {
		
		return groupDAO.searchById(groupid);
	}

	
}
