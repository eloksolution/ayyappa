package in.eloksolutions.ayyappa.controller;


import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.service.UserService;
import in.eloksolutions.ayyappa.util.Util;
import in.eloksolutions.ayyappa.vo.DeekshaVO;
import in.eloksolutions.ayyappa.vo.UserConnectionVO;
import in.eloksolutions.ayyappa.vo.UserVo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addUser(@RequestBody UserVo userVo){
		System.out.println("Request is coming "+userVo);
		User user=getUser(userVo);
		return userService.addUser(user);
	}

	private User getUser(UserVo userVo) {
		return new User(userVo.getUserId(),userVo.getFirstName(),userVo.getLastName(),userVo.getMobile(),userVo.getEmail(),userVo.getArea(),userVo.getCity(),userVo.getState(),userVo.getLat(),userVo.getLon(),userVo.getImgPath());
	}

	@ResponseBody
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	public List<User> getUsers( HttpServletRequest request){
		System.out.println("Request xxxx is coming "+request);
		List<User> usercollection = userService.getUsers();
		System.out.println("Colection is coming "+usercollection);
		return usercollection;
	}
	
	@ResponseBody
	@RequestMapping(value = "/search/{tok}", method = RequestMethod.GET)
	public List<User> getUsers(@PathVariable("tok") String tok){
		System.out.println("Request xxxx is coming "+tok);
		List<User> usercollection = userService.search(tok);
		System.out.println("Colection is coming "+usercollection);
		return usercollection;
	}
	
	@ResponseBody
	@RequestMapping(value = "/user/{fromuserid}/{touserid}")
	public User getUserById(@PathVariable("fromuserid") String fromUserId,@PathVariable("touserid") String toUserId, HttpServletRequest request) {
		System.out.println("Fetching all Users with X00001 fromUserId " + fromUserId+" toUserId "+toUserId);
		User  useredit = userService.searchById(fromUserId,toUserId);
		System.out.println("Fetching all user details " + useredit);
		return useredit;
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateUser(@RequestBody UserVo userVo){
		System.out.println("Request is coming groupVO "+userVo);
		User user=new User(userVo.getUserId(),userVo.getFirstName(),userVo.getLastName(),userVo.getMobile(),userVo.getEmail(),userVo.getArea(),userVo.getCity(),userVo.getState(),userVo.getImgPath());
		 userService.update(user);
		 return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateUserToken", method = RequestMethod.POST)
	public String updateUserToken(@RequestBody UserVo userVo){
		System.out.println("Request is updateUserToken coming userid "+userVo);
		 try {
			userService.updateUserToken(userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "/connections/{userid}")
	public User getConnections(@PathVariable("userid") String userid, HttpServletRequest request) {
		System.out.println("getConnections userid "+userid);
		try {
			return userService.getConnections(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/receivedConnections/{userid}")
	public List<UserVo> receivedConnections(@PathVariable("userid") String userid, HttpServletRequest request) {
		System.out.println("receivedConnections userid "+userid);
		try {
			return userService.receivedConnections(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/requestConnection")
	public String requestConnection(@RequestBody UserConnectionVO user) throws Exception {
		System.out.println("requestConnection userid "+user);
		return userService.requestConnection(user);
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkSentConnection")
	public String checkSentConnection(@RequestBody UserConnectionVO user) throws Exception {
		System.out.println("checkSentConnection userid "+user);
		return userService.checkIfAlreadySentRequest(user)?"Y":"N";
	}
	@ResponseBody
	@RequestMapping(value = "/connect")
	public String connect(@RequestBody UserConnectionVO user) {
		System.out.println("connect userid "+user);
		return userService.connect(user);
	}
	
	@ResponseBody
	@RequestMapping(value = "/groups/{userid}")
	public User getGroups(@PathVariable("userid") String userid, HttpServletRequest request) throws Exception  {
		System.out.println("getGroups userid "+userid);
		return userService.getGroups(userid);
	}
	
	@ResponseBody
	@RequestMapping(value = "/topics/{userid}")
	public User getTopics(@PathVariable("userid") String userid, HttpServletRequest request) throws Exception {
		return userService.getTopics(userid);
	}
	
	@ResponseBody
	@RequestMapping(value = "/padis/{userid}")
	public List<Padipooja> getPadis(@PathVariable("userid") String userid, HttpServletRequest request) {
		return userService.getPadis(userid);
	}
	
	@ResponseBody
	@RequestMapping(value = "/loc/{userid}")
	public List<User> findNearMe(@PathVariable("userid") String userid, HttpServletRequest request) throws Exception {
		return userService.findNearMe(userid);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addDeeksha", method = RequestMethod.POST)
	public String addDeeksha(@RequestBody DeekshaVO deekshaVO){
		System.out.println("Request is coming "+deekshaVO);
		if(!Util.isValidDate(deekshaVO.getStartDate())){
			return "Please provide valid start date DD/MM/YYYY";
		}
		if(!Util.isValidDate(deekshaVO.getEndDate())){
			return "Please provide valid end date DD/MM/YYYY";
		}
		String id=null;
		try {
			 id= userService.addDeeksha(deekshaVO);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while saving Deeksha";
		}
		return id;
	}

	@ResponseBody
	@RequestMapping(value = "/getDeeksha{userid}", method = RequestMethod.GET)
	public DeekshaVO getDeeksha(@PathVariable("userid") String userId, HttpServletRequest request){
		System.out.println("Request padipooja xxxx is coming "+request);
		return userService.getDeeksha(userId);
	}
}
