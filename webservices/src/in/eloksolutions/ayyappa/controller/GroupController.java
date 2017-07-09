package in.eloksolutions.ayyappa.controller;


import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.service.GroupService;

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
		groupService.addGroup(group);
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/getgroups", method = RequestMethod.GET)
	public List<Group> getGroup( HttpServletRequest request){
		System.out.println("Request xxxx is coming "+request);
		List<Group> groupcollection = groupService.getGroup();
		System.out.println("Colection is coming "+groupcollection);
		return groupcollection;
	}
	
	@ResponseBody
	@RequestMapping(value = "/groupEdit/{groupid}")
	public List<Group> memberEdit(@PathVariable("groupid") String groupid, HttpServletRequest request) {
		System.out.println("Fetching all members with X00001 memberEdit " + groupid);
		List<Group>  groupedit = groupService.searchById(groupid);
		
		System.out.println("Fetching all Group details " + groupedit);
		return groupedit;
	}
	
	

}
