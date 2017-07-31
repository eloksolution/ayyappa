package in.eloksolutions.ayyappa.controller;


import in.eloksolutions.ayyappa.model.Feedback;
import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.service.UserService;
import in.eloksolutions.ayyappa.vo.GroupVO;
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
	public String adduser(@RequestBody User user){
		System.out.println("Request is coming "+user);
		userService.adduser(user);
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	public List<User> getUsers( HttpServletRequest request){
		System.out.println("Request xxxx is coming "+request);
		List<User> usercollection = userService.getuser();
		System.out.println("Colection is coming "+usercollection);
		return usercollection;
	}
	
	@ResponseBody
	@RequestMapping(value = "/user/{userid}")
	public User getUserById(@PathVariable("userid") String userid, HttpServletRequest request) {
		System.out.println("Fetching all Users with X00001 UserEdit " + userid);
		User  useredit = userService.searchById(userid);
		System.out.println("Fetching all user details " + useredit);
		return useredit;
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateUser(@RequestBody UserVo userVo){
		System.out.println("Request is coming groupVO "+userVo);
		User user=new User(userVo.getUserId(),userVo.getFirstName(),userVo.getLastName(),userVo.getMobile(),userVo.getEmail(),userVo.getArea(),userVo.getCity(),userVo.getState());
		 userService.update(user);
		 return "success";
	}
	
	
}
