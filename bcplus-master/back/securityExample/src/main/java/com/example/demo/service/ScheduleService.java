package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.Schedule;

public interface ScheduleService {	
	Schedule createSchedule(Schedule schedule);
	List<Schedule> findByIdOrKey(String id,String Key);
	List<Schedule> findBySid(String sid);
	List<Schedule> findByStartTimeBetweenAndGroup(Date from,Date to,String uid);
	List<Schedule> findByRendBetweenAndGroup(Date from,Date to,String uid);
	List<Schedule> findByStartBetweenAndGroupAndMembers(Date from,Date to,String uid,String member);
	List<Schedule> findByRendBetweenAndGroupAndMembers(Date from,Date to,String uid,String member);
	void deleteAllBySid(String sid);
	boolean findBySidExists(String sid);
	void deleteAllByGroupName(String groupName);
	List<Schedule> findByGroup(String groupid);
}
