package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Schedule;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,ObjectId>{
	List<Schedule> findByIdOrKey(ObjectId id,String Key);
	List<Schedule> findBySid(String sid);
	List<Schedule> findByStartBetweenAndGroup(Date from,Date to,String uid);
	List<Schedule> findByRendBetweenAndGroup(Date from,Date to,String uid);
	List<Schedule> findByStartBetweenAndGroupAndMembers(Date from,Date to,String uid,String member);
	List<Schedule> findByRendBetweenAndGroupAndMembers(Date from,Date to,String uid,String member);
	List<Schedule> findByGroup(String group);
	@DeleteQuery
	void deleteAllBySid(String sid);	
	boolean existsBySid(String sid);
	void deleteAllByGroup(String groupName);
	
}
