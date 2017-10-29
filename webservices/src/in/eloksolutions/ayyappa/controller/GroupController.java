package in.eloksolutions.ayyappa.controller;


import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.service.GroupService;
import in.eloksolutions.ayyappa.vo.GroupMember;
import in.eloksolutions.ayyappa.vo.GroupVO;

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
@RequestMapping("/group")
public class GroupController {
	
	@Autowired
	GroupService groupService;
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addGroup(@RequestBody Group group){
		System.out.println("Request is coming "+group);
		return groupService.addGroup(group);
	}

	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateGroup(@RequestBody Group groupVO){
		System.out.println("Request is coming groupVO "+groupVO);
		 groupService.update(groupVO);
		 System.out.println("Updating  is coming groupVO "+groupVO);
		 return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getgroups/{userid}", method = RequestMethod.GET)
	public List<Group> getGroups( @PathVariable("userid") String userId){
		System.out.println("Request xxxx is getGroups "+userId);
		List<Group> groupcollection = groupService.getGroups(userId);
		System.out.println("Colection is coming "+groupcollection);
		return groupcollection;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getfirstgroups/{userid}", method = RequestMethod.GET)
	public List<Group> getFirstGroups( @PathVariable("userid") String userId){
		System.out.println("Request xxxx is coming "+userId);
		List<Group> groupcollection = groupService.getTopGroups(userId);
		System.out.println("Colection is coming "+groupcollection);
		return groupcollection;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getgroup/{groupid}/{userid}")
	public Group getGroupById(@PathVariable("groupid") String groupid,@PathVariable("userid") String userId) {
		System.out.println("Fetching all members with X00001 memberEdit " + groupid);
		Group  groupedit = groupService.searchById(groupid,userId);
		System.out.println("Fetching all Group details " + groupedit);
		return groupedit;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getGroups/{userid}")
	public List<Group> getUserGroups(@PathVariable("userid") String userId, HttpServletRequest request) {
		System.out.println("Fetching all members with X00001 memberEdit " + userId);
		return groupService.getUserGroups(userId);
	}
	
	@ResponseBody
	@RequestMapping(value = "/joined/{userid}")
	public List<Group> getJoinedGroups(@PathVariable("userid") String userId, HttpServletRequest request) {
		System.out.println("Fetching all members with X00001 memberEdit " + userId);
		return groupService.getJoinedGroups(userId);
	}
	
	@ResponseBody
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join( @RequestBody GroupMember groupMem){
		System.out.println("Request xxxx is groupMem  "+groupMem);
		String msg = groupService.join(groupMem);
		return msg;
	}
	
	
}
