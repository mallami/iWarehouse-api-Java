package com.electronics.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.electronics.model.AccessGroup;
import com.electronics.repository.AccessGroupRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccessGroupService {

	private AccessGroupRepo accessGroupRepo;
	
	// ADD NEW GROUP
	public void addGroup(AccessGroup group) {
		accessGroupRepo.save(group);
	}
	
	// COPY GROUP
	public Boolean copyGroup(int sourceGroupId, String targetGroupName) {
		boolean resp = false;
		
		AccessGroup sourceGroup = accessGroupRepo.getGroupById(sourceGroupId);
		AccessGroup targetGroup = sourceGroup;
		targetGroup.setGroupId(0);
		targetGroup.setGroupName(targetGroupName);
		
		if(targetGroupName != "") {
			resp = true;
			addGroup(targetGroup);
		}
		
		return resp;
	}
	
	// GET ALL GROUPS
	public List<AccessGroup> getAllGroups() {
		return accessGroupRepo.getAllGroups();
	}
	
	// GET GROUP BY groupID
	public AccessGroup getGroupById(int groupId) {
		return accessGroupRepo.getGroupById(groupId);
	}
	
	// GET GROUP BY groupNAME (OR CONTAINS PART OF THE NAME)
	public List<AccessGroup> getGroupByName(String groupName) {
		return accessGroupRepo.getGroupByName(groupName);
	}
	
	// UPDATE GROUP
	public void updateGroup(AccessGroup group) {
		accessGroupRepo.save(group);
	}
		
	// DELETE GROUP BY groupID
	public void deleteGroup(int groupId) {
		accessGroupRepo.deleteGroup(groupId);
	}
	
}
