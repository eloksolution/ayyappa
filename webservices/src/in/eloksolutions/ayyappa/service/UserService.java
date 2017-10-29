package in.eloksolutions.ayyappa.service;

import java.util.List;

import in.eloksolutions.ayyappa.dao.DeekshaDAO;
import in.eloksolutions.ayyappa.dao.PadipojaDAO;
import in.eloksolutions.ayyappa.dao.UserDAO;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.vo.DeekshaVO;
import in.eloksolutions.ayyappa.vo.UserConnectionVO;
import in.eloksolutions.ayyappa.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userService")
public class UserService {
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	DeekshaDAO deekshaDAO;
	
	@Autowired
	PadipojaDAO padipojaDAO;
	
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

	public User getConnections(String userid) throws Exception {
		return userDAO.getConnections(userid);
	}

	public User getGroups(String userid) throws Exception{
		return userDAO.getUserWithGroups(userid);
	}
	public User getTopics(String userid) throws Exception {
		return userDAO.getUserWithTopics(userid);
	}

	public List<Padipooja> getPadis(String userid) {
		return padipojaDAO.getUserPadiPoojas(userid);
	}
	public List<User> findNearMe(String userid) throws Exception {
		return userDAO.findNearMe(userid);
	}
	public String addDeeksha(DeekshaVO deekshaVO) {
		try {
			return deekshaDAO.addDeeksha(deekshaVO);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while Saving Deeksha, Please contact System Admin";
		}
	}
	public DeekshaVO getDeeksha(String userId) {
		return deekshaDAO.getDeeksha(userId);
	}
	public String requestConnection(UserConnectionVO user) throws Exception {
		return userDAO.requestConnect(user);
	}
	public String connect(UserConnectionVO user) {
		return userDAO.connect(user);
	}
	public List<UserVo> receivedConnections(String userid) throws Exception {
		return userDAO.getReceivedConnection(userid);
	}
	public String updateUserToken(UserVo userVo) {
		return userDAO.updateUserToken(userVo.getUserId(),userVo.getFcmToken());
	}
	public boolean checkIfAlreadySentRequest(UserConnectionVO user) throws Exception {
		return userDAO.checkIfAlreadySentRequest(user.getUserId(),user.getConnectedToId());
	}
	public User searchById(String fromUserId, String toUserId) {
		return userDAO.searchUserWithConnection(fromUserId,toUserId);
	}
	public List<User> search(String tok) {
		return userDAO.search(tok);
	}
	
}
