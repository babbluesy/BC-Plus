package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.example.demo.dto.Group;

public interface GroupService {
	Group createGroup(Group group);
	Optional<Group>  findById(ObjectId id);
	void deleteById(ObjectId id);
	Group findByGroupName(String name);
	List<Group> findByGroupMember(String member);
	List<Group> findAll();
	boolean findByGroupNameExists(String sid);
	boolean existsByHashCode(String code);
	Group findByHashCode(String code);
	void deleteAllByGroupName(String GroupName);
}
