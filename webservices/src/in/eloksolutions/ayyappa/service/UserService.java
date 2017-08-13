package in.eloksolutions.ayyappa.service;

import java.util.List;

import in.eloksolutions.ayyappa.dao.UserDAO;
import in.eloksolutions.ayyappa.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userService")
public class UserService {
	@Autowired
	UserDAO userDAO;
	
	public String addUser(User user){
		return userDAO.addUser(user);
	}
	public List<User> getUsers(){
		return userDAO.getUsers();
	}
	public User searchById(String userid) {
		return userDAO.searchById(userid);
	}

	public String update(User user) {
		return userDAO.update(user);
	}

	public User getConnections(String userid) {
		return userDAO.getConnections(userid);
	}

	public User getGroups(String userid) {
		return userDAO.getUserWithGroups(userid);
	}
	public User getTopics(String userid) {
		return userDAO.getUserWithTopics(userid);
	}

	public User getPadis(String userid) {
		return userDAO.getUserWithPaids(userid);
	}
	public List<User> findNearMe(String userid) {
		return userDAO.findNearMe(userid);
	}
	
}
