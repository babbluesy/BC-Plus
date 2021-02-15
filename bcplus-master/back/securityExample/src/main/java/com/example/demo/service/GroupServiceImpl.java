package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Group;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.MemberRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("groupService")
public class GroupServiceImpl implements GroupService{
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Override
	public Group createGroup(Group group) {		
		return groupRepository.save(group);
	}

	@Override
	public Optional<Group> findById(ObjectId id) {		
		return groupRepository.findById(id);
	}

	@Override
	public void deleteById(ObjectId id) {
		groupRepository.deleteById(id);		
	}

	@Override
	public Group findByGroupName(String name) {		
		return groupRepository.findByGroupName(name);
	}

	@Override
	public List<Group> findByGroupMember(String member) {	
		return groupRepository.findByGroupMember(member);
	}

	@Override
	public boolean findByGroupNameExists(String name) {		
		return groupRepository.existsByGroupName(name);
	}

	@Override
	public boolean existsByHashCode(String code) {		
		return groupRepository.existsByHashCode(code);
	}

	@Override
	public Group findByHashCode(String code) {		
		return groupRepository.findByHashCode(code);
	}

	@Override
	public void deleteAllByGroupName(String GroupName) {
		groupRepository.deleteByGroupName(GroupName);
	}

	@Override
	public List<Group> findAll() {		
		return groupRepository.findAll();
	}	
}
