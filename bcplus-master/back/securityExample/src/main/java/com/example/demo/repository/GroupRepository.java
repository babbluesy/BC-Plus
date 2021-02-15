package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Group;
import com.example.demo.dto.Schedule;
@Repository
public interface GroupRepository extends MongoRepository<Group,ObjectId>{
	@DeleteQuery
    void deleteById(ObjectId id);
	Group findByGroupName(String name);
	List<Group> findByGroupMember(String member);
	boolean existsByGroupName(String name);
	boolean existsByHashCode(String code);
	Group findByHashCode(String code);
	void deleteByGroupName(String GroupName);
}
