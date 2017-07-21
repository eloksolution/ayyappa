package in.eloksolutions.ayyappa.service;

import in.eloksolutions.ayyappa.dao.UserDAO;
import in.eloksolutions.ayyappa.model.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userService")
public class UserService {
	@Autowired
	UserDAO userDAO;
	
	public void adduser(User user){
		userDAO.adduser(user);
	}

	public List<User> getuser() {
		return userDAO.getuser();
	}

	public User searchById(String userid) {
		return userDAO.searchById(userid);
	}
}