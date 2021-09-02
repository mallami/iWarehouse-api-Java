package com.electronics.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.electronics.model.AccessGroup;
import com.electronics.service.AccessGroupService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "group")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@AllArgsConstructor
public class AccessGroupController {

	private AccessGroupService accessGroupService;
	
	// ADD NEW GROUP
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "addNewGroup.iwh")
	public void addGroup(HttpServletRequest req, @RequestBody AccessGroup group) {
		accessGroupService.addGroup(group);
	}
	
	// COPY GROUP 
	@PostMapping(value = "copyGroup.iwh:Source={sourceGroupId}&Target={targetGroupName}")
	public void copyGroup(HttpServletRequest req, HttpServletResponse resp, @PathVariable int sourceGroupId, @PathVariable String targetGroupName) {
		if(accessGroupService.copyGroup(sourceGroupId, targetGroupName)) {
			resp.setStatus(201);
		} else {
			resp.setStatus(406);
		}
	}
	// GET ALL GROUPS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listAllGroups.iwh")
	public List<AccessGroup> getAllGroups(HttpServletRequest req) {
		return accessGroupService.getAllGroups();
	}
	
	// GET GROUP BY groupID
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getGroup.iwh:Id={groupId}")
	public AccessGroup getGroupById(HttpServletRequest req, @PathVariable int groupId) {
		return accessGroupService.getGroupById(groupId);
	}
	
	// GET GROUP BY groupNAME (OR CONTAINS PART OF THE NAME)
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getGroup.iwh:Contains={groupName}")
	public List<AccessGroup> getGroupByName(HttpServletRequest req, @PathVariable String groupName) {
		return accessGroupService.getGroupByName(groupName);
	}
	
	// UPDATE GROUP
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "updateGroup.iwh:Id={groupId}")
	public void updateGroup(HttpServletRequest req, @RequestBody AccessGroup group, @PathVariable int groupId) {
		accessGroupService.updateGroup(group);
	}
		
	// DELETE GROUP BY groupID
	@ResponseStatus(value = HttpStatus.OK)
	@DeleteMapping(value = "deleteGroup.iwh:Id={groupId}")
	public void deleteGroup(HttpServletRequest req, @PathVariable int groupId) {
		accessGroupService.deleteGroup(groupId);
	}

}
